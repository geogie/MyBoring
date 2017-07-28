package com.georgeren.myboring.base.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.georgeren.myboring.R;
import com.georgeren.myboring.base.view.SToolBar;
import com.georgeren.myboring.base.view.swipebacklayout.SwipeBackActivity;
import com.georgeren.myboring.base.view.swipebacklayout.SwipeBackLayout;

/**
 * Created by georgeRen on 2017/7/14.
 */

public abstract class BaseActivity extends SwipeBackActivity {
    public SToolBar mToolBar;
    private SwipeBackLayout mSwipeBackLayout;

    @LayoutRes
    public abstract int getLayoutId();

    public abstract void findView();

    public abstract void initView();

    public abstract void initListener();

    public abstract void initData();

    protected void initParamsAndPresenter() {
    }

    protected void setStatusBar() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        initSwipeBackLayout();
        setContentView(getLayoutId());
        setStatusBar();
        initTransitionAnim();
        View view = findViewById(R.id.toolbar);
        if (view != null && view instanceof SToolBar) {
            mToolBar = (SToolBar) view;
            mToolBar.setLeftIcon(R.mipmap.ic_back);
        }

        initParamsAndPresenter();
        findView();
        initView();
        initListener();
        initData();
    }

    public void initTransitionAnim() {

    }

    @SuppressWarnings("deprecation")
    public void initSwipeBackLayout() {
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }
}
