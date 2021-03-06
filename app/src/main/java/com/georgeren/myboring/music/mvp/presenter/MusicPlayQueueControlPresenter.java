package com.georgeren.myboring.music.mvp.presenter;

import com.georgeren.myboring.base.entity.ModelTranslateContract;
import com.georgeren.myboring.http.APIHelper;
import com.georgeren.myboring.http.CommonObserver;
import com.georgeren.myboring.music.mvp.contract.MusicServiceContract;
import com.georgeren.myboring.music.mvp.model.PlayList;
import com.georgeren.myboring.music.mvp.model.Song;
import com.georgeren.myboring.utils.ULog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by georgeRen on 2017/7/18.
 */

public class MusicPlayQueueControlPresenter implements MusicServiceContract.PlayQueueControlPresenter {
    private static final String TAG = "MusicPlayQueueControlPresenter";

    private ModelTranslateContract.MusicModeTranslate musicModeTranslate;
    private List<Song> mQueue;
    private Set<String> mUniqueIds;
    private int mPlayMode;
    private int mCurrentIndex = 0;
    private final Random mRandom;

    public MusicPlayQueueControlPresenter(List<Song> initQueue) {

        if (mQueue == null) {
            mQueue = new ArrayList<>();
            mUniqueIds = new HashSet<>();
        }

        if (initQueue != null) {
            for (Song song : initQueue) {
//                ULog.d(TAG, "MusicPlayQueueControlPresenter", "添加音乐到队列");
                addMusicToQueue(0, song);
            }
        }

        mRandom = new Random();
        mCurrentIndex = 0;
//        ULog.d(TAG, "MusicPlayQueueControlPresenter", "mCurrentIndex:" + mCurrentIndex);
        mPlayMode = QUEUE_MODE;
        musicModeTranslate = new MusicModelTranslatePresenter();
    }

    private boolean addMusicToQueue(int index, Song song) {
        if (!mUniqueIds.contains(song.id)) {
            mCurrentIndex = index;
//            ULog.d(TAG, "addMusicToQueue", "添加音乐到队列mCurrentIndex：" + mCurrentIndex);
            mQueue.add(index, song);
            mUniqueIds.add(song.id);
            return true;
        } else {// 队列中已经存在了，但是开始播放这个旧音乐了，下标要改变啊
            changeCurrentIndex(song);
        }
//        ULog.d(TAG, "addMusicToQueue", "添加音乐到队列失败");
        return false;
    }

    private void changeCurrentIndex(Song song) {
        for (int i = 0; i < mQueue.size(); i++) {
            if (mQueue.get(i).id.contains(song.id)) {
                mCurrentIndex = i;
//                ULog.d(TAG, "addMusicToQueue", "mCurrentIndex:" + mCurrentIndex);
                break;
            }
        }
    }

    @Override
    public boolean addToPlayQueue(Song song) {
//        ULog.d(TAG, "addToPlayQueue", "添加音乐到队列");
        return addMusicToQueue(0, song);
    }

    @Override
    public boolean randomPlayQueueMusic() {
        mPlayMode = RANDOM_MODE;
        return false;
    }

    @Override
    public boolean addToNextPlay(Song song) {
        if (mQueue.isEmpty()) {
//            ULog.d(TAG,"addToNextPlay","添加音乐到队列");
            addMusicToQueue(0, song);
            return true;
        }

        if (mCurrentIndex + 1 >= mQueue.size()) {
//            ULog.d(TAG,"addToNextPlay2","添加音乐到队列");
            addMusicToQueue(mQueue.size(), song);
        } else {
//            ULog.d(TAG,"addToNextPlay3","添加音乐到队列");
            addMusicToQueue(mCurrentIndex + 1, song);
        }

        return true;
    }

    @Override
    public Song getNextPlayMusic() {
        if (mQueue.isEmpty() || mCurrentIndex >= mQueue.size()) {
            return null;
        }
        if (mPlayMode == QUEUE_MODE || mPlayMode == PLAY_LIST_CIRCLE_MODE) {// 队列，循环
            if (mCurrentIndex < mQueue.size() - 1) {
                ++mCurrentIndex;
//                ULog.d(TAG, "getNextPlayMusic", "mCurrentIndex:" + mCurrentIndex);
            } else {
                mCurrentIndex = 0;
//                ULog.d(TAG, "getNextPlayMusic", "1mCurrentIndex:0");
            }
        }

        if (mPlayMode == RANDOM_MODE) {// 随机
            mCurrentIndex = mRandom.nextInt(mQueue.size());
//            ULog.d(TAG, "getNextPlayMusic", "2mCurrentIndex:" + mCurrentIndex);
        }

        return mQueue.get(mCurrentIndex);
    }

    @Override
    public Song getPrePlayMusic() {
        if (mQueue.isEmpty() || mCurrentIndex < 0) {
//            return mQueue.get(mCurrentIndex);
            return null; // 侠客
        }

        if (mPlayMode == QUEUE_MODE || mPlayMode == PLAY_LIST_CIRCLE_MODE) {
            if (mCurrentIndex > 0) {
                mCurrentIndex--;
//                ULog.d(TAG, "getPrePlayMusic", "mCurrentIndex:" + mCurrentIndex);
            } else {
                mCurrentIndex = mQueue.size() - 1;
//                ULog.d(TAG, "getPrePlayMusic", "1mCurrentIndex:" + mCurrentIndex);
            }
        }

        if (mPlayMode == RANDOM_MODE) {
            mCurrentIndex = mRandom.nextInt(mQueue.size());
//            ULog.d(TAG, "getPrePlayMusic", "2mCurrentIndex:" + mCurrentIndex);
        }

        return mQueue.get(mCurrentIndex);
    }

    @Override
    public void clearPlayQueue() {
        mQueue.clear();
    }

    @Override
    public void setPlayMode(int mode) {
        if (mode == CIRCLE_MODE && mPlayMode == CIRCLE_MODE) {
            mPlayMode = QUEUE_MODE;
            return;
        }

        if (mode == RANDOM_MODE && mPlayMode == RANDOM_MODE) {
            mPlayMode = QUEUE_MODE;
            return;
        }

        this.mPlayMode = mode;
    }


    public int getPlayMode() {
        return mPlayMode;
    }

    @Override
    public Observable<Boolean> reLoadPlayQueue(final PlayList playList) {
        mQueue.clear();
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(final Subscriber<? super Boolean> subscriber) {
                APIHelper.subscribeSimpleRequest(musicModeTranslate.getSongFromPlayList(playList),
                        new CommonObserver<List<Song>>() {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                subscriber.onNext(false);
                            }

                            @Override
                            public void onNext(List<Song> songs) {
//                                ULog.d(TAG, "reLoadPlayQueue", "添加音乐到队列");
                                mQueue.addAll(songs);
                                addmUniqueIds(songs);
                                subscriber.onNext(true);
                            }
                        });
            }
        });
    }

    /**
     * 把播放队列也标记到唯一队列中，防止重复
     * @param songs
     */
    private void addmUniqueIds(List<Song> songs){
        for (int i = 0; i < songs.size(); i++) {
            if (!mUniqueIds.contains(songs.get(i).id)){
                mUniqueIds.add(songs.get(i).id);
            }
        }
    }

    @Override
    public List<Song> getPlayQueue() {
        return mQueue;
    }

    @Override
    public boolean removeSong(Song song) {

        int songIndex = -1;

        for (int i = 0; i < mQueue.size(); i++) {
            if (song.id.equals(mQueue.get(i).id)) {
                songIndex = i;
            }
        }

        if (songIndex > 0) {
            mQueue.remove(songIndex);
            return true;
        }

        return false;
    }

    @Override
    public void markCurrentPlayMusic(Song markSong) {
        if (mQueue == null || mQueue.isEmpty()) {
            return;
        }
        boolean haveSong = false;
        for (Song song : mQueue) {
            song.isPlaying = false;
            if (song.id.equals(markSong.id)) {
                song.isPlaying = true;
                haveSong = true;
            }
        }

        if (!haveSong) {
            markSong.isPlaying = true;
//            ULog.d(TAG, "markCurrentPlayMusic", "添加音乐到队列");
            mQueue.add(0, markSong);
        }
    }

}
