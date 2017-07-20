package com.georgeren.myboring.read.itemhandler;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.georgeren.myboring.R;
import com.georgeren.myboring.base.adapter.ViewHolder;
import com.georgeren.myboring.base.ui.SimpleItemHandler;
import com.georgeren.myboring.read.mvp.entity.PictureCategoryResult;
import com.georgeren.myboring.utils.UIUtils;

/**
 * Created by georgeRen on 2017/7/20.
 */

public class PictureCategoryIH extends SimpleItemHandler<PictureCategoryResult.CategoryList.PictureCategory> {
    private TextView mTvTitle;

    @Override
    public void onCreateItemHandler(ViewHolder vh, ViewGroup parent) {
        super.onCreateItemHandler(vh, parent);
        mTvTitle = vh.getTextView(R.id.item_picture_category_tv_name);

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mTvTitle.getLayoutParams();
        layoutParams.leftMargin = UIUtils.dp2Px(10);
    }

    @Override
    public void onBindDataView(ViewHolder vh, PictureCategoryResult.CategoryList.PictureCategory data, int position) {
        mTvTitle.setText(data.getName());
        mTvTitle.setTextColor(UIUtils.getColor(mContext, R.color.colorPrimary));
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_picture_category;
    }

    @Override
    public void onClick(View v) {
    }

}
