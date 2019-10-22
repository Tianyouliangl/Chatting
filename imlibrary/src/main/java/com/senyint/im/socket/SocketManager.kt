package com.senyint.im.socket

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.senyint.im.BuildConfig
import com.senyint.im.bean.ChatMessage
import com.senyint.im.listener.Constant
import com.senyint.im.service.SocketService
import com.senyint.im.utils.ChatUtils
import com.senyint.im.utils.JsonParser
import com.senyint.im.utils.RxUtils
import com.senyint.xiaohuatuo.doctor.im.notify.SocketNotificationManager
import io.reactivex.disposables.Disposable
import org.json.JSONObject
import java.lang.ref.WeakReference

/**
 * 对外开放的Socket通讯类
 * Author:Ljb
 * Time:2018/9/6
 * There is a lot of misery in life
 **/
object SocketManager {


    private var mToken = ""

    /**
     * 发送消息的CallBack
     * */
    interface RequestCallBack {
        fun call(msg: String)
    }

    /**
     * 发送消息的CallBack处理广播
     * */
    class RequestChatMsgCallReceiver : BroadcastReceiver() {

        companion object {
            const val ACTION = "${BuildConfig.APPLICATION_ID}.ACTION_CHAT_MSG_REQUEST"
            const val MSG_ID = "pid"
            const val RESULT = "callResult"
        }

        override fun onReceive(context: Context?, intent: Intent?) {
            if (ACTION != intent?.action) return
            val msgId = intent.getStringExtra(MSG_ID)
            val result = intent.getStringExtra(RESULT)

            mRequestCallMap[msgId]?.let {
                Log.i(SocketService.TAG, "Socket 发送完成后，回调给UI")
                it.call(result)
            }
            mRequestCallMap.remove(msgId)
        }

    }

    /**
     * 接收消息的CallBack广播
     * */
    class ResponseChatMsgCallReceiver : BroadcastReceiver() {

        companion object {
            const val ACTION = "${BuildConfig.APPLICATION_ID}.ACTION_CHAT_MSG_RESPONSE"
            const val ACTION_SOCKET_KILLED = "${BuildConfig.APPLICATION_ID}.ACTION_CHAT_MSG_RESPONSE_KILLED"
            const val ACTION_CONFLICT = "${BuildConfig.APPLICATION_ID}.ACTION_CHAT_MSG_RESPONSE_CONFLICT"
            const val ACTION_CHAT = "${BuildConfig.APPLICATION_ID}.ACTION_CHAT_MSG_RESPONSE_CHAT"
            const val RESULT = "callResult"
        }

        override fun onReceive(context: Context, intent: Intent?) {
            if (TextUtils.isEmpty(intent?.action)) return
            val type = intent!!.action
            val result = intent.getStringExtra(RESULT)
            when (type) {
                ACTION_SOCKET_KILLED -> responseKilled(context)
                ACTION_CONFLICT -> responseConflict(context, result)
                ACTION_CHAT -> responseChat(context, result)
            }
        }
    }


    private val mRequestCallMap = HashMap<String, RequestCallBack?>()
    private var mRequestCallReceiver: RequestChatMsgCallReceiver? = null
    private var mResponseCallReceiver: ResponseChatMsgCallReceiver? = null
    private val mRxLife by lazy { ArrayList<WeakReference<Disposable>>() }

    /**
     * 获取当前用户的Socket Token
     * */
    fun getSocketToken(uid: String, token: String) = "uid=$uid&token=$token&device=android"

    /**
     * 登录Socket
     * */
    fun loginSocket(context: Context, token: String) {
        mToken = token
        val appContext = context.applicationContext
        // 开启Socket
        val intent = Intent(context, SocketService::class.java)
        intent.putExtra(SocketService.CMD, SocketService.CMD_INIT_SOCKET)
        intent.putExtra(SocketService.DATA, token)
        startService(appContext, intent)

        // 注册发送消息广播
        if (mRequestCallReceiver == null) {
            mRequestCallReceiver = RequestChatMsgCallReceiver()
            val reqIntentFilter = IntentFilter(RequestChatMsgCallReceiver.ACTION)
            appContext.registerReceiver(mRequestCallReceiver, reqIntentFilter)
            mRequestCallMap.clear()
        }

        // 注册接收消息广播
        if (mResponseCallReceiver == null) {
            mResponseCallReceiver = ResponseChatMsgCallReceiver()
            val respIntentFilter = IntentFilter(ResponseChatMsgCallReceiver.ACTION)
            respIntentFilter.addAction(ResponseChatMsgCallReceiver.ACTION_CONFLICT)
            respIntentFilter.addAction(ResponseChatMsgCallReceiver.ACTION_CHAT)
            respIntentFilter.addAction(ResponseChatMsgCallReceiver.ACTION_SOCKET_KILLED)
            appContext.registerReceiver(mResponseCallReceiver, respIntentFilter)
        }
    }

    /**
     * 退出Socket
     * */
    fun logoutSocket(context: Context) {

        SocketNotificationManager.releaseAll()

        mRxLife.map { RxUtils.dispose(it.get()) }
        mRxLife.clear()

        val appContext = context.applicationContext
        // 关闭Socket
        val intent = Intent(context, SocketService::class.java)
        intent.putExtra(SocketService.CMD, SocketService.CMD_RELEASE_SOCKET)
        startService(appContext, intent)

        // 注销发送消息广播
        if (mRequestCallReceiver != null) {
            appContext.unregisterReceiver(mRequestCallReceiver)
            mRequestCallMap.clear()
            mRequestCallReceiver = null
        }

        // 注销发送消息广播
        if (mResponseCallReceiver != null) {
            appContext.unregisterReceiver(mResponseCallReceiver)
            mResponseCallReceiver = null
        }
    }

    private fun startService(appContext: Context, intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            appContext.startForegroundService(intent)
        } else {
            appContext.startService(intent)
        }
    }

    /**
     * 发送消息
     * */
    fun sendChatMsg(context: Context, event: String, msg: String, call: RequestCallBack? = null) {
        try {
            val jsonObject = JSONObject(msg)
            val msgId = jsonObject.getString(RequestChatMsgCallReceiver.MSG_ID)
            val intent = Intent(context, SocketService::class.java)
            intent.putExtra(SocketService.CMD, SocketService.CMD_SEND_MSG)
            intent.putExtra(SocketService.MSG, msg)
            intent.putExtra(SocketService.MSG_EVENT, event)
            intent.putExtra(SocketService.MSG_ID, msgId)
            startService(context, intent)
            mRequestCallMap[msgId] = call
        } catch (e: Exception) {
            Log.e(SocketService.TAG, "消息：非法格式 -> $msg")
        }
    }

    /**
     * 发送命令
     * */
    fun sendCmd(context: Context, event: String, cmdMsg: String, call: RequestCallBack? = null) {
        try {
            val jsonObject = JSONObject(cmdMsg)
            val msgId = jsonObject.getString(RequestChatMsgCallReceiver.MSG_ID)
            val intent = Intent(context, SocketService::class.java)
            intent.putExtra(SocketService.CMD, SocketService.CMD_SEND_CMD)
            intent.putExtra(SocketService.MSG, cmdMsg)
            intent.putExtra(SocketService.MSG_EVENT, event)
            intent.putExtra(SocketService.MSG_ID, msgId)
            startService(context, intent)
            mRequestCallMap[msgId] = call
        } catch (e: Exception) {
            Log.e(SocketService.TAG, "消息：非法格式 -> $cmdMsg")
        }
    }

    fun clearCallBack() {
        if (mRequestCallMap.size != 0) {
            mRequestCallMap.clear()
        }
    }

    /**
     * 聊天响应
     * */
    private fun responseChat(context: Context, result: String?) {
        if (TextUtils.isEmpty(result)) return
        val chatMessage = JsonParser.fromJsonObj(result!!, ChatMessage::class.java)
        when (chatMessage.type) {
            ChatMessage.TYPE_CMD -> handleCmdChatMessage(context, chatMessage)
            ChatMessage.TYPE_CHAT -> handleImChatMessage(context, chatMessage)
        }
    }

    /**
     * 命令类型消息处理
     * */
    private fun handleCmdChatMessage(context: Context, chatMessage: ChatMessage) {
        sendCmd(context, SocketEvent.EVENT_CHAT, ChatUtils.getAck(chatMessage))
        val bodyType = chatMessage.bodyType
        if (bodyType == ChatMessage.MSG_BODY_TYPE_SYS) {
            //系统消息
            handleSysChatMessage(context, chatMessage)
        } else if (bodyType == ChatMessage.MSG_BODY_TYPE_ORDER) {
            //订单消息
            handleOrderChatMessage(context, chatMessage)
        }
    }


    /**
     * 系统消息
     * */
    private fun handleSysChatMessage(context: Context, chatMessage: ChatMessage) {
        ChatUtils.SOCKET_SERVICE_ID = chatMessage.fromId
        chatMessage.conversation = ChatUtils.getSysMsgConversation()
//        val sysMsgTable = ImSysMsgTable()
//        val newNumTable = ImNewNumTable()
//        val subscribe = DaoFactory.getProtocol(ISysMsgDaoProtocol::class.java)
//                .insertSysMsg(sysMsgTable, chatMessage)
//                .flatMap {
//                    DaoFactory.getProtocol(INewNumDaoProtocol::class.java)
//                            .insertNewNum(newNumTable, 1, chatMessage.conversation, chatMessage.fromId, chatMessage.serviceType)
//                }.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    EventBus.getDefault().post(ImNewNumEvent(chatMessage.conversation)) //刷新消息计数
//                    SocketNotificationManager.showNotificationMessage(context, chatMessage)
//                }, { XLog.e(it) })
//        mRxLife.add(WeakReference(subscribe))
    }

    /**
     * 订单消息
     * */
    private fun handleOrderChatMessage(context: Context, chatMessage: ChatMessage) {
        ChatUtils.SOCKET_SERVICE_ID = chatMessage.fromId
        chatMessage.conversation = ChatUtils.getOrderMsgConversation()
//        val orderMsgTable = ImOrderMsgTable()
//        val newNumTable = ImNewNumTable()
//
//        DaoFactory.getProtocol(IOrderMsgDaoProtocol::class.java)
//                .insertOrderMsg(orderMsgTable, chatMessage)
//        val subscribe = DaoFactory.getProtocol(IOrderMsgDaoProtocol::class.java)
//                .insertOrderMsg(orderMsgTable, chatMessage)
//                .flatMap {
//                    DaoFactory.getProtocol(INewNumDaoProtocol::class.java)
//                            .insertNewNum(newNumTable, 1, chatMessage.conversation, chatMessage.fromId, chatMessage.serviceType)
//                }.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    EventBus.getDefault().post(ImNewNumEvent(chatMessage.conversation)) //刷新消息计数
//                    SocketNotificationManager.showNotificationMessage(context, chatMessage)
//                }, { XLog.e(it) })
//        mRxLife.add(WeakReference(subscribe))
    }

    /**
     * 聊天类型消息处理
     * */
    private fun handleImChatMessage(context: Context, chatMessage: ChatMessage) {
        sendCmd(context, SocketEvent.EVENT_CHAT, ChatUtils.getAck(chatMessage))
        val conversation = ChatUtils.createConversation(chatMessage.topic, chatMessage.fromId, chatMessage.toId)
//        val historyTable = ImHistoryTable(conversation)
//        val newNumTable = ImNewNumTable()
//        val conversationTable = ImConversationTable()
//        val subscribe = DaoFactory.getProtocol(IConversationDaoProtocol::class.java).insertConversation(conversationTable, chatMessage)
//                .flatMap { DaoFactory.getProtocol(IInitDaoProtocol::class.java).createTableNotExists(historyTable) }
//                .flatMap { DaoFactory.getProtocol(IChatHistoryDaoProtocol::class.java).insertHistory(historyTable, chatMessage) }
//                .flatMap { DaoFactory.getProtocol(INewNumDaoProtocol::class.java).insertNewNum(newNumTable, 1, chatMessage.conversation, chatMessage.fromId, chatMessage.serviceType) }
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    EventBus.getDefault().post(ImNewNumEvent(chatMessage.conversation))   //刷新消息计数
//                    EventBus.getDefault().post(ChatNewMsgEvent(chatMessage))   //通知聊天页面
//                    SocketNotificationManager.showNotificationMessage(context, chatMessage)
//                }, { XLog.e(it) })
//        mRxLife.add(WeakReference(subscribe))
    }


    /**
     * Socket异常被杀死
     * */
    private fun responseKilled(context: Context) {
        Log.e(SocketService.TAG, "Socket异常被杀死")
        if (TextUtils.isEmpty(mToken)) {
            // TODO
//            val uid = SPUtils.getString(Constant.SPKey.KEY_UID, "")
//            val token = SPUtils.getString(Constant.SPKey.KEY_TOKEN, "")
            mToken = getSocketToken(Constant.uid, Constant.token)
        }
        //重新登录
        loginSocket(context, mToken)
    }

    /**
     * 用户冲突响应
     * */
    private fun responseConflict(context: Context, result: String?) {
        Toast.makeText(context.applicationContext, "您的账号在另一台设备登陆", Toast.LENGTH_SHORT).show()
      //  ActivityUtils.goLogin(context)
    }

    /**
     * 根据ID关闭通知栏
     * */
    fun cancelNotification(context: Context, id: Int) {
        SocketNotificationManager.cancelNotification(context, id)
    }


    fun testSocketKilled(context: Context) {
        val intent = Intent(context, SocketService::class.java)
        intent.putExtra(SocketService.CMD, SocketService.CMD_TEST_SOCKET_KILLED)
        startService(context, intent)
    }
}
