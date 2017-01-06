package com.newbiechen.chatframeview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by PC on 2017/1/6.
 */

public class RefreshListView extends RefreshLayout<ListView> {
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    public RefreshListView(Context context) {
        super(context);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public ListView createContentView(Context context) {
        mListView = new ListView(context);
        ArrayList<String> strings = new ArrayList<>();
        for(int i=0; i<20; ++i){
            strings.add("测试+"+i);
        }
        mAdapter = new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,strings);
        mListView.setAdapter(mAdapter);
        return mListView;
    }

    @Override
    public boolean isTop() {
        return mListView.getFirstVisiblePosition() == 0 ? true : false;
    }
}
