package com.georgeren.myboring.event;

import com.georgeren.myboring.music.mvp.model.Song;

/**
 * Created by georgeRen on 2017/7/18.
 */

public class ChangeSongEvent {
    public  Song song;

    public ChangeSongEvent(Song mData) {
        song = mData;
    }
}
