package com.georgeren.myboring.base.ui.mainui.drawer;

import android.content.Context;

import com.georgeren.myboring.R;
import com.georgeren.myboring.base.SAppApplication;
import com.georgeren.myboring.base.ui.AppInfoActivity;
import com.georgeren.myboring.base.ui.AuthorActivity;
import com.georgeren.myboring.base.ui.CollectActivity;
import com.georgeren.myboring.base.ui.SettingActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by georgeRen on 2017/7/14.
 * 侧滑菜单UI控制常量
 */

public class DrawerData {
    public static final String DRAWER_AUTHOR = "关于作者";
    public static final String DRAWER_COLLECT = "我的收藏";
    public static final String DRAWER_APP_INFO = "应用信息";
    public static final String DRAWER_SETTING = "设置";
    public static List<DividerMark> sData;// 抽屉中的第二中item的数据源（图片+文字+是否显示分割线）

    /**
     * 抽屉中的第二种item的childItem数据源+是否显示分割线
     * @return
     */
    public static List<DividerMark> getData() {
        if (sData == null) {
            sData = new ArrayList<>();
            sData.add(new DrawerHeader(SAppApplication.getAppContext().getString(R.string.author_nickname), true));
            sData.add(new DrawerItem(DRAWER_AUTHOR, R.mipmap.ic_drawer_author, false));
            sData.add(new DrawerItem(DRAWER_COLLECT, R.mipmap.ic_drawer_collect, false));// 我的收藏
            sData.add(new DrawerItem(DRAWER_APP_INFO, R.mipmap.ic_drawer_app_info, false));// 应用信息
            sData.add(new DrawerItem(DRAWER_SETTING, R.mipmap.ic_drawer_setting, false));
        }
        return sData;
    }

    public static class DrawerItem extends DividerMark {
        public int imageRes;
        public String type;

        public DrawerItem(String item, int imageRes, boolean needDivider) {
            super(needDivider);
            this.type = item;
            this.imageRes = imageRes;
        }
    }
    public static void onItemClick(Context context, String type) {
        switch (type) {
            case DRAWER_AUTHOR:
                AuthorActivity.start(context);
                break;

            case DRAWER_COLLECT:
                CollectActivity.start(context);
                break;

            case DRAWER_APP_INFO:
                AppInfoActivity.start(context);
                break;

            case DRAWER_SETTING:
                SettingActivity.start(context);
                break;
        }
    }

    public static class DrawerHeader extends DividerMark {
        public String username;
        public String background;

        public DrawerHeader(String username, boolean needDivider) {
            super(needDivider);
            this.username = username;
        }
    }
}
