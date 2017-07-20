package com.georgeren.myboring.music.mvp.contract;

import android.content.Context;

import com.georgeren.myboring.base.view.IView;
import com.georgeren.myboring.music.mvp.model.PlayList;
import com.georgeren.myboring.music.mvp.model.Song;

import java.util.List;

/**
 * Created by georgeRen on 2017/7/13.
 */

public interface MediaPlayerContract {
    //for panel view
    interface BaseView extends IView {
        void refreshSong(Song song, boolean playStatus);
    }

    //media player notify view
    interface MediaPlayerRefreshView {
        void updateBufferedProgress(int percent);

        void updatePlayProgress(int curPos, int duration, int max);

        void preparedPlay(int duration);

        void completionPlay();
    }
    //Client send command interact with play service
    interface ClientPlayControlCommand {

        void changeToNextMusic();

        void changeToPreMusic();

        void getCurrentPlayMusic();

        void pausePlay();

        void play();

        void seekProgressTo(int currentProgress);
    }


    //media player
    interface Presenter {
        void initMediaPlayer(String mediaUri) throws Exception;

        boolean startPlay();

        void pausePlay();

        void stopPlay();

        void releaseResource();

        void seekTo(int pos);

        boolean isPrepared();

        int getDuration();

        boolean isPlaying();

        int getCurrentProgress();
    }

    //for music play
    interface PlayMusicControlPresenter extends Presenter {
        void saveLastPlayMusic(Song song, Context c);
    }

    //client broadcast receiver
    interface ClientReceiverPresenter {
        void registerReceiver();

        void releaseResource();

        void setBaseView(BaseView view);

        void setPlayView(PlayView view);
    }
    //play music activity
    interface PlayView extends BaseView, MediaPlayerRefreshView {
        void setPlayDuration(int duration);

        void refreshPlayMode(int playMode);

        void updatePlayProgressForSetMax(int curPos, int duration);

        void loadNewMusic();

        void setPlayQueue(List<Song> playQueue);

        void showNoMoreMusic();
    }


    interface ClientPlayModeCommand {
        void queryCurrentPlayMode();

        void startCirclePlayMode();

        void startRandomPlayMode();

        void circlePlayPlayList(PlayList mData);

        void randomPlayPlayList(PlayList mPlayList);

        void startQueuePlayMode();

    }

    interface ClientPlayQueueControlCommand {
        void getPlayQueue();

        void removeSongFromQueue(Song song);

        void changeMusic(Song song);

        void addMusicToQueue(Song song);

        void addMusicToNextPlay(Song song);
    }
}
