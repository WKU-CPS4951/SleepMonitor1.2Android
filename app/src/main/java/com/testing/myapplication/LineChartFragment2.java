package com.testing.myapplication;

import android.app.Activity;
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
import android.widget.TextView;
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
 * Use the {@link LineChartFragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LineChartFragment2 extends Fragment {
    ProgressBar progressBar;
    LineChart mpLineChart;
    Button Temperature;
    Button Motion;

    TextView DeepSleep,REM;
    public static Float DeepSleepF=0f,REMF=0f;
    public static int DSRate,REMRate;
    private static final String TAG = "LineChartFragment2";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LineChartFragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LineChartFragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static LineChartFragment2 newInstance(String param1, String param2) {
        LineChartFragment2 fragment = new LineChartFragment2();
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
        View view = inflater.inflate(R.layout.fragment_line_chart2, container, false);
        progressBar = view.findViewById(R.id.progress);
        Temperature = view.findViewById(R.id.btnLineChart1);
        Motion= view.findViewById(R.id.btnLineChart2);

        mpLineChart = (LineChart) view.findViewById(R.id.line_chart2);
        DeepSleep=(TextView)view.findViewById(R.id.DeepSleep);
        REM=(TextView)view.findViewById(R.id.REM);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "run: Waiting for Fetching Motion");
                    LineDataSet lineDataSet1 = new LineDataSet(FetchMotion(getActivity()), "Data Set 1");
                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(lineDataSet1);
                    LineData data = new LineData(dataSets);
                    DSRate = (int) ((DeepSleepF / (DeepSleepF + REMF)) * 100);
                    REMRate = (int) ((REMF / (DeepSleepF + REMF)) * 100);

                    Log.d(TAG, "run: Deep Sleep Rate" + DSRate);
                    Log.d(TAG, "run: REM Sleep Rate" + REMRate);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "run: UI has been updated");
                            mpLineChart.setData(data);
                            mpLineChart.invalidate();
                            DeepSleep.setText("DeepSleep: "+DSRate+"%");
                            REM.setText("REM: "+REMRate+"%");
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

        Temperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start ProgressBar first (Set visibility VISIBLE)
                progressBar.setVisibility(View.VISIBLE);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"LineChartTest",Toast.LENGTH_SHORT).show();
                        ((MainActivity)getActivity()).replaceFragment(new LineChartFragment1());
                        //End Write and Read data with URL
                    }
                });
            }
        });

        //Sleep Motion button function
        Motion.setEnabled(true);
        
        return view;
    }

    public static ArrayList<Entry> FetchMotion (Activity activity)
    {
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        FetchData fetchData = new FetchData(String.format("http://%s/FetchData/FetchData.php",activity.getResources().getString(R.string.IP)));
        // dong zi bro's ip: 122.239.216.214
        if (fetchData.startFetch()) {
            //progressBar.setVisibility(View.VISIBLE);
            if (fetchData.onComplete()) {
                String result = fetchData.getResult();
                try {
                    JSONArray arr = new JSONArray(result);
                    double[] recordarr=new double[3];
                    float G0=0f;
                    for(int i = 0; i < arr.length(); i++){

                        if (arr.getJSONObject(i).getString("id").equals("null")||arr.getJSONObject(i).getString("accel_x").equals("null"))
                        {
                            continue;
                        }
                        float id_v = 0f;
                        double a_x= 0f,a_y= 0f,a_z= 0f,g_x= 0f,g_y= 0f,g_z = 0f,temp_mean = 0f;
                        float temp_sum = 0f, temp_A = 0f;
                        id_v = Float.parseFloat(arr.getJSONObject(i).getString("id"));
//                        a_x = Float.parseFloat(arr.getJSONObject(i).getString("accel_x"));
//                        a_y = Float.parseFloat(arr.getJSONObject(i).getString("accel_y"));
//                        a_z = Float.parseFloat(arr.getJSONObject(i).getString("accel_z"));
//                        g_x = Float.parseFloat(arr.getJSONObject(i).getString("gyro_x"));
//                        g_y = Float.parseFloat(arr.getJSONObject(i).getString("gyro_y"));
//                        g_z = Float.parseFloat(arr.getJSONObject(i).getString("gyro_z"));
//                        temp_mean = Math.abs((a_x+a_y+a_z+g_x+g_y+g_z)/6);
                        a_x = Double.parseDouble(arr.getJSONObject(i).getString("accel_x"));
                        a_y = Double.parseDouble(arr.getJSONObject(i).getString("accel_y"));
                        a_z = Double.parseDouble(arr.getJSONObject(i).getString("accel_z"));

                        temp_sum = (float)(Math.pow(a_x,2)+ Math.pow(a_y,2)+ Math.pow(a_z,2));
                        temp_A = (float)Math.pow(temp_sum,0.5);

                        //判断是否是深度睡眠 e.g. 实例：合加速度>11。1为深度睡眠
                        if(i%2==0) {//even
                            recordarr[0]=a_x;
                            recordarr[1]=a_y;
                            recordarr[2]=a_z;
                            G0=temp_A;
                        }
                        else {
                            float Gu=(float)0.5*(G0+temp_A);
                            float t_sum=(float)(Math.pow(G0-Gu, 2))+(float)(Math.pow(temp_A-Gu, 2));
                            float std=(float)Math.pow(0.5*t_sum, 0.5);
                            if(std>0.0225&&Math.abs(Gu-temp_A)>1) {
                                DeepSleepF++;
                            }
                            else {
                                REMF++;
                            }
                        }

                        dataVals.add(new Entry(id_v,temp_A));
                    }
                    Log.d(TAG, "FetchMotion: Fetch Success");
                } catch (JSONException e) {
                    Log.e(TAG, "FetchMotion: JSON Error", e);
                }
            } else {
                Log.d(TAG, "FetchMotion: fetchData.onComplete() Error");
            }
        } else {
            Log.d(TAG, "FetchMotion: fetchData.startFetch() Error");
        }
        return  dataVals;
    }
}