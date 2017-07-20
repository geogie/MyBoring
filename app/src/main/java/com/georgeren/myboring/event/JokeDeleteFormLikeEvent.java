package com.georgeren.myboring.event;

import com.georgeren.myboring.read.mvp.entity.Joke;

/**
 * Created by georgeRen on 2017/7/19.
 */

public class JokeDeleteFormLikeEvent {
    public Joke joke;

    public JokeDeleteFormLikeEvent(Joke data) {
        joke = data;
    }
}
