package com.georgeren.myboring.music.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.georgeren.myboring.R;
import com.georgeren.myboring.base.adapter.BaseRVAdapter;
import com.georgeren.myboring.base.ui.ItemHandler;
import com.georgeren.myboring.base.ui.ItemHandlerFactory;
import com.georgeren.myboring.event.ChangeSongEvent;
import com.georgeren.myboring.event.SongDeleteFromPlayQueueEvent;
import com.georgeren.myboring.music.itemhandler.DialogMusicIH;
import com.georgeren.myboring.music.mvp.model.Song;
import com.georgeren.myboring.utils.RVUtils;
import com.georgeren.myboring.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by georgeRen on 2017/7/18.
 *
 * 这里使用的继承, 改成装饰也是不错的选择
 * 上弹显示音乐队列
 */

public class MusicPlayQueueWindow extends Dialog {
    private RecyclerView mRV;
    private List<Object> mDialogData = new ArrayList<>();
    private ImageView mIvLoading;
    private Context mContext;

    public MusicPlayQueueWindow(Context context) {
        super(context);
        mContext = context;
        initView();
    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_play_control);
        mRV = (RecyclerView) findViewById(R.id.list_view);
        mIvLoading = (ImageView) findViewById(R.id.loading);

        Window dialogWindow = getWindow();
        dialogWindow.setBackgroundDrawableResource(R.drawable.bg_dialog); // 想要完全匹配父窗口, 必须设置背景
        dialogWindow.setGravity(Gravity.BOTTOM);
        this.setCancelable(true);
        setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.height = UIUtils.getScreenHeight() * 4 / 7;
        p.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(p);
        dialogWindow.setWindowAnimations(R.style.dialog_animation_frombottom);

        initRv();
    }

    private void initRv() {
        mRV.setLayoutManager(RVUtils.getLayoutManager(mContext, LinearLayoutManager.VERTICAL));
        mRV.addItemDecoration(new RVUtils.NoLastDividerDecoration(getContext(), R.color.divider, 1, new Rect(UIUtils.dp2Px(15), 0, 0, 0)));
        mRV.setAdapter(new BaseRVAdapter((Activity) mContext, mDialogData) {
            final int TYPE_MUSIC = 1;
            @Override
            protected void initHandlers() {
                registerItemHandler(TYPE_MUSIC, new ItemHandlerFactory() {
                    @Override
                    public ItemHandler newInstant(int viewType) {
                        return new DialogMusicIH();
                    }
                });

            }

            @Override
            protected int getViewType(int position) {
                Object o = mData.get(position);
                if (o instanceof Song) {
                    return TYPE_MUSIC;
                }
                return 0;
            }
        });
    }



    public void addMusicQueue(List<Song> songs) {
        mDialogData.addAll(songs);
        mRV.getAdapter().notifyDataSetChanged();
    }


    public void startLoadingAnimation() {
        mIvLoading.setVisibility(View.VISIBLE);
        mRV.setVisibility(View.INVISIBLE);
        UIUtils.startSimpleRotateAnimation(mIvLoading);
    }

    public void stopLoadingAnimation() {
        mIvLoading.setVisibility(View.INVISIBLE);
        mRV.setVisibility(View.VISIBLE);
        mIvLoading.clearAnimation();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SongDeleteFromPlayQueueEvent event) {
        mDialogData.remove(event.song);
        mRV.getAdapter().notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeSongEvent event) {
        dismiss();
        Song song = event.song;
        song.isPlaying = true;
        mRV.getAdapter().notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}
