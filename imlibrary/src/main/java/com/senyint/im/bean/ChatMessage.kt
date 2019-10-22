package com.senyint.im.bean

import android.os.Parcel
import android.os.Parcelable

class ChatMessage() : Parcelable {

    companion object CREATOR : Parcelable.Creator<ChatMessage> {

        override fun createFromParcel(parcel: Parcel): ChatMessage {
            return ChatMessage(parcel)
        }

        override fun newArray(size: Int): Array<ChatMessage?> {
            return arrayOfNulls(size)
        }

        //聊天类型
        const val TYPE_CHAT = 1
        const val TYPE_CHAT_GROUP = 2
        const val TYPE_CMD = 3

        //聊天相关cmd
        const val CMD_RECEIVE_ACK = 5 //发送消息，服务器端ack响应

        //消息状态
        const val MSG_STATUS_SEND_ING = 0
        const val MSG_STATUS_SEND_SUCCESS = 1
        const val MSG_STATUS_SEND_ERROR = -1

        //语音是否已读
        const val MSG_NOT_READ = 0
        const val MSG_HAS_READ = 1

        //消息体类型
        const val MSG_BODY_TYPE_TEXT = 0 // 文本
        const val MSG_BODY_TYPE_IMAGE = 1 // 图片
        const val MSG_BODY_TYPE_VOICE = 2 // 语音
        const val MSG_BODY_TYPE_CARD = 15 // 病历卡

        const val MSG_BODY_TYPE_SYS = 7 // 系统消息
        const val MSG_BODY_TYPE_ORDER = 8 // 订单消息

        //订单状态
        const val ORDER_INTEND = 10  // 待服务
        const val ORDER_UNDERWAY = 11// 进行中
        const val ORDER_FINISH = 20  // 结束

        //设备类型
        const val DEV_ANDROID = 1
        const val DEV_IOS = 2
        const val DEV_WEB = 3
        const val DEV_PC = 4
        const val DEV_SERVER = 5

        //业务类型
        const val `SERVICE_TYPE_NORMAL` = 1

        //Topic
        const val TOPIC_NORMAL = "simpleChat"

        val Empty = ChatMessage()

        /**是否是IM聊天消息*/
        fun isChatBodyType(bodyType: Int): Boolean {
            return bodyType == MSG_BODY_TYPE_TEXT
                    || bodyType == MSG_BODY_TYPE_IMAGE
                    || bodyType == MSG_BODY_TYPE_VOICE
        }
    }

    var fromId: String = ""
    var toId: String = ""
    var topic: String = ""
    var conversation: String = ""
    var pid: String = ""
    var dev: Int = 0
    var type: Int = 0
    var cmd: Int = 0
    var body: String = ""
    var bodyType: Int = 0
    var time: Long = 0L
    var status: Int = MSG_STATUS_SEND_ING
    var read: Int = MSG_NOT_READ
    var serviceType: Int = SERVICE_TYPE_NORMAL

    constructor(parcel: Parcel) : this() {
        fromId = parcel.readString()
        toId = parcel.readString()
        topic = parcel.readString()
        conversation = parcel.readString()
        pid = parcel.readString()
        dev = parcel.readInt()
        type = parcel.readInt()
        cmd = parcel.readInt()
        body = parcel.readString()
        bodyType = parcel.readInt()
        time = parcel.readLong()
        status = parcel.readInt()
        read = parcel.readInt()
        serviceType = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fromId)
        parcel.writeString(toId)
        parcel.writeString(topic)
        parcel.writeString(conversation)
        parcel.writeString(pid)
        parcel.writeInt(dev)
        parcel.writeInt(type)
        parcel.writeInt(cmd)
        parcel.writeString(body)
        parcel.writeInt(bodyType)
        parcel.writeLong(time)
        parcel.writeInt(status)
        parcel.writeInt(read)
        parcel.writeInt(serviceType)
    }

    override fun describeContents(): Int {
        return 0
    }


}