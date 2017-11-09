package checker

import search.*
import search.ExprType.*
import parser.Node
import parser.Type

open abstract class AbstractBean {

    lateinit var root: Node

    fun check(key: String): String {
        val keys = Expr.parseExpr(key)
        var cache = root
        for((type, value) in keys) {
            when(type) {
                ROOT -> while(cache.parent != null) {cache = cache.parent!!}
                ARRAY -> {
                    cache.children
                            .asSequence()
                            .filter { it.value == value && it.type == Type.ARRAY}
                            .forEach { cache = it }
                }
                ARRAY_INDEX -> {
                    cache = if(value.contains("[+\\-*/#]+".toRegex())) cache.children[Expr.parseCalExpr(value, cache.children.size)]
                    else cache.children[value.toInt()]
                }
                LENGTH -> {
                    cache.value = (if(cache.type == Type.ARRAY) cache.children.size else cache.value.length).toString()
                }
                CHECK_STRING -> {
                    cache.children
                            .asSequence()
                            .filter { it.value == value }
                            .forEach {
                                if(it.type == Type.OBJECT || it.type == Type.ARRAY) {
                                    cache = it
                                } else if(it.type == Type.STRING) {
                                    cache = it.getKid()
                                }
                            }
                }
            }
        }
        return cache.value
    }

}