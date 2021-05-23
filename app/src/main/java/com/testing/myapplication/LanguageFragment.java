package com.testing.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Utils.Utils;
import Utils.Utils.*;

import static Utils.Utils.changeAppLanguage;
import static Utils.Utils.getLanguage;
import static Utils.Utils.setLanguage;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LanguageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LanguageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Activity activity;
    private Toolbar toolbar;
    private static final String TAG = "LanguageActivity";
    private List<MyTextItem> data = new ArrayList<>();
    private String languages[] = {"en","zh"};
    private String attributes[] = {"English","中文"};
    private int visiblilities[] = {ImageView.INVISIBLE,ImageView.INVISIBLE};
    private MyAdapter adapter;


    public LanguageFragment() {
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
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_language, container, false);
        activity = getActivity();
        ((MainActivity)activity).transactKey(R.id.bt_user);
        String str = getLanguage();
        int i = 0;
        for(i=0; i<languages.length; i++){
            if (languages[i].equals(str)) {
                visiblilities[i]=ImageView.VISIBLE;
            }
        }

        for(i=0; i<languages.length; i++) {
            Resources resource = getResources();
            data.add(new MyTextItem(attributes[i],R.drawable.pick,visiblilities[i]));
        }
        adapter = new MyAdapter(activity, R.layout.item_setting, data);
        ListView listView = (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = getLanguage();
                int i = 0;
                for(i=0; i<languages.length; i++){
                    if (languages[i].equals(str)) {
                        break;
                    }
                }

                if (i!=position) {
                    switch (position) {
                        case 0:
                            adapter.setVisibility(position,ImageView.VISIBLE);
                            adapter.setVisibility(i,ImageView.INVISIBLE);
                            Log.d(TAG, "onItemClick:"+i);
                            setLanguage(activity, "en");
                            changeAppLanguage(activity);
                            activity.recreate();
                            break;
                        case 1:
                            adapter.setVisibility(position,ImageView.VISIBLE);
                            adapter.setVisibility(i,ImageView.INVISIBLE);
                            Log.d(TAG, "onItemClick:"+i);
                            setLanguage(activity, "zh");
                            changeAppLanguage(activity);
                            activity.recreate();
                            break;
                        default:
                            break;
                    }
                    //TODO
                    Toast.makeText(activity, getResources().getString(R.string.language_prompt), Toast.LENGTH_SHORT).show();
//                    recreate();
                }
            }
        });
        listView.setDivider(null);

        return view;
    }

}