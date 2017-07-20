package com.georgeren.myboring.read.mvp.contract;

import com.georgeren.myboring.base.view.IView;

import java.util.List;

/**
 * Created by georgeRen on 2017/7/20.
 */

public interface ZhiHuEssayContract {
    interface View extends IView {
        void showEssayContent(String contentHtml);

        void setHeaderImage(String url);

        void refreshLikeStatus(boolean status);
    }

    interface Presenter {
        void loadEssay(String essayId);

        void loadPreEssay();

        void loadNextEssay();

        void likeEssay();
    }

    interface EssayQueue {
        String getPreEssayId();

        String getNextEssayId();

        void loadEssayQueue(List<String> ids);
    }

}
