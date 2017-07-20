package com.georgeren.myboring.music.mvp.model;

import java.io.Serializable;

/**
 * Created by georgeRen on 2017/7/13.
 */

public class Singer implements Serializable {
    public String id;
    public String name;
    public String picUrl;

    public Singer(String name) {
        this.name = name;
    }
}
