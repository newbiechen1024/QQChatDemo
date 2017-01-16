package com.newbiechen.chatframeview.entity;

/**
 * Created by PC on 2016/11/15.
 */

public class ChatMsgEntity {

    //判断当前信息状态
    public static final int STATUS_SENDING = 0;
    public static final int STATUS_FAIL = 1;
    public static final int STATUS_SUCCEED=  2;

    //判断当前文件类型
    public static final int TYPE_TEXT = 3;
    public static final int TYPE_IMG = 4;

    private boolean isSend = false;
    //头像
    private String userIcon;
    //内容
    private String content;
    //日期
    private String date;
    //判断Msgcotent的类型
    private int msgType;
    //当前Msg的状态
    private int msgStatus;


    public ChatMsgEntity(String date,String icon, String content, boolean isSend, int msgType,int msgStatus) {
        this.date = date;
        this.userIcon = icon;
        this.content = content;
        this.isSend = isSend;
        this.msgType = msgType;
        this.msgStatus = msgStatus;

    }

    public void setMsgStatus(int msgStatus) {
        this.msgStatus = msgStatus;
    }

    public String getDate(){
        return date;
    }

    public String getContent() {
        return content;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public boolean getIsSend() {
        return isSend;
    }

    public int getMsgType(){
        return msgType;
    }

    public int getMsgStatus() {
        return msgStatus;
    }
}
