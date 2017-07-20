package com.georgeren.myboring.read.mvp.presenter;

import com.georgeren.myboring.http.APIHelper;
import com.georgeren.myboring.http.CommonObserver;
import com.georgeren.myboring.read.mvp.contract.ZhiHuDailyContract;
import com.georgeren.myboring.read.mvp.entity.DailyNews;
import com.georgeren.myboring.read.mvp.entity.NewsDetail;
import com.georgeren.myboring.utils.TimeUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by georgeRen on 2017/7/19.
 * 主要负责加载文章
 */

public class ZhiHuDailyNewsPresenter implements ZhiHuDailyContract.Presenter {
    private ZhiHuDailyContract.View mView;
    private Date mCurrentDate;
    private boolean mIsLatest;

    public ZhiHuDailyNewsPresenter(ZhiHuDailyContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void loadData(final int page) {
        if (page == ZhiHuDailyContract.LATEST_NEWS) {
            mIsLatest = true;
            requestForLatestNews(page);
            return;
        }

        mIsLatest = false;
        requestForFixDateNews(page);
    }

    private void requestForFixDateNews(final int page) {
        if (mCurrentDate == null) {
            return;
        }

        mCurrentDate.setTime(mCurrentDate.getTime() - 1000 * 60 * 60 * page * 24);
        String date = TimeUtils.formatDate(mCurrentDate, "yyyyMMdd");

        APIHelper.subscribeSimpleRequest(APIHelper.getZhiHuService().getFixDateNews(date), new CommonObserver<DailyNews>() {
            @Override
            public void onNext(DailyNews dailyNews) {
                if (dailyNews != null) {
                    mView.addNewsData(addHeaderTitle(dailyNews.getStories()));
                }
            }
        });
    }

    private List<NewsDetail> addHeaderTitle(List<NewsDetail> stories) {
        for (NewsDetail bean : stories) {
            bean.setShowTitle(true);
            bean.setHeaderTitle(mIsLatest ? "今日新闻" : TimeUtils.getDateCnDescForZhiHu(mCurrentDate));
        }
        return stories;
    }

    private void requestForLatestNews(final int page) {
        APIHelper.subscribeSimpleRequest(APIHelper.getZhiHuService().getLatestNews(), new CommonObserver<DailyNews>() {
            @Override
            public void onNext(DailyNews dailyNews) {
                if (dailyNews != null) {
                    if (page == 0) {
                        mView.setDataForViewPage(dailyNews.getTop_stories());
                    }
                    mView.addNewsData(addHeaderTitle(dailyNews.getStories()));
                }
            }
        });
    }

    @Override
    public void setCurrentDate(Date date) {
        mCurrentDate = date;
    }

}
