package parser

import log.*
import resource.*
import java.io.BufferedInputStream
import java.io.File
import java.net.URL

class Parser {

    var stream: String = ""
    var pointer = 0
    var row = 0
    var root: Node = Node(Type.OBJECT, "")

    init {

        //Log.recordLevel(Level.WARN)//level above WARN will not be recorded
        Log.record(Level.NORMAL, "start parsing...")
    }

    fun parse(string: String) {
        pointer = 0//confirm pointer location, ready for parsing
        (0 until string.length)//Kotlin style
                .map {
                    string[it]
                }
                .forEach { stateLog(parse(it), it) }
        Log.record(Level.NORMAL, "parse success")
    }

    fun parse(file: File) {

    }

    fun parse(url: URL) {

    }

    private fun stateLog(s: State, c: Char) {
        when(s) {
            State.PARSE_EXPECT_VALUE -> Log.record(Level.ERROR, "${s.name}:$row $c")
            State.PARSE_INVALID_VALUE -> Log.record(Level.ERROR, "${s.name}:$row $c")
            State.PARSE_ROOT_NOT_SINGULAR -> Log.record(Level.WARN, s.name)
            State.PARSE_SUCCESS -> Log.skip()
        }
    }

    private fun parse(c: Char): State {
        return if(c == '{') {
            parseObject()
        /*
        } else if(c == '[') {
            parseArray()
        } else if(c == '"') {
            parseString()
        } else if(c == 'f' || c == 't') {
            parseBoolean()
        } else if(Content.isNumber(c)) {
            parseNumber()
        } else if(c == 'n') {
            parseNull()
        */
        } else if(c == '\n') {
            row++
            State.PARSE_SUCCESS
        } else {
            State.PARSE_EXPECT_VALUE
        }
    }

    private fun parseObject(): State {
        var size = 0
        var point = pointer
        return State.PARSE_INVALID_VALUE
    }

}