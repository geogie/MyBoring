package com.georgeren.myboring.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.georgeren.myboring.R;

/**
 * Created by georgeRen on 2017/7/14.
 */

public class UIUtils {
    private static DisplayMetrics sMetrics;

    public static String translatePlayCount(int playCount) {
        if (playCount < 9999) {
            return playCount + "";
        }
        return playCount / 10000 + "万";
    }
    private static DisplayMetrics getDisplayMetrics() {
        if (sMetrics == null) {
            sMetrics = Resources.getSystem().getDisplayMetrics();
        }
        return sMetrics;
    }

    /*设备屏幕宽度*/
    public static int getScreenWidth() {
        return getDisplayMetrics().widthPixels;
    }

    public static int getColor(Context context, int resId) {
        int color;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = context.getResources().getColor(resId, null);
        } else {
            color = context.getResources().getColor(resId);
        }
        return color;
    }

    public static void expandContentLayoutFullScreen(Activity activity) {
        activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }


    /*设备屏幕高度*/
    public static int getScreenHeight() {
        return getDisplayMetrics().heightPixels;
    }

    public static int dp2Px(float dp) {
        final float scale = getDisplayMetrics() != null ? getDisplayMetrics().density : 1;
        return (int) (dp * scale + 0.5f);
    }
    public static void loadSmallPicture(SimpleDraweeView simpleDraweeView, String picPath) {
        int width = 50, height = 50;
        ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(TextUtils.isEmpty(picPath) ? "" : picPath))
                .setResizeOptions(new ResizeOptions(width, height))
                .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setOldController(simpleDraweeView.getController())
                .setImageRequest(request)
                .build();
        simpleDraweeView.setController(controller);
    }
    public static void startSimpleRotateAnimation(View view) {
        RotateAnimation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1000);
        animation.setRepeatCount(1000);
        view.startAnimation(animation);
    }


    public static void refreshLikeStatus(ImageView imageView, boolean like) {
        if (like) {
            imageView.setImageResource(R.mipmap.ic_love);
        } else {
            imageView.setImageResource(R.mipmap.ic_un_love);
        }
    }
}
