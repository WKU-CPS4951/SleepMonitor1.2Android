package com.testing.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;


//class LineChartXAxisValueFormatter extends IndexAxisValueFormatter {
//
//    @Override
//    public String getFormattedValue(float value) {
//
//        // Convert float value to date string
//        // Convert from seconds back to milliseconds to format time  to show to the user
//        long emissionsMilliSince1970Time = ((long) value) * 1000;
//
//        // Show time in local version
//        Date timeMilliseconds = new Date(emissionsMilliSince1970Time);
////        DateFormat dateTimeFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
//        DateFormat dateTimeFormat = DateFormat.getDateTimeInstance();
//
//        return dateTimeFormat.format(timeMilliseconds);
//    }
//}

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageButton[] bt_bottom = new ImageButton[3];
    Button Home;
    Button Temperature;
    Button Motion;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_bottom[0] = findViewById(R.id.bt_home);
        bt_bottom[0].setOnClickListener(this);

        bt_bottom[1] = findViewById(R.id.bt_sleep);
        bt_bottom[1].setOnClickListener(this);

        bt_bottom[2] = findViewById(R.id.bt_user);
        bt_bottom[2].setOnClickListener(this);

        replaceFragment(new MainbodyFragment());
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        String backStateName = fragment.getClass().getName();
//        fragmentManager.popBackStackImmediate(backStateName,FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(backStateName);
        transaction.replace(R.id.frag_mainbody,fragment);
        transaction.commit();
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_home:
                replaceFragment(new MainbodyFragment());
                break;
            case R.id.bt_sleep:
//                Toast.makeText(this, "Still Developing",Toast.LENGTH_SHORT).show();
                replaceFragment(new SleepQualityFragment());
                break;
            case R.id.bt_user:
                replaceFragment(new UserFragment());
                break;
            default:break;
        }
    }


    public void transactKey(int id){
        for (ImageButton bt: bt_bottom) {
            if (bt.getId() != id) bt.setEnabled(true);
            else bt.setEnabled(false);
        }
    }
}