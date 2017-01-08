package com.newbiechen.chatframeview.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.newbiechen.chatframeview.R;

/**
 * Created by PC on 2017/1/7.
 */

public class RefreshHeaderView extends View {
    private static final String TAG = "RefreshHeaderView";

    private static final int RADIUM = 8;
    private static final int STICK_LENGTH = RADIUM;
    private static final int STICK_BREADTH = STICK_LENGTH;
    private static final int STICK_COUNT = 12;
    //旋转一周所花的时间
    private static final int ROTATE_TIME = 1200;
    private static final int ROTATE_ANGLE = 360/STICK_COUNT;

    private static final int STICK_COLOR[] = {
            android.R.color.holo_red_light, android.R.color.holo_orange_light,
            android.R.color.holo_green_light, android.R.color.holo_blue_light
    };

    private Context mContext;
    private final Paint mStickPaint = new Paint();

    private int mViewWidth;
    private int mViewHeight;
    private int mRadium;
    private int mStickLength;
    private int mStickBreadth;
    private int mRotateTime;
    private int mRotateAngle;

    public RefreshHeaderView(Context context) {
        this(context,null);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initAttrs(attrs);
        initWidget();
    }

    /**
     *  you can set the inner circle's radium and rotate time
     *
     * @param attrs
     */
    private void initAttrs(AttributeSet attrs) {
        mStickBreadth = getDip(STICK_BREADTH);
        mStickLength = getDip(STICK_LENGTH);

        TypedArray a = mContext.obtainStyledAttributes(attrs,R.styleable.RefreshHeaderView);
        mRadium = (int) a.getDimension(R.styleable.RefreshHeaderView_radium,getDip(RADIUM));
        mRotateTime = a.getInt(R.styleable.RefreshHeaderView_duration,ROTATE_TIME);
    }

    private void initWidget(){
        //抗锯齿
        mStickPaint.setAntiAlias(true);
        mStickPaint.setDither(true);
        mStickPaint.setStrokeCap(Paint.Cap.ROUND);
        //设置颜色
        mStickPaint.setColor(mContext.
                getResources().getColor(R.color.black));
        //旋转动画
        setRotateAnimation();
    }

    private void setRotateAnimation(){
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(mRotateTime)
                .setInterpolator(new LinearInterpolator());
        valueAnimator.setIntValues(STICK_COUNT);
        valueAnimator.setRepeatCount(-1);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                mRotateAngle = -90 + value * ROTATE_ANGLE;
                postInvalidate();
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
        //选择最小的距离当做circle的半径
        int viewRadium = mViewWidth <= mViewHeight ? mViewWidth/2 : mViewHeight/2;
        //判断用户是否改变过radium
        if (mRadium == getDip(RADIUM)){
            //平分
            mRadium = viewRadium/2;
            mStickLength = viewRadium/2;
        }
        else {
            int distance = viewRadium - mRadium;
            mStickLength = distance > 0 ? distance : 0;
        }
        mStickBreadth = mStickLength/3;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        //默认的大小
        int viewWidth = (mRadium + mStickLength) * 2;
        int viewHeight = viewWidth;

        if (widthSpecMode == MeasureSpec.AT_MOST &&
                heightSpecMode == MeasureSpec.EXACTLY) {
            viewHeight = heightSpecSize;
        }
        else if (widthSpecMode == MeasureSpec.EXACTLY &&
                heightSpecMode == MeasureSpec.AT_MOST){
            viewWidth = widthSpecSize;
        }
        else if (widthSpecMode == MeasureSpec.EXACTLY &&
                heightSpecMode == MeasureSpec.EXACTLY){
            viewWidth = widthSpecSize;
            viewHeight = heightSpecSize;
        }

        setMeasuredDimension(viewWidth,viewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //将View的中点设置为顶点
        canvas.translate(mViewWidth/2,mViewHeight/2);

        canvas.save();
        //切换到竖直的位置
        canvas.rotate(-90);
        //绘制STICK
        for(int i=0; i<STICK_COUNT; ++i){
            //旋转
            canvas.rotate(mRotateAngle);
            mRotateAngle = ROTATE_ANGLE;
            if (i >= 0 && i < STICK_COLOR.length) {
                mStickPaint.setColor(getResources().getColor(STICK_COLOR[i]));
            } else {
                mStickPaint.setColor(getResources().getColor(R.color.gray));
            }
            //绘制长方形
            Rect rect = new Rect(mRadium,-mStickBreadth/2,
                    mRadium+mStickLength,mStickBreadth/2);
            canvas.drawRect(rect,mStickPaint);
        }
        canvas.restore();
    }

    /**
     * px转dip工具
     * @param value   px值
     * @return
     */
    private int getDip(int value){
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,value,
                mContext.getResources().getDisplayMetrics());
    }
}
