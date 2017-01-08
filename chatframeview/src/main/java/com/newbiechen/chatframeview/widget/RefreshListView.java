package com.newbiechen.chatframeview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by NewBiechen on 2017/1/6.
 *
 *
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
        return mListView;
    }

    @Override
    public boolean isTop() {
        return mListView.getFirstVisiblePosition() == 0 ? true : false;
    }

    public void setAdapter(ListAdapter adapter){
        mListView.setAdapter(adapter);
    }

    public ListView getListView(){
        return mListView;
    }
}
