package com.georgeren.myboring.event;

import com.georgeren.myboring.music.mvp.model.Song;

/**
 * Created by georgeRen on 2017/7/19.
 */

public class AddMusicToQueueEvent {
    public Song song;
    public AddMusicToQueueEvent(Song song) {
        this.song = song;
    }
}
