package UnitTest

import parser.Parser
import search.Expr

fun main(args: Array<String>) {
    val p = Parser()//创建解析器
    val b = p.parseFile("./test.json", true)
    println(b.check("$[#]"))
}