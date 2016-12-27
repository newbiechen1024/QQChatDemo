package com.newbiechen.chatframeview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatEditText;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.DynamicDrawableSpan;
import android.util.AttributeSet;

import com.newbiechen.chatframeview.utils.EmojiHandler;
import com.newbiechen.chatframeview.R;

/**
 * Created by PC on 2016/12/4.
 */

public class EmojiEditText extends AppCompatEditText {

    private Context mContext;
    private int mEmojiOffset;
    private int mEmojiSize;
    private boolean isUseDefaultMode = false;

    public EmojiEditText(Context context) {
        super(context,null);
    }

    public EmojiEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initWidget(attrs);
    }

    public EmojiEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initWidget(attrs);
    }

    private void initWidget(AttributeSet attrs){
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.EmojiTextView);
        mEmojiSize = (int) a.getDimension(R.styleable.EmojiTextView_emojiSize,getTextSize());
        mEmojiOffset = (int) a.getDimension(R.styleable.EmojiTextView_emojiOffset, 0);
        isUseDefaultMode = a.getBoolean(R.styleable.EmojiTextView_useDefaultMode,false);
        a.recycle();
        /**
         * 大小必须大于0
         */
        if (mEmojiSize <= 0){
            mEmojiSize = (int) getTextSize();
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        CharSequence emojiText = getEmojiText(text);
        super.setText(emojiText, type);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        updateEmojiText(getText());
    }

    /**
     * 将输入含有Emoji的编码，进行转换
     * @param text
     */
    private CharSequence getEmojiText(CharSequence text){
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        updateEmojiText(builder);
        return builder;
    }


    private void updateEmojiText(Spannable spannable){
        if (!isUseDefaultMode){
            EmojiHandler.getIntance()
                    .textToEmoji(getContext(),spannable,mEmojiSize , mEmojiOffset);
        }
    }
}