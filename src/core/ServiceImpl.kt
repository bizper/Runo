package core

import java.net.ServerSocket
import java.net.Socket

fun main(args: Array<String>) {
    var ss: SerSoc = SerSoc(8080)

    var s: Soc = Soc("127.0.0.1", 8080)

}

class SerSoc(port: Int) {

    var list = ArrayList<Socket>()

    var ss: ServerSocket = ServerSocket(port)

    init {
        val t: timer = timer()
        t.start()
    }

    inner class timer: Thread() {
        override fun run() {
            while (true) {
                var s: Socket = ss.accept()
                println("New Client connect, Address: ${s.inetAddress}")
                list.plus(s)
            }
        }
    }

}

class Soc(host: String?, port: Int) {

    init{
        var s = Socket(host, port)
    }
}