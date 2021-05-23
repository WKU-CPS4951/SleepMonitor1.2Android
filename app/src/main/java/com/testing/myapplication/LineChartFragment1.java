package com.testing.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.vishnusivadas.advanced_httpurlconnection.FetchData;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LineChartFragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LineChartFragment1 extends Fragment {
    ProgressBar progressBar;
    LineChart mpLineChart;
    Button Home;
    Button Temperature;
    Button Motion;
    private static final String TAG = "LineChartFragment1";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LineChartFragment1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LineChartFragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static LineChartFragment1 newInstance(String param1, String param2) {
        LineChartFragment1 fragment = new LineChartFragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_line_chart1, container, false);
        //view initialization
        progressBar = view.findViewById(R.id.progress);
        Temperature = view.findViewById(R.id.btnLineChart1);
        Motion= view.findViewById(R.id.btnLineChart2);
        mpLineChart = view.findViewById(R.id.line_chart1);

        //Line chart download and update
        progressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    LineDataSet lineDataSet1 = new LineDataSet(FetchTimeTemp(), "Data Set 1");
                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(lineDataSet1);

                    LineData data = new LineData(dataSets);
                    Log.d(TAG, "run: data has been read " + dataSets.size());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "run: UI has been updated");
                            mpLineChart.setData(data);
                            mpLineChart.invalidate();
                        }
                    });
                }catch(Error e){
                    Log.e(TAG, "run: error happens");
                    Log.e(TAG, e.getMessage());
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

        //temperature button function
        Temperature.setEnabled(true);
//
//        //Sleep Motion button function
        Motion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start ProgressBar first (Set visibility VISIBLE)
                progressBar.setVisibility(View.VISIBLE);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"LineChartTest",Toast.LENGTH_SHORT).show();
                        ((MainActivity)getActivity()).replaceFragment(new LineChartFragment2());
                        //End Write and Read data with URL
                    }
                });
            }
        });
        return view;
    }

    private ArrayList<Entry> dataValues1()
    {
        ArrayList<Entry> dataVals = new ArrayList<Entry>();

        for (int i = 900000000; i <900004000 ; i++) {
            dataVals.add(new Entry(i,i));
        }
        return  dataVals;

    }
    private ArrayList<Entry> FetchTimeTemp ()
    {
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        FetchData fetchData = new FetchData(String.format("http://%s/FetchData/FetchData.php",getResources().getString(R.string.IP)));
        if (fetchData.startFetch()) {
            //progressBar.setVisibility(View.VISIBLE);
            if (fetchData.onComplete()) {
                String result = fetchData.getResult();
                try {
                    Log.d(TAG, "FetchTimeTemp: result: "+result);
                    JSONArray arr = new JSONArray(result);

                    for(int i = 0; i < arr.length(); i++){

                        if (arr.getJSONObject(i).getString("time").equals("null")||
                                arr.getJSONObject(i).getString("temp").equals("null"))
                        {
                            continue;
                        }
                        float time_v = 0f;
                        float temp_v = 0f;
                        time_v = Float.parseFloat(arr.getJSONObject(i).getString("id"));
                        temp_v = Float.parseFloat(arr.getJSONObject(i).getString("temp"));
                        dataVals.add(new Entry(time_v,temp_v));
                    }
                    Log.d(TAG, "FetchTimeTemp: Fetch Success");
                } catch (JSONException e) {
                    Log.e(TAG, "FetchTimeTemp: JSON Error", e);
                }
                //TODO:No Use
//                                    Intent intent = new Intent(getApplicationContext(),TestResult.class);
//                                    startActivity(intent);
//                                    finish();
            } else {
                Log.d(TAG, "FetchTimeTemp: fetchData.onComplete() Error");
            }
        } else {
            Log.d(TAG, "FetchTimeTemp: fetchData.startFetch() Error");
        }
        return  dataVals;
    }
}