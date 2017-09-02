package log

import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

fun main(args: Array<String>) {
    var l = Log(Log.SIMPLE)
    l.record(Level.WARNING, "this is a test")
    l.record(Level.NORMAL, "this is a test")
    l.record(Level.ERROR, "this is a test")
    l.end()
}

class Log {

    companion object {
        val DETAIL = 0x0a
        val SIMPLE = 0x0b
    }

    private val model = "%s:[%s] %s\n"
    private val default_format = "z E a hh:mm:ss:SS"
    private val default_simple_format = "HH:mm:ss"
    private val filename_format = "yyyy-MM-dd"
    private var sf: SimpleDateFormat = SimpleDateFormat(filename_format)

    private var f: File = File(String.format("./src/log/%s.txt", sf.format(Date())))
    private var fw: FileWriter? = null

    constructor() {
        sf = SimpleDateFormat(default_format)
        init()
    }

    constructor(format: String): this() {
        sf = SimpleDateFormat(format)
        init()
    }

    constructor(st: Int) {
        when(st) {
            DETAIL ->  sf = SimpleDateFormat(default_format)
            SIMPLE -> sf = SimpleDateFormat(default_simple_format)
        }
        init()
    }

    private fun init() {
        if (f.exists().not()) {
            f.createNewFile()
        }
        fw = FileWriter(f)
    }

    fun record(l: Level, state: String) {
        fw?.append(String.format(model, l.name, sf.format(Date()), state))
    }

    fun end() {
        fw?.flush()
        fw?.close()
    }

}