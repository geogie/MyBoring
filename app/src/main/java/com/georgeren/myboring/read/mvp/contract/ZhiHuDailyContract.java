package com.georgeren.myboring.read.mvp.contract;

import com.georgeren.myboring.base.view.IView;
import com.georgeren.myboring.read.mvp.entity.DailyNews;
import com.georgeren.myboring.read.mvp.entity.NewsDetail;

import java.util.Date;
import java.util.List;

/**
 * Created by georgeRen on 2017/7/14.
 */

public interface ZhiHuDailyContract {
    int LATEST_NEWS = 0;
    int AFTER_DAY_NEWS = 1;

    interface View extends IView {
        void addNewsData(List<NewsDetail> news);

        void setDataForViewPage(List<DailyNews.TopStoriesBean> banners);
    }


    interface Presenter {
        void loadData(int page);

        void setCurrentDate(Date date);
    }

    interface DailyNewsStickHeader {
        String getTitle(int position);

        int getHeaderColor(int position);

        boolean isShowTitle(int position);
    }

}
