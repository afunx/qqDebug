package com.qqdebug.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * @ClassName PermissionHelper
 * @date 2016/11/3
 * @modifier
 * @modifyTime 2016/11/3
 * @Description: Android 6.0运行时权限
 */
public class PermissionHelper {

    private static final String TAG = "PermissionHelper";

    public static boolean hasPermission(Context activity, String permission){
        return ContextCompat.checkSelfPermission(activity,
                permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermission(Activity activity, String permission, int requestCode){
        if (!AndroidVersionCheckUtils.hasM() || TextUtils.isEmpty(permission)) {
            return;
        }
        if(!hasPermission(activity, permission)){
            String[] permissions = new String[]{permission};
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,permission)) {
                // 官方建议向用户解析为什么要使用这个权限，然后再请求权限
                ActivityCompat.requestPermissions(activity, permissions, requestCode);
            } else {
                ActivityCompat.requestPermissions(activity, permissions, requestCode);
            }
        }
    }

    public static void requestPermissions(Activity activity, String[] permission, int requestCode){
        if (!AndroidVersionCheckUtils.hasM() || permission.length < 1) {
            return;
        }
        ActivityCompat.requestPermissions(activity, permission, requestCode);
    }

    public static void requestDrawOverlaysPermission(Activity activity, int requestCode){
        Intent intent = new Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + activity.getPackageName()));
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 检测 meizu 悬浮窗权限
     */
    public static boolean checkFloatWindowPermission(Context context) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            return checkOp(context, 24); //OP_SYSTEM_ALERT_WINDOW = 24;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static boolean checkOp(Context context, int op) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                Class clazz = AppOpsManager.class;
                Method method = clazz.getDeclaredMethod("checkOp",
                        int.class, int.class, String.class);
                return AppOpsManager.MODE_ALLOWED == (int)method.invoke(manager,
                        op, Binder.getCallingUid(), context.getPackageName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "Below API 19 cannot invoke!");
        }
        return false;
    }

    /**
     * 去魅族权限申请页面
     */
    public static void applyPermission(Context context){
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.setClassName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity");
        intent.putExtra("packageName", context.getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}



















