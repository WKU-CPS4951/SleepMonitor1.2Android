package com.testing.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SleepQualityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SleepQualityFragment extends Fragment {
    private long firstClick = 0;
    private long secondClick = 0;
    private long duration = 1000;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private MainActivity activity;
    private static final String TAG = "SleepQualityFragment";
    private final int[] BENTCHMARK = new int[]{46,54}; //46% for deep sleep, 54% for REM
    private float score;
    private String type;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public SleepQualityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SleepQualityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SleepQualityFragment newInstance(String param1, String param2) {
        SleepQualityFragment fragment = new SleepQualityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void onStart() {
        super.onStart();
        activity = (MainActivity)this.getActivity();
        activity.transactKey(R.id.bt_sleep);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sleep_quality, container, false);
        View sqLayout = (View) view.findViewById(R.id.sleep_quality_layout);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);
        TextView sleepRate = (TextView)view.findViewById(R.id.sleep_rate);
        TextView sleepScore = (TextView)view.findViewById(R.id.sleep_score);
        TextView sleepType = (TextView)view.findViewById(R.id.sleep_type);

        new Thread(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: progress bar has been updated");
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });
                try {
                    Log.d(TAG, "run: Waiting for Fetching Motion");
                    LineDataSet lineDataSet1 = new LineDataSet(LineChartFragment2.FetchMotion(getActivity()), "Data Set 1");
                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(lineDataSet1);
                    LineData data = new LineData(dataSets);
                    LineChartFragment2.DSRate = (int) ((LineChartFragment2.DeepSleepF / (LineChartFragment2.DeepSleepF + LineChartFragment2.REMF)) * 100);
                    LineChartFragment2.REMRate = (int) ((LineChartFragment2.REMF / (LineChartFragment2.DeepSleepF + LineChartFragment2.REMF)) * 100);

                    score = LineChartFragment2.DSRate*LineChartFragment2.REMRate+BENTCHMARK[0]*BENTCHMARK[1];
                    score /= Math.sqrt(LineChartFragment2.DSRate+BENTCHMARK[0])*Math.sqrt(LineChartFragment2.REMRate+BENTCHMARK[1]);
                    type = "";
                    if(score>90) type=getResources().getString(R.string.sleep_type_excellent);
                    else if(score>80) type=getResources().getString(R.string.sleep_type_great);
                    else if(score>70) type=getResources().getString(R.string.sleep_type_good);
                    else if(score>60) type=getResources().getString(R.string.sleep_type_medium);
                    else type=getResources().getString(R.string.sleep_type_bad);



                    Log.d(TAG, "run: Deep Sleep Rate" + LineChartFragment2.DSRate);
                    Log.d(TAG, "run: REM Sleep Rate" + LineChartFragment2.REMRate);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sleepScore.setText(""+(int)score);
                            sleepType.setText(getResources().getString(R.string.sleep_quality)+type);
                            sleepRate.setText(String.format("%s\t\t\t\t\t\t%d%%\n%s\t%d%%",getResources().getString(R.string.REM),LineChartFragment2.REMRate,getResources().getString(R.string.deep_sleep),LineChartFragment2.DSRate));
                        }
                    });
                }catch (Exception e){
                    Log.e(TAG, "run: ", e);
                }finally {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "run: progress bar has been updated");
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        }).start();

        sqLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(firstClick == 0){
                    firstClick = System.currentTimeMillis();
                    Toast.makeText(getActivity(),"Click Again For Details",Toast.LENGTH_SHORT).show();
                }else{
                    secondClick = System.currentTimeMillis();
                    if(secondClick-firstClick>duration){
                        firstClick = secondClick;
                        secondClick = 0;
                        Toast.makeText(getActivity(),"Click Again For Details",Toast.LENGTH_SHORT).show();
                    } else{
                        firstClick = 0;
                        secondClick = 0;
                        Log.d(TAG, "onClick: Layout has been clicked");
                        if(getActivity() instanceof MainActivity){
                            Log.d(TAG, "onClick: Fragment replacing");
                            //TODO: bugs in LineChartFragment1
                            ((MainActivity)getActivity()).replaceFragment(new LineChartFragment1());
                        }
                    }
                }
            }
        });
        return view;
    }
}