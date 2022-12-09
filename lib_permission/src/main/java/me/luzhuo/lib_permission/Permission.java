/* Copyright 2020 Luzhuo. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.luzhuo.lib_permission;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    public static void request(@NonNull FragmentActivity activity, @Nullable PermissionCallback requestCallback, @NonNull String... permissions) {
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

    public static void request(@NonNull Fragment fragment, @Nullable PermissionCallback requestCallback, @NonNull String... permissions) {
        request(fragment.getActivity(), requestCallback, permissions);
    }

    /**
     * 检查使用拥有该权限
     */
    public static boolean checkPermission(@Nullable Context context, @Nullable String permission) {
        if (context == null || TextUtils.isEmpty(permission)) return false;
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
