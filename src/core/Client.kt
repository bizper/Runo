package core

import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

class Client {

    init {
        val s = Socket("127.0.0.1", 8080)
        msgThread(s)
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
                dos.writeUTF("SUCCESS")
                dos.flush()
                val string = dis.readUTF()
                println(string)
                dis.close()
                dos.close()
            }
        }
    }

}