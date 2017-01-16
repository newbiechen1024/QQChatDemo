package com.newbiechen.chatframeview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.newbiechen.chatframeview.adapter.ChatMsgAdapter;
import com.newbiechen.chatframeview.entity.ChatMsgEntity;
import com.newbiechen.chatframeview.entity.EmojiEntity;
import com.newbiechen.chatframeview.entity.FaceEntity;
import com.newbiechen.chatframeview.utils.DateUtils;
import com.newbiechen.chatframeview.widget.ChatFrameView;
import com.newbiechen.chatframeview.widget.ChatRefreshRecyclerView;
import com.newbiechen.chatframeview.widget.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ChatFrameView mChatFrame;
    private ChatRefreshRecyclerView mLv;
    private ChatMsgAdapter mAdapter;
    private final List<ChatMsgEntity> mChatMessageItems = new ArrayList<>();
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mChatFrame = (ChatFrameView) findViewById(R.id.main_chat_frame);
        mLv = (ChatRefreshRecyclerView) findViewById(R.id.main_lv);

        setUpListView();

        mChatFrame.setOnChatFrameListener(new ChatFrameView.OnChatFrameListener() {
            @Override
            public void sendMessage(CharSequence text) {
                String date = DateUtils.getDateStr(DateUtils.FORAMT_DATE+" "+DateUtils.FORMAT_TIME);
                mAdapter.addChatMsgItems(new ChatMsgEntity(date,"",
                        text.toString(),false,ChatMsgEntity.TYPE_TEXT,ChatMsgEntity.STATUS_SUCCEED));
                mLv.getRecyclerView().scrollToPosition(0);
            }

            @Override
            public void onKeyboardOpen(int keyboardHeight) {
            }

            @Override
            public void onKeyboardClose() {
            }

            @Override
            public void onEmojiSelected(EmojiEntity emoji) {

            }

            @Override
            public void onFaceSelected(FaceEntity face) {

            }
        });
    }

    public void setUpListView(){
        mAdapter = new ChatMsgAdapter(this);
        String date = DateUtils.getDateStr(DateUtils.FORAMT_DATE+" "+DateUtils.FORMAT_TIME);
        mChatMessageItems.add(new ChatMsgEntity(date,"","http://www.google.com支持http、https、svn、ftp开头的链接",false,ChatMsgEntity.TYPE_TEXT,ChatMsgEntity.STATUS_SUCCEED));
        mChatMessageItems.add(new ChatMsgEntity(date,"","也可以这样www.csdn.com",true,ChatMsgEntity.TYPE_TEXT,ChatMsgEntity.STATUS_SUCCEED));
        mChatMessageItems.add(new ChatMsgEntity(date,"","手机号也没问题12345678910",true,ChatMsgEntity.TYPE_TEXT,ChatMsgEntity.STATUS_SUCCEED));
        mChatMessageItems.add(new ChatMsgEntity(date,"","邮箱当然也可以13456789@csdn.com",true,ChatMsgEntity.TYPE_TEXT,ChatMsgEntity.STATUS_SUCCEED));
        mChatMessageItems.add(new ChatMsgEntity(date,"","还有发送成功和失败的效果~~",false,ChatMsgEntity.TYPE_TEXT,ChatMsgEntity.STATUS_FAIL));
        mChatMessageItems.add(new ChatMsgEntity(date,"","正在发送中哦",true,ChatMsgEntity.TYPE_TEXT,ChatMsgEntity.STATUS_SENDING));
        mChatMessageItems.add(new ChatMsgEntity(date,"","哈哈~~~",false,ChatMsgEntity.TYPE_TEXT,ChatMsgEntity.STATUS_SUCCEED));
        mChatMessageItems.add(new ChatMsgEntity(date,"","可以，可以",true,ChatMsgEntity.TYPE_TEXT,ChatMsgEntity.STATUS_SUCCEED));
        mChatMessageItems.add(new ChatMsgEntity(date,"","github地址:https://github.com/newbiechen1024/ChatDemo",true,ChatMsgEntity.TYPE_TEXT,ChatMsgEntity.STATUS_SUCCEED));mAdapter.addItems(mChatMessageItems);
        mLv.setAdapter(mAdapter);

        mLv.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(final int refreshHeight) {
                String date = DateUtils.getDateStr(DateUtils.FORAMT_DATE+" "+DateUtils.FORMAT_TIME);
                List<ChatMsgEntity> historyEntity = new ArrayList<ChatMsgEntity>();
                historyEntity.add(new ChatMsgEntity(date,"","可以，可以",true,ChatMsgEntity.TYPE_TEXT,ChatMsgEntity.STATUS_SUCCEED));
                historyEntity.add(new ChatMsgEntity(date,"","github地址:https://github.com/newbiechen1024/ChatDemo",true,ChatMsgEntity.TYPE_TEXT,ChatMsgEntity.STATUS_SUCCEED));
                mAdapter.addItems(historyEntity);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mLv.refreshComplete();
                        mLv.getRecyclerView().scrollBy(0,-refreshHeight);
                    }
                },1000);
        }
        });
    }

    @Override
    protected void onDestroy() {
        mChatFrame.removeChatFrame();
        super.onDestroy();
    }
}
