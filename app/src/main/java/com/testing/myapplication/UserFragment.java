package com.testing.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "UserFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Thread thread;
    MainActivity activity;

    int count = 0;
    private static Intent EastEgg;
    private List<MyTextItem> data = new ArrayList<>();


    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
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

        Resources resource = getResources();
        //TODO
        data.add(new MyTextItem(resource.getString(R.string.language)));
//        data.add(new MyTextItem(resource.getString(R.string.privacy)));
        data.add(new MyTextItem(resource.getString(R.string.version), getResources().getString(R.string.version_content)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        activity = (MainActivity) this.getActivity();
        activity.transactKey(R.id.bt_user);

        MyAdapter adapter = new MyAdapter(activity, R.layout.item_setting, data);
        ListView listView = (ListView) activity.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        activity.replaceFragment(new LanguageFragment());
                        break;
                    case 1:
                        Log.d(TAG, "onItemClick: ");
                        count++;
                        if (count == 2) {
                            if(EastEgg != null) {
                                stopEastEgg();
                                count = 0;
                            }
                            else
                                EastEgg = play();
                        }
                        if (count == 4 && EastEgg != null) {
                            stopEastEgg();
                            count = 0;
                        }
                        break;
                    default:break;
                }
            }
        });
        listView.setDivider(null);

    }

    private Intent play(){
//      //无限音量
//        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
//        thread = new Thread() {
//            public void run() {
//                //这儿是耗时操作，完成之后更新UI；
//                while(true){
//                    final int m = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//                    getActivity().runOnUiThread(new Runnable(){
//                        @Override
//                        public void run() {
//                            //更新UI
//                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, m, AudioManager.FLAG_PLAY_SOUND);
//                        }
//                    });
//                    try {
//                        sleep(500);
//                    } catch (InterruptedException e) {}
//                }
//
//            }
//        };
//        thread.start();

        final Intent intent = new Intent(activity,WhiteNoise.class);
        activity.startService(intent);
        return intent;
    }

    private void stopEastEgg(){
        activity.stopService(EastEgg);
        EastEgg = null;
//        thread.interrupt();
    }

}