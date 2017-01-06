package com.newbiechen.chatframeview.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by PC on 2017/1/1.
 */

public class EmojiEntity implements Parcelable{

    private int code;
    private String name;

    public static final Creator<EmojiEntity> CREATOR = new Creator<EmojiEntity>() {
        @Override
        public EmojiEntity createFromParcel(Parcel source) {
            return new EmojiEntity(source);
        }

        @Override
        public EmojiEntity[] newArray(int size) {
            return new EmojiEntity[size];
        }
    };

    public EmojiEntity(int code,String name) {
        this.code = code;
        this.name = name;
    }

    public EmojiEntity(Parcel in){
        this.code = in.readInt();
        this.name = in.readString();
    }

    public int getCode() {
        return code;
    }

    public String getValue(){
        char [] data = Character.toChars(code);
        return new String(data);
    }

    public String getName(){
        return name;
    }

    public int getCodeSize(){
        return Character.charCount(code);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(code);
        dest.writeString(name);
    }

    public void changeName(String name) {
        this.name = name;
    }
}
