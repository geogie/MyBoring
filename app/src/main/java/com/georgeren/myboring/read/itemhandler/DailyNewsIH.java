package com.georgeren.myboring.read.itemhandler;

import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.georgeren.myboring.R;
import com.georgeren.myboring.base.adapter.ViewHolder;
import com.georgeren.myboring.base.ui.SimpleItemHandler;
import com.georgeren.myboring.read.mvp.entity.NewsDetail;
import com.georgeren.myboring.read.mvp.view.ZhiHuEssayActivity;

/**
 * Created by georgeRen on 2017/7/19.
 */

public class DailyNewsIH extends SimpleItemHandler<NewsDetail> {

    @Override
    public void onCreateItemHandler(ViewHolder vh, ViewGroup parent) {
        super.onCreateItemHandler(vh, parent);
    }

    @Override
    public void onBindDataView(ViewHolder vh, NewsDetail data, int position) {
        vh.getTextView(R.id.item_daily_new_tv_title).setText(data.getTitle());
        SimpleDraweeView mIvImg = vh.get(R.id.item_daily_new_iv_img);
        if (data.getImages() != null && !data.getImages().isEmpty()) {
            mIvImg.setImageURI(data.getImages().get(0));
        } else if (data.getImage() != null) {
            mIvImg.setImageURI(data.getImage());
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_daily_news;
    }

    @Override
    public void onClick(View v) {
        ZhiHuEssayActivity.start(mContext, mData.getId(), mData.date);
    }
}
