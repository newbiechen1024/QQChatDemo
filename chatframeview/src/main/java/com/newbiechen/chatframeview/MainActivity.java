package com.newbiechen.chatframeview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.newbiechen.chatframeview.utils.EmojiHandler;
import com.newbiechen.chatframeview.widget.ChatFrameView;

public class MainActivity extends AppCompatActivity {
    private TextView mTvContent;
    private ChatFrameView mChatFrame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvContent = (TextView) findViewById(R.id.main_tv_show);
        mChatFrame = (ChatFrameView) findViewById(R.id.main_chat_frame);
        mChatFrame.setOnChatFrameListener(new ChatFrameView.OnChatFrameListener() {
            @Override
            public void sendMessage(CharSequence text) {
                mTvContent.setText(text);
            }
        });
    }

    @Override
    protected void onDestroy() {
        mChatFrame.removeChatFrame();
        super.onDestroy();
    }
}
