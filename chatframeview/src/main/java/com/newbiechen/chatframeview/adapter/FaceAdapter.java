package com.newbiechen.chatframeview.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.newbiechen.chatframeview.R;
import com.newbiechen.chatframeview.base.BaseAdapter;
import com.newbiechen.chatframeview.entity.FaceEntity;
import com.newbiechen.chatframeview.utils.ImageUtils;

/**
 * Created by PC on 2016/12/25.
 */

public class FaceAdapter extends BaseAdapter<FaceEntity,FaceAdapter.FaceViewHolder> {
    private Context mContext;
    public FaceAdapter(Context context) {
        mContext = context;
    }

    @Override
    public FaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FaceViewHolder(View.inflate(parent.getContext(),R.layout.holder_face,null));
    }

    @Override
    public void setUpViewHolder(FaceViewHolder holder, int position) {
        FaceEntity entity = getItem(position);
        int iconSize = (int) mContext.getResources().getDimension(R.dimen.face_icon_size);
        Bitmap icon = ImageUtils.compressBitmap(entity.getFacePath(),iconSize,iconSize);
        holder.ivIcon.setImageBitmap(icon);
        holder.tvDescribe.setText(entity.getFileName());
    }

    class FaceViewHolder extends RecyclerView.ViewHolder{
        ImageView ivIcon;
        TextView tvDescribe;
        public FaceViewHolder(View itemView) {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.face_iv_icon);
            tvDescribe = (TextView) itemView.findViewById(R.id.face_tv_describe);
        }
    }
}
