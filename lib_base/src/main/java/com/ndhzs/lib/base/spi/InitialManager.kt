package com.ndhzs.lib.base.spi

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Application
import android.os.Build
import android.os.Process
import android.provider.Settings
import android.text.TextUtils
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * @author ZhiQiang Tu
 * @time 2022/3/24  12:46
 * @signature 我将追寻并获取我想要的答案
 */
interface InitialManager {
    val application: Application
    fun isMainProcess(): Boolean  = currentProcessName()?.equals(applicationId()) ?: false

    fun currentProcessName(): String? {
        //不允许通过getRunningAppProcess获取当前线程号，因为有可能触发隐私条例
        val applicationContext = application.applicationContext
        val am = applicationContext.getSystemService(Application.ACTIVITY_SERVICE) as ActivityManager
        return am.runningAppProcesses
            .firstOrNull {
                it.pid == Process.myPid()
            }?.processName
    }


    fun applicationId() = application.packageName
    fun applicationVersion() = application.packageManager.getPackageInfo(application.packageName, 0).versionName
    fun applicationCode() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        application.packageManager.getPackageInfo(application.packageName, 0).longVersionCode
    } else {
        application.packageManager.getPackageInfo(application.packageName, 0).versionCode.toLong()
    }
    
    /*
     * 使用 androidId 来代替设备 id
     * android id 会在设备重置后还原，并且不具有唯一性
     * 但都重置系统了，可以不用管这么多
     * */
    fun getAndroidID(): String {
        return Settings.Secure.getString(application.contentResolver, Settings.Secure.ANDROID_ID)
    }
    
    /*
    * 获取设备型号
    * 小米：https://dev.mi.com/console/doc/detail?pId=2226
    * */
    @SuppressLint("PrivateApi")
    fun getDeviceModel(): String {
        var deviceName = ""
        try {
            val systemProperties = Class.forName("android.os.SystemProperties")
            val get: Method =
                systemProperties.getDeclaredMethod("get", String::class.java, String::class.java)
            deviceName = get.invoke(systemProperties, "ro.product.marketname", "") as String
            if (TextUtils.isEmpty(deviceName)) {
                deviceName = get.invoke(systemProperties, "ro.product.model", "") as String
            }
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return deviceName
    }
}