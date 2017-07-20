package com.georgeren.myboring.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.georgeren.myboring.base.service.BaseService;
import com.georgeren.myboring.utils.FileUtils;
import com.georgeren.myboring.utils.ImagePipelineConfigFactory;

/**
 * Created by georgeRen on 2017/7/13.
 */

public class SAppApplication extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this, ImagePipelineConfigFactory.getImagePipelineConfig(this));
        FileUtils.initAppDir();

        Intent intent = new Intent(this, BaseService.class);
        startService(intent);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        sContext = base;
    }

    public static Context getAppContext() {
        return sContext;
    }
}
