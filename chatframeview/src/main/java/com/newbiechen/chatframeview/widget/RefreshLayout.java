package com.newbiechen.chatframeview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.newbiechen.chatframeview.R;

/**
 * Created by PC on 2016/8/21.
 */
public abstract class RefreshLayout<T extends View> extends ViewGroup{
    private static final String TAG = "RefreshLayout";
    //表示空闲状态
    private static final int STATUS_FREE = 0;
    //表示下拉，准备展开Header刷新状态
    private static final int STATUS_RELEASE_TO_REFRESH = 1;
    //表示正在刷新的状态
    private static final int STATUS_REFRESHING = 2;
    //表示上划，准备回收的状态
    private static final int STATUS_PULL_TO_REFRESH = 3;
    //表示滑动的时间
    private static final int SCROLL_TIME = 600;
    //当用户在刷新时候上滑隐藏Header的时候，为提高切换的流畅性。
    private static final int SHRINK_DISTANCE = -50;

    private View mHeaderView;
    private T mContentView;

    private OnRefreshListener mRefreshListener;
    private Context mContext;
    private Scroller mScroller;

    private int mCurrentStatus;
    //默认的高度
    private int mHeaderHeight;
    //溢出的高度
    private int mOverflowHeight;
    //总高度
    private int mHeaderTotalHeight;
    private int mInterceptDownX;
    private int mInterceptDownY;

    private int mTouchLastX;
    private int mTouchLastY;

    public RefreshLayout(Context context) {
        this(context,null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mScroller = new Scroller(context);
        //初始化三个控件
        mCurrentStatus = STATUS_FREE;
        initView();
    }

    private void initView(){
        mHeaderView = createHeaderView(mContext,this);
        mContentView = createContentView(mContext);
        initHeader();
        //初始化ContentView的Layout，只允许设置为match
        initContentView();
    }

    //创建头部
    private void initHeader(){

        //添加到ViewGroup中
        addView(mHeaderView);
        //直接测量Header
        measureView(mHeaderView);
        mHeaderHeight = mHeaderView.getMeasuredHeight();
    }

    private void measureView(View child){
        ViewGroup.LayoutParams lp = child.getLayoutParams();
        if (lp == null){
            lp = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
        }
        int widthSpec = ViewGroup.getChildMeasureSpec(0,0,lp.width);
        int heightSpec = 0;
        if (lp.height > 0){
            heightSpec = MeasureSpec.makeMeasureSpec(lp.height,MeasureSpec.EXACTLY);
        }
        else {
            heightSpec = MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED);
        }
        child.measure(widthSpec,heightSpec);
    }


    //将ContentView设置为Match全屏
    private void initContentView(){
        mContentView.setLayoutParams(
                new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT));
        addView(mContentView);
    }

    //无视Padding且全部设为Match
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int childCount = getChildCount();
        final int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        for (int i=0; i<childCount; ++i){
            View child = getChildAt(i);
            if (i == 0){
                //设置溢出的高度
                mOverflowHeight = heightSpecSize - mHeaderHeight;
                mHeaderView.setPadding(0,mOverflowHeight,0,0);
                mHeaderTotalHeight = mHeaderHeight + mOverflowHeight;
            }
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
        }
        setMeasuredDimension(widthSpecSize,heightSpecSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int childCount = getChildCount();
        int top = 0;
        //设定child摆放的位置
        for(int i=0; i<childCount; ++i){
            final View child = getChildAt(i);
            child.layout(0,top,child.getMeasuredWidth(),child.getMeasuredHeight()+top);
            top += child.getMeasuredHeight();
        }

        scrollTo(0,mHeaderTotalHeight);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //是否拦截
        boolean isIntercept = false;
        //当前的raw位置，为什么使用raw因为，当手触碰到Header与Content的交界的时候，会出问题
        int currentX = (int) ev.getRawX();
        int currentY = (int) ev.getRawY();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mInterceptDownX = (int) ev.getRawX();
                mInterceptDownY = (int) ev.getRawY();
                isIntercept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = mInterceptDownX - currentX;
                int deltaY = mInterceptDownY - currentY;
                /**判断是否进行刷新**/
                if (Math.abs(deltaX) < Math.abs(deltaY)
                        && isTop() ){
                    //判断是下滑刷新，还是正在刷新的时候上滑隐藏刷新
                    if (deltaY < 0){
                        isIntercept = true;
                    }
                    else if (deltaY>0 && getScrollY() < mHeaderTotalHeight){
                        isIntercept = true;
                    }
                    else {
                        isIntercept = false;
                    }
                }
                else {
                    isIntercept = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                isIntercept = false;
                break;
        }
        //获取onTouch时候的起始点
        mTouchLastX = currentX;
        mTouchLastY = currentY;
        return isIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int currentX = (int) event.getRawX();
        int currentY = (int) event.getRawY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //不做任何动作
                break;
            case MotionEvent.ACTION_MOVE:
                //下滑为负，上滑为正
                int deltaX = mTouchLastX - currentX;
                int deltaY = mTouchLastY - currentY;

                changeScrollY(deltaY);
                break;
            case MotionEvent.ACTION_UP:
                if (mCurrentStatus == STATUS_RELEASE_TO_REFRESH ||
                        mCurrentStatus == STATUS_REFRESHING){

                    //判断是扩张刷新还是收缩刷新
                    if(getScrollY() >= mOverflowHeight){

                        //如果是扩张，还要判断是否是刷新时刷新
                        if (mCurrentStatus != STATUS_REFRESHING){
                            //Header向下扩张
                            smoothToScroll(-(getScrollY() - mOverflowHeight));
                        }
                        else {
                            int distance = getScrollY() - mHeaderTotalHeight;
                            //套了四个if 0 - 0 这逻辑写的尴尬。。
                            if (distance > SHRINK_DISTANCE && distance < 0){
                                smoothToScroll(-distance);
                            }
                        }
                    }
                    else {
                        //Header向上收缩
                        smoothToScroll((mOverflowHeight - getScrollY()));
                    }
                    refresh();
                }
                else if (mCurrentStatus == STATUS_PULL_TO_REFRESH){
                    refreshComplete();
                }
                break;
        }
        mTouchLastX = currentX;
        mTouchLastY = currentY;
        return true;
    }

    private void changeScrollY(int distance){
        int currentScrollY = getScrollY();
        //判断header是展开，还是收缩，并且限制展开和收缩的范围
        if (distance < 0 && currentScrollY+distance >= 0){
            scrollBy(0,distance);
        }
        else if (distance > 0 && currentScrollY+distance <= mHeaderTotalHeight){
            scrollBy(0,distance);
        }
        //因为已经滑动就改变了ScrollY()的位置，要重新获取
        currentScrollY = getScrollY();

        //正在刷新就不需要判断了。
        if (mCurrentStatus == STATUS_REFRESHING){
            return;
        }

        //设定当前滑动的状态
        //表示header出现的面积小于1/2,则设置为收缩状态
        if (currentScrollY >= mHeaderHeight/2+mOverflowHeight){
            mCurrentStatus = STATUS_PULL_TO_REFRESH;
        }
        //表示出现面积大于1/2，为展开状态
        else if (currentScrollY < mHeaderHeight/2+mOverflowHeight){
            mCurrentStatus = STATUS_RELEASE_TO_REFRESH;
        }
    }

    private void smoothToScroll(int dy){
        mScroller.startScroll(getScrollX(),getScrollY(),0,dy, SCROLL_TIME);
        invalidate();
    }

    private void refresh(){
        if (mRefreshListener != null &&
                mCurrentStatus != STATUS_REFRESHING){
            mRefreshListener.onRefresh(mHeaderHeight);
        }
        mCurrentStatus = STATUS_REFRESHING;
    }

    //Scroller的刷新
    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
    }

    protected View createHeaderView(Context context,ViewGroup parent){
        return  LayoutInflater.from(context).
                inflate(R.layout.pull_to_refresh_header,parent,false);
    }

    /***************************abstract method***************************************/
    protected abstract T createContentView(Context context);

    //ContentView是否滑动到顶部
    protected abstract boolean isTop();

    public interface OnRefreshListener{
        void onRefresh(int refreshHeight);
    }
    /***************************public method***************************************/

    public void setOnRefreshListener(OnRefreshListener listener){
        mRefreshListener = listener;
    }

    public void refreshComplete(){
        //复原
        smoothToScroll(mHeaderTotalHeight - getScrollY());
        mCurrentStatus = STATUS_FREE;
    }
}
