package UnitTest

import parser.Parser
import sizable.Sizable

fun main(args: Array<String>) {
    val netfors = test()
    println(Sizable.toJSON("", netfors, 0))
}