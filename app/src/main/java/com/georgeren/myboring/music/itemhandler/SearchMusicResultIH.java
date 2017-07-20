package com.georgeren.myboring.music.itemhandler;

import android.view.View;
import android.view.ViewGroup;

import com.georgeren.myboring.base.adapter.ViewHolder;
import com.georgeren.myboring.event.AddToNextPlayEvent;
import com.georgeren.myboring.music.mvp.model.Song;
import com.georgeren.myboring.music.mvp.view.PlayMusicActivity;
import com.georgeren.myboring.utils.ToastUtils;
import com.georgeren.myboring.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by georgeRen on 2017/7/17.
 */

public class SearchMusicResultIH extends SimpleMusicIH<Song> {
    public SearchMusicResultIH(boolean showNextPlay) {
        super(showNextPlay);
    }

    @Override
    public void onCreateItemHandler(ViewHolder vh, ViewGroup parent) {
        super.onCreateItemHandler(vh, parent);
        mTvDuration.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onAddToNextPlayClick() {
        ToastUtils.showShort("已经添加下一首播放");
        EventBus.getDefault().post(new AddToNextPlayEvent(mData));
    }

    @Override
    protected void onItemClick() {
        PlayMusicActivity.start(mContext, mData, false);
    }

    @Override
    protected void bindData(ViewHolder vh, Song data, int position) {
        mTvTile.setText(data.name);
        if (!data.artists.isEmpty()) {
            mTvSecondTile.setText(data.artists.get(0).name + "-" + data.album.name);
        }
        UIUtils.loadSmallPicture(mSdvAlbum, data.album.picUrl);
    }

}
