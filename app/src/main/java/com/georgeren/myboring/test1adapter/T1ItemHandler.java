package com.georgeren.myboring.test1adapter;

import android.support.annotation.LayoutRes;
import android.view.ViewGroup;

/**
 * Created by georgeRen on 2017/7/24.
 */

public interface T1ItemHandler<T extends Object> {
    void  onCreateItemHandler(T1ViewHolder vh, ViewGroup parent);

    @LayoutRes
    int getLayoutResId();

    void onBindView(Object adapter, T1ViewHolder vh, T data, int position);
}
