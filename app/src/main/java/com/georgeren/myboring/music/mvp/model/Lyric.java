package com.georgeren.myboring.music.mvp.model;

/**
 * Created by georgeRen on 2017/7/18.
 */

public class Lyric {
    public static int NO_MEASURE = -1;

    public String version;
    public String time;
    public String lyric;
    public int pos;
    public int height = NO_MEASURE;

    public Lyric(String time, String lyric, int pos) {
        this.time = time;
        this.lyric = lyric;
        this.pos = pos;
    }

}
