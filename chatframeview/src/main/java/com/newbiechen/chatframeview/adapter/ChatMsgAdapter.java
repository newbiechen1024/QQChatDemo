package com.newbiechen.chatframeview.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.newbiechen.chatframeview.R;
import com.newbiechen.chatframeview.base.BaseAdapter;
import com.newbiechen.chatframeview.entity.ChatMsgEntity;
import com.newbiechen.chatframeview.utils.PatternUtils;

import java.util.regex.Matcher;

/**
 * Created by PC on 2017/1/12.
 */

public class ChatMsgAdapter extends BaseAdapter<ChatMsgEntity,ChatMsgAdapter.ChatMsgViewHolder> {
    private static final String TAG = "ChatMsgAdapter";

    private static final int SENDER = 0;
    private static final int REPLY = 1;

    private Context mContext;

    public ChatMsgAdapter(Context context){
        mContext = context;
    }

    @Override
    public ChatMsgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout;
        if (viewType == SENDER){
            layout = R.layout.adapter_msg_right;
        }
        else {
            layout = R.layout.adapter_msg_left;
        }

        View view = LayoutInflater.from(parent.getContext())
                .inflate(layout,parent,false);
        return new ChatMsgViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getIsSend() ? SENDER : REPLY;
    }

    @Override
    public void setUpViewHolder(ChatMsgViewHolder holder, int position) {
        ChatMsgEntity chatMsgEntity = getItem(position);

        holder.tvDate.setText(chatMsgEntity.getDate());
        /* holder.ivIcon.setImageResource(chatMsgEntity.getUserIcon());*/
        switch (chatMsgEntity.getMsgStatus()){
            case ChatMsgEntity.STATUS_SENDING:
                holder.pbLoad.setVisibility(View.VISIBLE);
                holder.ivSendFail.setVisibility(View.GONE);
                break;
            case ChatMsgEntity.STATUS_FAIL:
                holder.pbLoad.setVisibility(View.GONE);
                holder.ivSendFail.setVisibility(View.VISIBLE);
                break;
            case ChatMsgEntity.STATUS_SUCCEED:
                holder.pbLoad.setVisibility(View.GONE);
                holder.ivSendFail.setVisibility(View.GONE);
                break;
        }

        if (chatMsgEntity.getMsgType() == ChatMsgEntity.TYPE_TEXT){
            holder.tvContent.setText(setTextHighLight(
                    chatMsgEntity.getContent()));
        }
        else if (chatMsgEntity.getMsgType() == ChatMsgEntity.TYPE_IMG){
            final String imgPath = chatMsgEntity.getContent();
            SpannableStringBuilder builder = new SpannableStringBuilder(imgPath);

            Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
            ImageSpan imageSpan = new ImageSpan(mContext,bitmap);
            builder.setSpan(imageSpan,0,builder.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {

                }
            },0,builder.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tvContent.setText(builder);
            //这样点击事件才有作用。如果想改变点击事件的颜色，就必须重写LinkMovementMethod
            holder.tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    private SpannableStringBuilder setTextHighLight(String str){
        SpannableStringBuilder spanString = new SpannableStringBuilder(str);
        //每句话全部用正则表达式检查一遍
        for (PatternUtils.PatternType patternType : PatternUtils.PatternType.values()){
            Matcher matcher = PatternUtils.pattern(str,patternType);
            int colorId = mContext.getResources().getColor(R.color.text_foreground_color);
            while (matcher.find()){
                spanString.setSpan(new ForegroundColorSpan(colorId),matcher.start()
                        ,matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //下划线
                spanString.setSpan(new UnderlineSpan(),
                        matcher.start(),matcher.end(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spanString;
    }

    class ChatMsgViewHolder extends RecyclerView.ViewHolder{
        TextView tvDate;
        ImageView ivIcon;
        TextView tvContent;
        ImageView ivSendFail;
        ProgressBar pbLoad;
        public ChatMsgViewHolder(View itemView) {
            super(itemView);
            tvDate = getViewById(itemView, R.id.msg_tv_date);
            ivIcon = getViewById(itemView, R.id.msg_iv_icon);
            tvContent = getViewById(itemView,R.id.msg_tv_content);
            ivSendFail = getViewById(itemView,R.id.msg_iv_send_fail);
            pbLoad = getViewById(itemView,R.id.msg_pb_load);
        }

        private <VT> VT getViewById(View itemView,int id){
          return (VT) itemView.findViewById(id);
        }
    }

    public void addChatMsgItems(ChatMsgEntity chatMsgEntity){
        mItemList.add(0,chatMsgEntity);
        notifyDataSetChanged();
    }
}
