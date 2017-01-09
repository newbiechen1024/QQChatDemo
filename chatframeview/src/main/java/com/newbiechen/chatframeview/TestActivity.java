package com.newbiechen.chatframeview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.newbiechen.chatframeview.widget.RefreshLayout;
import com.newbiechen.chatframeview.widget.RefreshListView;

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
        final RefreshListView listView = (RefreshListView)findViewById(R.id.test_refresh);
        mStrList = new ArrayList<>();
        for (int i=0; i<20; ++i) {
            mStrList.add("测试：" + i);
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,mStrList);
        listView.setAdapter(adapter);
        listView.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh: 测试");
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