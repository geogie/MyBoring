package com.georgeren.myboring.base.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.georgeren.myboring.R;
import com.georgeren.myboring.base.view.LoadMoreRecycleView;
import com.georgeren.myboring.base.view.ViewPageFragment;
import com.georgeren.myboring.base.adapter.BaseRVAdapter;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by georgeRen on 2017/7/19.
 */

public abstract class ListFragment<T> extends ViewPageFragment {
    protected LoadMoreRecycleView mRv;
    protected List<T> mData = new ArrayList<>();
    private ImageView mEmptyView;

    @Override
    public View initContentView(LayoutInflater inflater, ViewGroup container) {
        mView = inflater.inflate(R.layout.fragment_list, container, false);
        initView();
        return mView;
    }

    @Override
    public void initView() {
        mRv = (LoadMoreRecycleView) mView.findViewById(R.id.list_view);
        mEmptyView = (ImageView) mView.findViewById(R.id.empty_view);

        RecyclerView.ItemDecoration itemDecoration = getItemDecoration();
        if (itemDecoration != null) {
            mRv.addItemDecoration(getItemDecoration());
        }

        mRv.setLayoutManager(getLayoutManager());

        mRv.setAdapter(new BaseRVAdapter(getActivity(), mData) {
            @Override
            protected void initHandlers() {
                registerItemHandler(0, new ItemHandlerFactory() {
                    @Override
                    public ItemHandler newInstant(int viewType) {
                        return getSingletonItemHandler();
                    }
                });
            }

            @Override
            protected int getViewType(int position) {
                return 0;
            }
        });

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        loadData();
    }

    public void addData(List<T> datas) {
        if (datas == null || datas.isEmpty()) {
            mRv.setVisibility(View.INVISIBLE);
            mEmptyView.setVisibility(View.VISIBLE);
            return;
        }

        mRv.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.INVISIBLE);
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.addAll(datas);
        mRv.getAdapter().notifyDataSetChanged();
    }

    protected abstract void loadData();

    protected abstract ItemHandler getSingletonItemHandler();

    protected abstract RecyclerView.LayoutManager getLayoutManager();

    protected abstract RecyclerView.ItemDecoration getItemDecoration();

}
