package com.georgeren.myboring.base.ui.mainui.drawer;

import android.view.View;
import android.view.ViewGroup;

import com.georgeren.myboring.R;
import com.georgeren.myboring.base.adapter.ViewHolder;
import com.georgeren.myboring.base.ui.SimpleItemHandler;

/**
 * Created by georgeRen on 2017/7/14.
 *
 * 抽屉中第一种item：实例（必须实现下面三个方法）
 *
 * onBindDataView：数据绑定到view上
 *
 * getLayoutResId：item的布局
 *
 * onClick：点击事件
 *
 */

public class DrawerHeaderItemHandler extends SimpleItemHandler<DrawerData.DrawerHeader> {
    @Override
    public void onBindDataView(ViewHolder vh, DrawerData.DrawerHeader data, int position) {
        vh.getTextView(R.id.drawer_header_username).setText(data.username);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_drawer_header;
    }


    @Override
    public void onClick(View view) {

    }

}
