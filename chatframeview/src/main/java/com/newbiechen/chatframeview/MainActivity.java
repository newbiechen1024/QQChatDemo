package com.newbiechen.chatframeview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.newbiechen.chatframeview.widget.ChatFrameView;
import com.newbiechen.chatframeview.widget.RefreshLayout;
import com.newbiechen.chatframeview.widget.RefreshListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private TextView mTvContent;
    private ChatFrameView mChatFrame;
    private RefreshListView mLv;
    private ArrayList<String> mStrList;
    private Handler mHandler = new Handler();
    private int mKeyboardHeight = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLv = (RefreshListView) findViewById(R.id.main_lv);
        mStrList = new ArrayList<>();
        for (int i=0; i<20; ++i) {
            mStrList.add("测试：" + i);
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,mStrList);
        mLv.setAdapter(adapter);
        mLv.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mLv.refreshComplete();
                        Log.d(TAG, "run: 测试");
                    }
                },3000);
            }
        });
        mChatFrame = (ChatFrameView) findViewById(R.id.main_chat_frame);
     /*   mChatFrame.setOnChatFrameListener(new ChatFrameView.OnChatFrameListener() {
            @Override
            public void sendMessage(CharSequence text) {
            }

            @Override
            public void onKeyboardOpen(int keyboardHeight) {
                mKeyboardHeight = keyboardHeight;
                mLv.getListView().smoothScrollBy(mKeyboardHeight,100);
            }

            @Override
            public void onKeyboardClose() {

                Log.d("MainActivity",""+mLv.getListView().getLastVisiblePosition());
                if (mLv.getListView().getLastVisiblePosition() != adapter.getCount()-1){
                    mLv.getListView().smoothScrollBy(-mKeyboardHeight,100);
                }
            }

            @Override
            public void onEmojiSelected(EmojiEntity emoji) {

            }

            @Override
            public void onFaceSelected(FaceEntity face) {

            }
        });*/
    }

    @Override
    protected void onDestroy() {
        mChatFrame.removeChatFrame();
        super.onDestroy();
    }
}
