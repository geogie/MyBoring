package com.georgeren.myboring.test1adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.georgeren.myboring.R;

/**
 * Created by georgeRen on 2017/7/24.
 * 第一种item
 */

public class T1DrawerHeaderItemHandler extends T1SimpleItemHandler<T1DrawerData.DrawerHeader> {
    private static final String TAG = "T1DrawerHeaderItemHandl";
    @Override
    public void onBindDataView(T1ViewHolder vh, T1DrawerData.DrawerHeader data, int position) {
        vh.getTextView(R.id.drawer_header_username).setText(data.username);
    }


    @Override
    public int getLayoutResId() {
        return R.layout.item_drawer_header;
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG,"onClick-"+mData.username);
    }
}
