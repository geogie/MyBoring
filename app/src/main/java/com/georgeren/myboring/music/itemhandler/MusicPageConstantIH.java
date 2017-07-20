package com.georgeren.myboring.music.itemhandler;

import android.app.Activity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.georgeren.myboring.R;
import com.georgeren.myboring.base.adapter.ViewHolder;
import com.georgeren.myboring.base.ui.SimpleItemHandler;
import com.georgeren.myboring.db.DbManager;
import com.georgeren.myboring.db.operate.MusicDbOperator;
import com.georgeren.myboring.music.mvp.model.MusicPageConstantItem;
import com.georgeren.myboring.music.mvp.model.SimpleSong;
import com.georgeren.myboring.music.mvp.view.LocalMusicActivity;
import com.georgeren.myboring.music.mvp.view.MyLikeActivity;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by georgeRen on 2017/7/14.
 * item：2个
 * 本地音乐
 * 我的喜欢
 */

public class MusicPageConstantIH extends SimpleItemHandler<MusicPageConstantItem> {
    public static final int LOCAL_MUSIC = 1;
    public static final int MY_COLLECT = 2;
    private MusicPageConstantItem mData;
    private TextView tvItem;

    @Override
    public void onCreateItemHandler(ViewHolder vh, ViewGroup parent) {
        super.onCreateItemHandler(vh, parent);
        ViewGroup.LayoutParams layoutParams = vh.getConvertView().getLayoutParams();
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
        }
    }

    @Override
    public void onBindDataView(final ViewHolder vh, MusicPageConstantItem data, int position) {
        mData = data;
        vh.getImageView(R.id.item_music_page_constant_iv_icon).setImageResource(data.iconId);
        tvItem = vh.getTextView(R.id.item_music_page_constant_tv_item);
        tvItem.setText(data.item);
        if (data.type == LOCAL_MUSIC) {
            vh.getTextView(R.id.item_music_page_constant_tv_append_dec).setText("-_-?");
            new MusicDbOperator(DbManager.getLiteOrm(), mContext, SimpleSong.class)
                    .getLocalMusicCount()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Long>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            vh.getTextView(R.id.item_music_page_constant_tv_append_dec).setText("统计出错");
                        }

                        @Override
                        public void onNext(Long count) {
                            vh.getTextView(R.id.item_music_page_constant_tv_append_dec).setText("" + count);
                        }
                    });
        }


        if (data.type == MY_COLLECT) {
            vh.getTextView(R.id.item_music_page_constant_tv_append_dec).setText("-_-?");
            new MusicDbOperator(DbManager.getLiteOrm(), mContext, SimpleSong.class)
                    .getLikeMusicCount()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Long>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            vh.getTextView(R.id.item_music_page_constant_tv_append_dec).setText("统计出错");
                        }

                        @Override
                        public void onNext(Long count) {
                            vh.getTextView(R.id.item_music_page_constant_tv_append_dec).setText("" + count);
                        }
                    });
        }

    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_music_page_constant;
    }

    @Override
    public void onClick(View view) {
        switch (mData.type) {
            case LOCAL_MUSIC:
                LocalMusicActivity.start((Activity) mContext);
                break;
            case MY_COLLECT:
                MyLikeActivity.start(mContext);
                break;
        }
    }
}
