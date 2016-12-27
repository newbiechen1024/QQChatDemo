package com.newbiechen.chatframeview.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newbiechen.chatframeview.R;
import com.newbiechen.chatframeview.adapter.FaceAdapter;
import com.newbiechen.chatframeview.base.BaseFragment;
import com.newbiechen.chatframeview.entity.FaceEntity;
import com.newbiechen.chatframeview.utils.ImageFileFilter;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by PC on 2016/12/25.
 */

public class FaceFragment extends BaseFragment {

    private static final String EXTRA_FILE_PATH = "file_path";
    private static final int FACE_COL = 4;

    private RecyclerView mRvContent;
    private FaceAdapter mFaceAdapter;

    @Override
    protected View onCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_face,container,false);
    }

    @Override
    protected void initView() {
        mRvContent = getViewById(R.id.face_rv_content);
    }

    @Override
    protected void initWidget() {
        setUpRecyclerView();
    }

    private void setUpRecyclerView(){
        ArrayList<FaceEntity> faceEntityList = getFaceEntityList();
        mFaceAdapter = new FaceAdapter(getContext());
        mFaceAdapter.addItems(faceEntityList);
        mRvContent.setLayoutManager(new GridLayoutManager(getContext(),FACE_COL));
        mRvContent.setAdapter(mFaceAdapter);
    }

    private ArrayList<FaceEntity> getFaceEntityList(){
        ArrayList<FaceEntity> faceEntityList = new ArrayList<>();
        String filePath = getArguments().getString(EXTRA_FILE_PATH);
        File file = new File(filePath);

        if (file.exists() && file.isDirectory()){
            File [] imgs = file.listFiles(new ImageFileFilter());
            for (File img : imgs){
                FaceEntity entity = new FaceEntity();
                entity.setFileName(img.getName());
                entity.setFacePath(img.getAbsolutePath());
                faceEntityList.add(entity);
            }
        }
        return faceEntityList;
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    /********************************公共方法*******************************************************/

    public static FaceFragment newInstance(String filePath){
        Bundle args = new Bundle();
        args.putString(EXTRA_FILE_PATH,filePath);
        FaceFragment fragment = new FaceFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
