package UnitTest

import parser.Parser

fun main(args: Array<String>) {
    val b = Parser("file", "./test.json", true).getBean()
    println(b.check("$.test_array[3]"))
}