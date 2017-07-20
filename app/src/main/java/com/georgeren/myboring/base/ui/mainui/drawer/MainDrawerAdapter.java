package com.georgeren.myboring.base.ui.mainui.drawer;

import android.app.Activity;

import com.georgeren.myboring.base.adapter.BaseRVAdapter;
import com.georgeren.myboring.base.ui.ItemHandler;
import com.georgeren.myboring.base.ui.ItemHandlerFactory;

import java.util.List;

/**
 * Created by georgeRen on 2017/7/14.
 * Item: 2种
 */

public class MainDrawerAdapter extends BaseRVAdapter {
    private final int ITEM_HANDLER_HEADER = 1;
    private final int ITEM_HANDLER_ITEM = 2;

    public MainDrawerAdapter(Activity context, List<?> data) {
        super(context, data);
    }

    @Override
    protected void initHandlers() {
        registerItemHandler(ITEM_HANDLER_HEADER, new ItemHandlerFactory() {
            @Override
            public ItemHandler newInstant(int viewType) {
                return new DrawerHeaderItemHandler();
            }
        });

        registerItemHandler(ITEM_HANDLER_ITEM, new ItemHandlerFactory() {
            @Override
            public ItemHandler newInstant(int viewType) {
                return new DrawerItemHandler();
            }
        });

    }

    @Override
    protected int getViewType(int position) {
        Object o = mData.get(position);

        if (o instanceof DrawerData.DrawerHeader) {
            return ITEM_HANDLER_HEADER;
        }

        if (o instanceof DrawerData.DrawerItem) {
            return ITEM_HANDLER_ITEM;
        }

        return -1;
    }
}
