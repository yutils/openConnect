package com.yujing.openConnect.utlis

import android.util.Log
import com.yujing.openConnect.App
import com.yujing.openConnect.UsePort
import com.yujing.utils.YSave
import com.yujing.utils.YToast
import java.io.DataOutputStream
import java.io.IOException
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.NetworkInterface
import java.net.SocketException

object Utils {
    val IPV4 get() = getIPv4()
    val IPV6 get() = getIPv6()

    var usePortCustom: UsePort?
        get() = YSave.get(App.get(), "Custom", UsePort::class.java)
        set(value) {
            YSave.put(App.get(), "Custom", value)
        }

    /**
     * 获取ipv6
     *
     * @return ipv6地址列表
     */
    private fun getIPv6(): List<String>? {
        val ips: MutableList<String> = ArrayList()
        try {
            val en =
                NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val ni = en.nextElement()
                val item = ni.inetAddresses
                while (item.hasMoreElements()) {
                    val inetAddress = item.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet6Address) {
                        inetAddress.getHostAddress()?.let { ips.add(it) }
                    }
                }
            }
        } catch (ex: SocketException) {
            Log.e("获取IPv6失败", ex.toString())
        }
        return ips
    }

    /**
     * 获取ipv4
     *
     * @return ipv4地址列表
     */
    private fun getIPv4(): List<String>? {
        val ips: MutableList<String> = ArrayList()
        try {
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val ni = en.nextElement()
                val item = ni.inetAddresses
                while (item.hasMoreElements()) {
                    val inetAddress = item.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        inetAddress.getHostAddress()?.let { ips.add(it) }
                    }
                }
            }
        } catch (ex: SocketException) {
            Log.e("获取IPv4失败", ex.toString())
        }
        return ips
    }

    fun open(port: String) {
        val success = openNetworkDebugging(port)
        if (success) {
            YToast.show(App.get(), "打开网络调试:成功" + if (IPV4?.size!! > 0) IPV4!![0] + ":" + port else "")
        } else {
            YToast.show(App.get(), "打开网络调试:失败\n长按打开设置")
        }
    }

    fun close(port: String) {
        val success = closeNetworkDebugging(port)
        if (success) {
            YToast.show(App.get(), "关闭网络调试:成功" + if (IPV4?.size!! > 0) IPV4!![0] + ":" + port else "")
        } else {
            YToast.show(App.get(), "关闭网络调试:失败\n长按打开设置")
        }
    }

    /**
     * 打开网络调试
     * @return 是否成功
     */
    fun openNetworkDebugging(port: String): Boolean {
        var os: DataOutputStream? = null
        return try {
            val process = Runtime.getRuntime().exec("su")
            os = DataOutputStream(process.outputStream)
            os.writeBytes("setprop service.adb.tcp.port $port\n")
            os.writeBytes("stop adbd\n")
            os.writeBytes("start adbd\n")
            os.flush()
            true
        } catch (e: Exception) {
            false
        } finally {
            try {
                os?.close()
            } catch (e: IOException) {
            }
        }
    }

    /**
     * 关闭网络调试
     * @return 是否成功
     */
    fun closeNetworkDebugging(port: String): Boolean {
        var os: DataOutputStream? = null
        return try {
            val process = Runtime.getRuntime().exec("su")
            os = DataOutputStream(process.outputStream)
            os.writeBytes("setprop service.adb.tcp.port $port\n")
            os.writeBytes("stop adbd\n")
            os.flush()
            true
        } catch (e: Exception) {
            false
        } finally {
            try {
                os?.close()
            } catch (e: IOException) {
            }
        }
    }
}