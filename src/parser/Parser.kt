package parser

import log.*
import log.Log.Level
import resource.*
import java.io.File
import java.net.URL

class Parser {

    enum class JSONType {
        JSON,
        FILE,
        URL
    }

    companion object {
        val JSON = JSONType.JSON
        val FILE = JSONType.FILE
        val URL = JSONType.URL
    }

    private var stream: String = ""
    private var pointer = 0
    private var length = 0
    private var root = Node(Type.OBJECT_ROOT, "root")

    private val keywords = arrayOf(' ', '{', '[', '}', ']', ',', '\n', ':', '"', '\r')

    private var valueFlag = false
    private var logFlag = true

    constructor() {
        if(logFlag) {
            Log.record(Level.INFO, "PARSER INITIALIZED")
        }
    }

    constructor(flag: Boolean): this() {
        this.logFlag = flag
    }

    constructor(type: JSONType, path: String): this(type, path, false)

    constructor(type: JSONType, path: String, flag: Boolean): this() {
        when(type) {
            FILE -> parseFile(path, flag)
            JSON -> parse(path, flag)
            URL -> parse(URL(path))
            else -> Log.record(Level.ERROR, "type error")
        }
    }

    fun getBean() = root

    private fun parse(string: String, isDebug: Boolean) {
        if(logFlag) {
            Log.record(Level.INFO, "input string: $string")
            Log.record(Level.INFO, "start parsing...")
        }
        pointer = 0//confirm pointer location, ready for parsing
        stream = string.trim()
        length = stream.length
        while(pointer < length) {
            stateLog(parse(stream[pointer]))
        }
        if(isDebug) print(root, 0)
        if(logFlag) Log.record(Level.INFO, "parse complete")
    }

    fun parseFile(path: String) {
        return parseFile(path, false)
    }

    private fun parseFile(path: String, isDebug: Boolean) {
        val file = File(path)
        if(logFlag) {
            Log.info("input file: ${file.canonicalPath}")
            Log.info("start parsing...")
        }
        pointer = 0
        stream = file.readText()
        length = stream.length
        while(pointer < length) {
            stateLog(parse(stream[pointer]))
        }
        if(isDebug) print(root, 0)
        if(logFlag) Log.info("parse complete")
    }

    private fun parse(url: URL) {
        //TODO
    }

    private fun stateLog(sp: Sp) {
        when(sp.state) {
            State.PARSE_NOT_EXPECT_VALUE -> if(logFlag) Log.record(Level.ERROR, sp) else println(sp)
            State.PARSE_INVALID_VALUE -> if(logFlag) Log.record(Level.ERROR, sp) else println(sp)
            State.PARSE_ROOT_NOT_SINGULAR -> if(logFlag) Log.record(Level.WARN, sp) else println(sp)
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
        } else if(c == ',') {
            parseComma()
        } else {
            if(c in keywords) return Sp(State.PARSE_SUCCESS, c)
            else Sp(State.PARSE_NOT_EXPECT_VALUE, c)
        }
    }

    private fun parseObject(): Sp {//解析对象
        var point = pointer
        if(root.type == Type.ARRAY) {
            val node = Node(root, Type.OBJECT, "")
            root.add(node)
            root = node
        }
        if(root.type == Type.STRING) root.type = Type.OBJECT
        valueFlag = false
        var i = point
        while(true) {
            if(stream[i] == '}') {
                point = pointer
                break
            }
            stateLog(parse(stream[i]))
            i = pointer
        }
        pointer = point + 1
        root = if(root.parent == null) root else root.parent!!
        return Sp(State.PARSE_SUCCESS, Type.OBJECT)
    }

    private fun parseArray(): Sp {//解析数组
        var point = pointer
        if(root.type == Type.ARRAY) {
            val node = Node(root, Type.ARRAY, "")
            root.add(node)
            root = node
        }
        if(root.type == Type.STRING) root.type = Type.ARRAY
        val cache = root
        var i = point
        while(true) {
            if(stream[i] == ']') {
                point = pointer
                break
            }
            valueFlag = true//add buffer as value in array
            root = cache
            stateLog(parse(stream[i]))
            i = pointer
        }
        pointer = point + 1
        root = if(root.parent == null) root else root.parent!!
        valueFlag = false//reset value flag
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
        if(!valueFlag) {
            val node = Node(root, Type.STRING, buffer.trim())
            root.add(node)
            root = node
            valueFlag = !valueFlag
        } else {
            val node = Node(root, Type.STRING, buffer.trim())
            root.add(node)
            root = if(root.parent == null) root else root.parent!!
            valueFlag = !valueFlag
        }
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
        if(!valueFlag) {
            valueFlag = !valueFlag
            return Sp(State.PARSE_INVALID_VALUE, Type.BOOLEAN, "${buffer.trim()} can not be key")
        } else {
            val node = Node(root, Type.BOOLEAN, buffer.trim())
            root.add(node)
            root = if(root.parent == null) root else root.parent!!
            valueFlag = !valueFlag
        }
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
        buffer = buffer.trim()
        if(!valueFlag) {
            valueFlag = !valueFlag
            return Sp(State.PARSE_INVALID_VALUE, Type.NUMBER, "$buffer can not be key")
        } else {
            val node = Node(root, Type.NUMBER, buffer)
            root.add(node)
            root = if(root.parent == null) root else root.parent!!
            valueFlag = !valueFlag
        }
        pointer = point + 1
        return if(!Content.isNumber(buffer)) {//校验数字格式是否正确
            Sp(State.PARSE_INVALID_VALUE, Type.NUMBER, buffer)
        }
        else {
            Sp(State.PARSE_SUCCESS, Type.NUMBER, buffer)
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
        if(!valueFlag) {
            valueFlag = !valueFlag
            return Sp(State.PARSE_INVALID_VALUE, Type.NULL, "${buffer.trim()} can not be key")
        } else {
            val node = Node(root, Type.NULL, buffer.trim())
            root.add(node)
            root = if(root.parent == null) root else root.parent!!
            valueFlag = !valueFlag
        }
        pointer = point + 1
        return if(buffer == "null") Sp(State.PARSE_SUCCESS, Type.NULL, buffer.trim())
        else Sp(State.PARSE_INVALID_VALUE, Type.NULL, buffer)
    }

    private fun print(node: Node, height: Int) {
        (0..height).forEach {
            if(it >= 1) print("\t")
        }
        print("|--<${node.type}${if(node.value == "") "" else " "}${node.value}>\n")
        if(node.children.size != 0) {
            for(n in node.children) {
                print(n, height + 1)
            }
        }
    }

    private fun parseComma(): Sp {
        var point = pointer
        var c = stream[point]
        while(Content.isWhiteSpace(c)) {
            point ++
            c = stream[point]
        }
        return if(Content.isEnd(stream[pointer])) {
            Sp(State.PARSE_INVALID_VALUE, stream[pointer])
        } else {
            Sp(State.PARSE_SUCCESS, stream[pointer])
        }
    }

}
