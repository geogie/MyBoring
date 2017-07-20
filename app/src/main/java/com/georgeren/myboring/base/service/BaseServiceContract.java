package com.georgeren.myboring.base.service;

/**
 * Created by georgeRen on 2017/7/13.
 */

public interface BaseServiceContract {
    void initService();

    void onTaskMoved();

    void onDestroy();

    interface ReceiverPresenter {
        void releaseResource();
    }
}
