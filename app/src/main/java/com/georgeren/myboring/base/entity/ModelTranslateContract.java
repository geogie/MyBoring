package com.georgeren.myboring.base.entity;

import com.georgeren.myboring.music.mvp.model.PlayList;
import com.georgeren.myboring.music.mvp.model.PlayListDetail;
import com.georgeren.myboring.music.mvp.model.SimpleSong;
import com.georgeren.myboring.music.mvp.model.Song;

import java.util.List;

import rx.Observable;

/**
 * Created by georgeRen on 2017/7/18.
 */

public interface ModelTranslateContract {
    interface MusicModeTranslate {
        Observable<List<Song>> getSongFromPlayList(PlayList playList);

        List<SimpleSong> translateTracksToSimpleSong(List<PlayListDetail.PlaylistBean.TracksBean> tracks);

        List<Song> translateTracksToSong(List<PlayListDetail.PlaylistBean.TracksBean> tracks);

        Observable<Boolean> checkIfHasPlayUrl(Song song);
    }
}
