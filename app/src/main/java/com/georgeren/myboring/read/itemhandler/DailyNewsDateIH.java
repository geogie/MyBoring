package com.georgeren.myboring.read.itemhandler;

import android.view.View;

import com.georgeren.myboring.R;
import com.georgeren.myboring.base.adapter.ViewHolder;
import com.georgeren.myboring.base.ui.SimpleItemHandler;
import com.georgeren.myboring.read.mvp.entity.DailyNewsDate;

/**
 * Created by georgeRen on 2017/7/19.
 */

public class DailyNewsDateIH extends SimpleItemHandler<DailyNewsDate> {

    @Override
    public void onBindDataView(ViewHolder vh, DailyNewsDate data, int position) {
        vh.getTextView(R.id.item_daily_news_date_tv_date).setText(data.date+"");
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_daily_news_date;
    }

    @Override
    public void onClick(View v) {
    }
}
