package com.newbiechen.chatframeview.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.SparseArray;

import com.newbiechen.chatframeview.entity.EmojiEntity;
import com.newbiechen.chatframeview.widget.EmojiSpan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by PC on 2016/12/4.
 * String转换成Emoji工具
 */

public class EmojiHandler {

    private static EmojiHandler sEmojiHandler;
    private static SparseArray<EmojiEntity> sEmojiEntityArray;
    private static Context sContext;
    //内置的Emoji
    private static NormalEmoji sNormalEmoji;

    private EmojiHandler(){
        initEmoji();
    }

    /**
     * 初始化Emoji
     */
    private static void initEmoji(){
        sNormalEmoji = new NormalEmoji();
        sEmojiEntityArray = sNormalEmoji.getEmojiEntityArray();
    }

    public static final void init(Context context){
        synchronized (EmojiHandler.class){
            if (sEmojiHandler == null){
                sEmojiHandler = new EmojiHandler();
                sContext = context;
            }
        }
    }
    /**
     * 删除textz中含有的EmojiSpan
     * @param text
     */
    private static void removeOldEmojiSpan(Spannable text){
        EmojiSpan [] oldEmojiSpans = text.getSpans(0,text.length(),EmojiSpan.class);
        for (int i=0; i< oldEmojiSpans.length; ++i){
            text.removeSpan(oldEmojiSpans[i]);
        }
    }

    /**
     * 内置的Emoji
     */
    public static class NormalEmoji{
        //Emoji图片的位置
        private static final String EMOJI_PATH = "drawable";
        //Emoji的图片名
        private static final String EMOJI_NAME = "emoji_";

        private static int EMOJI_START_EXTENT = 0x1f600;
        private static int EMOJI_END_EXTENT = 0x1f917;

        private static int NO_EMOJI_ICON = 0;

        private SparseArray<EmojiEntity> mEmojiEntityArray = new SparseArray<>();

        public NormalEmoji(){
            initEmoji();
        }

        private void initEmoji(){
            for(int i=EMOJI_START_EXTENT; i <= EMOJI_END_EXTENT; ++i) {
                if (isEmojiCode(i)) {
                    String value = Integer.toHexString(i);
                    EmojiEntity entity = new EmojiEntity(i,value);
                    mEmojiEntityArray.append(i,entity);
                }
            }
        }

        private boolean isEmojiCode(int code){
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

        //判断是否存在于内置的Emoji中
        private boolean isEmojiIconExist(int code){
            String hexStr = Integer.toHexString(code);
            int resId = sContext.getResources().
                    getIdentifier(EMOJI_NAME+hexStr,EMOJI_PATH,sContext.getPackageName());
            if (resId == NO_EMOJI_ICON){
                return false;
            }
            else {
                return true;
            }
        }

        /*************************************************************/
        SparseArray<EmojiEntity> getEmojiEntityArray() {
            return mEmojiEntityArray;
        }

        public void changeEmojiName(HashMap<Integer,String> emojiNameMap){
            if (emojiNameMap == null){
                return;
            }

            //遍历
            Set<Map.Entry<Integer,String>> emojiNameSet = emojiNameMap.entrySet();
            for (Map.Entry<Integer,String> emojiName : emojiNameSet){
                int code = emojiName.getKey();
                EmojiEntity entity = mEmojiEntityArray.get(code);
                if (entity != null){
                    entity.changeName(emojiName.getValue());
                }
            }
        }

        /**
         * 添加额外的Emoji
         * @param emojiEntityList
         */
        public void addEmojiEntity(List<EmojiEntity> emojiEntityList){
            for (EmojiEntity entity : emojiEntityList){
                addEmojiEntity(entity);
            }
        }

        public void addEmojiEntity(EmojiEntity entity){
            int code = entity.getCode();
            if (mEmojiEntityArray.get(code) == null &&
                    isEmojiIconExist(code)){
                mEmojiEntityArray.append(code,entity);
            }
        }
    }

    /*************************************公共方法*************************************************/

    /**
     * 将普通的text转化成EmojiText
     * @param context
     * @param text
     * @param emojiSize         emoji在文本框中的大小
     * @param emojiOffset       emoji在文本框中的位移
     */
    public static void textToEmoji(Context context, Spannable text, int emojiSize ,int emojiOffset){
        //移除文字中的Span
        removeOldEmojiSpan(text);
        //遍历所有的文字
        for(int i=0; i<text.length(); ++i){
            int code = Character.codePointAt(text,i);
            EmojiEntity entity = sEmojiEntityArray.get(code);
            if (entity != null){
                EmojiSpan emojiSpan = new EmojiSpan(context,code, emojiSize, emojiOffset);
                text.setSpan(emojiSpan,i,i+entity.getCodeSize(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    /**
     * 将text中的Emoji，转换成指定文字表示。
     * 主要用于网络交互。
     * @param text
     * @return
     */
    public static String getNormalStr(CharSequence text){
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        removeOldEmojiSpan(builder);
        for(int i=0; i<builder.length(); ++i){
            int code = Character.codePointAt(text,i);
            EmojiEntity entity = sEmojiEntityArray.get(code);
            if (entity != null){
                builder.replace(i,i+entity.getCodeSize(),entity.getValue());
            }
        }
        return builder.toString();
    }

    //获取当前存储的Emoji
    public static List<EmojiEntity> getEmojiCodesList(){
        ArrayList<EmojiEntity> emojiEntityList = new ArrayList<>(sEmojiEntityArray.size());
        for (int i=0; i<sEmojiEntityArray.size(); ++i){
            emojiEntityList.add(sEmojiEntityArray.valueAt(i));
        }
        return emojiEntityList;
    }

    //移除所有Emoji
    public static String getNoEmojiStr(CharSequence text){
        StringBuilder builder = new StringBuilder(text);
        //遍历所有的文字
        for(int i=0; i<text.length(); ++i){
            int code = Character.codePointAt(text,i);
            if (EmojiFilter.isEmoji(code)){
                int codeSize = Character.charCount(code);
                builder.replace(i,i+codeSize,"");
            }
        }
        return builder.toString();
    }


    /**
     * @param emojiNameMap
     */
    public static void changeEmojiName(HashMap<Integer,String> emojiNameMap){
        sNormalEmoji.changeEmojiName(emojiNameMap);
    }

    public static void addEmojiEntity(List<EmojiEntity> emojiEntityList){
        sNormalEmoji.addEmojiEntity(emojiEntityList);
    }

    public static void addEmojiEntity(EmojiEntity entity){
        sNormalEmoji.addEmojiEntity(entity);
    }
}
