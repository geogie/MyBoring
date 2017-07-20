package com.georgeren.myboring.event;

import com.georgeren.myboring.music.mvp.model.SimpleSong;

/**
 * Created by georgeRen on 2017/7/18.
 */

public class SongDeleteFromLikeEvent {
    public SimpleSong song;

    public SongDeleteFromLikeEvent(SimpleSong song) {
        this.song = song;
    }
}
