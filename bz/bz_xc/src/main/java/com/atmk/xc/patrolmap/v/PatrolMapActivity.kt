package com.atmk.xc.patrolmap.v

import android.os.Build
import com.amap.api.maps.MapView
import com.atmk.base.app.AppVMActivity
import com.atmk.base.ui.dialog.MessageDialog
import com.atmk.xc.databinding.XcActivityPatrolMapBinding
import com.hjq.base.BaseDialog
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions


/**
 * @author        YJ
 * @date          2022-09-22
 * @description
 */
class PatrolMapActivity : AppVMActivity<XcActivityPatrolMapBinding>() {
    override fun getViewBinding() = XcActivityPatrolMapBinding.inflate(layoutInflater)

    override fun initView() {

    }

    override fun initData() {
    }


    private val needPermissionsWithoutLocation = arrayOf(
        Permission.WRITE_EXTERNAL_STORAGE,
        Permission.READ_EXTERNAL_STORAGE,
        Permission.READ_PHONE_STATE
    )

    //后台定位权限必须一组申请  com.hjq.permissions.PermissionChecker 192行
    //ACCESS_BACKGROUND_LOCATION 这个权限只在版本号大于等于29（android 10）才需要申请
    private val needLocationPermissions = mutableListOf(
        Permission.ACCESS_COARSE_LOCATION,
        Permission.ACCESS_FINE_LOCATION
    )

    override fun onStart() {
        super.onStart()
        //申请权限
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissionsWithoutLocation(needPermissionsWithoutLocation)
        }
    }

    private fun checkPermissionsWithoutLocation(permissions: Array<String>) {

        XXPermissions.with(this)
            .permission(permissions)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>, all: Boolean) {
                    if (all) {
                        //假如大于28 ，就需要申请后台定位权限
                        if (Build.VERSION.SDK_INT > 28 && applicationContext.applicationInfo.targetSdkVersion > 28) {
                            needLocationPermissions.add(Permission.ACCESS_BACKGROUND_LOCATION)
                        }
                        //判断位置权限是否已经授权
                        val isGranted = XXPermissions.isGranted(this@PatrolMapActivity, needLocationPermissions)
                        if (!isGranted) {
                            //全部申请成功后，再申请位置权限
                            showLocationRequestTipDialog()
                        }else{
                            //已经授权，那就开始定位

                        }
                    }
                }

                override fun onDenied(permissions: MutableList<String>, never: Boolean) {
                    if (never) {
                        showMissingPermissionDialog(permissions)
                    }
                    checkPermissionsWithoutLocation(permissions.toTypedArray())
                }
            })

    }


    /**
     * 申请位置权限除外
     * 显示的提示信息
     */
    private fun showMissingPermissionDialog(permissions: MutableList<String>) {

        MessageDialog.Builder(this)
            .setTitle("提示")
            .setMessage("当前应用缺少必要权限，请手动设置权限。")
            .setConfirm("设置")
            .setCancel("取消")
            .setCancelable(false)
            .setListener(object : MessageDialog.OnListener {
                override fun onConfirm(dialog: BaseDialog?) {
                    XXPermissions.startPermissionActivity(this@PatrolMapActivity, permissions)
                }

                override fun onCancel(dialog: BaseDialog?) {
                    finish()
                }
            })
            .show()

    }

    /**
     * 申请位置权限除外
     * 显示的提示信息
     */
    private fun showLocationRequestTipDialog() {

        MessageDialog.Builder(this)
            .setTitle("提示")
            .setMessage("如果需要使用巡查功能，还需要授权定位权限。注意：在授权后台定位权限时，需要选择【始终允许】才可以！")
            .setConfirm("设置")
            .setCancel("取消")
            .setCancelable(false)
            .setListener(object : MessageDialog.OnListener {
                override fun onConfirm(dialog: BaseDialog?) {
                    checkLocationPermission(needLocationPermissions.toTypedArray())
                }

                override fun onCancel(dialog: BaseDialog?) {
                    finish()
                }
            })
            .show()

    }


    /**
     * 申请位置权限
     */
    private fun checkLocationPermission(permissions: Array<String>) {
        XXPermissions.with(this)
            .permission(permissions)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>, all: Boolean) {
                    if (all) {
                        //开始定位
                    }
                }

                override fun onDenied(permissions: MutableList<String>, never: Boolean) {
                    if (never) {
                        showMissingPermissionDialog(permissions)
                    }
                    checkLocationPermission(permissions.toTypedArray())
                }
            })
    }


}