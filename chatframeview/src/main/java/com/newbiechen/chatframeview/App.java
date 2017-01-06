package com.newbiechen.chatframeview;

import android.app.Application;

import com.newbiechen.chatframeview.utils.EmojiHandler;

/**
 * Created by PC on 2016/12/24.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        EmojiHandler.init(this);
    }
}
