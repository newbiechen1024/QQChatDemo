package com.newbiechen.chatframeview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ReplacementSpan;
import android.util.TypedValue;

import java.lang.ref.WeakReference;

/**
 * Created by PC on 2016/12/4.
 * 绘制Emoji到TextView上
 */

public class EmojiSpan extends ReplacementSpan {
    //设置默认的间距,被不负责任的我固定死了。
    private static final int LEADING_TOP = 3;
    private static final int LEADING_BOTTOM = 6;
    //对位移进行修正。（不添加的话，Emoji与文字相互之间有偏移，而不是基于基准线）
    private static final int ALERT_VALUE = 3;
    //获取Emoji图片的路径
    private static final String EMOJI_PATH = "drawable";
    private static final String EMOJI_NAME = "emoji_";

    private final Context mContext;
    private WeakReference<Drawable> mWeakReference;
    private Drawable mEmojiDrawable;

    private final int mEmojiCode;
    private final int mEmojiSize;
    private final int mEmojiOffset;
    private int mEmojiExtent;
    private int mBottom;

    public EmojiSpan(Context context,int emojiCode,int emojiSize,int emojiOffset){
        mContext = context;
        mEmojiCode = emojiCode;
        mEmojiSize = emojiSize;
        mEmojiOffset = emojiOffset + getDip(ALERT_VALUE);

    }

    public Drawable getDrawable() {
        if (mEmojiDrawable == null){
            String hexStr = Integer.toHexString(mEmojiCode);
            //首先从一级缓存中获取数据
            int resId = mContext.getResources().getIdentifier(EMOJI_NAME + hexStr,
                    EMOJI_PATH,mContext.getPackageName());
            mEmojiDrawable = mContext.getResources().getDrawable(resId);
            //获取图片的宽高
            int width = mEmojiDrawable.getIntrinsicWidth();
            int height = mEmojiDrawable.getIntrinsicHeight();
            scaleDrawableSize(width,height);
        }
        return mEmojiDrawable;
    }

    /**
     * 切换尺寸
     * @param width  drawable的宽度
     * @param height drawable的高度
     */
    private void scaleDrawableSize(int width,int height){
        float scaleFraction = mEmojiSize/(float) height;
        int scaleWidth = (int) (width * scaleFraction);
        int scaleHeight = mEmojiSize;
        //设置上间距
        mEmojiDrawable.setBounds(0,getDip(LEADING_TOP),scaleWidth,getDip(LEADING_TOP)+scaleHeight);
    }


    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        Drawable d = getCachedDrawable();
        Rect rect = d.getBounds();
        if (fm != null){
            fm.ascent = -rect.bottom;
            //默认设置的下间距
            fm.descent = getDip(LEADING_BOTTOM);

            //原理：移动EmojiSpan
            fm.ascent += mEmojiOffset;
            fm.descent += mEmojiOffset;

            fm.top = fm.ascent;
            fm.bottom = fm.descent;
            mEmojiExtent = fm.bottom - fm.top;
            mBottom = fm.bottom;
        }
        return rect.right;
    }


    @Override
    public void draw(Canvas canvas, CharSequence text,
                     int start, int end, float x, int top,
                     int y, int bottom, Paint paint) {
        Drawable d = getCachedDrawable();
        canvas.save();
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        //当出现文字的时候，并且EmojiSpan的大小小于文字的大小的时候，会出现bottom增大的现象，导致EmojiSpan上移
        //为了解决这个问题，就是当bottom != mBottom表示可能出现了文字，这时如果文字的bottom比较大的话就给文字。
        if (bottom != mEmojiExtent && mBottom < fm.bottom){
            mBottom = fm.bottom;
        }
        //bottom：View的总高度
        //d.getBounds().bottom:图片的高度
        //mBottom：整个EmojiSpan的距离基准线以下的高度
        //mEmojiOffset：图片的偏移量
        //原理：
        // bottom-mBottom:表示基准线的位置
        //然后再减去d.getBounds().bottom，表示图片显示在基准线正上方
        //最后+mEmojiOffset表示图片在基准线周围的偏移量。
        int transY = bottom - d.getBounds().bottom - mBottom + mEmojiOffset;
        //绘制图形
        canvas.translate(x,transY);
        d.draw(canvas);
        canvas.restore();
    }

    private Drawable getCachedDrawable(){
        Drawable d = null;

        if (mWeakReference != null){
            d = mWeakReference.get();
        }
        if (d == null){
            d = getDrawable();
            mWeakReference = new WeakReference<>(d);
        }
        return d;
    }

    private int getDip(int px){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                px,mContext.getResources().getDisplayMetrics());
    }

    /********************************公共方法***********************************/
    public int getEmojiCode(){
        return mEmojiCode;
    }
}
