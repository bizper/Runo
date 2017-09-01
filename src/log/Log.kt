package log

import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*
import java.io.IOException

fun main(args: Array<String>) {
    var l = Log()
    l.record(Level.NORMAL, "this is a test")
    l.close()
}

class Log {

    private val model = "%s:[%s] %s\n"
    private val default_format = "yyyy-MM-dd HH:mm:ss:SS"
    private val filename_format = "yyyy-MM-dd"
    private var sf: SimpleDateFormat = SimpleDateFormat(filename_format)

    private var f: File? = null
    private var fw: FileWriter? = null

    constructor() {
        f = File(String.format("./src/log/%s.txt", sf.format(Date())))
        sf = SimpleDateFormat(default_format)
        init()
    }

    constructor(format: String) {
        f = File(String.format("./src/log/%s.txt", sf.format(Date())))
        sf = SimpleDateFormat(format)
        init()
    }

    private fun init() {
        if (!f!!.exists()) {
            f?.createNewFile()
        }
        fw = FileWriter(f)
    }

    fun record(l: Level, state: String) {
        try {
            fw?.append(String.format(model, l.name, sf.format(Date()), state))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun close() {
        try {
            fw?.flush()
            fw?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }



}