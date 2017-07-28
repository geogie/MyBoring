package com.georgeren.myboring.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Author：George on 2016/7/11 0011 11:35
 * Email：1063658094@qq.com
 * Api23权限，是否有权限
 */
public class PermissionUtils {
    private final static String TAG = "PermissionUtils";
    public final static int CAMERA_CODE = 1;//拍照权限

    private static PermissionUtils mPermissionUtils = new PermissionUtils();

    // 音频获取源
    public static int audioSource = MediaRecorder.AudioSource.MIC;
    // 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    public static int sampleRateInHz = 44100;
    // 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
    public static int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
    // 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。
    public static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    // 缓冲区字节大小
    public static int bufferSizeInBytes = 0;

    public static PermissionUtils getInstance() {
        return mPermissionUtils;
    }

    public static boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }
        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 单个权限
     *
     * @param activity
     * @param permission
     * @param reqCode
     * @return true:已经有这个权限，false：没有这个权限
     */
    @TargetApi(Build.VERSION_CODES.M)
    public boolean isSinglePermission(Activity activity, String permission, int reqCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//大于等于6.0
            if (ActivityCompat.checkSelfPermission(activity, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    Log.d(TAG, "Displaying " + permission + " permission rationale to provide additional context.");
                    ActivityCompat.requestPermissions(activity,
                            new String[]{permission},
                            reqCode);
                } else {
                    Log.d(TAG, "permission has not been granted yet " + permission + " Request it directly.");
                    ActivityCompat.requestPermissions(activity, new String[]{permission},
                            reqCode);
                }
                return false;
            } else {
                return true;
            }
        } else {//小于6.0
            PackageManager pm = activity.getPackageManager();
            boolean isHas = (PackageManager.PERMISSION_GRANTED ==
                    pm.checkPermission(permission, activity.getPackageName()));
            if (isHas) {
                Log.d(TAG, "有这个权限");
            } else {
                Log.d(TAG, "木有这个权限");
            }
            return true;
        }

    }

    /**
     * 多个权限
     *
     * @param activity
     * @param permission
     * @param reqCode
     * @return true：有权限，false:没有权限
     */
    @TargetApi(Build.VERSION_CODES.M)
    public boolean isMuPermission(Activity activity, String[] permission, int reqCode) {
        int size = permission.length;
        boolean isCheckSelf = true;//是否已经有这个权限
        for (int i = 0; i < size; i++) {
            if (ActivityCompat.checkSelfPermission(activity, permission[i])
                    != PackageManager.PERMISSION_GRANTED) {
                isCheckSelf = false;
                break;
            }
        }
        Log.d(TAG, "isCheckSelf:" + isCheckSelf);
        if (isCheckSelf) {//所有权限通过

        } else {
            boolean isShouldShow = false;
            for (int i = 0; i < size; i++) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission[i])) {
                    isShouldShow = true;
                    break;
                }
            }
            Log.d(TAG, "isShouldShow:" + isShouldShow);
            if (isShouldShow) {
                ActivityCompat.requestPermissions(activity, permission, reqCode);
            } else {
                ActivityCompat.requestPermissions(activity, permission, reqCode);
            }
        }
        return isCheckSelf;
    }

    /**
     * 判断是是否有录音权限
     */
    public static boolean isHasPermission() {
        bufferSizeInBytes = 0;
        bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz,
                channelConfig, audioFormat);
        AudioRecord audioRecord = new AudioRecord(audioSource, sampleRateInHz,
                channelConfig, audioFormat, bufferSizeInBytes);
        //开始录制音频
        try {
            // 防止某些手机崩溃，例如联想
            audioRecord.startRecording();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        /**
         * 根据开始录音判断是否有录音权限
         */
        if (audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {//STATE_UNINITIALIZED
            return false;
        }

        audioRecord.stop();
        audioRecord.release();
        audioRecord = null;
        return true;
    }
}
