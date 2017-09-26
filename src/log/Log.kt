package log

import resource.Sp
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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
        private var limit = 4
        private var limit_array = ArrayList<Int>()

        init {
            if (f.exists().not()) {
                f.createNewFile()
            }
        }

        fun mode(mode: Int) {
            when(mode) {
                DETAIL -> sf = SimpleDateFormat(default_format)
                SIMPLE -> sf = SimpleDateFormat(default_simple_format)
                else -> Log.record(Level.WARN, "NO ALLOWED MODE")
            }

        }
        /**
         * level above this appoint level will not be recorded.
         */
        fun recordLevel(level: Int) {
            limit = level
        }
        /**
         * add level in a blacklist, level in this list will be not recorded
         */
        fun addUnrecordedLevel(vararg level: Int) {
            for(i in level) {
                limit_array.add(i)
            }
        }

        fun record(level: Int, state: String) {
            if(level > limit || level in limit_array) return//check level list
            fw.append("${Level.getTypeName(level)}:[${sf.format(Date())}] $state\n")
            fw.flush()
        }

        fun record(level: Int, sp: Sp) {
            if(level > limit || level in limit_array) return
            fw.append("${Level.getTypeName(level)}:[${sf.format(Date())}] ${sp.state} ${sp.string}\n")
            fw.flush()
        }

        fun end() {
            fw.flush()
            fw.close()
        }

    }

}