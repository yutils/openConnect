package com.yujing.openConnect

import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.yujing.openConnect.utlis.Utils

/**
 * 1像素Activity，且背景透明
 * @author yujing 2020年7月7日15:04:17
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            initDynamicShortcuts()
        }
        //设置1像素
        val window: Window = window
        window.setGravity(Gravity.LEFT or Gravity.TOP)
        val params: WindowManager.LayoutParams = window.attributes
        params.x = 0
        params.y = 0
        params.height = 1
        params.width = 1
        window.attributes = params

        val data = intent.getStringExtra("data")
        if (data != null) {
            if (data == "open") {
                open()
                finish()
                return
            }
            if (data == "close") {
                close()
                finish()
                return
            }
        }
        open()
        startActivity(Intent(this,SettingActivity::class.java))
        return
    }

    private fun open() {
        val success = Utils.openNetworkDebugging()
        if (success) {
            Toast.makeText(
                this,
                "打开网络调试:成功" + if (Utils.IPV4?.size!! > 0) Utils.IPV4!![0] + ":" + Utils.PORT else "",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(this, "打开网络调试:失败\n长按打开设置", Toast.LENGTH_SHORT).show()
        }
    }

    private fun close() {
        val success = Utils.closeNetworkDebugging()
        if (success) {
            Toast.makeText(
                this,
                "关闭网络调试:成功" + if (Utils.IPV4?.size!! > 0) Utils.IPV4!![0] + ":" + Utils.PORT else "",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                this, "关闭网络调试:失败\n长按打开设置", Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * 为App创建动态Shortcuts
     */
    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun initDynamicShortcuts() {
        //①、创建动态快捷方式的第一步，创建ShortcutManager
        val scManager = getSystemService(ShortcutManager::class.java)
        //②、构建动态快捷方式的详细信息
        val scInfo1 = ShortcutInfo.Builder(this, "shortcut_id_1")
            .setShortLabel("打开网络调试")
            .setLongLabel("打开网络调试")
            .setIcon(Icon.createWithResource(this, R.mipmap.open))
            //.setIntent(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.baidu.com")))
            .setIntents(
                arrayOf(
                    Intent(
                        Intent.ACTION_MAIN,
                        Uri.EMPTY,
                        this,
                        MainActivity::class.java
                    ).putExtra("data", "open")
                )
            )
            .build()

        val scInfo2 = ShortcutInfo.Builder(this, "shortcut_id_2")
            .setShortLabel("关闭网络调试")
            .setLongLabel("关闭网络调试")
            .setIcon(Icon.createWithResource(this, R.mipmap.close))
            .setIntents(
                arrayOf(
                    Intent(
                        Intent.ACTION_MAIN,
                        Uri.EMPTY,
                        this,
                        MainActivity::class.java
                    ).putExtra("data", "close")
                )
            )
            .build()
        val scInfo3 = ShortcutInfo.Builder(this, "shortcut_id_3")
            .setShortLabel("打开设置")
            .setLongLabel("打开设置")
            .setIcon(Icon.createWithResource(this, R.mipmap.setting))
            .setIntents(
                arrayOf(
                    Intent(
                        Intent.ACTION_MAIN,
                        Uri.EMPTY,
                        this,
                        SettingActivity::class.java
                    ).putExtra("data", "setting")
                )
            )
            .build()
        //③、为ShortcutManager设置动态快捷方式集合
        scManager.dynamicShortcuts = listOf(scInfo1, scInfo2, scInfo3)

        //如果想为两个动态快捷方式进行排序，可执行下面的代码
        val dynamic1 = ShortcutInfo.Builder(this, "shortcut_id_1")
            .setRank(0)
            .build()
        val dynamic2 = ShortcutInfo.Builder(this, "shortcut_id_2")
            .setRank(1)
            .build()
        val dynamic3 = ShortcutInfo.Builder(this, "shortcut_id_3")
            .setRank(3)
            .build()
        //④、更新快捷方式集合
        scManager.updateShortcuts(listOf(dynamic1, dynamic2, dynamic3))
    }
}