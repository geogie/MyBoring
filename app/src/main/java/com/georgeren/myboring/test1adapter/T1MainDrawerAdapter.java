package com.georgeren.myboring.test1adapter;

import android.app.Activity;

import java.util.List;

/**
 * Created by georgeRen on 2017/7/24.
 * 两种item：
 *
 */

public class T1MainDrawerAdapter extends T1BaseRVAdapter{
    private final int ITEM_HANDLER_HEADER = 1;
    private final int ITEM_HANDLER_ITEM = 2;

    public T1MainDrawerAdapter(Activity context, List<?> data) {
        super(context, data);
    }

    /**
     * 初始化两种item：
     */
    @Override
    protected void initHandlers() {
        registerItemHandler(ITEM_HANDLER_HEADER, new T1ItemHandlerFactory() {
            @Override
            public T1ItemHandler newInstant(int viewType) {
                return new T1DrawerHeaderItemHandler();
            }
        });

        registerItemHandler(ITEM_HANDLER_ITEM, new T1ItemHandlerFactory() {
            @Override
            public T1ItemHandler newInstant(int viewType) {
                return new T1DrawerItemHandler();
            }
        });
    }

    /**
     * 获取item类型：
     * @param position
     * @return
     */
    @Override
    protected int getViewType(int position) {
        Object o = mData.get(position);

        if (o instanceof T1DrawerData.DrawerHeader) {
            return ITEM_HANDLER_HEADER;
        }

        if (o instanceof T1DrawerData.DrawerItem) {
            return ITEM_HANDLER_ITEM;
        }

        return -1;
    }
}
