package com.newbiechen.chatframeview;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by PC on 2016/12/9.
 */

public class TestActivity extends AppCompatActivity {
    private EditText mEtSend;
    private TextView mTvShow;
    private Button mBtnEmoji;
    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mEtSend = (EditText) findViewById(R.id.test_et_send);
        mTvShow = (TextView) findViewById(R.id.test_tv_show);
        mBtnEmoji = (Button) findViewById(R.id.test_btn_emoji);

        mEtSend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final CharSequence data = s;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mTvShow.setText(data);
                    }
                },30);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mBtnEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int emoji = 0x1f60a;
                char [] code = Character.toChars(emoji);
                String s = new String(code);
                mEtSend.getText().append(s);
            }
        });
    }
}
