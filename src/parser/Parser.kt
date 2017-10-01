package parser

import log.*
import resource.*
import java.io.BufferedInputStream
import java.io.File
import java.net.URL

class Parser {

    private var stream: String = ""
    private var pointer = 0
    private var length = 0
    private var root: Node = Node(Type.OBJECT, "")

    private var list = ArrayList<String>()

    private val keywords = arrayOf(',', ':', '"', '{', '}', '[', ']', ' ', '\n', '\r')//white space will be ignored

    init {
        Log.mode(11)//使用SIMPLE模式
        Log.addUnrecordedLevel(Level.NORMAL)//NORMAL级别的log将不会被记录
        Log.record(Level.INFO, "Parser initialized")
    }

    fun parse(string: String) {
        Log.record(Level.INFO, "input string: $string")
        Log.record(Level.INFO, "start parsing...")
        pointer = 0//confirm pointer location, ready for parsing
        stream = string
        length = stream.length
        while(pointer < length) {
            val c = stream[pointer]//get the character at the local pointer
            stateLog(parse(c))
        }
        Log.record(Level.INFO, "parse complete")
    }

    fun parseFile(path: String) {
        val file = File(path)
        Log.record(Level.INFO, "input file: ${file.canonicalPath}")
        Log.record(Level.INFO, "start parsing")
        pointer = 0
        stream = file.readText()
        length = stream.length
        while(pointer < length) {
            val c = stream[pointer]//get the character at the local pointer
            stateLog(parse(c))
        }
        print(root!!, 0)
        Log.record(Level.INFO, "parse complete")
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

    private fun parse(c: Char): Sp {//分派器
        pointer++//此处指针值比char所在的值增加了1
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
        } else {
            if(c in keywords) return Sp(State.PARSE_SUCCESS, Type.KEYWORDS, c)
            else Sp(State.PARSE_EXPECT_VALUE, c)
        }
    }

    private fun parseObject(): Sp {//解析对象
        var point = pointer
        root = Node(Type.OBJECT, "")
        while(true) {
            var i = point
            if(stream[i] == '}') {
                break
            }
            val s = parse(stream[i])
            i = pointer
            point = i
            return s
        }
        pointer = point + 1
        return Sp(State.PARSE_SUCCESS, Type.OBJECT)
    }

    private fun parseArray(): Sp {//解析数组
        var point = pointer
        while(true) {
            var i = point
            if(stream[i] == ']') {
                break
            }
            val s = parse(stream[i])
            i = pointer
            point = i
            return s
        }
        pointer = point + 1
        return Sp(State.PARSE_SUCCESS, Type.ARRAY)
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
        list.add(buffer.trim())
        pointer = point + 2//shift the pointer to the location where behind the next quote.
        return Sp(State.PARSE_SUCCESS, buffer)
    }

    private fun parseBoolean(): Sp {//解析布尔值
        var buffer = ""
        var point = pointer - 1
        (point until length)
                .takeWhile { !Content.isEnd(stream[it]) }
                .forEach {
                    if(!Content.isWhiteSpace(stream[it])) {
                        buffer += stream[it]
                        point = it
                    }
                }
        list.add(buffer.trim())
        pointer = point + 1
        return if(buffer == "true" || buffer == "false") {
            Sp(State.PARSE_SUCCESS, Type.BOOLEAN, buffer.trim())
        }
        else {
            Sp(State.PARSE_INVALID_VALUE, Type.BOOLEAN, buffer)
        }
    }

    private fun parseNumber(): Sp {//解析数字
        var buffer = ""
        var point = pointer - 1
        for(i in point until length) {
                if(Content.isEnd(stream[i])) break
                else if(!Content.isWhiteSpace(stream[i])){
                    buffer += stream[i]
                    point = i
                }
        }
        list.add(buffer.trim())
        pointer = point + 1
        return if(!Content.isNumber(buffer)) {//校验数字格式是否正确
            Sp(State.PARSE_INVALID_VALUE, Type.NUMBER, buffer)
        }
        else {
            Sp(State.PARSE_SUCCESS, Type.NUMBER, buffer.trim())
        }
    }

    private fun parseNull(): Sp {//解析空值
        var buffer = ""
        var point = pointer - 1
        (point until length)
                .takeWhile { !Content.isEnd(stream[it]) }
                .forEach {
                    if(!Content.isWhiteSpace(stream[it])) {
                        buffer += stream[it]
                        point = it
                    }
                }
        list.add(buffer.trim())
        pointer = point + 1
        return if(buffer == "null") Sp(State.PARSE_SUCCESS, Type.NULL, buffer.trim())
        else Sp(State.PARSE_INVALID_VALUE, Type.NULL, buffer)
    }

    fun print() {
        for(s in list) println(s)
    }

    fun print(node: Node, height: Int) {
        (0..height).forEach {
            print(" ")
        }
        print("|--<${node.type} ${node.value}>\n")
        if(node.children.size != 0) {
            for(n in node.children) {
                print(n, height + 1)
            }
        }
    }

}