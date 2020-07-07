# 一键打开网络调试（无界面APP）

开发环境AS4.0，kotlin1.72，java8

## 说明

为了开发调试方便<br/>
我们经常需要网络调试<br/>
蛋疼的是<br/>
每次关机又要重新打开网络调试<br/>
很麻烦，而且容易被累死<br/>
所以我开发了这<br/>
另外，此APP需要root权限<br/>
如果失败，那就USB连接电脑adb<br/>
运行："adb tcpip 5555"<br/>

### 特点

1.把MainActivity做成了1像素，而且透明。打开网络调试后就关掉，这样用户就无感<br/>
2.桌面APP图标长按弹出对话框，进行端口设置

## 核心代码
```
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
```

## 效果图

![效果图1](https://raw.githubusercontent.com/yutils/openConnect/master/image/image1.jpg)

![效果图2](https://raw.githubusercontent.com/yutils/openConnect/master/image/image2.jpg)


[我的github地址](https://github.com/yutils/openConnect)

[我的csdn地址](https://blog.csdn.net/Yu1441)
