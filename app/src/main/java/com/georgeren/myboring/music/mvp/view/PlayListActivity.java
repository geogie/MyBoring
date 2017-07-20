package com.georgeren.myboring.music.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.georgeren.myboring.R;
import com.georgeren.myboring.base.adapter.BaseRVAdapter;
import com.georgeren.myboring.base.ui.BaseActivity;
import com.georgeren.myboring.base.ui.ItemHandler;
import com.georgeren.myboring.base.ui.ItemHandlerFactory;
import com.georgeren.myboring.base.view.LoadMoreRecycleView;
import com.georgeren.myboring.base.view.SToolBar;
import com.georgeren.myboring.db.DbBaseOperate;
import com.georgeren.myboring.db.DbManager;
import com.georgeren.myboring.event.AddMusicToQueueEvent;
import com.georgeren.myboring.event.AddToNextPlayEvent;
import com.georgeren.myboring.event.PlayListDeleteFromLikeEvent;
import com.georgeren.myboring.http.APIHelper;
import com.georgeren.myboring.http.CommonObserver;
import com.georgeren.myboring.music.itemhandler.LocalMusicIH;
import com.georgeren.myboring.music.mvp.contract.MediaPlayerContract;
import com.georgeren.myboring.music.mvp.contract.MusicServiceContract;
import com.georgeren.myboring.music.mvp.model.PlayList;
import com.georgeren.myboring.music.mvp.model.PlayListDetail;
import com.georgeren.myboring.music.mvp.model.SimpleSong;
import com.georgeren.myboring.music.mvp.presenter.MusicModelTranslatePresenter;
import com.georgeren.myboring.music.service.action.ClientPlayModeCommand;
import com.georgeren.myboring.music.service.action.ClientPlayQueueControlCommand;
import com.georgeren.myboring.music.view.PlayOperatorView;
import com.georgeren.myboring.utils.RVUtils;
import com.georgeren.myboring.utils.ToastUtils;
import com.georgeren.myboring.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class PlayListActivity extends BaseActivity {
    private static final String PLAY_LIST = "PLAY_LIST";
    private SimpleDraweeView mSdvBg;
    private PlayList mPlayList;
    private LoadMoreRecycleView mRv;
    private List<SimpleSong> mData = new ArrayList<>();
    private SToolBar mToolBar2;
    private AppBarLayout mAppBarLayout;
    private int mMaxScrollSize;
    private boolean mIsLove;
    private static final int PERCENTAGE_TO_SHOW_IMAGE = 90;

    private PlayOperatorView mPlayOperatorView;
    private MediaPlayerContract.ClientPlayModeCommand mPlayModeCommand;
    private MediaPlayerContract.ClientPlayQueueControlCommand mPlayQueueCommand;
    private DbBaseOperate<PlayList> mDbOperator;
    private ImageView mIvLoading;


    public static void start(Context mContext, PlayList mData) {
        Intent intent = new Intent();
        intent.putExtra(PLAY_LIST, mData);
        intent.setClass(mContext, PlayListActivity.class);
        mContext.startActivity(intent);
    }


    @Override
    protected void setStatusBar() {
        UIUtils.expandContentLayoutFullScreen(this);
    }

    @Override
    public int getLayoutId() {
        mPlayList = (PlayList) getIntent().getSerializableExtra(PLAY_LIST);
        return R.layout.activity_play_list;
    }

    @Override
    public void findView() {
        EventBus.getDefault().register(this);
        mPlayModeCommand = new ClientPlayModeCommand(this);
        mPlayQueueCommand = new ClientPlayQueueControlCommand(this);
        mDbOperator = new DbBaseOperate<>(DbManager.getLiteOrm(), this, PlayList.class);

        mSdvBg = (SimpleDraweeView) findViewById(R.id.ac_play_list_iv_bg);
        mRv = (LoadMoreRecycleView) findViewById(R.id.list_view);
        mToolBar2 = (SToolBar) findViewById(R.id.ac_play_list_tool_bar2);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.ac_play_list_app_bar_layout);
        mPlayOperatorView = (PlayOperatorView) findViewById(R.id.play_operator);
        mIvLoading = (ImageView) findViewById(R.id.iv_loading);
    }

    @Override
    public void initView() {
        mToolBar.setTitle(mPlayList.getName());
        mToolBar.setContext(this);
        mToolBar.setBackgroundResource(R.color.transparent);

        mSdvBg.setImageURI(mPlayList.getCoverImgUrl());
        mToolBar2.setTitle("共 " + mPlayList.getTrackCount() + " 首");
        mToolBar2.setBackgroundResource(R.color.white);
        mToolBar2.setTitleColorRes(R.color.black);

        mIvLoading.setVisibility(View.VISIBLE);
        mRv.setLayoutManager(RVUtils.getLayoutManager(this, LinearLayoutManager.VERTICAL));
        mRv.setAdapter(new BaseRVAdapter(this, mData) {
            @Override
            protected void initHandlers() {
                registerItemHandler(0, new ItemHandlerFactory() {
                    @Override
                    public ItemHandler newInstant(int viewType) {
                        return new LocalMusicIH(true);
                    }
                });
            }

            @Override
            protected int getViewType(int position) {
                return 0;
            }
        });

        mPlayOperatorView.hideNextPlay();
    }

    @Override
    public void initListener() {
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (mMaxScrollSize == 0)
                    mMaxScrollSize = appBarLayout.getTotalScrollRange();

                int currentScrollPercentage = (Math.abs(verticalOffset)) * 100
                        / mMaxScrollSize;

                if (currentScrollPercentage >= PERCENTAGE_TO_SHOW_IMAGE) {
                    mToolBar2.setBackgroundResource(R.color.transparent);
                    mToolBar2.setTitle(mPlayList.getName());
                    mToolBar2.setLeftIcon(R.mipmap.ic_black_back);
                    getWindow().setStatusBarColor(UIUtils.getColor(PlayListActivity.this, R.color.colorPrimary));
                } else {
                    mToolBar2.setLeftIcon(SToolBar.HIDDEN_LEFT_ICON_RES);
                    mToolBar2.setBackgroundResource(R.color.white);
                    mToolBar2.setTitle("共 " + mPlayList.getTrackCount() + " 首");
                    getWindow().setStatusBarColor(UIUtils.getColor(PlayListActivity.this, R.color.transparent));
                }
            }
        });

        mPlayOperatorView.setItemClickListener(new PlayOperatorView.OnItemClickListener() {
            @Override
            public void onCirclePlayItemClick(int mode) {
                if (mode == MusicServiceContract.PlayQueueControlPresenter.CIRCLE_MODE) {
                    mPlayModeCommand.circlePlayPlayList(mPlayList);
                } else {
                    mPlayModeCommand.startQueuePlayMode();
                }
            }

            @Override
            public void onRandomPlayItemClick(int mode) {
                if (mode == MusicServiceContract.PlayQueueControlPresenter.RANDOM_MODE) {
                    mPlayModeCommand.randomPlayPlayList(mPlayList);
                } else {
                    mPlayModeCommand.startQueuePlayMode();
                }
            }

            @Override
            public void onLikeItemClick(boolean like) {
                if (mIsLove) {
                    APIHelper.subscribeSimpleRequest(mDbOperator.delete(mPlayList), new CommonObserver<Boolean>() {
                        @Override
                        public void onNext(Boolean flag) {
                            ToastUtils.showShort(flag ? "已经从喜欢列表移除" : "从喜欢列表移除失败");
                            mIsLove = flag ? false : true;
                            mPlayOperatorView.refreshLikeStatus(mIsLove);
                            if (!mIsLove) {
                                EventBus.getDefault().post(new PlayListDeleteFromLikeEvent(mPlayList));
                            }
                        }
                    });
                } else {
                    APIHelper.subscribeSimpleRequest(mDbOperator.add(mPlayList), new CommonObserver<Boolean>() {
                        @Override
                        public void onNext(Boolean flag) {
                            ToastUtils.showShort(flag ? "已喜欢" : "喜欢失败");
                            mIsLove = flag ? true : false;
                            mPlayOperatorView.refreshLikeStatus(mIsLove);
                        }
                    });
                }
            }

            @Override
            public void onMusicListClick() {

            }
        });
    }

    @Override
    public void initData() {
        initLikeStatus();
        APIHelper.subscribeSimpleRequest(APIHelper.getMusicServices().getPlayListDetail(Integer.valueOf(mPlayList.getId())), new CommonObserver<PlayListDetail>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onNext(PlayListDetail playListDetail) {
                mIvLoading.setVisibility(View.INVISIBLE);
                mData.addAll(new MusicModelTranslatePresenter().translateTracksToSimpleSong(playListDetail.getPlaylist().getTracks()));
                mRv.getAdapter().notifyDataSetChanged();
            }
        });
    }
    private void initLikeStatus() {
        APIHelper.subscribeSimpleRequest(mDbOperator.query(mPlayList.getId()), new CommonObserver<PlayList>() {
            @Override
            public void onNext(PlayList playList) {
                mIsLove = playList != null ? true : false;
                mPlayOperatorView.refreshLikeStatus(mIsLove);
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AddMusicToQueueEvent event) {
        mPlayQueueCommand.addMusicToQueue(event.song);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AddToNextPlayEvent event) {
        mPlayQueueCommand.addMusicToNextPlay(event.song);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
