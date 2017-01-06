package com.newbiechen.chatframeview.widget;

import static com.newbiechen.chatframeview.fragment.EmojiFragment.EmojiEvent;
import static com.newbiechen.chatframeview.fragment.FaceFragment.FaceEvent;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.newbiechen.chatframeview.R;
import com.newbiechen.chatframeview.entity.EmojiEntity;
import com.newbiechen.chatframeview.entity.FaceEntity;
import com.newbiechen.chatframeview.fragment.FaceCategoryFragment;
import com.newbiechen.chatframeview.fragment.ToolFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by PC on 2016/12/8.
 * 问题：changeToolboxState这个方法有硬伤，逻辑有问题
 * 如何将keyboard融入进去才是大问题
 */

public class ChatFrameView extends RelativeLayout implements
        KeyboardStateHelper.OnKeyboardStateChangeListener{
    /**
     * 当前ChatFrame的状态参数
     */
    public static final int STATE_HIDE = 0;
    public static final int STATE_FACE = 1;
    public static final int STATE_MORE = 2;
    public static final int STATE_BOARD = 3;

    private Context mContext;
    private KeyboardStateHelper mKeyboardHelper;
    private Fragment mToolFragment;
    private Fragment mFaceCategoryFragment;
    private OnChatFrameListener mListener;

    private FrameLayout mFlBox;
    private CheckBox mCbFace;
    private CheckBox mCbMore;
    private EditText mEtInput;
    private Button mBtnSend;

    private FragmentManager mFm;
    private View mRootView;

    private int mFrameState = STATE_HIDE;
    private String mCurEditStr = "";

    public ChatFrameView(Context context) {
        this(context,null);
    }

    public ChatFrameView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ChatFrameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init(){
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.view_chat_frame,this,false);
        addView(mRootView);

        initView();
        initWidget();
        initListener();
    }

    private void initView(){
        mCbFace = getViewById(R.id.frame_cb_face);
        mCbMore = getViewById(R.id.frame_cb_more);
        mEtInput = getViewById(R.id.frame_et_message);
        mBtnSend = getViewById(R.id.frame_btn_send);
        mFlBox = getViewById(R.id.frame_fl_box);
    }

    private void initWidget(){
        //注册EventBus
        EventBus.getDefault().register(this);

        AppCompatActivity mActivity = (AppCompatActivity) mContext;
        mKeyboardHelper = new KeyboardStateHelper(mActivity);
        mFm = mActivity.getSupportFragmentManager();

        //初始化点击事件状态
        changeSendState();

        //初始化Box中的Fragment
        initFragment();
    }

    private void initFragment(){
        //当销毁重绘的时候获取当前
        mFaceCategoryFragment = mFm.findFragmentByTag(FaceCategoryFragment.TAG);
        mToolFragment = mFm.findFragmentByTag(ToolFragment.TAG);

        if(mFaceCategoryFragment == null && mToolFragment == null){
            mFaceCategoryFragment = new FaceCategoryFragment();
            mToolFragment = new ToolFragment();

            FragmentTransaction ft = mFm.beginTransaction();
            ft.add(R.id.frame_fl_box,mFaceCategoryFragment,FaceCategoryFragment.TAG);
            ft.hide(mFaceCategoryFragment);
            ft.add(R.id.frame_fl_box,mToolFragment,ToolFragment.TAG);
            ft.hide(mToolFragment);
            ft.commit();
        }
    }

    private void initListener(){
        mKeyboardHelper.setOnKeyboardStateChangeListener(this);
        mEtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCurEditStr = s.toString().trim();
                changeSendState();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mBtnSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null && !mCurEditStr.equals("")){
                    mListener.sendMessage(mCurEditStr);
                    mEtInput.getText().clear();
                }
            }
        });

        mCbFace.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToolboxState(STATE_FACE);
            }
        });

        mCbMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToolboxState(STATE_MORE);
            }
        });
    }



    /**
     * 设置Button是否可点击
     */
    private void changeSendState(){
        if (mCurEditStr.equals("")){
            mBtnSend.setSelected(false);
        }
        else{
            mBtnSend.setSelected(true);
        }
    }

    /**
     * 改变当前Toolbox的状态
     * 切换问题没有解决
     * @param state
     */
    private void changeToolboxState(final int state){
          //判断是否ViewPager打开了，如果打开了，并且等于当前状态，就表示关闭
        if (isBoxOpening() && state == mFrameState){
            hideToolBox();
        }
        else{
            //首先关闭软键盘
            mKeyboardHelper.hideKeyboard();
            //首先关闭软件盘后再打开工具箱
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mFlBox.setVisibility(VISIBLE);
                    changeFragment(state);
                }
            }, 50);

            changeFrameState(state);
        }
    }

    /**
     * 隐藏ChatFrame的工具栏
     */
    private void hideToolBox(){
        mFlBox.setVisibility(GONE);

        //将所有的Fragment隐藏
        FragmentTransaction ft = mFm.beginTransaction();
        ft.hide(mFaceCategoryFragment);
        ft.hide(mToolFragment);
        ft.commit();

        //将按钮恢复原状
        mCbFace.setChecked(false);
        mCbMore.setChecked(false);
    }

    /**
     * 改变
     * @param state
     */
    private void changeFrameState(int state){
        //修改当前状态
        mFrameState = state;

        //将CheckBox的状态修改回来
        mCbFace.setChecked(state == STATE_FACE);
        mCbMore.setChecked(state == STATE_MORE);
    }

    private void changeFragment(int state){
        FragmentTransaction ft = mFm.beginTransaction();
        switch (state){
            case STATE_FACE:
                //隐藏所有Fragment
                ft.hide(mToolFragment);
                ft.show(mFaceCategoryFragment);
                ft.commit();
                break;
            case STATE_MORE:
                ft.hide(mFaceCategoryFragment);
                ft.show(mToolFragment);
                ft.commit();
                break;
        }
    }

    /**
     * 判断Toolbox是否开启
     * @return
     */
    private boolean isBoxOpening(){
        return mFlBox.getVisibility() == View.VISIBLE;
    }

    private <V> V getViewById(int id){
        return (V) mRootView.findViewById(id);
    }

    @Override
    public void onKeyboardOpen(int keyboardHeight) {
        hideToolBox();
        mFrameState = STATE_BOARD;
        mListener.onKeyboardOpen(keyboardHeight);
    }

    @Override
    public void onKeyboardClosed() {
        if (mFrameState == STATE_BOARD){
            mFrameState = STATE_HIDE;
        }
        mListener.onKeyboardClose();
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mCurEditStr = savedState.curEditStr;
        mFrameState = savedState.frameState;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.curEditStr = mCurEditStr;
        savedState.frameState = mFrameState;
        return savedState;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEmojiEvent(EmojiEvent event) {
        EmojiEntity entity = event.getEmojiEntitiy();
        mEtInput.getText().
                append(entity.getValue());

        if (mListener != null){
            mListener.onEmojiSelected(entity);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFaceEvent(FaceEvent faceEvent){
        if (mListener != null){
            mListener.onFaceSelected(faceEvent.getFaceEntity());
        }

    }

    static class SavedState extends BaseSavedState {
        String curEditStr;
        int frameState;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            curEditStr = in.readString();
            frameState = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(curEditStr);
            dest.writeInt(frameState);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    public interface OnChatFrameListener{
        //点击发送的监听
        void sendMessage(CharSequence text);

        //键盘监听
        void onKeyboardOpen(int keyboardHeight);
        void onKeyboardClose();

        //表情监听
        void onEmojiSelected(EmojiEntity emoji);
        void onFaceSelected(FaceEntity face);
    }

    public abstract class OnKeyboardListener implements OnChatFrameListener{

        @Override
        public void onEmojiSelected(EmojiEntity emoji) {
        }

        @Override
        public void onFaceSelected(FaceEntity face) {
        }
    }

    public abstract class OnSendListener implements OnChatFrameListener{

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
    }

    public abstract class OnFaceListener implements OnChatFrameListener{

        @Override
        public void onKeyboardOpen(int keyboardHeight) {

        }

        @Override
        public void onKeyboardClose() {

        }
    }
    /*****************************公共方法********************************************/
    /**
     * 设置监听器
     * @param listener
     */
    public void setOnChatFrameListener(OnChatFrameListener listener){
        mListener = listener;
    }

    /**
     * 恢复聊天框到底部
     */
    public void hideChatFrame(){
        mKeyboardHelper.hideKeyboard();
        hideToolBox();
    }

    /**
     * 移除聊天框
     */
    public void removeChatFrame(){
        mKeyboardHelper.removeKeyboardStateHelper();
        EventBus.getDefault().unregister(this);
    }
}
