package com.newbiechen.chatframeview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;

import com.newbiechen.chatframeview.widget.RefreshLayout;
import com.newbiechen.chatframeview.widget.RefreshListView;

public class TestActivity extends Activity{
    private ListView listview;
    private Handler mHandler = new Handler();
    private Adapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        final RefreshListView listView = (RefreshListView)findViewById(R.id.test_refresh);
        listView.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listView.refreshComplete();
                    }
                },3000);
            }
        });
    }

}