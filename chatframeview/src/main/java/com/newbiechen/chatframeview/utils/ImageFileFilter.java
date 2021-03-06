package com.newbiechen.chatframeview.utils;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by PC on 2016/12/25.
 */

public class ImageFileFilter implements FileFilter {

    @Override
    public boolean accept(File pathname) {
        //判断文件类型是.png/.jpg/.jpeg则返回正确  （不支持.gif）
        String name = pathname.getName();
        if (name.endsWith(".png") || name.endsWith(".jpg")
                || name.endsWith(".jpeg")){
            return true;
        }
        return false;
    }
}
