package com.newbiechen.chatframeview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.newbiechen.chatframeview.entity.EmojiEntity;
import com.newbiechen.chatframeview.entity.FaceEntity;
import com.newbiechen.chatframeview.utils.EmojiHandler;
import com.newbiechen.chatframeview.utils.ScreenUtils;
import com.newbiechen.chatframeview.widget.ChatFrameView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView mTvContent;
    private ChatFrameView mChatFrame;
    private ListView mLv;
    private ArrayList<String> mStrList;
    private int mKeyboardHeight = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLv = (ListView) findViewById(R.id.main_lv);
        mStrList = new ArrayList<>();
        for (int i=0; i<20; ++i) {
            mStrList.add("测试：" + i);
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mStrList);
        mLv.setAdapter(adapter);
        mChatFrame = (ChatFrameView) findViewById(R.id.main_chat_frame);
        mChatFrame.setOnChatFrameListener(new ChatFrameView.OnChatFrameListener() {
            @Override
            public void sendMessage(CharSequence text) {
            }

            @Override
            public void onKeyboardOpen(int keyboardHeight) {
                mKeyboardHeight = keyboardHeight;
                mLv.smoothScrollBy(mKeyboardHeight,100);
            }

            @Override
            public void onKeyboardClose() {
                Log.d("MainActivity",""+mLv.getLastVisiblePosition());
                if (mLv.getLastVisiblePosition() != adapter.getCount()-1){
                    mLv.smoothScrollBy(-mKeyboardHeight,100);
                }
            }

            @Override
            public void onEmojiSelected(EmojiEntity emoji) {

            }

            @Override
            public void onFaceSelected(FaceEntity face) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        mChatFrame.removeChatFrame();
        super.onDestroy();
    }
}
