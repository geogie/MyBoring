package com.georgeren.myboring.music.mvp.model;

import java.io.Serializable;

/**
 * Created by georgeRen on 2017/7/13.
 */

public class Album implements Serializable {
    public String id;
    public String name;
    public Singer artist;
    public String picUrl;

    public Album(String name) {
        this.name = name;
    }

    public Album(String name, String picUrl) {
        this.name = name;
        this.picUrl = picUrl;
    }
}
