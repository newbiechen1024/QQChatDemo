package com.newbiechen.chatframeview.base;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by PC on 2016/9/9.
 */
public abstract class BaseFragment extends Fragment {
    //Fragment的根布局对象
    private View mContentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = onCreateContentView(inflater,container,savedInstanceState);
        initView();
        initWidget();
        initListener();
        processLogic(savedInstanceState);
        return mContentView;
    }

    protected abstract View onCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    protected abstract void initView();

    protected abstract void initWidget();

    protected abstract void initListener();

    protected abstract void processLogic(Bundle savedInstanceState);

/****************************************公共方法*****************************************************/
    /**
     * 获取Fragment的根布局
     * @return View
     */
    public View getContentView(){
        return mContentView;
    }

    /**
     * 获取根布局的子View
     * @param id
     * @return View
     */
    public <VT extends View> VT getViewById(int id){
        return (VT) mContentView.findViewById(id);
    }

}
