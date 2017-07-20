package com.georgeren.myboring.http.service;

import com.georgeren.myboring.read.mvp.entity.JokeList;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by georgeRen on 2017/7/19.
 */

public interface JokeService {
    @GET("http://v.juhe.cn/joke/randJoke.php?key=9ce3fed0e5ac5442ec6dc52f7e2d770a&sort=asc")
    Observable<JokeList> getJokes(@Query("page") int page, @Query("pagesize") int pageSize, @Query("time") String timestamp);
}
