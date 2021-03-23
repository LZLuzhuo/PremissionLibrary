package me.luzhuo.lib_permission;

import java.util.List;

/**
 * Description: 权限申请结果回调
 *
 * @Author: Luzhuo
 * @Creation Date: 2020/9/11 1:47
 * @Copyright: Copyright 2020 Luzhuo. All rights reserved.
 **/
public abstract class PermissionCallback {
    /**
     * 权限申请结果
     * 不管是否通过都调用
     * @param isAllGranted 是否全部申请通过, true是, false否
     * @param denieds 未通过申请的权限
     */
    public void onRequst(boolean isAllGranted, List<String> denieds){}

    /**
     * 所有权限申请通过时调用
     */
    public abstract void onGranted();

    /**
     * 被拒绝的权限
     * 有申请的权限不通过时调用
     * @param denieds
     */
    public void onDenieds(List<String> denieds){}
}