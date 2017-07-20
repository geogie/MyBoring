package com.georgeren.myboring.http.service;

import com.georgeren.myboring.http.BaseURL;
import com.georgeren.myboring.read.mvp.entity.DailyNews;
import com.georgeren.myboring.read.mvp.entity.NewsDetail;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by georgeRen on 2017/7/19.
 */

public interface ZhiHuService {
    @GET(BaseURL.ZHI_HU_DAILY_LATEST_NEWS)
    Observable<DailyNews> getLatestNews();

    @GET(BaseURL.ZHI_HU_DAILY_FIX_DATE_NEWS + "/{date}")
    Observable<DailyNews> getFixDateNews(@Path("date") String oid);


    @GET(BaseURL.ZHI_HU_ESSAY_CONTENT + "/{id}")
    Observable<NewsDetail> getEssayContent(@Path("id") String id);

    @GET
    Observable<ResponseBody> getEssayCSS(@Url String url);

}
