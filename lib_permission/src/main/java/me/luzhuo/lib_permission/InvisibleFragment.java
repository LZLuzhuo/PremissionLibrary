package me.luzhuo.lib_permission;

import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * Description: 实现原理, Fragment存在一份与Activity相同的API, 使得 Fragment 也能申请权限.
 * Fragment不像Activity那样必须有界面, 我们想Activity添加一个隐藏的Fragment, 然后在这个Fragment中对运行时权限的API进行封装.
 *
 * @Author: Luzhuo
 * @Creation Date: 2020/9/11 1:04
 * @Copyright: Copyright 2020 Luzhuo. All rights reserved.
 **/
public class InvisibleFragment extends Fragment {

    private PermissionCallback requestCallback;

    public void requestPermission(PermissionCallback callback, String... permissions){
        this.requestCallback = callback;
        requestPermissions(permissions, 0x01);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0x01) {
            List<String> deniedList = new ArrayList<>(); // 存放被拒绝的权限
            List<String> foreverDeniedList = new ArrayList<>(); // 存放永久被拒绝的权限

            for (int i = 0; i < grantResults.length; i++) {
                // 将未通过申请的权限, 添加到拒绝的权限列表中
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    deniedList.add(permissions[i]);
                    /*
                    拥有 [仅使用时允许] [每次询问] [禁止] 的设备:
                    第一次拒绝: -1拒绝 & true
                    第二次拒绝: -1拒绝 & false
                    永久拒绝:   -1拒绝 & false (不再弹窗)

                    拥有 [始终允许] [仅使用期间允许] [禁止] 的设备, 在用户禁止后, 仅有 [允许] [禁止] 选项:
                    第一次拒绝: -1拒绝 & false
                    永久拒绝:   -1拒绝 & false (不再弹窗)
                     */
                    if (!shouldShowRequestPermissionRationale(permissions[i])) foreverDeniedList.add(permissions[i]);
                }
            }
            // 是否所有权限都被同意
            boolean allGranted = deniedList.size() <= 0;

            if(requestCallback != null) {
                requestCallback.onRequst(allGranted, deniedList);
                if(allGranted) requestCallback.onGranted();
                if(!allGranted) requestCallback.onDenieds(deniedList);
                if(foreverDeniedList.size() > 0) requestCallback.onForeverDenieds(foreverDeniedList);
            }
        }
    }
}
