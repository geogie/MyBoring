package com.georgeren.myboring.music.mvp.model;

import java.util.List;

/**
 * Created by georgeRen on 2017/7/18.
 */

public class MusicSearchResult {
    public int code;
    public SearchResult result;

    public class SearchResult{
        public int songCount;
        public List<Song> songs;
    }
}
