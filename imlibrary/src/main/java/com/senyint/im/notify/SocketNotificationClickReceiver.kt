package com.senyint.xiaohuatuo.doctor.im.notify

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.senyint.im.bean.ChatMessage

/**
 * Author:Ljb
 * Time:2018/11/15
 * There is a lot of misery in life
 **/
class SocketNotificationClickReceiver : BroadcastReceiver() {

    companion object {
        const val KEY_CHAT_MESSAGE = "chat"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val chatMessage = intent.getParcelableExtra(KEY_CHAT_MESSAGE) as? ChatMessage
                ?: return
        //TODO 通知栏消息点击处理
//        val startIntent = Intent(context, MainActivity::class.java)
//        startIntent.putExtra(KEY_CHAT_MESSAGE, chatMessage)
//        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        context.startActivity(startIntent)
    }

}
