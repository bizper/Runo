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
                    cache = if(value.contains("[+\\-*/#]+".toRegex())) {
                        val index = Expr.parseCalExpr(value, cache.children.size)
                        if(index >= cache.children.size) getNullNode(cache)
                        else cache.children[index]
                    }
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

    private fun getNullNode(parent: Node): Node = Node(parent, "Null")

}