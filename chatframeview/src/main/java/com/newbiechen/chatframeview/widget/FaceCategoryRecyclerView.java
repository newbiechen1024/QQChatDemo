package com.newbiechen.chatframeview.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by PC on 2016/12/28.
 */

public class FaceCategoryRecyclerView extends RecyclerView {
    private static int SELECT_NULL = -1;

    private int mLastItemPos = SELECT_NULL;

    public FaceCategoryRecyclerView(Context context) {
        this(context,null);
    }

    public FaceCategoryRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FaceCategoryRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void selectItem(int position){
        LayoutManager manager = getLayoutManager();
        //判断之前是否有选中，如果选中则取消选中
        if (mLastItemPos != SELECT_NULL) {
            manager.findViewByPosition(mLastItemPos)
                    .setSelected(false);
        }
        //选中指定的item
        manager.findViewByPosition(position)
                .setSelected(true);
        //滑动
        smoothScrollToPosition(position);
        //滑动到当前的item
        mLastItemPos = position;
    }
}
