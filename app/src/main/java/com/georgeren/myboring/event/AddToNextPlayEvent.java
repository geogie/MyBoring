package com.georgeren.myboring.event;

import com.georgeren.myboring.music.mvp.model.SimpleSong;
import com.georgeren.myboring.music.mvp.model.Song;

/**
 * Created by georgeRen on 2017/7/17.
 */

public class AddToNextPlayEvent {

    public Song song;

    public AddToNextPlayEvent(SimpleSong data) {
        song = data.translateToSong();
    }

    public AddToNextPlayEvent(Song data) {
        song = data;
    }
}
