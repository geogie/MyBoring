package com.georgeren.myboring.base.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.georgeren.myboring.R;
import com.georgeren.myboring.base.ui.ItemHandler;
import com.georgeren.myboring.base.ui.ItemHandlerFactory;

import java.util.List;

/**
 * Created by georgeRen on 2017/7/14.
 *
 * 顶层封装适配器：数据源（？）、上下文（activity）、Item工厂模型
 *
 * 构造1：数据源、上下文、initHandlers初始化。
 *
 * 继承的方法：getItemCount、getItemViewType、onCreateViewHolder、onBindViewHolder
 * onCreateViewHolder：viewHolder(item)采用顶级父类，多态
 * onBindViewHolder：viewHolder(item)采用顶级父类，多态。把item（holder）绑定到系统的根itemView（tag）。
 *
 *
 * initHandlers（自己的抽象方法）：提供实例实现item的初始化。
 *
 * getViewType（自己的抽象方法）：提供实例实现item对应的类型。
 *
 * registerItemHandler（自己的方法）：提供给子类注册item（实例），采用工厂多态技术。
 *
 * getItemHandler（自己的方法自己用protected）：根据类型取出item（实例）
 *
 * RcvAdapterItem（内部类）：viewHolder，其实类似工厂，起到实例化（布局）作用。实例化每一种item（实例）
 * 把item给自定义的ViewHolder中进行管理
 *
 *
 */

public abstract class BaseRVAdapter extends RecyclerView.Adapter{
    protected List<?> mData;
    protected Activity mActivity;
    public SparseArray<ItemHandlerFactory> mItemHandlerHashMap = new SparseArray<>();

    public BaseRVAdapter(Activity activity, List<?> data) {
        mData = data;
        mActivity = activity;
        initHandlers();

    }

    public BaseRVAdapter(Fragment fragment, List<?> data) {
        mData = data;
        mActivity = fragment.getActivity();
        initHandlers();
    }

    public BaseRVAdapter() {
        initHandlers();
    }

    public Object getItem(int position) {
        return mData != null && mData.size() > position && position >= 0 ? mData.get(position) : null;
    }

    public void registerItemHandler(int viewType, ItemHandlerFactory itemHandlerFactory) {
        mItemHandlerHashMap.put(viewType, itemHandlerFactory);
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return getViewType(position);
    }

    protected abstract void initHandlers();
    protected abstract int getViewType(int position);


    protected ItemHandler getItemHandler(int viewType) {
        return mItemHandlerHashMap.get(viewType).newInstant(viewType);
    }

    @Override
    public RcvAdapterItem onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RcvAdapterItem(parent.getContext(), parent, getItemHandler(viewType));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ItemHandler itemHandler = ((ViewHolder) holder.itemView.getTag(R.id.item_tag_id)).itemHandler;

        if (itemHandler == null) {
            throw new RuntimeException(mData.get(position).getClass() + "  缺少ItemHandler 类,导致不能绑定数据");
        } else {
            if (mData != null) {
                itemHandler.onBindView(this, (ViewHolder) holder.itemView.getTag(R.id.item_tag_id), mData.get(position), position);
            }
        }
    }


    public static class RcvAdapterItem extends RecyclerView.ViewHolder {
        private ViewHolder vh;
        public RcvAdapterItem(Context context, ViewGroup parent, ItemHandler t) {
            super((LayoutInflater.from(context).inflate(t.getLayoutResId(), parent, false)));
            vh = ViewHolder.newInstant(itemView);
            vh.itemHandler = t;
            t.onCreateItemHandler(vh, parent);
        }
    }

}
