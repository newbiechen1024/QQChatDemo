package com.newbiechen.chatframeview.utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

/**
 * Created by newbiechen on 2016/11/19.
 */

public class ScreenUtils {

    /**
     * 获取导航栏的大小
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context){
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height","dimen","android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 获取虚拟键盘的大小
     * @param activity
     * @return
     */
    public static int getNavBarHeight(AppCompatActivity activity){
        return getScreenHeight(activity) - getAppHeight(activity);
    }

    /**
     * 获取Actionbar的大小
     * @param activity
     * @return
     */
    public static int getActionbarHeight(AppCompatActivity activity){
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null){
            return actionBar.getHeight();
        }
        else {
            return 0;
        }
    }

    /**
     * 获取根布局的大小
     * @param activity
     * @return
     */
    public static int getContentLayoutHeight(AppCompatActivity activity){
        return activity.findViewById(android.R.id.content).getMeasuredHeight();
    }

    /**
     * 获取手机显示App区域的大小（头部导航栏+ActionBar+根布局），不包括虚拟按钮
     * @param context
     * @return
     */
    public static int getAppHeight(Context context){
        DisplayMetrics metrics =  context.
                getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }

    /**
     * 获取整个手机屏幕的大小
     * 必须在onWindowFocus方法之后使用
     * @param activity
     * @return
     */
    public static int getScreenHeight(AppCompatActivity activity){
        return activity.getWindow().getDecorView().getHeight();
    }
}
