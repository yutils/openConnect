package com.yujing.openConnect

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import com.yujing.base.YBaseDialog
import com.yujing.openConnect.databinding.DialogSettingBinding
import com.yujing.openConnect.utlis.Utils
import com.yujing.utils.YCheck
import com.yujing.utils.YToast
import com.yujing.utils.YUtils

/**
 * 添加质量因素
 */
class SettingDiaLog(activity: Activity) : YBaseDialog<DialogSettingBinding>(activity, R.layout.dialog_setting) {
    init {
        fullscreen = false
        openAnimation = false
        //如果是横屏
        if (activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            widthPixels = 0.6f
            heightPixels = 0.8f
        } else {
            widthPixels = 0.8f
            heightPixels = 0.6f
        }
        fullscreen = false
        screenWidthDp = 360F
        fillColor = Color.parseColor("#C0FFFFFF")
    }

    override fun init() {
        binding.tvTitle.text = "网络调试"
        //本机IP
        binding.tvIp.isSelected = true
        binding.tvIp.text = "本机IP：" + YUtils.getIPv4().toTypedArray().contentToString()
        binding.switchOpenCustom.isChecked = Utils.usePortCustom?.open!!
        binding.switchAutoCustom.isChecked = Utils.usePortCustom?.auto!!
        //赋值自定义
        binding.etCustomPort.setText(Utils.usePortCustom?.port!!)
        //执行
        binding.btConfirm.setOnClickListener {
            //打开Custom
            var portCustom = binding.etCustomPort.text.toString()
            if (YCheck.isPort(portCustom)) {
                Utils.usePortCustom?.port = portCustom
                Utils.usePortCustom?.open = binding.switchOpenCustom.isChecked
                Utils.usePortCustom?.auto = binding.switchAutoCustom.isChecked
                Utils.usePortCustom = Utils.usePortCustom
            }

            if (Utils.usePortCustom!!.open && YCheck.isPort(Utils.usePortCustom?.port))
                Utils.open(Utils.usePortCustom!!.port) else Utils.close(Utils.usePortCustom!!.port)
            dismiss()
        }

        //github
        //本机IP
        binding.tvTips.isSelected = true
        binding.tvTips.setOnClickListener {
            val color = (Math.random() * 0x00FFFFFF + -0xffffff).toInt()
            binding.tvTips.setTextColor(color)
            val it2 = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/yutils/openConnect"))
            activity.startActivity(it2)
        }

        //打赏
        binding.tvReward.setOnClickListener {
            val color = (Math.random() * 0x00FFFFFF + -0xffffff).toInt()
            binding.tvTips.setTextColor(color)
            try {
                val it2 = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("alipays://platformapi/startapp?saId=10000007&qrcode=https%3A%2F%2Fqr.alipay.com%2Ffkx1504358uixr8gaa0jxb3")
                )
                activity.startActivity(it2)
            } catch (e: Exception) {
                YToast.show(App.get(), "打赏失败，什么情况？没安装支付宝？")
            }
        }
    }
}