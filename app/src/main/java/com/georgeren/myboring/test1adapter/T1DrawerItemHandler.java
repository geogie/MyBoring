package com.georgeren.myboring.test1adapter;

import android.util.Log;
import android.view.View;

import com.georgeren.myboring.R;

/**
 * Created by georgeRen on 2017/7/24.
 */

public class T1DrawerItemHandler extends T1SimpleItemHandler<T1DrawerData.DrawerItem> {
    private static final String TAG = "T1DrawerItemHandler";
    @Override
    public void onClick(View v) {
        Log.d(TAG,"onClick-type:"+mData.type);
    }


    @Override
    public int getLayoutResId() {
        return R.layout.item_drawer_item;
    }


    @Override
    public void onBindDataView(T1ViewHolder vh, T1DrawerData.DrawerItem data, int position) {
        vh.getTextView(R.id.drawer_item_title).setText(data.type);
        if (data.imageRes != -1) {
            vh.getImageView(R.id.drawer_item_image).setImageResource(data.imageRes);
        }
    }
}
