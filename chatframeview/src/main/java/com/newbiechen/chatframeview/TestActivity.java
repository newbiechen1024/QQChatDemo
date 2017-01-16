package com.newbiechen.chatframeview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Adapter;
import android.widget.ListView;

import java.util.ArrayList;

public class TestActivity extends Activity{
    private static final String TAG = "TestActivity";
    private ListView listview;
    private Handler mHandler = new Handler();
    private Adapter adapter;
    private ArrayList<String> mStrList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
/*        RefreshHeaderView refreshHeaderView = (RefreshHeaderView) findViewById(R.id.test_refresh);*/
    }

}