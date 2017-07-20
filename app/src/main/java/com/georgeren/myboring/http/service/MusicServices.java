package com.georgeren.myboring.http.service;

import com.georgeren.myboring.http.BaseURL;
import com.georgeren.myboring.music.mvp.model.GetPlayListResult;
import com.georgeren.myboring.music.mvp.model.MusicSearchResult;
import com.georgeren.myboring.music.mvp.model.PlayListDetail;
import com.georgeren.myboring.music.mvp.model.PlayListSong;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by georgeRen on 2017/7/14.
 */

public interface MusicServices {

    @GET(BaseURL.MUSIC_SEARCH)
    Observable<MusicSearchResult> searchMusic(@Query("s") String musicName, @Query("limit") int limit,
                                              @Query("type") String type, @Query("offset") int page);

    @GET(BaseURL.MUSIC_PLAY_LIST + "?type=topPlayList&cat=全部")
    Observable<GetPlayListResult> getPlayList(@Query("offset") int offset, @Query("limit") int limit);

    @GET(BaseURL.MUSIC_PLAY_LIST_DETAIL + "?type=playlist")
    Observable<PlayListDetail> getPlayListDetail(@Query("id") int id);

    @GET(BaseURL.CLOUD_MUSIC_API_MUSICINGO + "?type=url")
    Observable<PlayListSong> getSongDetail(@Query("id") int id);
}
