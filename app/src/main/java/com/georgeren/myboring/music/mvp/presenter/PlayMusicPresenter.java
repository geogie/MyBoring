package com.georgeren.myboring.music.mvp.presenter;

import android.content.Context;

import com.georgeren.myboring.music.mvp.contract.MediaPlayerContract;
import com.georgeren.myboring.music.mvp.model.Song;
import com.georgeren.myboring.utils.SPUtils;

/**
 * Created by georgeRen on 2017/7/18.
 */

public class PlayMusicPresenter extends MediaPlayPresenter implements MediaPlayerContract.PlayMusicControlPresenter {

    public PlayMusicPresenter(MediaPlayerContract.MediaPlayerRefreshView mView, Context mContext) {
        super(mView, mContext);
    }

    @Override
    public void saveLastPlayMusic(Song song, Context c) {
        if (song != null && song.hasDown) {
            SPUtils.writeStringConfig(SPUtils.KEY_LAST_PLAY_MUSIC, song.id);
        }
    }
}