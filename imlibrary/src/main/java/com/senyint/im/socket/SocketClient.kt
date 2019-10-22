package com.senyint.im.socket

import android.content.Context
import android.content.Intent
import android.util.Log
import com.senyint.im.BuildConfig
import com.senyint.im.bean.ChatMessage
import com.senyint.im.listener.DataErrorListener
import com.senyint.im.service.SocketService
import com.senyint.im.utils.JsonParser
import com.senyint.im.utils.RxUtils
import com.senyint.xiaohuatuo.doctor.im.socket.listener.*
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * Author:Ljb
 * Time:2018/9/5
 * There is a lot of misery in life
 **/
object SocketClient {

    @JvmStatic
    private var mSocket: Socket? = null
    //"uid=$uid&token=$token&device=1"
    @JvmStatic
    private var mToken: String? = null
    @JvmStatic
    var isLinked = false
    var HOST_SOCKET = "https://testxhttcp.cinyi.com"

    private var mConnectListener: Emitter.Listener? = null

    private var mDisConnectListener: Emitter.Listener? = null

    private var mConnectErrorListener: Emitter.Listener? = null

    private var mConnectTimeOutListener: Emitter.Listener? = null

    private var mDataErrorListener: Emitter.Listener? = null

    private var mLoginConflictListener: Emitter.Listener? = null

    private var mChatMessageListener: Emitter.Listener? = null


    /**
     * 根据Token初始化一个Socket
     * */
    fun init(context: Context, token: String) {
        //是否复用Socket
        if (mSocket != null) {
            //用户信息发生改变，重新创建Socket对象，并销毁之前的Socket对象
            if (mToken != token) {
                newSocket(context, token)
            } else {
                Log.i(SocketService.TAG, "socket 复用")
            }
        } else {
            newSocket(context, token)
        }
    }


    /***
     * 断开连接
     */
    fun close() {
        mSocket?.let {
            Log.i(SocketService.TAG, "socket 链接已关闭")
            it.disconnect()
        }
    }

    /**
     * 开启连接，调用前请先初始化 @init()
     * */
    fun open() {
        mSocket?.connect()
    }

    /**
     * 销毁当前Socket
     * */
    fun releaseAll() {
        mSocket?.let {
            //            close()
            removeSocketListener()
            mSocket = null
            Log.i(SocketService.TAG, "socket 已释放")
        }
        RxUtils.dispose(reNewSocketSubscription)
    }

    /**
     * 检查Socket当前是否可用
     * */
    private fun checkSocketStatus(context: Context): Boolean {
        return if (mSocket == null) {
            //Socket对象异常被杀死
            val result = "socket is killed"
            Log.e(SocketService.TAG, result)
            sendSocketKilled(context, result)
            false
        } else {
            isLinked
        }
    }

    /**
     * 发送Socket被杀广播
     * */
    private fun sendSocketKilled(context: Context, result: String) {
        val intent = Intent(SocketManager.ResponseChatMsgCallReceiver.ACTION_SOCKET_KILLED)
        intent.putExtra(SocketManager.ResponseChatMsgCallReceiver.RESULT, result)
        context.sendBroadcast(intent)
    }


    /**
     * 发送命令
     * */
    fun sendCmd(context: Context, event: String, msg: String, msgId: String) {
        if (checkSocketStatus(context)) {
            sendCmdImpl(context, event, msg, msgId)
        }
    }

    private fun sendCmdImpl(context: Context, event: String, msg: String, msgId: String) {
        mSocket?.let {
            Log.i(SocketService.TAG, "socket 发送命令：$msg")
            it.emit(event, JSONObject(msg), MsgCallAck(context, event, msgId))
        }
    }

    /**
     * 发送消息
     * */
    fun sendMsg(context: Context, event: String, msg: String, msgId: String) {
        if (checkSocketStatus(context)) {
            sendMsgImpl(context, event, msg, msgId)
        } else {
            MsgCallErrorTask(context, event, msg, msgId).run()
        }
    }

    private fun sendMsgImpl(context: Context, event: String, msg: String, msgId: String) {
        mSocket?.let {
            Log.i(SocketService.TAG, "socket 发送消息：$msg")
            it.emit(event, JSONObject(msg), MsgCallAck(context, event, msgId))
        }
    }


    /**
     * 创建新的 Socket 对象
     * */
    private fun newSocket(context: Context, token: String) {
        Log.i(SocketService.TAG, "socket 创建  -> $token")
        mToken = token
        val opts = IO.Options()
        opts.forceNew = false
        opts.reconnection = true
        opts.reconnectionDelay = 3000
        opts.reconnectionDelayMax = 5000
        opts.timeout = -1
        opts.query = token  //uid=uid&token=token&device=1
        //高版本Socket支持共用http对象 ，如：1.0.0
//        opts.callFactory = HttpClient.getHttpClient()
//        opts.webSocketFactory = HttpClient.getHttpClient()

        try {
            if (!HOST_SOCKET.isEmpty()){
                mSocket = IO.socket(HOST_SOCKET, opts)
                //添加全局监听器
                initSocketListener(context)
                //连接
                open()
                Log.i(SocketService.TAG, "socket 创建成功  -> $token")
            }else{
                Log.e(SocketService.TAG, "socket 创建失败 -> The url is invalid")
            }

        } catch (e: Exception) {
            Log.e(SocketService.TAG, "socket 创建失败 -> $token")
            e.printStackTrace()
        }
    }

    private var reNewSocketSubscription: Disposable? = null


    /**
     * 初始化监听器
     * */
    private fun initSocketListener(context: Context) {
        mChatMessageListener = ChatMessageListener(context)
        mLoginConflictListener = LoginConflictListener(context)

        //链接已断开监听
        mDisConnectListener = Emitter.Listener { args ->
            val result = if (args.isNotEmpty()) args[0].toString() else args.toString()
            Log.i(SocketService.TAG, "socket 链接已断开 -> $result")
            isLinked = false
            releaseAll()

            try {
                if (result.startsWith("{")) {
                    val chatMessage = JsonParser.fromJsonObj(result, ChatMessage::class.java)
                    if (chatMessage.body.contains("conflicted", true)) {
                        conflictLogin(context, result)
                    } else {
                        restartSocket(context)
                    }
                } else {
                    restartSocket(context)
                }
            } catch (e: Exception) {
                restartSocket(context)
            }
        }

        //链接成功监听
        mConnectListener = Emitter.Listener { args ->
            isLinked = true
            Log.i(SocketService.TAG, "socket 连接成功")
        }

        //链接发生错误监听
        mConnectErrorListener = Emitter.Listener { args ->
            val result = if (args.isNotEmpty()) args[0].toString() else args.toString()
            Log.i(SocketService.TAG, "socket 连接发生错误 -> $result")
            isLinked = false
        }
        mConnectTimeOutListener = ConnectTimeOutListener(context, mSocket)
        mDataErrorListener = DataErrorListener(context)

        mSocket?.apply {
            Log.i(SocketService.TAG, "socket 初始化监听器")
            on(SocketEvent.EVENT_CHAT, mChatMessageListener)
            on(SocketEvent.EVENT_CONFLICT, mLoginConflictListener)
            on(Socket.EVENT_CONNECT, mConnectListener)
            on(Socket.EVENT_DISCONNECT, mDisConnectListener)
            on(Socket.EVENT_CONNECT_ERROR, mConnectErrorListener)
            on(Socket.EVENT_CONNECT_TIMEOUT, mConnectTimeOutListener)
            on(Socket.EVENT_ERROR, mDataErrorListener)
        }
    }

    /**
     * 互踢
     * */
    private fun conflictLogin(context: Context, result: String) {
        val intent = Intent(SocketManager.ResponseChatMsgCallReceiver.ACTION_CONFLICT)
        intent.putExtra(SocketManager.ResponseChatMsgCallReceiver.RESULT, result)
        context.sendBroadcast(intent)
    }

    /**
     * 重启Socket
     * */
    private fun restartSocket(context: Context) {
        //延时3秒重新开启Socket
        RxUtils.dispose(reNewSocketSubscription)
        reNewSocketSubscription = Observable.timer(5000, TimeUnit.MILLISECONDS)
                .subscribe { init(context, mToken!!) }
    }


    /**
     * 清除监听器
     */
    private fun removeSocketListener() {
        mSocket?.apply {
            Log.i(SocketService.TAG, "socket 清空消息监听器")
            off(SocketEvent.EVENT_CHAT, mChatMessageListener)
            off(SocketEvent.EVENT_CONFLICT, mLoginConflictListener)
            off(Socket.EVENT_CONNECT, mConnectListener)
            off(Socket.EVENT_DISCONNECT, mDisConnectListener)
            off(Socket.EVENT_CONNECT_ERROR, mConnectErrorListener)
            off(Socket.EVENT_CONNECT_TIMEOUT, mConnectTimeOutListener)
            off(Socket.EVENT_ERROR, mDataErrorListener)
        }
    }

    fun testKillSocket() {
        if (BuildConfig.DEBUG) {
            Log.e(SocketService.TAG, "杀死当前进程：${android.os.Process.myPid()}")
            android.os.Process.killProcess(android.os.Process.myPid())
        }
    }

}