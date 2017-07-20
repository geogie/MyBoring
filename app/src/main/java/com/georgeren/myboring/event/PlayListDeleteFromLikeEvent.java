package com.georgeren.myboring.event;

import com.georgeren.myboring.music.mvp.model.PlayList;

/**
 * Created by georgeRen on 2017/7/19.
 */

public class PlayListDeleteFromLikeEvent {
    public PlayList playList;
    public PlayListDeleteFromLikeEvent(PlayList mPlayList) {
        playList = mPlayList;
    }
}
