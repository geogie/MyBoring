package com.georgeren.myboring.db.operate;

import android.content.Context;
import android.util.Log;

import com.georgeren.myboring.db.DbBaseOperate;
import com.georgeren.myboring.music.mvp.model.SimpleSong;
import com.georgeren.myboring.music.mvp.model.Song;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by georgeRen on 2017/7/17.
 */

public class MusicDbOperator extends DbBaseOperate<SimpleSong> implements DataBaseOperateContract.MusicOperator {
    private static final String TAG = "MusicDbOperator";
    public MusicDbOperator(LiteOrm mLiteOrm, Context mContext, Class c) {
        super(mLiteOrm, mContext, c);
    }
    @Override
    public Observable<List<SimpleSong>> getLikeMusic() {
        return Observable.create(new Observable.OnSubscribe<List<SimpleSong>>() {
            @Override
            public void call(Subscriber<? super List<SimpleSong>> subscriber) {
                Log.d("MusicDbOperator", "getLikeMusic -> call method");
                List<SimpleSong> likes = mLiteOrm.query(new QueryBuilder<>(SimpleSong.class).whereEquals("favorite", true));
                subscriber.onNext(likes);
            }
        });
    }

    @Override
    public Observable<Long> getLikeMusicCount() {
        return Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {
                long count = mLiteOrm.queryCount(new QueryBuilder(SimpleSong.class).whereEquals("favorite", true));
                subscriber.onNext(count);
            }
        });
    }

    @Override
    public Observable<List<SimpleSong>> getLocalMusic() {
        return Observable.create(new Observable.OnSubscribe<List<SimpleSong>>() {
            @Override
            public void call(Subscriber<? super List<SimpleSong>> subscriber) {
                List<SimpleSong> locals = mLiteOrm.query(new QueryBuilder<>(SimpleSong.class).whereEquals("hasDown", true));
                subscriber.onNext(locals);
            }
        });
    }

    @Override
    public Observable<Long> getLocalMusicCount() {
        return Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {
                long count = mLiteOrm.queryCount(new QueryBuilder(SimpleSong.class).whereEquals("hasDown", true));
                subscriber.onNext(count);
            }
        });
    }

    public Observable<List<Song>> getInitPlayQueue() {
        return Observable.create(new Observable.OnSubscribe<List<Song>>() {
            @Override
            public void call(final Subscriber<? super List<Song>> subscriber) {
                getLocalMusic()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<SimpleSong>>() {
                            @Override
                            public void onCompleted() {
                                Log.d(TAG,"onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG,"onError");
                            }

                            @Override
                            public void onNext(List<SimpleSong> songs) {
                                subscriber.onNext(translateSimpleSong2Song(songs));
                            }
                        });
            }
        });
    }

    private List<Song> translateSimpleSong2Song(List<SimpleSong> songs) {
        List<Song> news = new ArrayList<>();
        for (SimpleSong s : songs) {
            news.add(s.translateToSong());
        }
        return news;
    }
}
