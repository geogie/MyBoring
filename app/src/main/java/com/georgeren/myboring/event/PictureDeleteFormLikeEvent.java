package com.georgeren.myboring.event;

import com.georgeren.myboring.read.mvp.entity.SimplePicture;

/**
 * Created by georgeRen on 2017/7/20.
 */

public class PictureDeleteFormLikeEvent {
    public SimplePicture picture;

    public PictureDeleteFormLikeEvent(SimplePicture data) {
        picture = data;
    }

}
