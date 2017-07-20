package com.georgeren.myboring.event;

import com.georgeren.myboring.read.mvp.entity.NewsDetail;

/**
 * Created by georgeRen on 2017/7/20.
 */

public class EssayDeleteFromLikeEvent {
    public NewsDetail newsDetail;
    public EssayDeleteFromLikeEvent(NewsDetail detail) {
        newsDetail = detail;
    }

}
