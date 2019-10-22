package com.senyint.im.utils

import android.widget.TextView
import com.senyint.im.bean.AckMessage
import com.senyint.im.bean.BodyTxt
import com.senyint.im.bean.ChatMessage
import com.senyint.im.listener.Constant
import java.util.*


object ChatUtils {

    /**
     * 生成消息唯一标识
     * */
    fun getPid(): String {
        val stringBuffer = StringBuilder()
        val time = System.currentTimeMillis().toString()
        stringBuffer.append("AN")
                .append(getRandomInt())
                .append(getRandomString())
                .append(getRandomInt())
                .append(getRandomString())
                .append(getRandomInt())
                .append(time.substring(time.length - 4, time.length))
        return stringBuffer.toString()
    }

    /**
     * 随机产生一个4个字节的int
     */
    fun getRandomInt(): Int {
        val min = 10
        val max = 99
        val random = Random()
        return random.nextInt(max - min + 1) + min
    }

    /**
     * 随机产生字符串
     */
    fun getRandomString(): String {
        val str = "abcdefghigklmnopkrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ0123456789"
        val random = Random()
        val sf = StringBuffer()
        for (i in 0..1) {
            val number = random.nextInt(62)// 0~61
            sf.append(str[number])

        }
        return sf.toString()
    }


    /**
     * 生成会话
     */
    fun createConversation(topic: String, fromUid: String, toUid: String): String {
        val str = arrayOf(fromUid, toUid, topic)
        Arrays.sort(str)
        val buf = StringBuilder()
        for (s in str) {
            buf.append(s)
        }
        return EncodeMD5.hash(buf.toString())
    }

    /**
     * 创建一条聊天消息
     * */
    fun createChatMessage(topic: String, fromId: String, toId: String, bodyType: Int, body: String, serviceType: Int): ChatMessage {
        val chatMessage = ChatMessage()
        chatMessage.fromId = fromId
        chatMessage.toId = toId
        chatMessage.topic = topic
        chatMessage.conversation = createConversation(topic, fromId, toId)
        chatMessage.pid = getPid()
        chatMessage.type = ChatMessage.TYPE_CHAT
        chatMessage.bodyType = bodyType
        chatMessage.body = body
        chatMessage.status = ChatMessage.MSG_STATUS_SEND_ING
        chatMessage.time = System.currentTimeMillis()
        chatMessage.dev = ChatMessage.DEV_ANDROID
        chatMessage.serviceType = serviceType
        return chatMessage
    }

    /**
     * 复制一条消息
     * */
    fun copyChatMessage(chatMessage: ChatMessage): ChatMessage {
        return JsonParser.fromJsonObj(JsonParser.toJson(chatMessage), ChatMessage::class.java)
    }

    /**
     * 设置body到TextView
     * */
    fun setTextViewBody(tv: TextView, chatMessage: ChatMessage) {
        when (chatMessage.bodyType) {
            ChatMessage.MSG_BODY_TYPE_TEXT -> {
//                val bodyTxt = JsonParser.fromJsonObj(chatMessage.body, BodyTxt::class.java)
//                EmoticonFilterUtils.spannableEmoticonFilter(tv, bodyTxt.text)
            }
            ChatMessage.MSG_BODY_TYPE_VOICE -> {
                tv.text = "[语音]"
            }
            ChatMessage.MSG_BODY_TYPE_IMAGE -> {
                tv.text = "[图片]"
            }
            else -> {
                tv.text = "[未知消息]"
            }
        }
    }

    /**
     * 获取body对应string
     * */
    fun getBodyStr(chatMessage: ChatMessage): String {
        return when (chatMessage.bodyType) {
            ChatMessage.MSG_BODY_TYPE_TEXT -> {
                JsonParser.fromJsonObj(chatMessage.body, BodyTxt::class.java).text
            }
            ChatMessage.MSG_BODY_TYPE_VOICE -> {
                "[语音]"
            }
            ChatMessage.MSG_BODY_TYPE_IMAGE -> {
                "[图片]"
            }
            else -> {
                "[未知消息]"
            }
        }
    }

    /**
     * ack命令
     * */
    fun getAck(chatMessage: ChatMessage): String {
        val ackMessage = AckMessage()
        ackMessage.pid = getPid()
        ackMessage.type = ChatMessage.TYPE_CMD
        ackMessage.cmd = ChatMessage.CMD_RECEIVE_ACK
        ackMessage.body = chatMessage.pid
        ackMessage.fromId = chatMessage.toId
        return JsonParser.toJson(ackMessage)
    }


    fun formatNewNum(num: Int): String {
        return if (num > 99) {
            "99+"
        } else {
            num.toString()
        }
    }

    var SOCKET_SERVICE_ID = "100000"

    fun getSysMsgConversation(): String {
        return createConversation(ChatMessage.MSG_BODY_TYPE_SYS.toString(),  Constant.uid, SOCKET_SERVICE_ID)
    }

    fun getOrderMsgConversation(): String {
        return createConversation(ChatMessage.MSG_BODY_TYPE_ORDER.toString(), Constant.uid, SOCKET_SERVICE_ID)
    }

}