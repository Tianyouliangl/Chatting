package com.senyint.xiaohuatuo.doctor.im.notify

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import com.senyint.im.BuildConfig
import com.senyint.im.R
import com.senyint.im.bean.ChatMessage
import com.senyint.im.utils.ChatUtils
import com.senyint.im.utils.RxUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference

/**
 * Author:Ljb
 * Time:2018/11/15
 * There is a lot of misery in life
 **/
object SocketNotificationManager {


    @JvmStatic
    private var isShowIm = true

    /**
     * 普通聊天 通知栏开关
     * */
    @JvmStatic
    fun setShowImNotification(show: Boolean) {
        isShowIm = show
    }

    private val mRxLife by lazy { ArrayList<WeakReference<Disposable>>() }

    fun showNotificationMessage(context: Context, chatMessage: ChatMessage) {
//        val switch = SPUtils.getBoolean(Constant.SPKey.SWITCH_NOTIFICATION, true)
        val switch = true
        //消息开关
        if (switch && isShowIm) {
            val data = NotificationData()
            val intent = Intent(context, SocketNotificationClickReceiver::class.java)
            intent.putExtra(SocketNotificationClickReceiver.KEY_CHAT_MESSAGE, chatMessage)
            data.pIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            data.notifId = chatMessage.conversation.hashCode()
            if (chatMessage.bodyType == ChatMessage.MSG_BODY_TYPE_SYS) {
                //系统消息
                data.bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher)
                data.title = context.getString(R.string.notify_sys_msg_title)
                data.content = context.getString(R.string.notify_sys_msg_content)
                showNotification(context, data)
            } else if (chatMessage.bodyType == ChatMessage.MSG_BODY_TYPE_ORDER) {
                //订单消息
                data.bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher)
                data.title = context.getString(R.string.notify_order_msg_title)
                data.content = context.getString(R.string.notify_order_msg_content)
                showNotification(context, data)
            } else {
                //其他消息，统一处理
                data.title = "新消息提醒"
                data.bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher)
                val subscribe = Observable.just(data)
                        .map { transformContent(it, chatMessage) }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { showNotification(context, it) }
                mRxLife.add(WeakReference(subscribe))
            }
        }
    }

    private fun transformContent(data: NotificationData, chatMessage: ChatMessage): NotificationData {
//        val newNum = DaoFactory.getProtocol(INewNumDaoProtocol::class.java)
//                .queryNewNumImpl(ImNewNumTable(), chatMessage.conversation, chatMessage.fromId, chatMessage.serviceType)
        val newNum = 2
        val body = ChatUtils.getBodyStr(chatMessage)
        if (newNum > 1) {
            data.content = "[${newNum}条] $body"
        } else {
            data.content = body
        }
        return data
    }


    private fun showNotification(context: Context, data: NotificationData) {
        val notifBuilder = Notification.Builder(context)
                .setContentTitle(data.title)
                .setContentText(data.content)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(data.bitmap)
                .setContentIntent(data.pIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notifBuilder.setChannelId(BuildConfig.APPLICATION_ID)
        } else {
            notifBuilder.setPriority(Notification.PRIORITY_HIGH)
            notifBuilder.setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE)
            notifBuilder.setVibrate(longArrayOf(100, 200, 300))
        }

        val notification = notifBuilder.build()
        notification.flags = Notification.FLAG_AUTO_CANCEL
        val notifManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notifManager.notify(data.notifId, notification)
    }


    fun cancelNotification(context: Context, notifId: Int) {
        val notifManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notifManager.cancel(notifId)
    }

    fun releaseAll() {
        mRxLife.map { RxUtils.dispose(it.get()) }
        mRxLife.clear()
    }
}