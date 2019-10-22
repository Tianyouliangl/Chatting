package com.senyint.xiaohuatuo.doctor.im.socket.listener

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.senyint.im.bean.ChatMessage
import com.senyint.im.service.SocketService
import com.senyint.im.utils.JsonParser
import io.socket.client.Ack

/**
 * 消息发送结果回调
 * Author:Ljb
 * Time:2018/9/6
 * There is a lot of misery in life
 **/
class MsgCallAck(val context: Context, val event: String, val msgId: String) : Ack {


    override fun call(vararg args: Any?) {
        var result = if (args.isNotEmpty()) args[0].toString() else args.toString()
        log(result)
        val ackMessage = JsonParser.fromJsonObj(result, ChatMessage::class.java)
        ackMessage.status = ChatMessage.MSG_STATUS_SEND_SUCCESS
        result = JsonParser.toJson(ackMessage)
        val serviceIntent = Intent(context, SocketService::class.java)
        serviceIntent.putExtra(SocketService.CMD, SocketService.CMD_SEND_MSG_CALLBACK)
        serviceIntent.putExtra(SocketService.MSG_ID, msgId)
        serviceIntent.putExtra(SocketService.DATA, result)
        startService(context, serviceIntent)
    }

    private fun startService(appContext: Context, intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            appContext.startForegroundService(intent)
        } else {
            appContext.startService(intent)
        }
    }

    private fun log(result: Any?) {
        Log.i(SocketService.TAG, """
            消息发送成功：
            -> 消息Id：$msgId
            -> 消息类型：$event
            -> result: $result
        """.trimIndent())
    }
}
