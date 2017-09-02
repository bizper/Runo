package core

import log.*
import java.net.ServerSocket

class MediaService {

    val log = Log(Log.SIMPLE)//创建日志类，使用DETAIL模式
    //val ss = ServerSocket(8080)

    init {
        log.record(Level.NORMAL, "service start....")
        log.record(Level.NORMAL, "service port: 8080")
    }


}