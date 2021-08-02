package com.yujing.openConnect

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.yujing.openConnect.databinding.ActivitySettingBinding
import com.yujing.openConnect.utlis.Utils


/**
 * 设置页面，主要是设置端口
 * @author yujing 2020年7月7日15:04:45
 */
class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding: ActivitySettingBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_setting)

        val data = intent.getStringExtra("data")
        if (data != null) {
            if (data == "setting") {
            }
        }

        binding.btOpen.setOnClickListener { open() }
        binding.btClose.setOnClickListener { close() }
        binding.etPort.setText(Utils.PORT)
        binding.etPort.setOnClickListener {
            if (Utils.isPort(binding.etPort.text.toString())) {
                Utils.PORT = binding.etPort.text.toString()
                Toast.makeText(this, "设置成功，请重新打开网络调试", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "端口格式不正确", Toast.LENGTH_SHORT).show()
            }
        }
        var sb = StringBuilder();
        binding.tvIpPort.text = "获取IP失败"
        if (Utils.IPV4 != null) for (item in Utils.IPV4!!) sb.append(item + "\n")
        //if (Utils.IPV6 != null) for (item in Utils.IPV6!!) sb.append(item + "\n")
        binding.tvIpPort.text = sb.toString()

        binding.tvTips.setOnClickListener {
            val color = (Math.random() * 0x00FFFFFF + -0xffffff).toInt()
            binding.tvTips.setTextColor(color)
            val it2 = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/yutils/openConnect"))
            startActivity(it2)
        }

        binding.btReward.setOnClickListener {
            val color = (Math.random() * 0x00FFFFFF + -0xffffff).toInt()
            binding.tvTips.setTextColor(color)
            try {
                val it2 = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("alipays://platformapi/startapp?saId=10000007&qrcode=https%3A%2F%2Fqr.alipay.com%2Ffkx1504358uixr8gaa0jxb3")
                )
                startActivity(it2)
            } catch (e: Exception) {
                Toast.makeText(this, "打赏失败，什么情况？没安装支付宝？", Toast.LENGTH_SHORT).show()
            }
        }
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
            Toast.makeText(this, "打开网络调试:失败", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(this, "关闭网络调试:失败", Toast.LENGTH_SHORT).show()
        }
    }
}