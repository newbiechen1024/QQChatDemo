package com.newbiechen.chatframeview.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newbiechen.chatframeview.R;
import com.newbiechen.chatframeview.adapter.EmojiAdapter;
import com.newbiechen.chatframeview.base.BaseAdapter;
import com.newbiechen.chatframeview.base.BaseFragment;
import com.newbiechen.chatframeview.entity.EmojiEntity;
import com.newbiechen.chatframeview.utils.EmojiHandler;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by PC on 2016/12/24.
 */

public class EmojiFragment extends BaseFragment {
    private static final String TAG = "EmpojiFragment";
    //每行显示多少个Emoji表情
    private static final int EMOJI_COL = 8;

    private RecyclerView mRvContent;
    private EmojiAdapter mEmojiAdapter;

    @Override
    protected View onCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(
                R.layout.fragment_face,container,false);
        return v;
    }

    @Override
    protected void initView() {
        mRvContent = getViewById(R.id.face_rv_content);

    }

    @Override
    protected void initWidget() {
        setUpRecyclerView();
    }

    private void setUpRecyclerView(){
        mEmojiAdapter = new EmojiAdapter();
        //添加所有存在的Emoji
        mEmojiAdapter.addItems(EmojiHandler.getEmojiCodesList());

        mRvContent.setLayoutManager(new GridLayoutManager(getContext(),EMOJI_COL));
        mRvContent.setAdapter(mEmojiAdapter);
    }


    @Override
    protected void initListener() {
        mEmojiAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void itemClick(View view, int pos) {
                EventBus.getDefault().
                        post(new EmojiEvent(mEmojiAdapter.getItem(pos)));
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    public static class EmojiEvent{
        private EmojiEntity emojiEntity;

        public EmojiEvent(EmojiEntity emojiEntity) {
            this.emojiEntity = emojiEntity;
        }

        public EmojiEntity getEmojiEntitiy() {
            return emojiEntity;
        }
    }
}
