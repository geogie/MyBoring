package com.georgeren.myboring.base.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.georgeren.myboring.R;
import com.georgeren.myboring.base.ui.ItemHandler;

/**
 * Created by georgeRen on 2017/7/14.
 *
 * ViewHolder封装：类似于所有ViewHolder的管理，只在BaseRVAdapter（顶层adapter）中初始化。成员变量
 * viewHolder：存储holder的view（textView、imageView等）方便取（根据id），复用
 * view：item布局的根view
 * itemHandler：item的顶级父类，持有了这个成员（item管理员）
 *
 * newInstant：实例化，holder的复用
 *
 * ViewHolder（私有构造）：
 *
 * getTextView（普通方法）：获取textView来设置内容
 *
 * get（普通方法）：findview用
 *
 *
 */

public class ViewHolder {
    private SparseArray<View> viewHolder;
    private View view;
    public ItemHandler itemHandler;

    public static ViewHolder newInstant(View view) {
        ViewHolder viewHolder = (ViewHolder) view.getTag(R.id.item_tag_id);
        if (viewHolder == null) {
            viewHolder = new ViewHolder(view);
            view.setTag(R.id.item_tag_id, viewHolder);
        }
        return viewHolder;
    }
    private ViewHolder(View view) {
        this.view = view;
        viewHolder = new SparseArray<>();
    }

    public TextView getTextView(int id) {
        return get(id);
    }
    public <T extends View> T get(int id) {
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
    public ImageView getImageView(int id) {
        return get(id);
    }

    public Context getContext() {
        return getConvertView().getContext();
    }
    public View getConvertView() {
        return view;
    }
}
