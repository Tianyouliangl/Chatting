package com.senyint.im.bean

/**
 * Author:Ljb
 * Time:2019/2/26
 * There is a lot of misery in life
 **/
class AckMessage {
    var pid: String = ""
    var type: Int = ChatMessage.TYPE_CMD
    var cmd: Int = ChatMessage.CMD_RECEIVE_ACK
    var fromId: String = ""
    var body: String = ""
}
