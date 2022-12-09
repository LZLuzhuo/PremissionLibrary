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

import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.fragment.app.Fragment;
import me.luzhuo.lib_sqlite.search_history.SearchHistoryDBManager;
import me.luzhuo.lib_sqlite.search_history.SystemType;
import me.luzhuo.lib_sqlite.search_history.bean.SearchHistoryBean;

/**
 * Description: 实现原理, Fragment存在一份与Activity相同的API, 使得 Fragment 也能申请权限.
 * Fragment不像Activity那样必须有界面, 我们想Activity添加一个隐藏的Fragment, 然后在这个Fragment中对运行时权限的API进行封装.
 *
 * @Author: Luzhuo
 * @Creation Date: 2020/9/11 1:04
 * @Copyright: Copyright 2020 Luzhuo. All rights reserved.
 **/
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class InvisibleFragment extends Fragment {

    private PermissionCallback requestCallback;
    private SearchHistoryDBManager foreverDeniedDB;

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public InvisibleFragment() {}

    public void requestPermission(@Nullable PermissionCallback callback, @NonNull String... permissions){
        this.requestCallback = callback;
        this.foreverDeniedDB = new SearchHistoryDBManager(requireContext());
        requestPermissions(permissions, 0x01);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0x01) {
            List<String> deniedList = new ArrayList<>(); // 存放被拒绝的权限
            List<String> foreverDeniedList = new ArrayList<>(); // 存放永久被拒绝的权限
            List<SearchHistoryBean> historyForeverDeniedList = foreverDeniedDB.query(SystemType.Type_Permission_ForeverDenied_History, Integer.MAX_VALUE);

            for (int i = 0; i < grantResults.length; i++) {
                // 将未通过申请的权限, 添加到拒绝的权限列表中
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    // deniedList.add(permissions[i]);
                    /*
                    拥有 [仅使用时允许] [每次询问] [禁止] 的设备:
                    第一次拒绝: -1拒绝 & true
                    第二次拒绝: -1拒绝 & false
                    永久拒绝:   -1拒绝 & false (不再弹窗)

                    拥有 [始终允许] [仅使用期间允许] [禁止] 的设备, 在用户禁止后, 仅有 [允许] [禁止] 选项:
                    第一次拒绝: -1拒绝 & false
                    永久拒绝:   -1拒绝 & false (不再弹窗)
                     */
                    if (!shouldShowRequestPermissionRationale(permissions[i])) {
                        SearchHistoryBean history = new SearchHistoryBean(SystemType.Type_Permission_ForeverDenied_History, permissions[i]);
                        foreverDeniedDB.add(history);
                        // 第一次记录的时候, 不会添加到永久禁止名单里, 此时会有系统弹窗;
                        // 第二次记录的时候, 会被添加到永久禁止名单里, 此时不会有系统弹窗.
                        if (historyForeverDeniedList.contains(history)) foreverDeniedList.add(permissions[i]);
                        else deniedList.add(permissions[i]);
                    } else {
                        deniedList.add(permissions[i]);
                    }
                }
            }
            // 是否所有权限都被同意
            boolean allGranted = deniedList.isEmpty() && foreverDeniedList.isEmpty();

            if(requestCallback != null) {
                requestCallback.onRequst(allGranted, deniedList, foreverDeniedList);
                if(allGranted) requestCallback.onGranted();
                if(!deniedList.isEmpty()) requestCallback.onDenieds(deniedList);
                if(!foreverDeniedList.isEmpty()) requestCallback.onForeverDenieds(foreverDeniedList);
            }
        }
    }
}
