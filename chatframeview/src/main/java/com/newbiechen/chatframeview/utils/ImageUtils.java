package com.newbiechen.chatframeview.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by PC on 2016/12/27.
 * 图片压缩
 */

public class ImageUtils {

    public static Bitmap compressBitmap (String filePath,int width,int height){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath,options);
        int bitmapWidth = options.outWidth;
        int bitmapHeight = options.outHeight;
        int sampleSize = calculateSampleSize(width,height,bitmapWidth,bitmapHeight);
        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath);
    }

    private static int calculateSampleSize(int reqWidth, int reqHeight, int bitmapWidth, int bitmapHeight){
        int sampleSize = 1;
        while ((bitmapWidth/sampleSize > reqWidth) ||
                (bitmapHeight/sampleSize) > reqHeight){
            sampleSize *= 2;
        }
        return sampleSize;
    }
}
