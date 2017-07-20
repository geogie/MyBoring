package com.georgeren.myboring.music.itemhandler;

import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.georgeren.myboring.R;
import com.georgeren.myboring.base.adapter.ViewHolder;
import com.georgeren.myboring.base.ui.SimpleItemHandler;
import com.georgeren.myboring.music.mvp.model.SimpleTitle;

/**
 * Created by georgeRen on 2017/7/17.
 *
 * 推荐歌单 Item
 */

public class MusicPageTitleIH extends SimpleItemHandler<SimpleTitle> {
    @Override
    public void onCreateItemHandler(ViewHolder vh, ViewGroup parent) {
        super.onCreateItemHandler(vh, parent);
        ViewGroup.LayoutParams layoutParams = vh.getConvertView().getLayoutParams();
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
        }
    }

    @Override
    public void onBindDataView(ViewHolder vh, SimpleTitle data, int position) {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_simple_title;
    }

    @Override
    public void onClick(View v) {

    }
}
