package me.luzhuo.lib_permission;

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

    public static void request(FragmentActivity activity, PermissionCallback requestCallback, String... permissions) {
        final FragmentManager fragmentManager = activity.getSupportFragmentManager();
        final Fragment exitedFragment = fragmentManager.findFragmentByTag(tag);

        // 判断Activity中是否包含了指定tag的Fragment, 有就直接使用这个Fragment, 没有就创建一个
        final InvisibleFragment fragment;
        if (exitedFragment != null) fragment = (InvisibleFragment) exitedFragment;
        else {
            // 把新创建的Fragment添加到Activity中
            final InvisibleFragment invisibleFragment = new InvisibleFragment();
            fragmentManager.beginTransaction().add(invisibleFragment, tag).commitNow(); // commitNow() 立即执行添加
            fragment = invisibleFragment;
        }
        fragment.requestPermission(requestCallback, permissions);
    }

    public static void request(Fragment fragment, PermissionCallback requestCallback, String... permissions) {
        request(fragment.getActivity(), requestCallback, permissions);
    }
}
