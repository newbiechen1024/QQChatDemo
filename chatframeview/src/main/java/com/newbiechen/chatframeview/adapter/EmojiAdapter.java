package com.newbiechen.chatframeview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newbiechen.chatframeview.R;
import com.newbiechen.chatframeview.base.BaseAdapter;
import com.newbiechen.chatframeview.entity.EmojiEntity;

/**
 * Created by PC on 2016/12/24.
 */

public class EmojiAdapter extends BaseAdapter<EmojiEntity,EmojiAdapter.EmojiViewHolder>{

    @Override
    public EmojiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EmojiViewHolder(View.inflate(
                parent.getContext(),R.layout.holder_emoji,null));
    }

    @Override
    public void setUpViewHolder(EmojiViewHolder holder, int position) {
        EmojiEntity entity = getItem(position);
        String emoji = entity.getValue();
        holder.tvIcon.setText(emoji);
    }

    class EmojiViewHolder extends RecyclerView.ViewHolder{
        TextView tvIcon;
        public EmojiViewHolder(View itemView) {
            super(itemView);
            tvIcon = (TextView) itemView.findViewById(R.id.emoji_tv_icon);
        }
    }
}
