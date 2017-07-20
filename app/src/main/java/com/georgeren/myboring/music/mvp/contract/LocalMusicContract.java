package com.georgeren.myboring.music.mvp.contract;

import android.app.LoaderManager;

import com.georgeren.myboring.base.view.IView;
import com.georgeren.myboring.music.mvp.model.SimpleSong;

import java.util.List;

/**
 * Created by georgeRen on 2017/7/19.
 */

public interface LocalMusicContract {

    interface View extends IView {
        LoaderManager getMyLoaderManager();

        void showScanResult(List<SimpleSong> songs);

        void showScanErrorUI();

        void hideScanLocalMusicUI();

        void startScanLocalMusic();
    }

    interface Presenter{
        void loadLocalMusic();
    }

}
