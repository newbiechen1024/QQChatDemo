package com.newbiechen.chatframeview.entity;

/**
 * Created by PC on 2017/1/1.
 */

public class FaceEntity {
    //图片的名字
    private String fileName;
    //图片的绝对路径
    private String facePath;

    public String getFacePath() {
        return facePath;
    }

    public void setFacePath(String facePath) {
        this.facePath = facePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


}
