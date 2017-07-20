package com.georgeren.myboring.base.ui.mainui.drawer;

import android.view.View;
import android.view.ViewGroup;

import com.georgeren.myboring.R;
import com.georgeren.myboring.base.adapter.ViewHolder;
import com.georgeren.myboring.base.ui.SimpleItemHandler;

/**
 * Created by georgeRen on 2017/7/14.
 */

public class DrawerHeaderItemHandler extends SimpleItemHandler<DrawerData.DrawerHeader> {

    @Override
    public void onCreateItemHandler(ViewHolder vh, ViewGroup parent) {
        super.onCreateItemHandler(vh, parent);
    }

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
