package com.georgeren.myboring.music.itemhandler;

import android.text.TextUtils;
import android.view.View;

import com.georgeren.myboring.base.adapter.ViewHolder;
import com.georgeren.myboring.event.AddMusicToQueueEvent;
import com.georgeren.myboring.event.AddToNextPlayEvent;
import com.georgeren.myboring.music.mvp.model.SimpleSong;
import com.georgeren.myboring.music.mvp.view.PlayMusicActivity;
import com.georgeren.myboring.utils.AlbumUtils;
import com.georgeren.myboring.utils.TimeUtils;
import com.georgeren.myboring.utils.ToastUtils;
import com.georgeren.myboring.utils.UIUtils;
import com.georgeren.myboring.utils.ULog;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by georgeRen on 2017/7/19.
 */

public class LocalMusicIH extends SimpleMusicIH<SimpleSong> {
    private static final String TAG = "LocalMusicIH";

    public LocalMusicIH(boolean showNextPlay) {
        super(showNextPlay);
    }

    @Override
    protected void onAddToNextPlayClick() {
        ToastUtils.showShort("已经添加下一首播放");
        EventBus.getDefault().post(new AddToNextPlayEvent(mData));
    }

    @Override
    protected void onItemClick() {
        if (!mData.isFromPlayList() && mData.isHasDown()) {
            PlayMusicActivity.start(mContext, mData.translateToSong(), false);
        } else {
            PlayMusicActivity.start(mContext, mData.translateToSong(), true);
        }

        EventBus.getDefault().post(new AddMusicToQueueEvent(mData.translateToSong()));
    }

    @Override
    protected void bindData(ViewHolder vh, SimpleSong data, int position) {
        mTvTile.setText(data.getDisplayName() + "");
        String desc = data.getArtist();

        if (!TextUtils.isEmpty(data.getAlbum())) {
            desc += "-" + data.getAlbum();
        }

        mTvSecondTile.setText(desc);
        mSdvAlbum.setImageBitmap(null);// 解决没有bitmap的被有bitmap覆盖问题
        if (data.isHasDown()) {
            ULog.d(TAG, "bindData", "has position:" + position + " path:" + data.getPath());
            AlbumUtils.setAlbum(mSdvAlbum, data.getPath());
            String duration = TimeUtils.formatDuration(data.getDuration());
            mTvDuration.setText(duration.equals("00:00") ? "" : duration);
        } else {
            ULog.d(TAG, "bindData", "hasNo position:" + position + " path:" + data.getPicPath());
            UIUtils.loadSmallPicture(mSdvAlbum, data.getPicPath());
            mTvDuration.setVisibility(View.GONE);
        }
    }

}
