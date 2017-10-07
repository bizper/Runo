package unitTest

import parser.Parser

fun main(args: Array<String>) {
    val p = Parser()//创建解析器
    p.parseFile("./test.json")//解析文件
}