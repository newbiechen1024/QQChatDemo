package com.newbiechen.chatframeview.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by PC on 2017/1/12.
 */

public class ChatRefreshRecyclerView extends RefreshLayout<RecyclerView> {
    private static final String TAG = "ChatRefreshReyclerView";

    private RecyclerView mRecyclerView;
    private int mScrollY = 0;
    public ChatRefreshRecyclerView(Context context) {
        super(context);
    }

    public ChatRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatRefreshRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected RecyclerView createContentView(Context context) {
        mRecyclerView = new RecyclerView(context);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(
                context,LinearLayoutManager.VERTICAL,true));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrollY += dy;
            }
        });
        return mRecyclerView;
    }

    @Override
    protected boolean isTop() {
        int lastVisibleItem = -1;
        int totalItem = 0;
        int itemTop = 0;
        try {
            if (mRecyclerView.getLayoutManager() instanceof  LinearLayoutManager){
                LinearLayoutManager lm = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                lastVisibleItem = lm.findLastVisibleItemPosition();
                totalItem = mRecyclerView.getAdapter().getItemCount() - 1;
                itemTop = lm.findViewByPosition(lastVisibleItem)
                        .getTop();
            }
            else {
                throw new ClassCastException("RecyclerView's LayoutManager must be LinearLayout");
            }
        }catch (ClassCastException e){
            e.printStackTrace();
        }

        if (lastVisibleItem == totalItem && itemTop == 0){
            return true;
        }
        return false;
    }

    /********************************公共方法**********************************/
    public void setAdapter(RecyclerView.Adapter adapter){
        mRecyclerView.setAdapter(adapter);
    }

    public RecyclerView getRecyclerView(){
        return mRecyclerView;
    }
}
