package UnitTest

import parser.Parser
import resource.Content

fun main(args: Array<String>) {
    Parser().parse("{\"1\":true, \"key\":0.4e+5}")
}