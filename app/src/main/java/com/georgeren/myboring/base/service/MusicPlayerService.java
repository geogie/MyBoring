package com.georgeren.myboring.base.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.georgeren.myboring.db.DbManager;
import com.georgeren.myboring.db.operate.MusicDbOperator;
import com.georgeren.myboring.http.APIHelper;
import com.georgeren.myboring.http.CommonObserver;
import com.georgeren.myboring.music.mvp.contract.MediaPlayerContract;
import com.georgeren.myboring.music.mvp.contract.MusicServiceContract;
import com.georgeren.myboring.music.mvp.model.PlayList;
import com.georgeren.myboring.music.mvp.model.PlayListSong;
import com.georgeren.myboring.music.mvp.model.SimpleSong;
import com.georgeren.myboring.music.mvp.model.Song;
import com.georgeren.myboring.music.mvp.presenter.MusicModelTranslatePresenter;
import com.georgeren.myboring.music.mvp.presenter.MusicPlayQueueControlPresenter;
import com.georgeren.myboring.music.mvp.presenter.PlayMusicPresenter;
import com.georgeren.myboring.music.mvp.presenter.ServiceReceiverPresenter;
import com.georgeren.myboring.music.service.MusicServiceInstruction;
import com.georgeren.myboring.utils.SPUtils;
import com.georgeren.myboring.utils.ToastUtils;
import com.georgeren.myboring.utils.ULog;

import java.io.Serializable;
import java.util.List;

/**
 * Created by georgeRen on 2017/7/13.
 * 音乐播放服务, 本身并不是一个服务
 * 主要管理音乐的播放控制, 音乐队列, 具体控制委托给 PlayMusicControlPresenter、PlayQueueControlPresenter
 * 通知client有关音乐UI的改变
 * <p/>
 * 接下来的refactor: 把通知客户端UI改变的Action也抽取到一个单独的类中
 */

public class MusicPlayerService implements MediaPlayerContract.MediaPlayerRefreshView, MusicServiceContract.Service, BaseServiceContract {
    private static final String TAG = "MusicPlayerService";
    private static final String PLAY_NOT_IS_PLAY_LIST = "PLAY_NOT_IS_PLAY_LIST";
    private MediaPlayerContract.PlayMusicControlPresenter mMusicPlayControlPresenter;
    private MusicDbOperator mDbOperator;
    private BaseServiceContract.ReceiverPresenter mReceiverPresenter;
    private MusicServiceContract.PlayQueueControlPresenter mPlayQueuePresenter;
    private boolean mQueueIsPrepare;

    private Song mSong;    //current play music
    private String mHasLoadPlayListId = PLAY_NOT_IS_PLAY_LIST;
    private boolean mAutoPlay;
    private Service mServiceParent;

    public MusicPlayerService(Service mServiceParent) {
        this.mServiceParent = mServiceParent;
    }

    @Override
    public void initService() {
        mQueueIsPrepare = false;
        mDbOperator = new MusicDbOperator(DbManager.getLiteOrm(), getContext(), SimpleSong.class);
        mMusicPlayControlPresenter = new PlayMusicPresenter(this, mServiceParent);
        mReceiverPresenter = new ServiceReceiverPresenter(this);

        APIHelper.subscribeSimpleRequest(mDbOperator.getInitPlayQueue(), new CommonObserver<List<Song>>() {
            @Override
            public void onNext(List<Song> songs) {
                mQueueIsPrepare = true;
                mPlayQueuePresenter = new MusicPlayQueueControlPresenter(songs);
            }
        });
    }

    @Override
    public void onTaskMoved() {
        clear();
    }

    @Override
    public void onDestroy() {
        clear();
    }

    @Override
    public void updateBufferedProgress(int percent) {
        Intent intent = new Intent(MusicServiceInstruction.CLIENT_RECEIVER_UPDATE_BUFFERED_PROGRESS);
        intent.putExtra(MusicServiceInstruction.CLIENT_PARAM_BUFFERED_PROGRESS, percent);
        LocalBroadcastManager.getInstance(mServiceParent).sendBroadcast(intent);
    }

    @Override
    public void updatePlayProgress(int curPos, int duration, int max) {
        Intent intent = new Intent(MusicServiceInstruction.CLIENT_RECEIVER_UPDATE_PLAY_PROGRESS);
        intent.putExtra(MusicServiceInstruction.CLIENT_PARAM_PLAY_PROGRESS_CUR_POS, curPos);
        intent.putExtra(MusicServiceInstruction.CLIENT_PARAM_PLAY_PROGRESS_DURATION, duration);
        intent.putExtra(MusicServiceInstruction.CLIENT_PARAM_PLAY_PROGRESS_MAX_DURATION, mMusicPlayControlPresenter.getDuration());
        LocalBroadcastManager.getInstance(mServiceParent).sendBroadcast(intent);
    }

    @Override
    public void preparedPlay(int duration) {
        mPlayQueuePresenter.markCurrentPlayMusic(mSong);
        if (mAutoPlay) {
            ULog.d(TAG, "preparedPlay", "准备音乐播放自动播放-音乐播放");
            mMusicPlayControlPresenter.startPlay();
        }
        Intent intent = new Intent(MusicServiceInstruction.CLIENT_RECEIVER_PLAYER_PREPARED);
        intent.putExtra(MusicServiceInstruction.CLIENT_PARAM_PREPARED_TOTAL_DURATION, duration);
        LocalBroadcastManager.getInstance(mServiceParent).sendBroadcast(intent);
    }

    @Override
    public void completionPlay() {
        if (!playQueueIsPrepare()) return;
        mSong = mPlayQueuePresenter.getNextPlayMusic();
        changeMusic(mSong);
    }

    private boolean playQueueIsPrepare() {
        if (!mQueueIsPrepare) {
            ToastUtils.showShort("播放队列正在准备");
            return false;
        }
        return true;
    }

    @Override
    public void loadMusicInfo(Song song, boolean autoPlay) {
        mAutoPlay = autoPlay;
        if (song != null) {
            mSong = song;
            try {
                ULog.d(TAG, "loadMusicInfo", "初始化音乐");
                mMusicPlayControlPresenter.initMediaPlayer(mSong.audio);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void playMusic() {
        if (!mMusicPlayControlPresenter.isPrepared()) {
            mAutoPlay = true;
            return;
        }
        ULog.d(TAG, "playMusic", "音乐播放");
        mMusicPlayControlPresenter.startPlay();
    }

    @Override
    public void pausePlay() {
        mMusicPlayControlPresenter.pausePlay();
    }

    @Override
    public void seekTo(Intent intent) {
        mMusicPlayControlPresenter.seekTo(intent.getIntExtra(MusicServiceInstruction.SERVER_PARAM_SEEK_TO_POS, 0));
        if (!mMusicPlayControlPresenter.isPlaying() && mMusicPlayControlPresenter.isPrepared()) {
            ULog.d(TAG, "seekTo", "音乐播放");
            mMusicPlayControlPresenter.startPlay();
        }
    }

    @Override
    public void saveLastPlayMusic() {
        mMusicPlayControlPresenter.stopPlay();
        mMusicPlayControlPresenter.saveLastPlayMusic(mSong, mServiceParent);
    }

    @Override
    public void informCurrentPlayMusic() {
        if (mSong != null) {
            notifyCurrentPlayMusic(mMusicPlayControlPresenter.isPlaying());
            return;
        }
        String songId = SPUtils.getStringFromConfig(SPUtils.KEY_LAST_PLAY_MUSIC);
        APIHelper.subscribeSimpleRequest(mDbOperator.query(songId), new CommonObserver<SimpleSong>() {
            @Override
            public void onNext(SimpleSong s) {
                if (s != null) {
                    mSong = s.translateToSong();
                    notifyCurrentPlayMusic(mMusicPlayControlPresenter.isPlaying());
                }
            }
        });
    }

    @Override
    public void informCurrentIfPlayProgress() {
        if (mMusicPlayControlPresenter.isPrepared()) {
            Intent intent = new Intent(MusicServiceInstruction.CLIENT_RECEIVER_CURRENT_PLAY_PROGRESS);
            intent.putExtra(MusicServiceInstruction.CLIENT_PARAM_CURRENT_PLAY_PROGRESS, mMusicPlayControlPresenter.getCurrentProgress());
            intent.putExtra(MusicServiceInstruction.CLIENT_PARAM_MEDIA_DURATION, mMusicPlayControlPresenter.getDuration());
            LocalBroadcastManager.getInstance(mServiceParent).sendBroadcast(intent);
        }
    }

    @Override
    public void updateSong(Song song) {
        mSong = song;
    }

    @Override
    public Context getContext() {
        return mServiceParent;
    }

    @Override
    public void notifyCurrentPlayMusic(boolean isPlaying) {
        Intent intent = new Intent(MusicServiceInstruction.CLIENT_RECEIVER_CURRENT_PLAY_MUSIC);
        intent.putExtra(MusicServiceInstruction.CLIENT_PARAM_CURRENT_PLAY_MUSIC, mSong);
        intent.putExtra(MusicServiceInstruction.CLIENT_PARAM_CURRENT_PLAY_MUSIC_PLAY_STATUS, isPlaying);
        LocalBroadcastManager.getInstance(mServiceParent).sendBroadcast(intent);
    }

    @Override
    public void clear() {
        saveLastPlayMusic();
        mMusicPlayControlPresenter.releaseResource();
        mReceiverPresenter.releaseResource();
    }

    @Override
    public void playNextMusic() {
        if (!playQueueIsPrepare()) return;
        changeMusicFromQueue(mPlayQueuePresenter.getNextPlayMusic());
    }

    private void changeMusicFromQueue(Song song) {
        if (song == null) {
            notifyQueueNoMoreMusic();
            return;
        }
        changeMusic(song);
    }

    private void notifyQueueNoMoreMusic() {
        Intent intent = new Intent(MusicServiceInstruction.CLIENT_RECEIVER_QUEUE_NO_MORE_MUSIC);
        LocalBroadcastManager.getInstance(mServiceParent).sendBroadcast(intent);
    }

    @Override
    public void PlayPreMusic() {
        if (!playQueueIsPrepare()) return;
        changeMusicFromQueue(mPlayQueuePresenter.getPrePlayMusic());
    }

    @Override
    public void changeMusic(Song song) {
        if (song == null) {
            return;
        }

        mSong = song;
        if (mSong.fromPlayList) {
            APIHelper.subscribeSimpleRequest(new MusicModelTranslatePresenter().checkIfHasPlayUrl(mSong), new CommonObserver<Boolean>() {
                @Override
                public void onNext(Boolean flag) {
                    if (flag) {
                        ULog.d(TAG, "changeMusic", "初始化音乐");
                        preparePlay();
                    }
                }
            });
        } else {
            if (!mSong.hasDown) {
                APIHelper.subscribeSimpleRequest(APIHelper.getMusicServices().getSongDetail(Integer.valueOf(mSong.id)), new CommonObserver<PlayListSong>() {
                    @Override
                    public void onNext(PlayListSong songs) {
                        if (songs != null && !songs.getData().isEmpty()) {
                            mSong.audio = songs.getData().get(0).getUrl();
                            ULog.d(TAG, "changeMusic2", "初始化音乐");
                            preparePlay();
                        }
                    }
                });
            }
            ULog.d(TAG, "changeMusic", "初始化音乐3");
            preparePlay();
        }
    }

    private void preparePlay() {
        mAutoPlay = true;
        try {
            ULog.d(TAG, "preparePlay", "初始化音乐");
            mMusicPlayControlPresenter.initMediaPlayer(mSong.audio);
        } catch (Exception e) {
            e.printStackTrace();
        }
        notifyCurrentPlayMusic(true);
    }

    @Override
    public void startCircleMode() {
        mPlayQueuePresenter.setPlayMode(MusicServiceContract.PlayQueueControlPresenter.CIRCLE_MODE);
        notifyCurrentMode();
    }

    @Override
    public void startRandomMode() {
        mPlayQueuePresenter.setPlayMode(MusicServiceContract.PlayQueueControlPresenter.RANDOM_MODE);
        notifyCurrentMode();
    }

    @Override
    public void songToNextPlay(Song song) {
        if (!mQueueIsPrepare) {
            return;
        }

        if (mPlayQueuePresenter.getPlayQueue().isEmpty()) {
            changeMusic(song);
        }

        mPlayQueuePresenter.addToNextPlay(song);
    }

    @Override
    public void notifyCurrentMode() {
        int mode = mPlayQueuePresenter.getPlayMode();
        Intent intent = new Intent(MusicServiceInstruction.CLIENT_RECEIVER_REFRESH_MODE);
        intent.putExtra(MusicServiceInstruction.CLIENT_PARAM_PLAY_MODE, mode);
        LocalBroadcastManager.getInstance(mServiceParent).sendBroadcast(intent);
    }

    @Override
    public void circlePlayPlayList(final PlayList playList) {
        if (mPlayQueuePresenter != null && !playList.getId().equals(mHasLoadPlayListId)) {
            mMusicPlayControlPresenter.stopPlay();
            mQueueIsPrepare = false;
            APIHelper.subscribeSimpleRequest(mPlayQueuePresenter.reLoadPlayQueue(playList), new CommonObserver<Boolean>() {
                @Override
                public void onNext(Boolean flag) {
                    if (flag) {
                        mHasLoadPlayListId = playList.getId();
                        mQueueIsPrepare = true;
                        mPlayQueuePresenter.setPlayMode(MusicServiceContract.PlayQueueControlPresenter.PLAY_LIST_CIRCLE_MODE);
                        playNextMusic();
                    }
                }
            });
        }
    }

    @Override
    public void getPlayQueue() {
        Intent intent = new Intent(MusicServiceInstruction.CLIENT_RECEIVER_PLAY_QUEUE);
        intent.putExtra(MusicServiceInstruction.CLIENT_PARAM_PLAY_QUEUE, (Serializable) mPlayQueuePresenter.getPlayQueue());
        LocalBroadcastManager.getInstance(mServiceParent).sendBroadcast(intent);
    }

    @Override
    public void randomPlayPlayList(final PlayList playList) {
        if (mPlayQueuePresenter != null && !playList.getId().equals(mHasLoadPlayListId)) {
            mMusicPlayControlPresenter.stopPlay();
            mQueueIsPrepare = false;
            APIHelper.subscribeSimpleRequest(mPlayQueuePresenter.reLoadPlayQueue(playList), new CommonObserver<Boolean>() {
                @Override
                public void onNext(Boolean flag) {
                    if (flag) {
                        mHasLoadPlayListId = playList.getId();
                        mQueueIsPrepare = true;
                        mPlayQueuePresenter.setPlayMode(MusicServiceContract.PlayQueueControlPresenter.RANDOM_MODE);
                        playNextMusic();
                    }
                }
            });
        }
    }

    @Override
    public void removeSongFromQueue(Song song) {
        mPlayQueuePresenter.removeSong(song);
    }

    @Override
    public void startQueueMode() {
        mPlayQueuePresenter.setPlayMode(MusicServiceContract.PlayQueueControlPresenter.QUEUE_MODE);
    }

    @Override
    public void addMusicToQueue(Song song) {
        mPlayQueuePresenter.addToPlayQueue(song);
    }
}
