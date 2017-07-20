package com.georgeren.myboring.base.ui;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;

import com.georgeren.myboring.base.ui.mainui.MainActivity;
import com.georgeren.myboring.R;
import com.georgeren.myboring.utils.SystemOperationUtils;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends Activity {
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 200;
    private boolean mHasSkip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        requestPermission();
    }

    public void requestPermission() {
        String[] needPermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        final List<String> requestPermissions = new ArrayList<>();

        for (int i = 0; i < needPermissions.length; i++) {
            if (ActivityCompat.checkSelfPermission(this, needPermissions[i]) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions.add(needPermissions[i]);
            }
        }

        if (!requestPermissions.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    skipToMainActivity();
                }
            });
            builder.setTitle("你需要开启以下权限")
                    .setMessage("访问存储空间, 读取媒体信息")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(SplashActivity.this, requestPermissions.toArray(new String[requestPermissions.size()]),
                                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                        }
                    });
            builder.show();
        } else {
            skipToMainActivity();
        }
    }
    public void skipToMainActivity() {
        if (mHasSkip) {
            return;
        }
        installShortcut();
        mHasSkip = true;
        getWindow().getDecorView().findViewById(android.R.id.content).postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.start(SplashActivity.this);
                SplashActivity.this.finish();
            }
        }, 3000);
    }

    private void installShortcut() {
        String shortCutName = getString(R.string.app_name);
        if (!SystemOperationUtils.hasShortcut(this, shortCutName)) {
            SystemOperationUtils.createShortCut(this, shortCutName, R.mipmap.ic_logo);
        }
    }
}
