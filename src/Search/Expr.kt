package search

import search.ExprType.*
import java.util.*

object Expr {

    private val       root_name = "root"
    private val     root_symbol = "$"
    private val    current_node = "@"
    private val  element_length = "#"
    private val     array_regex = "([\\w\\W]+)\\[([\\w#\\-+*/]+)]".toRegex()

    fun parseExpr(expr: String): ArrayList<ExprPack> {
        var command = ArrayList<ExprPack>()
        var exprList = expr.split(".")
        outer@for(i in exprList) {
            when(true) {
                i == root_symbol -> command.add(ExprPack(ROOT, root_name))
                i.matches(array_regex) -> {
                    val arrIndex = parseArrayIndex(i)
                    command.add(ExprPack(ARRAY, arrIndex[0]))
                    command.add(ExprPack(ARRAY_INDEX, arrIndex[1]))
                }
                i == element_length -> {
                    command.add(ExprPack(LENGTH, element_length))
                    break@outer
                }
                else -> command.add(ExprPack(CHECK_STRING, i))
            }
        }
        return command
    }

    private fun parseArrayIndex(string: String): Array<String> =
        parseCalIndex(array_regex.findAll(string).toList().flatMap(MatchResult::groupValues).toTypedArray())


    private fun parseCalIndex(list: Array<Any>): Array<String> =
        arrayOf(list[1].toString(), list[2].toString())


    fun parseCalExpr(string: String, length: Int): Int {
        var result = 0
        val stack = ArrayList<Char>()
        val num = ArrayList<Int>()
        var buffer = StringBuffer()
        for(c in string) {
            when(c) {
                '#' -> num.add(length)
                in '0'..'9' -> {
                    buffer.append(c)
                }
                '+' -> {
                    if(!buffer.isBlank()) {
                        num.add(buffer.toString().toInt())
                        buffer.delete(0, buffer.length)
                    }
                    stack.add(c)
                }
                '-' -> {
                    if(!buffer.isBlank()) {
                        num.add(buffer.toString().toInt())
                        buffer.delete(0, buffer.length)
                    }
                    stack.add(c)
                }
                '*' -> {
                    if(!buffer.isBlank()) {
                        num.add(buffer.toString().toInt())
                        buffer.delete(0, buffer.length)
                    }
                    stack.add(c)
                }
                '/' -> {
                    if(!buffer.isBlank()) {
                        num.add(buffer.toString().toInt())
                        buffer.delete(0, buffer.length)
                    }
                    stack.add(c)
                }
            }
        }
        if(!buffer.isBlank()) {
            num.add(buffer.toString().toInt())
            buffer.delete(0, buffer.length)
        }
        result += num[0]
        for(i in 0 until num.size - 1) {
            when(stack[i]) {
                '+' -> result += num[i+1]
                '-' -> result -= num[i+1]
                '*' -> result *= num[i+1]
                '/' -> result /= num[i+1]
                else -> {}
            }
        }
        return result
    }

}
