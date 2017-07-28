package com.georgeren.myboring.base.ui.mainui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.georgeren.myboring.R;
import com.georgeren.myboring.base.ui.BaseActivity;
import com.georgeren.myboring.base.ui.mainui.drawer.DrawerData;
import com.georgeren.myboring.base.ui.mainui.drawer.MainDrawerAdapter;
import com.georgeren.myboring.base.view.SToolBar;
import com.georgeren.myboring.music.MusicPageFragment;
import com.georgeren.myboring.music.service.MusicServiceInstruction;
import com.georgeren.myboring.read.ReadPageFragment;
import com.georgeren.myboring.utils.RVUtils;
import com.georgeren.myboring.utils.ToastUtils;
import com.georgeren.myboring.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawerMenu;
    private RecyclerView mDrawerRv;
    private ViewPager mViewPager;
    private List<Fragment> mMainUIFragments = new ArrayList<>();

    public static void start(Context srcContext) {
        Intent intent = new Intent();
        intent.setClass(srcContext, MainActivity.class);
        srcContext.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mMainUIFragments.add(SToolBar.ITEM_MUSIC, new MusicPageFragment());
        mMainUIFragments.add(SToolBar.ITEM_INTERESTING, new ReadPageFragment());
        super.onCreate(savedInstanceState);
        getSwipeBackLayout().setEnableGesture(false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void findView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerMenu = (LinearLayout) findViewById(R.id.drawer);
        mDrawerRv = (RecyclerView) findViewById(R.id.list_view);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
    }

    @Override
    public void initView() {
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mMainUIFragments.get(position);
            }

            @Override
            public int getCount() {
                return mMainUIFragments.size();
            }
        });

        mToolBar.setLeftIcon(R.mipmap.ic_menu);
        mToolBar.setMainPage(true, mViewPager);   //必须要先设置adapter
        mToolBar.showCurrentSelectedFragment();
        mDrawerRv.setLayoutManager(RVUtils.getLayoutManager(this, LinearLayoutManager.VERTICAL));
        //  被第二种item的第一个childitem遮盖住了这条线，childitme的分割线也受这个控制childitem相互遮盖（childitem还受DividerMark控制是否显示）
        mDrawerRv.addItemDecoration(RVUtils.getDrawerItemDecorationDivider(this,
                R.color.divider,
                new Rect(UIUtils.dp2Px(40), UIUtils.dp2Px(40), UIUtils.dp2Px(30), 0),
                DrawerData.getData()));
    }

    @Override
    public void initListener() {
        mToolBar.setItemClickListener(new SToolBar.OnItemClickListener() {
            @Override
            public void onMenuItemClick(View v) {
                mDrawerLayout.openDrawer(mDrawerMenu);
            }
        });

        mDrawerRv.setAdapter(new MainDrawerAdapter(this, DrawerData.getData()));
    }

    @Override
    public void initData() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(MusicServiceInstruction.SERVER_RECEIVER_SAVE_LAST_PLAY_MUSIC);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    private long exitTime = 0;

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtils.showLong("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}
