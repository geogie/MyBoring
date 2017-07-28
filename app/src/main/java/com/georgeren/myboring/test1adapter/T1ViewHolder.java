package com.georgeren.myboring.test1adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.georgeren.myboring.R;

/**
 * Created by georgeRen on 2017/7/24.
 */

public class T1ViewHolder {

    private SparseArray<View> viewHolder;
    private View view;
    public int position;

    public T1ItemHandler itemHandler;

    public static T1ViewHolder newInstant(View view) {
        T1ViewHolder viewHolder = (T1ViewHolder) view.getTag(R.id.item_tag_id);
        if (viewHolder == null) {
            viewHolder = new T1ViewHolder(view);
            view.setTag(R.id.item_tag_id, viewHolder);
        }

        return viewHolder;
    }
    private T1ViewHolder(View view) {
        this.view = view;
        viewHolder = new SparseArray<>();
    }

    public Context getContext() {
        return getConvertView().getContext();
    }

    public View getConvertView() {
        return view;
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
}
