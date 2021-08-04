package com.yujing.openConnect.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.yujing.openConnect.utlis.Utils
import com.yujing.utils.YCheck

/**
 * 开机自启
 */
class AutoStartBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
//        Intent autoStart = new Intent(context, MainActivity.class);
//        autoStart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        autoStart.putExtra("data", "auto");
//        context.startActivity(autoStart);

        //如果是设置打开的，直接打开端口
        if (Utils.usePortCustom!!.auto && YCheck.isPort(Utils.usePortCustom?.port))
            Utils.open(Utils.usePortCustom!!.port)
    }
}