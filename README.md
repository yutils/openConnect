# 一键打开网络调试（支持开机自启）

开发环境 AS2020.3.1 ，kotlin 1.5.21，java8，gradle7.1

## 开发环境准备
**推荐使用jetBrains Toolbox 中的android studio，并更新到最新正式版**  

【必须】打开AS的安装目录，在bin目录下找到这两个文件（studio.exe.vmoptions，studio64.exe.vmoptions）  
在其中最后一行添加	-Dfile.encoding=UTF-8   
```bat
安装目录位置
C:\Users\用户名\AppData\Local\JetBrains\Toolbox\apps\AndroidStudio\ch-0\版本\bin
如：
C:\Users\yujing\AppData\Local\JetBrains\Toolbox\apps\AndroidStudio\ch-0\211.7628.21.2111.8139111\bin
```

## 说明

为了开发调试方便  
我们经常需要网络调试  
蛋疼的是  
每次关机又要重新打开网络调试  
很麻烦，而且容易被累死  
所以我开发了这  
另外，此APP需要root权限  
如果失败，那就USB连接电脑adb  
运行："adb tcpip 5555"  
adb连接举例："adb connect 192.168.1.6"  

## [releases里面有APK文件。点击前往](https://github.com/yutils/openConnect/releases)

### 特点

1.把MainActivity做成了1像素，而且透明。打开网络调试后就关掉，这样用户就无感  
2.桌面APP图标长按弹出对话框，进行端口设置

## 核心代码
```
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
```

## 效果图

![效果图1](https://raw.githubusercontent.com/yutils/openConnect/master/image/image1.png)


[我的github地址](https://github.com/yutils/openConnect)

[我的csdn地址](https://blog.csdn.net/Yu1441)
