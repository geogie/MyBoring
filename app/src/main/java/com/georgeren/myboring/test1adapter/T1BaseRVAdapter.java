package com.georgeren.myboring.test1adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.georgeren.myboring.R;

import java.util.List;

/**
 * Created by georgeRen on 2017/7/24.
 */

public abstract class T1BaseRVAdapter extends RecyclerView.Adapter{
    private static final String TAG = "T1BaseRVAdapter";
    protected List<?> mData;
    protected Activity mActivity;
    public SparseArray<T1ItemHandlerFactory> mItemHandlerHashMap = new SparseArray<>();

    public T1BaseRVAdapter(Activity activity, List<?> data) {
        mData = data;
        mActivity = activity;
        initHandlers();
    }

    /**
     * 需要子类实现：根据position获取类型
     * @param position
     * @return
     */
    protected abstract int getViewType(int position);

    /**
     * 需要子类实现：初始化几种item对应的type
     */
    protected abstract void initHandlers();

    protected T1ItemHandler getItemHandler(int viewType) {
        Log.d(TAG,"viewType:"+viewType);
        return mItemHandlerHashMap.get(viewType).newInstant(viewType);
    }
    @Override
    public int getItemViewType(int position) {
        return getViewType(position);
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public T1RcvAdapterItem onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG,"viewType:"+viewType);
        return new T1RcvAdapterItem(parent.getContext(), parent, getItemHandler(viewType));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        T1ItemHandler itemHandler = ((T1ViewHolder) holder.itemView.getTag(R.id.item_tag_id)).itemHandler;

        if (itemHandler == null) {
            throw new RuntimeException(mData.get(position).getClass() + "  缺少ItemHandler 类,导致不能绑定数据");
        } else {
            if (mData != null) {
                itemHandler.onBindView(this, (T1ViewHolder) holder.itemView.getTag(R.id.item_tag_id), mData.get(position), position);
                ((T1ViewHolder) holder.itemView.getTag(R.id.item_tag_id)).position = position;
            }
        }
    }


    public void registerItemHandler(int viewType, T1ItemHandlerFactory itemHandlerFactory) {
        mItemHandlerHashMap.put(viewType, itemHandlerFactory);
    }

    public static class T1RcvAdapterItem extends RecyclerView.ViewHolder {

        private T1ViewHolder vh;

        public T1RcvAdapterItem(Context context, ViewGroup parent, T1ItemHandler t) {
            super((LayoutInflater.from(context).inflate(t.getLayoutResId(), parent, false)));
            vh = T1ViewHolder.newInstant(itemView);
            vh.itemHandler = t;
            t.onCreateItemHandler(vh, parent);
        }
    }

}
