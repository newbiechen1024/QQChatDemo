package com.newbiechen.chatframeview.widget;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import com.newbiechen.chatframeview.utils.ScreenUtils;

/**
 * Created by PC on 2016/11/20.
 */

public class KeyboardStateHelper implements ViewTreeObserver.OnGlobalLayoutListener{
    private static final String TAG = "KeyboardStateHelper";
    private AppCompatActivity mActivity;
    private OnKeyboardStateChangeListener mStateChangeListener;
    private InputMethodManager mImm;
    private boolean isKeyboardUp = false;

    public KeyboardStateHelper(AppCompatActivity activity) {
        mActivity = activity;
        mImm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        mActivity.getWindow().getDecorView().getViewTreeObserver()
                .addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        //判断keyboard是否显示或则隐藏
        //原理：整个Screen由StatusBar(导航栏)+ActionBar(工具栏)+NavBar(虚拟键盘)+ContentView组成
        //     就是 screen = s + a + n + c;
        //     当键盘显示的时候，ContentView会缩小。
        //     那么最后就是 screen > s + a + n + c  转换一下 screen - c > s + a + n
        //     otherHeight = s + a + ｎ    distance = screen - c
        //     最终公式就是 distance > otherHeight
        int otherHeight = ScreenUtils.getStatusBarHeight(mActivity) +
                ScreenUtils.getActionbarHeight(mActivity) + ScreenUtils.getNavBarHeight(mActivity);
        int distance = ScreenUtils.getScreenHeight(mActivity) -
                ScreenUtils.getContentLayoutHeight(mActivity);
        //如果之间的距离大于其他零件的距离，说明根布局缩小了
        if (distance > otherHeight && !isKeyboardUp){
            isKeyboardUp = true;
            if (mStateChangeListener != null){
                int keyboardHeight = distance - otherHeight;
                mStateChangeListener.onKeyboardOpen(keyboardHeight);
            }
        }
        else if (distance <= otherHeight && isKeyboardUp){
            isKeyboardUp = false;
            if (mStateChangeListener != null){
                mStateChangeListener.onKeyboardClosed();
            }
        }
    }

    public interface OnKeyboardStateChangeListener {
        void onKeyboardOpen(int keyboardHeight);
        void onKeyboardClosed();
    }

    /*********************************公共方法*************************************/
    /**
     * 设置软件盘的监听
     * @param listener
     */
    public void setOnKeyboardStateChangeListener(OnKeyboardStateChangeListener listener){
        mStateChangeListener = listener;
    }

    /**
     * 获取当前软键盘的状态
     * @return
     */
    public boolean getKeyboardState(){
        return isKeyboardUp;
    }

    /**
     * 隐藏软键盘
     */
    public void hideKeyboard(){
        if (isKeyboardUp){
            mImm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 移除监听器
     */
    public void removeKeyboardStateHelper(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN){
            mActivity.getWindow().getDecorView().getViewTreeObserver()
                    .removeOnGlobalLayoutListener(this);
        }
        else {
            mActivity.getWindow().getDecorView().getViewTreeObserver()
                    .removeGlobalOnLayoutListener(this);
        }
    }
}
