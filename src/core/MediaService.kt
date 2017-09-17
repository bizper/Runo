package core

import log.Log
import log.Level
import resource.Content
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.ServerSocket
import java.net.Socket

class MediaService {

    private val log = Log()//创建日志类，默认使用SIMPLE模式
    val ss = ServerSocket(Content.DEFAULT_PORT)
    val socket_list = HashMap<Int, Socket>()
    var count = 0

    init {
        log.record(Level.NORMAL, "service start....")
        log.record(Level.NORMAL, "service port: ${Content.DEFAULT_PORT}")
        socThread().start()
    }


    inner class socThread: Thread() {
        override fun run() {
            while(true) {
                val s = ss.accept()
                socket_list.put(count, s)
                msgThread(s)
                count++
                log.record(Level.NORMAL, "client info:${s.localAddress}")
                log.record(Level.NORMAL, "connected client:$count")
            }
        }
    }

    inner class msgThread(s: Socket): Thread() {

        var ips = s.getInputStream()
        var ops = s.getOutputStream()

        init {
            Thread(this).start()
        }

        override fun run() {
            while(true) {
                var dis = DataInputStream(ips)
                var dos = DataOutputStream(ops)
                val string = dis.readUTF()
                println(string)
                dos.writeUTF("SUCCESS")
                dos.flush()
                dis.close()
                dos.close()
            }
        }
    }


}