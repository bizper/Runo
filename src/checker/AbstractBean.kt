package checker

import objects.*
import parser.Node
import parser.Type
import parser.Type.*
import search.Expr
import search.ExprType
import search.ExprType.*

abstract class AbstractBean {

    lateinit var root: Node

    fun check(str: String): Node {
        val list = Expr.parseExpr(str)
        var cache = root
        list.forEach {
            var node = it
            when(node.type) {
                ROOT -> if(cache.parent != null) cache = cache.parent!!
                CHECK_STRING -> {
                    cache.getChildren().filter {
                        it.value == node.value
                    }.forEach {
                        cache = it
                    }
                }
                ExprType.ARRAY -> {
                    cache.getChildren().filter {
                        it.value == node.value && it.type == Type.ARRAY
                    }.forEach {
                        cache = it
                    }

                }
                ARRAY_INDEX -> {
                    cache = cache.getChildren()[node.value.toInt()]
                }
            }
        }
        return cache
    }

    fun checkForNumber(str: String): JSONNumber {
        return JSONNumber(check(str).value)
    }

    fun checkForArray(str: String): JSONArray {
        return JSONArray(check(str))
    }

    fun checkForBoolean(str: String): JSONBoolean {
        return JSONBoolean(check(str))
    }

    fun checkForString(str: String): JSONString {
        return JSONString(check(str))
    }

    fun checkForObject(str: String): JSONObject {
        return JSONObject(check(str))
    }

}
