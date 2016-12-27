package com.newbiechen.chatframeview.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newbiechen.chatframeview.R;
import com.newbiechen.chatframeview.adapter.EmojiAdapter;
import com.newbiechen.chatframeview.utils.EmojiHandler;

/**
 * Created by PC on 2016/12/24.
 */

public class EmojiFragment extends Fragment {
    private static final String TAG = "EmpojiFragment";
    private static final int EMOJI_COL = 8;
    private RecyclerView mRvContent;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_face,container,false);
        initView(v);
        return v;
    }

    private void initView(View view){
        mRvContent = (RecyclerView) view.findViewById(R.id.face_rv_content);
        setUpRecyclerView();
    }

    private void setUpRecyclerView(){
        EmojiAdapter emojiAdapter = new EmojiAdapter();
        emojiAdapter.addItems(EmojiHandler.getEmojiCodesList());

        mRvContent.setLayoutManager(new GridLayoutManager(getContext(),EMOJI_COL));
        mRvContent.setAdapter(emojiAdapter);
    }
}
