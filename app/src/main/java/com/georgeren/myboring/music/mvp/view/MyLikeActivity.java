package com.georgeren.myboring.music.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.georgeren.myboring.R;
import com.georgeren.myboring.base.adapter.ViewHolder;
import com.georgeren.myboring.base.ui.BaseActivity;
import com.georgeren.myboring.base.ui.ItemHandler;
import com.georgeren.myboring.base.ui.ListFragment;
import com.georgeren.myboring.db.DbBaseOperate;
import com.georgeren.myboring.db.DbManager;
import com.georgeren.myboring.db.operate.MusicDbOperator;
import com.georgeren.myboring.event.AddToNextPlayEvent;
import com.georgeren.myboring.event.PlayListDeleteFromLikeEvent;
import com.georgeren.myboring.event.SongDeleteFromLikeEvent;
import com.georgeren.myboring.http.APIHelper;
import com.georgeren.myboring.http.CommonObserver;
import com.georgeren.myboring.music.itemhandler.LocalMusicIH;
import com.georgeren.myboring.music.itemhandler.SimpleMusicIH;
import com.georgeren.myboring.music.mvp.contract.MediaPlayerContract;
import com.georgeren.myboring.music.mvp.model.PlayList;
import com.georgeren.myboring.music.mvp.model.SimpleSong;
import com.georgeren.myboring.music.service.action.ClientPlayQueueControlCommand;
import com.georgeren.myboring.utils.RVUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MyLikeActivity extends BaseActivity {

    private ViewPager mViewPager;
    private List<ListFragment> mFragments = new ArrayList<>();
    private TabLayout mTabLayout;
    private MediaPlayerContract.ClientPlayQueueControlCommand mPlayQueueCommand;

    public static void start(Context context) {
        Intent intent = new Intent(context, MyLikeActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void initParamsAndPresenter() {
        super.initParamsAndPresenter();
        EventBus.getDefault().register(this);
        mPlayQueueCommand = new ClientPlayQueueControlCommand(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_like;
    }

    @Override
    public void findView() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
    }

    @Override
    public void initView() {
        mToolBar.setTitle("我的喜欢");
        initMyFragment();

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mFragments.get(position).getTitle();
            }
        });
        mTabLayout.setupWithViewPager(mViewPager);
    }
    private void initMyFragment() {
        mFragments.add(new LocalMusicFragment());
        mFragments.add(new PlayListFragment());
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    public static class LocalMusicFragment extends ListFragment<SimpleSong> {
        private MusicDbOperator mDbOperator;

        @Override
        protected void loadData() {
            mDbOperator = new MusicDbOperator(DbManager.getLiteOrm(), getContext(), SimpleSong.class);
            APIHelper.subscribeSimpleRequest(mDbOperator.getLikeMusic(), new CommonObserver<List<SimpleSong>>() {
                @Override
                public void onNext(List<SimpleSong> simpleSongs) {
                    if (simpleSongs != null) {
                        addData(simpleSongs);
                    }
                }
            });
        }

        @Override
        protected ItemHandler getSingletonItemHandler() {
            return new LocalMusicIH(true);
        }

        @Override
        protected RecyclerView.LayoutManager getLayoutManager() {
            return RVUtils.getLayoutManager(getContext(), LinearLayoutManager.VERTICAL);
        }

        @Override
        protected RecyclerView.ItemDecoration getItemDecoration() {
            return null;
        }

        @Override
        public String getTitle() {
            return "音乐";
        }

        @Override
        protected void findView() {
            EventBus.getDefault().register(this);
        }

        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onMessageEvent(SongDeleteFromLikeEvent event) {
            List<SimpleSong> pics = mData;
            int i = 0;
            for (; i < pics.size(); i++) {
                if (pics.get(i).id.equals(event.song.id)) {
                    break;
                }
            }
            mData.remove(i);
            mRv.getAdapter().notifyDataSetChanged();
        }


        @Override
        public void onDestroy() {
            super.onDestroy();
            EventBus.getDefault().unregister(this);
        }
    }

    public static class PlayListFragment extends ListFragment<PlayList> {
        private DbBaseOperate<PlayList> mDbOperator;

        @Override
        protected void loadData() {
            mDbOperator = new DbBaseOperate<>(DbManager.getLiteOrm(), getContext(), PlayList.class);
            APIHelper.subscribeSimpleRequest(mDbOperator.getAll(), new CommonObserver<List<PlayList>>() {
                @Override
                public void onNext(List<PlayList> playLists) {
                    if (playLists != null) {
                        addData(playLists);
                    }
                }
            });
        }

        @Override
        protected ItemHandler getSingletonItemHandler() {
            return new SimpleMusicIH<PlayList>(false) {
                @Override
                protected void onAddToNextPlayClick() {

                }

                @Override
                protected void onItemClick() {
                    PlayListActivity.start(getContext(), mData);
                }

                @Override
                protected void bindData(ViewHolder vh, PlayList data, int position) {
                    vh.getTextView(R.id.item_local_music_tv_music_name).setText(data.getName().trim());
                    vh.getTextView(R.id.item_local_music_tv_artist_album).setText(data.getPlayCount() + "");
                    vh.getTextView(R.id.item_local_music_tv_duration).setVisibility(View.GONE);
                    mSdvAlbum.setImageURI(data.getCoverImgUrl());
                    mIvNextPlay.setVisibility(View.INVISIBLE);
                }
            };
        }

        @Override
        protected RecyclerView.LayoutManager getLayoutManager() {
            return RVUtils.getLayoutManager(getContext(), LinearLayoutManager.VERTICAL);
        }

        @Override
        protected RecyclerView.ItemDecoration getItemDecoration() {
            return null;
        }

        @Override
        public String getTitle() {
            return "专辑";
        }

        @Override
        protected void findView() {
            EventBus.getDefault().register(this);
        }

        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onMessageEvent(PlayListDeleteFromLikeEvent event) {
            List<PlayList> pics = mData;
            int i = 0;
            for (; i < pics.size(); i++) {
                if (pics.get(i).id.equals(event.playList.id)) {
                    break;
                }
            }
            mData.remove(i);
            mRv.getAdapter().notifyDataSetChanged();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AddToNextPlayEvent event) {
        mPlayQueueCommand.addMusicToNextPlay(event.song);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
