package com.georgeren.myboring.base.ui;

import android.support.annotation.LayoutRes;
import android.view.ViewGroup;

import com.georgeren.myboring.base.adapter.ViewHolder;

/**
 * Created by georgeRen on 2017/7/14.
 */

public interface ItemHandler <T extends Object>  {

    void  onCreateItemHandler(ViewHolder vh, ViewGroup parent);

    @LayoutRes
    int getLayoutResId();

    void onBindView(Object adapter, ViewHolder vh, T data, int position);

}
