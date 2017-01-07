package com.newbiechen.chatframeview.utils;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by PC on 2016/12/24.
 */

public final class FileUtils {

    public static String getSDCardPath(){
        String sdCardPath = "";
        if (isSDCardExist()){
            sdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return sdCardPath;
    }

    public static String getFilePath(String fileName){
        String sdCardPath = getSDCardPath();
        return sdCardPath + File.separator + fileName;
    }

    public static boolean isSDCardExist(){
        if (Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED)){
            return true;

        }
        return false;
    }
}
