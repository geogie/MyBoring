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
 */

public class ViewHolder {
    private SparseArray<View> viewHolder;
    private View view;
    public int position;

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
