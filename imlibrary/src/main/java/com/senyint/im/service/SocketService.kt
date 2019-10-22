package com.senyint.im.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.senyint.im.BuildConfig
import com.senyint.im.R
import com.senyint.im.socket.SocketClient
import com.senyint.im.socket.SocketManager
import com.senyint.xiaohuatuo.doctor.im.notify.SocketNotificationChannel

/**
 * IM通讯Service
 * Author:Ljb
 * Time:2018/9/5
 * There is a lot of misery in life
 **/
class SocketService : Service() {

    companion object {
        const val TAG = "SocketService"
        const val CMD = "cmd"
        const val DATA = "data"
        const val MSG_EVENT = "event"
        const val MSG_ID = "pid"
        const val MSG = "msg"


        const val ID_NOTIFICATION = 0x10099

        const val CMD_TEST_SOCKET_KILLED = -1

        const val CMD_INIT_SOCKET = 0
        const val CMD_RELEASE_SOCKET = 1
        const val CMD_SEND_MSG = 2
        const val CMD_SEND_MSG_CALLBACK = 3
        const val CMD_CHAT_CALLBACK = 4
        const val CMD_SEND_CMD = 5


    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand()")
        intent?.let {
            val cmd = it.getIntExtra(CMD, -1)
            when (cmd) {
                CMD_TEST_SOCKET_KILLED -> testSocketKilled()
                CMD_INIT_SOCKET -> initSocket(it)
                CMD_RELEASE_SOCKET -> releaseSocket()
                CMD_SEND_MSG -> sendMsg2Socket(it)
                CMD_SEND_MSG_CALLBACK -> callRequest2UI(it)
                CMD_CHAT_CALLBACK -> callResponse2UI(it)
                CMD_SEND_CMD -> sendCmd2Socket(it)
            }
        }
        return START_STICKY
    }


    override fun onCreate() {
        Log.i(TAG, "onCreate()")
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            SocketNotificationChannel.initChannel(this, BuildConfig.APPLICATION_ID)
            val icon = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
            val notification = Notification.Builder(this)
                    .setChannelId(BuildConfig.APPLICATION_ID)
                    .setContentTitle("Senyint Service")
                    .setContentText("IM Chat")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(icon)
                    .build()
            startForeground(ID_NOTIFICATION, notification)
        }
    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy()")
        super.onDestroy()
    }

    private fun callResponse2UI(intent: Intent) {
        val result = intent.getStringExtra(MSG)
        val callIntent = Intent(SocketManager.ResponseChatMsgCallReceiver.ACTION_CHAT)
        callIntent.putExtra(SocketManager.ResponseChatMsgCallReceiver.RESULT, result)
        sendBroadcast(callIntent)
    }

    private fun callRequest2UI(intent: Intent) {
        val msgId = intent.getStringExtra(MSG_ID)
        val result = intent.getStringExtra(DATA)

        val callIntent = Intent()
        callIntent.action = SocketManager.RequestChatMsgCallReceiver.ACTION
        callIntent.putExtra(SocketManager.RequestChatMsgCallReceiver.MSG_ID, msgId)
        callIntent.putExtra(SocketManager.RequestChatMsgCallReceiver.RESULT, result)
        sendBroadcast(callIntent)
    }

    private fun sendMsg2Socket(intent: Intent) {
        val msg = intent.getStringExtra(MSG)
        val event = intent.getStringExtra(MSG_EVENT)
        val msgId = intent.getStringExtra(MSG_ID)
        SocketClient.sendMsg(this, event, msg, msgId)
    }

    private fun sendCmd2Socket(intent: Intent) {
        val msg = intent.getStringExtra(MSG)
        val event = intent.getStringExtra(MSG_EVENT)
        val msgId = intent.getStringExtra(MSG_ID)
        SocketClient.sendCmd(this, event, msg, msgId)
    }

    private fun initSocket(intent: Intent) {
        val token = intent.getStringExtra(DATA)
        SocketClient.init(this, token)
    }

    private fun releaseSocket() {
        SocketClient.releaseAll()
        stopSelf()
    }


    private fun testSocketKilled() {
        if (BuildConfig.DEBUG) {
            SocketClient.testKillSocket()
        }
    }

}
