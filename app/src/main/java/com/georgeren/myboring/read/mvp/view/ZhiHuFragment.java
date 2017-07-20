package com.georgeren.myboring.read.mvp.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.georgeren.myboring.R;
import com.georgeren.myboring.base.entity.TitleMark;
import com.georgeren.myboring.base.ui.OnLastItemVisibleListener;
import com.georgeren.myboring.base.view.LoadMoreRecycleView;
import com.georgeren.myboring.base.view.ViewPageFragment;
import com.georgeren.myboring.read.adapter.ZhiHuDailyAdapter;
import com.georgeren.myboring.read.mvp.contract.ZhiHuDailyContract;
import com.georgeren.myboring.read.mvp.entity.DailyNews;
import com.georgeren.myboring.read.mvp.entity.NewsDetail;
import com.georgeren.myboring.read.mvp.presenter.ZhiHuDailyNewsPresenter;
import com.georgeren.myboring.utils.RVUtils;
import com.georgeren.myboring.utils.UIUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by georgeRen on 2017/7/19.
 */

public class ZhiHuFragment extends ViewPageFragment implements ZhiHuDailyContract.View, OnLastItemVisibleListener, SwipeRefreshLayout.OnRefreshListener {
    private LoadMoreRecycleView mRv;
    private List<Object> mData = new ArrayList<>();

    private ZhiHuDailyContract.Presenter mPresenter;
    private LinearLayoutManager mManager;
    private SwipeRefreshLayout mRefreshLayout;
    private boolean mIsRefresh;


    @Override
    public View initContentView(LayoutInflater inflater, ViewGroup container) {
        mView = inflater.inflate(R.layout.fragment_zhi_hu_layout, container, false);
        return mView;
    }

    @Override
    protected void initView() {
        mManager = RVUtils.getLayoutManager(getContext(), LinearLayoutManager.VERTICAL);
        mRefreshLayout.setColorSchemeColors(new int[]{getResources().getColor(R.color.colorAccent)});
        mRv.setLayoutManager(mManager);
        mRv.setAdapter(new ZhiHuDailyAdapter((Activity) getContext(), mData));
        mRv.setOnLastItemVisibleListener(this);
        mRefreshLayout.setOnRefreshListener(this);
        mRv.addItemDecoration(RVUtils.getZhiHuDailyNewsDecoration(getContext(), UIUtils.dp2Px(20), new ZhiHuDailyContract.DailyNewsStickHeader() {

            @Override
            public String getTitle(int position) {
                if (position < 0 || position >= mData.size()) {
                    return "";
                }

                if (position == 0) {
                    return "今日新闻";
                }

                if (mData.get(position) instanceof TitleMark) {
                    return ((TitleMark) mData.get(position)).getHeaderTitle();
                }
                return "";
            }

            @Override
            public int getHeaderColor(int position) {
                return getResources().getColor(R.color.colorAccent);
            }

            @Override
            public boolean isShowTitle(int position) {
                if (position == 0) {
                    return true;
                }
                if (mData.get(position) instanceof TitleMark) {
                    return ((TitleMark) mData.get(position)).isShowTitle();
                }
                return false;
            }
        }));
        mRv.addItemDecoration(new RVUtils.NoLastDividerDecoration(getContext(), R.color.divider, 1, new Rect(UIUtils.dp2Px(15), 0, 0, 0)));

    }

    @Override
    protected void findView() {
        mPresenter = new ZhiHuDailyNewsPresenter(this);
        mPresenter.setCurrentDate(new Date(System.currentTimeMillis()));
        mRv = (LoadMoreRecycleView) mView.findViewById(R.id.list_view);
        mRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.refresh_layout);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        mPresenter.loadData(ZhiHuDailyContract.LATEST_NEWS);
    }


    @Override
    public Context getViewContext() {
        return getContext();
    }

    @Override
    public void addNewsData(List<NewsDetail> news) {
        for (NewsDetail bean : news) {
            bean.date = new Date(System.currentTimeMillis());
            mData.add(bean);
        }
        mRv.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void setDataForViewPage(List<DailyNews.TopStoriesBean> banners) {
        if (mIsRefresh) {
            mIsRefresh = false;
            mRefreshLayout.setRefreshing(false);
        }

        mData.add(banners);
        mRv.getAdapter().notifyDataSetChanged();
    }
    @Override
    public void onLastItemVisible() {
        mPresenter.loadData(ZhiHuDailyContract.AFTER_DAY_NEWS);
    }

    @Override
    public String getTitle() {
        return "知乎日报";
    }

    @Override
    public void onRefresh() {
        mIsRefresh = true;
        mData.clear();
        mPresenter.loadData(ZhiHuDailyContract.LATEST_NEWS);
    }
}
