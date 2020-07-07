package com.yujing.openConnect.utlis

import android.util.Log
import com.yujing.openConnect.App
import java.io.*
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*
import java.util.concurrent.TimeoutException
import java.util.regex.Pattern

object Utils {
    val IPV4 get() = getIPv4()
    val IPV6 get() = getIPv6()

    //端口
    var PORT: String
        get() = if (YFileUtil.fileToString(File(App.get().filesDir.path + File.separator + "port.txt")) == null) "5555"
        else YFileUtil.fileToString(File(App.get().filesDir.path + File.separator + "port.txt"))
        set(port) = YFileUtil.stringToFile(
            File(App.get().filesDir.path + File.separator + "port.txt"), port
        )

    /**
     * 获取ipv6
     *
     * @return ipv6地址列表
     */
    private fun getIPv6(): List<String>? {
        val ips: MutableList<String> =
            ArrayList()
        try {
            val en =
                NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val ni = en.nextElement()
                val item = ni.inetAddresses
                while (item.hasMoreElements()) {
                    val inetAddress = item.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet6Address) {
                        ips.add(inetAddress.getHostAddress())
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
        val ips: MutableList<String> =
            ArrayList()
        try {
            val en =
                NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val ni = en.nextElement()
                val item = ni.inetAddresses
                while (item.hasMoreElements()) {
                    val inetAddress = item.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        ips.add(inetAddress.getHostAddress())
                    }
                }
            }
        } catch (ex: SocketException) {
            Log.e("获取IPv4失败", ex.toString())
        }
        return ips
    }

    /**
     * 打开网络调试
     * @return 是否成功
     */
    fun openNetworkDebugging(): Boolean {
        var os: DataOutputStream? = null
        return try {
            val process = Runtime.getRuntime().exec("su")
            os = DataOutputStream(process.outputStream)
            os.writeBytes("setprop service.adb.tcp.port $PORT\n")
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
    fun closeNetworkDebugging(): Boolean {
        var os: DataOutputStream? = null
        return try {
            val process = Runtime.getRuntime().exec("su")
            os = DataOutputStream(process.outputStream)
            os.writeBytes("setprop service.adb.tcp.port 5555\n")
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

    private fun shell(command: String): String {
        if (command.isEmpty()) return ""
        return try {
            val process = Runtime.getRuntime().exec(command)
            if (process.inputStream.available() > 0) {
                val mReader = BufferedReader(InputStreamReader(process.inputStream))
                val mRespBuff = StringBuffer()
                val buff = CharArray(1024)
                var ch: Int
                while (mReader.read(buff).also { ch = it } != -1)
                    mRespBuff.append(buff, 0, ch)
                mReader.close()
                mRespBuff.toString();
            } else {
                ""
            }
        } catch (e: IOException) {
            "异常：" + e.message
        }
    }

    @Throws(IOException::class)
    fun inputStreamToBytes(inputStream: InputStream): ByteArray? {
        // 网络传输时候，这样获取真正长度
        var count = 0
        while (count == 0) count = inputStream.available()
        val bytes = ByteArray(count)
        // 一定要读取count个数据，如果inputStream.read(bytes);可能读不完
        var readCount = 0 // 已经成功读取的字节的个数
        while (readCount < count)
            readCount += inputStream.read(bytes, readCount, count - readCount)
        return bytes
    }

    @Throws(IOException::class)
    fun inputStreamToBytes(inputStream: InputStream, timeOut: Long): ByteArray? {
        // 网络传输时候，这样获取真正长度
        val startTime = System.currentTimeMillis()
        var count = 0
        while (count == 0 && System.currentTimeMillis() - startTime < timeOut)
            count = inputStream.available()
        if (System.currentTimeMillis() - startTime >= timeOut)
            TimeoutException("读取超时").printStackTrace()
        val bytes = ByteArray(count)
        // 一定要读取count个数据，如果inputStream.read(bytes);可能读不完
        var readCount = 0 // 已经成功读取的字节的个数
        while (readCount < count) {
            readCount += inputStream.read(bytes, readCount, count - readCount)
        }
        return bytes
    }

    private const val PatternProt =
        "([0-9]|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-4]\\d{3}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])"

    private fun regular(str: String?, pattern: String): Boolean {
        if (null == str || str.trim { it <= ' ' }.isEmpty()) return false
        val p = Pattern.compile(pattern)
        val m = p.matcher(str)
        return m.matches()
    }

    fun isPort(str: String?): Boolean {
        return regular(str, PatternProt)
    }
}