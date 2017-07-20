package com.georgeren.myboring.music.itemhandler;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.georgeren.myboring.R;
import com.georgeren.myboring.base.adapter.ViewHolder;
import com.georgeren.myboring.base.ui.SimpleItemHandler;
import com.georgeren.myboring.event.ChangeSongEvent;
import com.georgeren.myboring.event.SongDeleteFromPlayQueueEvent;
import com.georgeren.myboring.music.mvp.model.Song;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by georgeRen on 2017/7/18.
 */

public class DialogMusicIH extends SimpleItemHandler<Song> {
    private TextView mTvMusicName;
    private TextView mTvArtist;
    private ImageView mIvDelete;

    @Override
    public void onCreateItemHandler(ViewHolder vh, ViewGroup parent) {
        super.onCreateItemHandler(vh, parent);
        mTvMusicName = vh.getTextView(R.id.item_dialog_music_tv_music_name);
        mTvArtist = vh.getTextView(R.id.item_dialog_music_tv_artist);
        mIvDelete = vh.getImageView(R.id.item_dialog_music_iv_delete);

        mIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new SongDeleteFromPlayQueueEvent(mData));
            }
        });
    }

    @Override
    public void onBindDataView(ViewHolder vh, Song data, int position) {
        if (data.isPlaying) {
            vh.getImageView(R.id.item_dialog_music_iv_volum).setVisibility(View.VISIBLE);
            mTvMusicName.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            mTvArtist.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        } else {
            vh.getImageView(R.id.item_dialog_music_iv_volum).setVisibility(View.INVISIBLE);
            mTvMusicName.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryText));
            mTvArtist.setTextColor(mContext.getResources().getColor(R.color.shadow_divider));
        }

        mTvMusicName.setText(data.name + "");
        mTvArtist.setText(" - " + data.getArtist());
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_dialog_music_item;
    }

    @Override
    public void onClick(View v) {
        EventBus.getDefault().post(new ChangeSongEvent(mData));
    }

}
