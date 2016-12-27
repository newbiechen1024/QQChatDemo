package com.newbiechen.chatframeview.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.LruCache;

import com.newbiechen.chatframeview.widget.EmojiSpan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by PC on 2016/12/4.
 */

public class EmojiHandler {
    private static EmojiHandler sEmojiHandler;
    private static final ArrayList<Integer> sEmojiCodesList = new ArrayList<>();
    private EmojiHandler(){
        addEmojiCode();
    }

    private void addEmojiCode(){

        for(int i=0x1f600; i<=0x1f917; ++i){
             if (isEmojiCode(i)){
                 sEmojiCodesList.add(i);
             }
        }

    }

    public static final EmojiHandler getIntance(){
        synchronized (EmojiHandler.class){
            if (sEmojiHandler == null){
                sEmojiHandler = new EmojiHandler();
            }
        }
        return sEmojiHandler;
    }

    public void textToEmoji(Context context, Spannable text, int emojiSize ,int emojiOffset){
        //移除文字中的Span
        removeOldEmoji(text);
        //遍历所有的文字
        for(int i=0; i<text.length(); ++i){
            int code = Character.codePointAt(text,i);
            //判断是否符合Emoji的unicode编码
            if (isEmojiCode(code)){
                int codeSize = Character.charCount(code);
                EmojiSpan emojiSpan = new EmojiSpan(context,code, emojiSize, emojiOffset);
                text.setSpan(emojiSpan,i,i+codeSize, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    /**
     * 删除textz中含有的EmojiSpan
     * @param text
     */
    private static void removeOldEmoji(Spannable text){
        //删除旧的EmojiSpan
        EmojiSpan [] oldEmojiSpans = text.getSpans(0,text.length(),EmojiSpan.class);
        for (int i=0; i< oldEmojiSpans.length; ++i){
            text.removeSpan(oldEmojiSpans[i]);
        }
    }

    /**
     * 将text中的Emoji，转换成其他文字表示
     * @param text
     * @return
     */
    public static String getNormalStr(CharSequence text){
        return getNormalStr(text,null);
    }

    public static String getNormalStr(CharSequence text, Map<Integer,String> emojiNameMap){
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        removeOldEmoji(builder);
        for(int i=0; i<builder.length(); ++i){
            int code = Character.codePointAt(text,i);
            //判断是否符合Emoji的unicode编码
            if (isEmojiCode(code)){
                int codeSize = Character.charCount(code);

                String emojiName = "";
                if (emojiNameMap != null){
                    emojiName = emojiNameMap.get(code);
                }
                else {
                    emojiName = Integer.toHexString(code);
                }
                builder.replace(i,i+codeSize,emojiName);
            }
        }
        return builder.toString();
    }

    public static String code2Emoji(int emojiCode){
        char [] code = Character.toChars(emojiCode);
        String str = new String(code);
        return str;
    }

    /**
     * 太不友好了 - -，然而不知道怎么改。要想个办法扩展。
     * @param code
     * @return
     */
    public static boolean isEmojiCode(int code){
        boolean isEmoji = false;
        if (code >= 0x1f600 && code <= 0x1f60f){
            isEmoji = true;
        }
        else if (code >= 0x1f610 && code <= 0x1f61f){
            isEmoji = true;
        }
        else if (code >= 0x1f620 && code <= 0x1f62f){
            isEmoji = true;
        }
        else if (code >= 0x1f630 && code <= 0x1f637){
            isEmoji = true;
        }
        else if (code >= 0x1f641 && code <= 0x1f644){
            isEmoji = true;
        }
        else if (code >= 0x1f910 && code <= 0x1f917){
            isEmoji = true;
        }
        return isEmoji;
    }

    public static List<Integer> getEmojiCodesList(){
        return Collections.unmodifiableList(sEmojiCodesList);
    }

}
