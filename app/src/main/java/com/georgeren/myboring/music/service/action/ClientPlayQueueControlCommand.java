package com.georgeren.myboring.music.service.action;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.georgeren.myboring.music.mvp.contract.MediaPlayerContract;
import com.georgeren.myboring.music.mvp.model.Song;
import com.georgeren.myboring.music.service.MusicServiceInstruction;

/**
 * Created by georgeRen on 2017/7/17.
 */

public class ClientPlayQueueControlCommand implements MediaPlayerContract.ClientPlayQueueControlCommand{
    private Context mContext;

    public ClientPlayQueueControlCommand(Context c) {
        mContext = c;
    }

    @Override
    public void getPlayQueue() {
        Intent intent = new Intent(MusicServiceInstruction.SERVER_RECEIVER_GET_PLAY_QUEUE);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    @Override
    public void removeSongFromQueue(Song song) {
        Intent intent = new Intent(MusicServiceInstruction.SERVER_RECEIVER_REMOVE_SONG_FROM_QUEUE);
        intent.putExtra(MusicServiceInstruction.SERVER_PARAM_REMOVE_SONG, song);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    @Override
    public void changeMusic(Song song) {
        Intent intent = new Intent(MusicServiceInstruction.SERVER_RECEIVER_CHANGE_MUSIC);
        intent.putExtra(MusicServiceInstruction.SERVER_PARAM_CHANGE_MUSIC, song);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    @Override
    public void addMusicToQueue(Song song) {
        Intent intent = new Intent(MusicServiceInstruction.SERVER_RECEIVER_ADD_MUSIC_TO_QUEUE);
        intent.putExtra(MusicServiceInstruction.SERVER_PARAM_QUEUE_ADD_SONG, song);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    @Override
    public void addMusicToNextPlay(Song song) {
        Intent intent = new Intent(MusicServiceInstruction.SERVER_RECEIVER_SONG_TO_NEXT_PLAY);
        intent.putExtra(MusicServiceInstruction.SERVER_PARAM_SONG_TO_NEXT_PLAY, song);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }
}
