package com.newbiechen.chatframeview.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.newbiechen.chatframeview.R;
import com.newbiechen.chatframeview.base.BaseAdapter;
import com.newbiechen.chatframeview.entity.FaceEntity;
import com.newbiechen.chatframeview.utils.ImageUtils;

/**
 * Created by PC on 2016/12/27.
 */

public class FaceCategoryAdapter extends BaseAdapter<FaceEntity,FaceCategoryAdapter.FaceCategoryViewHolder> {
    private Context mContext;
    public FaceCategoryAdapter(Context context) {
        mContext = context;
    }

    @Override
    public FaceCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FaceCategoryViewHolder(View.inflate(parent.getContext(),R.layout.holder_face_category,null));
    }

    @Override
    public void setUpViewHolder(FaceCategoryViewHolder holder, int position) {
        FaceEntity entity = getItem(position);
        if (position == 0){
            holder.ivIcon.setImageResource(Integer.valueOf(entity.getFileName()));
        }
        else {
            int iconSize = (int) mContext.getResources().getDimension(R.dimen.face_category_icon_size);
            Bitmap bitmap = ImageUtils.compressBitmap(entity.getFacePath(),iconSize,iconSize);
            holder.ivIcon.setImageBitmap(bitmap);
        }
    }

    class FaceCategoryViewHolder extends RecyclerView.ViewHolder{
        ImageView ivIcon;
        public FaceCategoryViewHolder(View itemView) {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.category_iv_icon);
        }
    }
}
