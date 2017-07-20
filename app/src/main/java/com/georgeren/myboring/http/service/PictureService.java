package com.georgeren.myboring.http.service;

import com.georgeren.myboring.http.BaseURL;
import com.georgeren.myboring.read.mvp.entity.PictureCategoryResult;
import com.georgeren.myboring.read.mvp.entity.SimplePictureList;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by georgeRen on 2017/7/20.
 */

public interface PictureService {

    @GET(BaseURL.PICTURE_GET_TYPE)
    Observable<SimplePictureList> getPicturesByType(@Query("type") String type, @Query("page") String page);

    @GET(BaseURL.PICTURE_GET_CLASS)
    Observable<PictureCategoryResult> getPictureCategory();

    @GET
    Observable<ResponseBody> getImage(@Url String url);

}
