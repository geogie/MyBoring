package com.georgeren.myboring.read.itemhandler;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.georgeren.myboring.R;
import com.georgeren.myboring.base.adapter.QuickPageAdapter;
import com.georgeren.myboring.base.adapter.ViewHolder;
import com.georgeren.myboring.base.ui.SimpleItemHandler;
import com.georgeren.myboring.read.mvp.entity.DailyNews;
import com.georgeren.myboring.read.view.BannerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by georgeRen on 2017/7/19.
 * Item： banner图片
 */

public class TopNewsIH extends SimpleItemHandler<List<DailyNews.TopStoriesBean>> {

    private ViewPager mViewPager;

    @Override
    public void onCreateItemHandler(ViewHolder vh, ViewGroup parent) {
        super.onCreateItemHandler(vh, parent);
        mViewPager = vh.get(R.id.view_pager);
    }

    @Override
    public void onBindDataView(ViewHolder vh, List<DailyNews.TopStoriesBean> data, int position) {
        List<BannerView> mBannerViews = new ArrayList<>();
        for (DailyNews.TopStoriesBean bean : data) {
            BannerView view = new BannerView(mContext, bean);
            view.setTitle(bean.getTitle());
            view.setImgUrl(bean.getImage());
            mBannerViews.add(view);
        }
        mViewPager.setAdapter(new QuickPageAdapter<>(mBannerViews));
        mViewPager.setOffscreenPageLimit(3);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_top_news;
    }

    @Override
    public void onClick(View v) {
    }
}