package com.georgeren.myboring.test1adapter;

import com.georgeren.myboring.R;
import com.georgeren.myboring.base.SAppApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by georgeRen on 2017/7/24.
 */

public class T1DrawerData {


    public static final String DRAWER_AUTHOR = "关于作者";
    public static final String DRAWER_COLLECT = "我的收藏";
    public static final String DRAWER_APP_INFO = "应用信息";
    public static final String DRAWER_SETTING = "设置";
    public static List<T1DividerMark> sData;
    public static List<T1DividerMark> getData() {
        if (sData == null) {
            sData = new ArrayList<>();
            sData.add(new DrawerHeader(SAppApplication.getAppContext().getString(R.string.author_nickname), true));
            sData.add(new DrawerItem(DRAWER_AUTHOR, R.mipmap.ic_drawer_author, false));
            sData.add(new DrawerItem(DRAWER_COLLECT, R.mipmap.ic_drawer_collect, false));
            sData.add(new DrawerItem(DRAWER_APP_INFO, R.mipmap.ic_drawer_app_info, false));
            sData.add(new DrawerItem(DRAWER_SETTING, R.mipmap.ic_drawer_setting, false));
        }
        return sData;
    }

    public static class DrawerHeader extends T1DividerMark {
        public String icon;
        public String username;
        public String background;

        public DrawerHeader(String username, boolean needDivider) {
            super(needDivider);
            this.username = username;
        }
    }

    public static class DrawerItem extends T1DividerMark {
        public int imageRes;
        public String type;
        public String appendDesc;


        public DrawerItem(String item, int imageRes, boolean needDivider) {
            super(needDivider);
            this.type = item;
            this.imageRes = imageRes;
        }
    }
}
