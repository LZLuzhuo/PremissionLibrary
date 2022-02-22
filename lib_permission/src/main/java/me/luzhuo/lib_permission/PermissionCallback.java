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
     * @param denieds 未通过申请的权限, 不包含被永久拒绝的权限
     * @param foreverDenieds 被永久拒绝的权限
     */
    public void onRequst(boolean isAllGranted, List<String> denieds, List<String> foreverDenieds) { }

    /**
     * 所有权限申请通过时调用
     */
    public abstract void onGranted();

    /**
     * 被拒绝的权限
     * 有申请的权限不通过时调用, 不包含被永久拒绝的权限
     * @param denieds
     */
    public void onDenieds(List<String> denieds) { }

    /**
     * 被永久拒绝的权限
     * 有申请的权限被永久禁用时调用, 不包含非永久禁止的权限
     * @param foreverDenieds
     */
    public void onForeverDenieds(List<String> foreverDenieds) { }
}