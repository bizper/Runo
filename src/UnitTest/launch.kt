package UnitTest

import parser.Parser

fun main(args: Array<String>) {
    val p = Parser()//创建解析器
    val b = p.parseFile("./test.json", true)
    println(b.check("$.name[2].plugin_version"))
}