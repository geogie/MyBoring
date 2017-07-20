package com.georgeren.myboring.music.mvp.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.georgeren.myboring.R;
import com.georgeren.myboring.base.ui.BaseActivity;
import com.georgeren.myboring.base.view.SToolBar;
import com.georgeren.myboring.db.DbManager;
import com.georgeren.myboring.db.operate.MusicDbOperator;
import com.georgeren.myboring.event.ChangeSongEvent;
import com.georgeren.myboring.event.SongDeleteFromLikeEvent;
import com.georgeren.myboring.event.SongDeleteFromPlayQueueEvent;
import com.georgeren.myboring.http.APIHelper;
import com.georgeren.myboring.http.CommonObserver;
import com.georgeren.myboring.music.mvp.contract.MediaPlayerContract;
import com.georgeren.myboring.music.mvp.contract.MusicServiceContract;
import com.georgeren.myboring.music.mvp.model.SimpleSong;
import com.georgeren.myboring.music.mvp.model.Song;
import com.georgeren.myboring.music.mvp.presenter.ClientReceiverPresenter;
import com.georgeren.myboring.music.service.action.ClientPlayControlCommand;
import com.georgeren.myboring.music.service.action.ClientPlayModeCommand;
import com.georgeren.myboring.music.service.action.ClientPlayQueueControlCommand;
import com.georgeren.myboring.music.view.LyricView;
import com.georgeren.myboring.music.view.MediaSeekBar;
import com.georgeren.myboring.music.view.MusicPlayControlView;
import com.georgeren.myboring.music.view.MusicPlayQueueWindow;
import com.georgeren.myboring.music.view.PlayOperatorView;
import com.georgeren.myboring.utils.AlbumUtils;
import com.georgeren.myboring.utils.TimeUtils;
import com.georgeren.myboring.utils.ToastUtils;
import com.georgeren.myboring.utils.TransitionHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

//音乐播放界面, MVP, 只控制UI的显示, 和UI交互事件的通知

public class PlayMusicActivity  extends BaseActivity implements MediaPlayerContract.PlayView {
    private static final String TO_PLAY_MUSIC_INFO = "played_music";
    private static final String FROM_LITTLE_PANEL = "from_little_panel";
    private static final String NEED_LOAD = "needLoad";

    private SToolBar mToolBar;
    private MediaSeekBar mSeekBar;
    private MusicPlayControlView mPlayControlView;
    private TextView mTvPlayedTime;
    private TextView mTvLeftTime;
    private SimpleDraweeView mSdvAlbum;
    private LyricView mTvMusicName;
    private PlayOperatorView mPovMusicPlayControl;

    private Song mSong;
    private boolean mIsFromLittlePanel;
    private boolean mIsLoading;

    private MusicDbOperator mDbOperator;
    private MediaPlayerContract.ClientPlayControlCommand mPlayControlCommand;
    private MediaPlayerContract.ClientPlayModeCommand mPlayModeCommand;
    private MediaPlayerContract.ClientReceiverPresenter mClientReceiver;
    private MediaPlayerContract.ClientPlayQueueControlCommand mPlayQueueCommand;
    private MusicPlayQueueWindow mDialog;
    private boolean mIsLove;


    public static void start(Context context, Song song, boolean needLoad) {
        Intent intent = new Intent();
        intent.putExtra(TO_PLAY_MUSIC_INFO, song);
        intent.putExtra(NEED_LOAD, needLoad);
        intent.setClass(context, PlayMusicActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initTransitionAnim() {
        if (getIntent().getBooleanExtra(FROM_LITTLE_PANEL, false)) {
            Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.slide_from_bottom);
            getWindow().setEnterTransition(transition);
        }
    }

    @Override
    protected void setStatusBar() {
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_play_music;
    }

    @Override
    protected void initParamsAndPresenter() {
        mIsFromLittlePanel = getIntent().getBooleanExtra(FROM_LITTLE_PANEL, false);
        mSong = (Song) getIntent().getSerializableExtra(TO_PLAY_MUSIC_INFO);
    }

    @Override
    public void findView() {
        EventBus.getDefault().register(this);
        mPlayControlCommand = new ClientPlayControlCommand(this);
        mPlayModeCommand = new ClientPlayModeCommand(this);
        mPlayQueueCommand = new ClientPlayQueueControlCommand(this);
        mClientReceiver = new ClientReceiverPresenter(this);
        mClientReceiver.setPlayView(this);
        mDbOperator = new MusicDbOperator(DbManager.getLiteOrm(), this, SimpleSong.class);

        mToolBar = (SToolBar) findViewById(R.id.toolbar);
        mSeekBar = (MediaSeekBar) findViewById(R.id.seek_bar);
        mPlayControlView = (MusicPlayControlView) findViewById(R.id.control_view);
        mTvPlayedTime = (TextView) findViewById(R.id.tv_has_play_time);
        mTvLeftTime = (TextView) findViewById(R.id.tv_left_time);
        mSdvAlbum = (SimpleDraweeView) findViewById(R.id.ac_play_music_sdv_album);
        mTvMusicName = (LyricView) findViewById(R.id.ac_play_tv_music_name);
        mPovMusicPlayControl = (PlayOperatorView) findViewById(R.id.ac_play_music_pov);
    }

    @Override
    public void initView() {
        mToolBar.setTitle("");
        mToolBar.setBackgroundColor(getResources().getColor(R.color.transparent));
    }

    private void seekTo(int currentProgress) {
        mPlayControlCommand.seekProgressTo(currentProgress);
        if (!mPlayControlView.ismIsPlay()) {
            mPlayControlView.setIsPlay(true);
        }
    }
    @Override
    public void initListener() {
        mPlayControlView.setOnControlItemClickListener(new MusicPlayControlView.MusicPlayerControlViewItemClickListener() {
            @Override
            public void onNextItemClick() {
                loadNewMusic();
                mPlayControlCommand.changeToNextMusic();
            }

            @Override
            public void onPreItemClick() {
                loadNewMusic();
                mPlayControlCommand.changeToPreMusic();
            }

            @Override
            public void onStartOrStartItemClick(boolean isPlay) {
                if (isPlay) {
                    mPlayControlCommand.play();
                } else {
                    mPlayControlCommand.pausePlay();
                }
            }
        });

        mSeekBar.setMediaSeekBarListener(new MediaSeekBar.MediaSeekBarListener() {
            @Override
            public void onStartDragThumb(int currentProgress) {
                mPlayControlView.setIsPlay(false);
            }

            @Override
            public void onDraggingThumb(int currentProgress) {
            }

            @Override
            public void onStopDragThumb(int currentProgress) {
                seekTo(currentProgress);
            }

            @Override
            public void onProgressChange(int currentProgress) {
                seekTo(currentProgress);
            }
        });

        mPovMusicPlayControl.setItemClickListener(new PlayOperatorView.OnItemClickListener() {
            @Override
            public void onCirclePlayItemClick(int mode) {
                if (mode == MusicServiceContract.PlayQueueControlPresenter.CIRCLE_MODE) {
                    mPlayModeCommand.startCirclePlayMode();
                } else {
                    mPlayModeCommand.startQueuePlayMode();
                }
            }

            @Override
            public void onRandomPlayItemClick(int mode) {
                if (mode == MusicServiceContract.PlayQueueControlPresenter.RANDOM_MODE) {
                    mPlayModeCommand.startRandomPlayMode();
                } else {
                    mPlayModeCommand.startQueuePlayMode();
                }
            }

            @Override
            public void onLikeItemClick(boolean like) {
                final SimpleSong simpleSong = mSong.translateToSimpleSong();
                simpleSong.favorite = !mIsLove;
                if (mIsLove) {
                    APIHelper.subscribeSimpleRequest(mDbOperator.add(simpleSong), new CommonObserver<Boolean>() {
                        @Override
                        public void onNext(Boolean flag) {
                            ToastUtils.showShort(flag ? "已经从喜欢列表移除" : "从喜欢列表移除失败");
                            mIsLove = flag ? false : true;
                            mPovMusicPlayControl.refreshLikeStatus(mIsLove);
                            if (!mIsLove) {
                                EventBus.getDefault().post(new SongDeleteFromLikeEvent(simpleSong));
                            }
                        }
                    });
                } else {
                    APIHelper.subscribeSimpleRequest(mDbOperator.add(simpleSong), new CommonObserver<Boolean>() {
                        @Override
                        public void onNext(Boolean flag) {
                            ToastUtils.showShort(flag ? "已喜欢" : "喜欢失败");
                            mIsLove = flag ? true : false;
                            mPovMusicPlayControl.refreshLikeStatus(mIsLove);
                        }
                    });
                }
            }

            @Override
            public void onMusicListClick() {
                mDialog = new MusicPlayQueueWindow(PlayMusicActivity.this);
                mDialog.show();
                mPlayQueueCommand.getPlayQueue();
                mDialog.startLoadingAnimation();
            }
        });
    }

    @Override
    public void initData() {
        initLike();
        changeMusic();
    }

    private void changeMusic() {
        if (mIsFromLittlePanel) {
            mPlayControlCommand.getCurrentPlayMusic();
            return;
        }
        mPlayQueueCommand.changeMusic(mSong);
    }

    private void initLike() {
        SimpleSong simpleSong = mSong.translateToSimpleSong();
        APIHelper.subscribeSimpleRequest(mDbOperator.query(simpleSong.getId()), new CommonObserver<SimpleSong>() {
            @Override
            public void onNext(SimpleSong song) {
                mIsLove = song != null ? song.isFavorite() : false;
                mPovMusicPlayControl.refreshLikeStatus(mIsLove);
            }
        });
    }

    public static void startFromLittlePanel(Activity activity, Song song) {
        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(activity, true);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pairs);
        Intent intent = new Intent();
        intent.putExtra(TO_PLAY_MUSIC_INFO, song);
        intent.putExtra(FROM_LITTLE_PANEL, true);
        intent.putExtra(NEED_LOAD, false);
        intent.setClass(activity, PlayMusicActivity.class);
        activity.startActivity(intent, transitionActivityOptions.toBundle());
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public void refreshSong(Song song, boolean playStatus) {
        mSong = song;
        if (!mIsFromLittlePanel) {
            loadNewMusic();
        }

        if (mSong.hasDown) {
            mSdvAlbum.setImageBitmap(AlbumUtils.parseAlbum(mSong.audio));
        } else {
            if (mSong.album.picUrl != null) {
                mSdvAlbum.setImageURI(mSong.album.picUrl);
            }
        }
        mTvMusicName.setText(mSong.name);
        mPlayModeCommand.queryCurrentPlayMode();
        mPlayControlView.setIsPlay(playStatus);
    }

    @Override
    public void updateBufferedProgress(int percent) {
        mSeekBar.setHasBufferProgress((int) (percent * 1.0f / 100 * mSeekBar.getMaxProgress()));
    }

    @Override
    public void updatePlayProgress(int curPos, int duration, int left) {
        if (!mIsLoading) {
            mSeekBar.setMaxProgress(left);
            mSeekBar.setCurrentProgress(curPos);
            mTvPlayedTime.setText(TimeUtils.getDurationString(curPos, false));
            mTvLeftTime.setText(TimeUtils.getDurationString(left, true));
        }
    }

    @Override
    public void preparedPlay(int duration) {
        if (mIsLoading) {
            mPlayControlView.endLoadingAnimationAndPlay();
            mPlayControlView.setIsPlay(true);
            mIsLoading = false;
            mSeekBar.setCanOperator(true);
        }

        mSeekBar.setCurrentProgress(0);
        mSeekBar.setHasBufferProgress(0);
        mSeekBar.setMaxProgress(duration);
    }

    @Override
    public void completionPlay() {
        mPlayControlView.setIsPlay(false);
        mSeekBar.setCurrentProgress(mSeekBar.getMaxProgress());
    }

    @Override
    public void setPlayDuration(int duration) {
        mSeekBar.setMaxProgress(duration);
    }

    @Override
    public void refreshPlayMode(int playMode) {
        mPovMusicPlayControl.refreshPlayMode(playMode);
    }

    @Override
    public void updatePlayProgressForSetMax(int curPos, int left) {
        if (!mIsLoading) {
            mSeekBar.setMaxProgress(left);
            mSeekBar.setCurrentProgress(curPos);
            mTvPlayedTime.setText(TimeUtils.getDurationString(curPos, false));
            mTvLeftTime.setText(TimeUtils.getDurationString(left, true));
        }
    }

    @Override
    public void loadNewMusic() {
        mPlayControlCommand.pausePlay();
        mTvPlayedTime.setText("00:00");
        mTvLeftTime.setText("--:--");
        mSeekBar.setCurrentProgress(0);
        mIsLoading = true;
        mSeekBar.setCanOperator(false);
        mPlayControlView.startLoadingAnimation();
    }

    @Override
    public void setPlayQueue(List<Song> playQueue) {
        if (mDialog != null) {
            mDialog.addMusicQueue(playQueue);
            mDialog.stopLoadingAnimation();
        }
    }

    @Override
    public void showNoMoreMusic() {
        ToastUtils.showShort("播放列表没有音乐了哦");
        mIsLoading = false;
        mSeekBar.setCanOperator(true);
        mPlayControlView.endLoadingAnimationAndPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mClientReceiver.releaseResource();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SongDeleteFromPlayQueueEvent event) {
        Song song = event.song;
        mPlayQueueCommand.removeSongFromQueue(song);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeSongEvent event) {
        mSong = event.song;
        mIsFromLittlePanel = false;
        changeMusic();
    }
}
