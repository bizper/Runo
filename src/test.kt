import parser.Parser
import parser.Parser.JSONType

fun main(args: Array<String>) {
    Parser(JSONType.FILE, "./test.json", true)
}