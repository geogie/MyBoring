package com.georgeren.myboring.read.adapter;

import android.app.Activity;

import com.georgeren.myboring.base.adapter.BaseRVAdapter;
import com.georgeren.myboring.base.ui.ItemHandler;
import com.georgeren.myboring.base.ui.ItemHandlerFactory;
import com.georgeren.myboring.read.itemhandler.DailyNewsDateIH;
import com.georgeren.myboring.read.itemhandler.DailyNewsIH;
import com.georgeren.myboring.read.itemhandler.TopNewsIH;
import com.georgeren.myboring.read.mvp.entity.DailyNewsDate;
import com.georgeren.myboring.read.mvp.entity.NewsDetail;

import java.util.List;

/**
 * Created by georgeRen on 2017/7/19.
 * item：3种
 */

public class ZhiHuDailyAdapter extends BaseRVAdapter {
    private static final int TYPE_NEWS = 1;
    private static final int TYPE_DATE = 2;
    private static final int TYPE_TOP_NEWS = 3;// banner

    public ZhiHuDailyAdapter(Activity activity, List<?> data) {
        super(activity, data);
    }

    @Override
    protected void initHandlers() {
        registerItemHandler(TYPE_TOP_NEWS, new ItemHandlerFactory() {
            @Override
            public ItemHandler newInstant(int viewType) {
                return new TopNewsIH();
            }
        });

        registerItemHandler(TYPE_NEWS, new ItemHandlerFactory() {
            @Override
            public ItemHandler newInstant(int viewType) {
                return new DailyNewsIH();
            }
        });

        registerItemHandler(TYPE_DATE, new ItemHandlerFactory() {
            @Override
            public ItemHandler newInstant(int viewType) {
                return new DailyNewsDateIH();
            }
        });
    }

    @Override
    protected int getViewType(int position) {

        Object item = getItem(position);

        if (item instanceof List) {
            return TYPE_TOP_NEWS;
        }

        if (item instanceof NewsDetail) {
            return TYPE_NEWS;
        }

        if (item instanceof DailyNewsDate) {
            return TYPE_DATE;
        }

        return 0;
    }

}
