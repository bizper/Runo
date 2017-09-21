package UnitTest

import parser.Parser
import resource.Content

fun main(args: Array<String>) {
    Parser().parse("{\"key\":123,\n\"no\":false}")
}