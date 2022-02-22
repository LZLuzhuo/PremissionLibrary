package me.luzhuo.lib_permission;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

/**
 * Description: 权限管理
 *
 * @Author: Luzhuo
 * @Creation Date: 2020/9/11 1:25
 * @Copyright: Copyright 2020 Luzhuo. All rights reserved.
 **/
public class Permission {

    private static final String tag = "invisibleFragment";

    /**
     * 请求权限
     */
    public static void request(FragmentActivity activity, PermissionCallback requestCallback, String... permissions) {
        final FragmentManager fragmentManager = activity.getSupportFragmentManager();
        final Fragment exitedFragment = fragmentManager.findFragmentByTag(tag);

        // 判断Activity中是否包含了指定tag的Fragment, 有就直接使用这个Fragment, 没有就创建一个
        final InvisibleFragment fragment;
        if (exitedFragment != null) fragment = (InvisibleFragment) exitedFragment;
        else {
            // 把新创建的Fragment添加到Activity中
            final InvisibleFragment invisibleFragment = new InvisibleFragment();
            fragmentManager.beginTransaction().add(invisibleFragment, tag).commitNowAllowingStateLoss(); // commitNow() 立即执行添加
            fragment = invisibleFragment;
        }
        fragment.requestPermission(requestCallback, permissions);
    }

    public static void request(Fragment fragment, PermissionCallback requestCallback, String... permissions) {
        request(fragment.getActivity(), requestCallback, permissions);
    }

    /**
     * 检查使用拥有该权限
     */
    public static boolean checkPermission(Context context, String permission) {
        if (context == null || TextUtils.isEmpty(permission)) return false;
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
