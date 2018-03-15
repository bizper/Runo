import parser.Parser

fun main(args: Array<String>) {
    println(Parser(Parser.FILE, "./test.json", true).getBean().checkForArray("$.first_array[0]"))
}