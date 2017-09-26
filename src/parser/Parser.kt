package parser

import log.*
import resource.*
import java.io.BufferedInputStream
import java.io.File
import java.net.URL

class Parser {

    var stream: String = ""
    var pointer = 0
    var length = 0
    var row = 0
    var root: Node = Node(Type.OBJECT, "")
    var error_flag = true

    private val keywords = arrayOf(',', ':', '"', '{', '}', '[', ']', ' ')//white space will be ignored

    init {
        Log.mode(Log.SIMPLE)//使用DETAIL模式
        Log.addUnrecordedLevel(Level.NORMAL)//NORMAL级别的log将不会被记录
        Log.record(Level.INFO, "Parser initialized")
    }

    fun parse(string: String) {
        Log.record(Level.INFO, "input string: $string")
        Log.record(Level.INFO, "start parsing...")
        pointer = 0//confirm pointer location, ready for parsing
        stream = string
        length = stream.length
        while(pointer < length && error_flag) {
            val c = stream[pointer]//get the character at the local pointer
            stateLog(parse(c))
        }
        Log.record(Level.INFO, "parse complete")
    }

    fun parse(file: File) {
        //TODO add method here
    }

    fun parse(url: URL) {
        //TODO
    }

    private fun stateLog(sp: Sp) {
        when(sp.state) {
            State.PARSE_EXPECT_VALUE -> Log.record(Level.ERROR, sp)
            State.PARSE_INVALID_VALUE -> Log.record(Level.ERROR, sp)
            State.PARSE_ROOT_NOT_SINGULAR -> Log.record(Level.WARN, sp)
            State.PARSE_SUCCESS -> Log.record(Level.NORMAL, sp)
        }
    }

    private fun parse(c: Char): Sp {
        pointer++
        return if(c == '{') {
            parseObject()
        } else if(c == '[') {
            parseArray()
        } else if(c == '"') {
            parseString()
        } else if(c == 'f' || c == 't') {
            parseBoolean()
        } else if(Content.isNumber(c)) {//confirm the character is number format
            parseNumber()
        } else if(c == 'n') {
            parseNull()
        } else if(c == '\n') {
            row++
            Sp(State.PARSE_SUCCESS, c)
        } else {
            if(c in keywords) return Sp(State.PARSE_SUCCESS)
            else Sp(State.PARSE_EXPECT_VALUE, c)
        }
    }

    private fun parseObject(): Sp {
        var size = 0
        var point = pointer
        return Sp(State.PARSE_SUCCESS)
    }

    private fun parseArray(): Sp {
        return Sp(State.PARSE_SUCCESS)
    }

    private fun parseString(): Sp {
        var buffer = ""
        var point = pointer
        (point until length)
                .takeWhile { stream[it] != '"' }
                .forEach {
                    buffer += stream[it]
                    point = it
                }
        println(buffer)
        pointer = point + 2//shift the pointer to the location where behind the next quote.
        return Sp(State.PARSE_SUCCESS)
    }

    private fun parseBoolean(): Sp {
        var buffer = ""
        var point = pointer - 1
        (point until length)
                .takeWhile { !Content.isEnd(stream[it]) }
                .forEach {
                    buffer += stream[it]
                    point = it
                }
        println(buffer)
        pointer = point + 1
        return if(buffer == "true" || buffer == "false") Sp(State.PARSE_SUCCESS)
        else Sp(State.PARSE_INVALID_VALUE, "$row $buffer")
    }

    private fun parseNumber(): Sp {
        var buffer = ""
        var point = pointer - 1
        for(i in point until length) {
                if(Content.isEnd(stream[i])) break
                else {
                    buffer += stream[i]
                    point = i
                }
        }
        pointer = point + 1
        println(buffer)
        return if(!Content.isNumber(buffer)) {
            Sp(State.PARSE_INVALID_VALUE, "$row $buffer")
        }
        else {
            Sp(State.PARSE_SUCCESS)
        }
    }

    private fun parseNull(): Sp {
        return Sp(State.PARSE_SUCCESS)
    }

}