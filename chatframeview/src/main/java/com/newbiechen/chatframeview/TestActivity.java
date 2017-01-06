package com.newbiechen.chatframeview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;

public class TestActivity extends Activity implements OnClickListener {
    private Button btn_up, btn_down, btn_stop; // 三个按钮
    private ListView listview;
    private Adapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        int resId = this.getResources().
                getIdentifier("emoji","drawable",this.getPackageName());
        Log.d("TestActivity",resId+"");
    }

    private void init() {
        btn_up.setOnClickListener(this);
        btn_down.setOnClickListener(this);
        btn_stop.setOnClickListener(this);

    }

    private void findBy() {
        btn_up = (Button) findViewById(R.id.btn_scroll_up);
        btn_down = (Button) findViewById(R.id.btn_scroll_down);
        btn_stop = (Button) findViewById(R.id.btn_scroll_stop);

        listview = (ListView) findViewById(R.id.listview);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_scroll_down:
                listScrollDown();
                break;
            case R.id.btn_scroll_up:
                listScrollUp();
                break;
            case R.id.btn_scroll_stop:
                listScrollOff();
                break;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            handler.removeCallbacks(run_scroll_down);
            handler.removeCallbacks(run_scroll_up);
        }
    };

    /**
     * 向上滚动
     */
    public void listScrollUp() {
        listScrollOff();
        handler.postDelayed(run_scroll_up, 0);
    }

    /**
     * 向下滚动
     */
    public void listScrollDown() {
        listScrollOff();
        handler.postDelayed(run_scroll_down, 0);
    }

    /**
     * 停止滚动
     */
    public void listScrollOff() {
        handler.removeCallbacks(run_scroll_down);
        handler.removeCallbacks(run_scroll_up);
    }

    Runnable run_scroll_up = new Runnable() {
        @Override
        public void run() {
            /**
             * public void smoothScrollBy (int distance, int duration)
             *
             * Added in API level 8 Smoothly scroll by distance pixels over duration milliseconds.
             *
             * Parameters
             *     distance Distance to scroll in pixels.
             *     duration Duration of the scroll animation in milliseconds.
             */
            listview.smoothScrollBy(1, 10);
            handler.postDelayed(run_scroll_up, 10);
        }
    };
    Runnable run_scroll_down = new Runnable() {
        @Override
        public void run() {
            listview.smoothScrollBy(-1, 10);
            handler.postDelayed(run_scroll_down, 10);
        }
    };
}