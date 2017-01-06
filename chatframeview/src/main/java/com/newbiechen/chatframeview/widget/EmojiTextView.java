package com.newbiechen.chatframeview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;

import com.newbiechen.chatframeview.utils.EmojiHandler;
import com.newbiechen.chatframeview.R;

/**
 * Created by PC on 2016/12/4.
 */

public class EmojiTextView extends AppCompatTextView {
    private Context mContext;
    private int mEmojiOffset;
    private int mEmojiSize;
    private boolean isBanEmoji;
    private boolean isUseDefaultMode = false;

    public EmojiTextView(Context context) {
        this(context,null);
    }

    public EmojiTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public EmojiTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initWidget(attrs);
    }

    private void initWidget(AttributeSet attrs){
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.EmojiTextView);
        int defualtTextSize = - getPaint().getFontMetricsInt().ascent;
        mEmojiSize = (int) a.getDimension(R.styleable.EmojiTextView_emojiSize,defualtTextSize);
        mEmojiOffset = (int) a.getDimension(R.styleable.EmojiTextView_emojiOffset, 0);
        isBanEmoji = a.getBoolean(R.styleable.EmojiTextView_banEmoji,false);
        isUseDefaultMode = a.getBoolean(R.styleable.EmojiTextView_useDefaultMode,false);
        a.recycle();
        /**
         * 大小必须大于0
         */
        if (mEmojiSize <= 0){
            mEmojiSize = defualtTextSize;
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        CharSequence emojiText = getEmojiText(text);
        super.setText(emojiText, type);
    }

    /**
     * 将输入含有Emoji的编码，进行转换
     * @param text
     */
    private CharSequence getEmojiText(CharSequence text){
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        if (!isUseDefaultMode){
            EmojiHandler.textToEmoji(getContext(),builder,mEmojiSize , mEmojiOffset);
        }
        return builder;
    }
}
