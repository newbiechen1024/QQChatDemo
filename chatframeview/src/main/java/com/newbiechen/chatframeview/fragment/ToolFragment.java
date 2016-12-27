package com.newbiechen.chatframeview.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newbiechen.chatframeview.R;


/**
 * Created by PC on 2016/11/26.
 */

public class ToolFragment extends Fragment {
    public static final String TAG = "ToolFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tool,container,false);
        return v;
    }
}
