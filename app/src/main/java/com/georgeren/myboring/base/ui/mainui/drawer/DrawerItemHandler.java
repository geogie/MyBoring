package com.georgeren.myboring.base.ui.mainui.drawer;

import android.view.View;
import android.view.ViewGroup;

import com.georgeren.myboring.R;
import com.georgeren.myboring.base.adapter.ViewHolder;
import com.georgeren.myboring.base.ui.SimpleItemHandler;

/**
 * Created by georgeRen on 2017/7/14.
 */

public class DrawerItemHandler extends SimpleItemHandler<DrawerData.DrawerItem> {

    @Override
    public void onCreateItemHandler(ViewHolder vh, ViewGroup parent) {
        super.onCreateItemHandler(vh, parent);
    }

    @Override

    public void onBindDataView(ViewHolder vh, DrawerData.DrawerItem data, int position) {
        vh.getTextView(R.id.drawer_item_title).setText(data.type);
        if (data.imageRes != -1) {
            vh.getImageView(R.id.drawer_item_image).setImageResource(data.imageRes);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_drawer_item;
    }

    @Override
    public void onClick(View view) {
        DrawerData.onItemClick(mContext, mData.type);
    }

}
