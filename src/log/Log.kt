package log

import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

class Log {

    companion object {

        val DETAIL = 0x0a
        val SIMPLE = 0x0b

        private val default_format = "z E a hh:mm:ss:SS"
        private val default_simple_format = "HH:mm:ss:SS"
        private val filename_format = "yyyy-MM-dd"

        private var sf = SimpleDateFormat(default_simple_format)
        private var f = File("./src/log/${SimpleDateFormat(filename_format).format(Date())}.txt")
        private var fw = FileWriter(f)

        init {
            if (f.exists().not()) {
                f.createNewFile()
            }
        }

        fun mode(mode: Int) {
            sf = SimpleDateFormat(when(mode) {
                DETAIL -> default_format
                else -> default_simple_format
            })
        }

        fun record(l: Level, state: String) {
            fw.append("${l.name}:[${sf.format(Date())}] $state\n")
            fw.flush()
        }

        fun end() {
            fw.flush()
            fw.close()
        }

    }

}