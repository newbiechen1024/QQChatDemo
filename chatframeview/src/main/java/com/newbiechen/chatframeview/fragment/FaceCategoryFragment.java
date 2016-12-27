package com.newbiechen.chatframeview.fragment;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newbiechen.chatframeview.R;
import com.newbiechen.chatframeview.adapter.FaceCategoryAdapter;
import com.newbiechen.chatframeview.base.BaseFragment;
import com.newbiechen.chatframeview.entity.FaceEntity;
import com.newbiechen.chatframeview.utils.ImageFileFilter;
import com.newbiechen.chatframeview.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by PC on 2016/12/19.
 */

public class FaceCategoryFragment extends BaseFragment {
    public static final String TAG = "FaceCategoryFragment";
    private static final String FACE_FILE_NAME = "FaceIconStorage";

    private ViewPager mVp;
    private RecyclerView mRvIndicator;
    private FaceCategoryAdapter mFaceCategoryAdapter;

    private EmojiFragment mEmojiFragment;


    private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private final ArrayList<FaceEntity> mFaceCategoryList = new ArrayList<>();

    @Override
    protected View onCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_face_category,container,false);
        return v;
    }

    @Override
    protected void initView() {
        mVp = getViewById(R.id.category_vp_showFace);
        mRvIndicator = getViewById(R.id.category_rv_pageIndicator);
    }

    @Override
    protected void initWidget() {
        initCategory();
        mVp.setAdapter(new FacePageAdpter(getChildFragmentManager()));
    }

    /**
     * 初始化表情目录
     */
    private void initCategory(){
        //初始化Emoji目录
        mEmojiFragment = new EmojiFragment();
        mFragmentList.add(mEmojiFragment);
        //初始化外置的表情目录
        initFaceCategory();
    }

    private void initFaceCategory(){

        //首先加入Emoji封面
        FaceEntity emojiEntity = new FaceEntity();
        int resId = R.drawable.emoji_1f600;
        emojiEntity.setFileName(""+resId);
        mFaceCategoryList.add(0,emojiEntity);

        File folder = new File(FileUtils.getFilePath(FACE_FILE_NAME));
        //获取文件的内的目录
        if (folder.exists() && folder.isDirectory()){

            File [] faceCategory = folder.listFiles();
            for (File category : faceCategory){
                //获取目录下的图片
                File [] imgs = category.listFiles(new ImageFileFilter());
                //判断目录下是否存在图片
                if (imgs.length != 0){
                    //加入Emoji的图片

                    //如果存在图片，则创建Fragment
                    String categoryPath = category.getAbsolutePath();
                    Fragment fragment = FaceFragment.newInstance(categoryPath);
                    mFragmentList.add(fragment);

                    //创建目录的封面
                    File bgImage = imgs[0];
                    FaceEntity entity = new FaceEntity();
                    entity.setFileName(bgImage.getName());
                    entity.setFacePath(bgImage.getAbsolutePath());
                    mFaceCategoryList.add(entity);

                    //显示
                    mRvIndicator.setVisibility(View.VISIBLE);
                }
            }

        }
        /**
         * 后面的任务：
         * 2、获取每个文件夹的第一个图片，作为Indicator，装进Indicator文件夹中
         * 3、解决点击事件的问题
         * 4、解决销毁重建的问题
         * 5、将Emoji设置为对象可能会更好一点（之后优化的问题）
         */
        setUpIndicator();
    }

    private void setUpIndicator(){
        mFaceCategoryAdapter = new FaceCategoryAdapter(getContext());
        mFaceCategoryAdapter.addItems(mFaceCategoryList);
        mRvIndicator.setLayoutManager(new LinearLayoutManager(
                getContext(),LinearLayoutManager.HORIZONTAL,false));
        mRvIndicator.setAdapter(mFaceCategoryAdapter);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    class FacePageAdpter extends FragmentStatePagerAdapter{

        public FacePageAdpter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }
}
