package com.newbiechen.chatframeview.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newbiechen.chatframeview.R;
import com.newbiechen.chatframeview.adapter.FaceCategoryAdapter;
import com.newbiechen.chatframeview.base.BaseAdapter;
import com.newbiechen.chatframeview.base.BaseFragment;
import com.newbiechen.chatframeview.entity.FaceEntity;
import com.newbiechen.chatframeview.utils.FileUtils;
import com.newbiechen.chatframeview.utils.ImageFileFilter;
import com.newbiechen.chatframeview.widget.FaceCategoryRecyclerView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by PC on 2016/12/19.
 */

public class FaceCategoryFragment extends BaseFragment {
    public static final String TAG = "FaceCategoryFragment";
    //SD卡上获取外置图片的文件夹名字
    private static final String FACE_FILE_NAME = "FaceIconStorage";

    private ViewPager mVp;
    private FaceCategoryRecyclerView mRvIndicator;
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

                    //如果存在图片，则创建FaceFragment
                    String categoryPath = category.getAbsolutePath();
                    Fragment fragment = FaceFragment.newInstance(categoryPath);
                    mFragmentList.add(fragment);

                    //创建目录的封面
                    File bgImage = imgs[0];   //选择第一张图片
                    FaceEntity entity = new FaceEntity();
                    entity.setFileName(bgImage.getName());
                    entity.setFacePath(bgImage.getAbsolutePath());
                    mFaceCategoryList.add(entity);

                    //显示表情目录指示器
                    mRvIndicator.setVisibility(View.VISIBLE);
                }
            }

        }

        setUpIndicator();
        mVp.setAdapter(new FacePageAdpter(getChildFragmentManager()));
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
        //将第一个指示器目录设为选中状态。（使用监听器的原因是，必须当LinearManager加载完的时候，才能使用selectItem()）
        //所以这个监听的作用就是：当LinearManager加载item完成的回调。
        mRvIndicator.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                mRvIndicator.selectItem(mVp.getCurrentItem());
            }
        });
        //根据当前的ViewPager使Indicator变为选中色
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mRvIndicator.selectItem(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //点击item切换表情
        mFaceCategoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void itemClick(View view, int pos) {
                //选中
                mRvIndicator.selectItem(pos);
                //切换
                mVp.setCurrentItem(pos,false);
            }
        });
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
