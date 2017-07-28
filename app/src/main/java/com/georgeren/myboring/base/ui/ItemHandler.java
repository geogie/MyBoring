package com.georgeren.myboring.base.ui;

import android.support.annotation.LayoutRes;
import android.view.ViewGroup;

import com.georgeren.myboring.base.adapter.ViewHolder;

/**
 * Created by georgeRen on 2017/7/14.
 * Item的顶层封装：范型有利于数据类型的通用性，公共方法提取3个。
 *
 * onCreateItemHandler：实例item和根布局parent
 *
 * getLayoutResId：item的布局
 *
 * onBindView：adapter、holder、数据源、position进行绑定。
 *
 */

public interface ItemHandler <T extends Object>  {

    void  onCreateItemHandler(ViewHolder vh, ViewGroup parent);

    @LayoutRes
    int getLayoutResId();

    void onBindView(Object adapter, ViewHolder vh, T data, int position);

}
