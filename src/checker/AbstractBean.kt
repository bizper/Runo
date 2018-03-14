package checker

import search.*
import search.ExprType.*
import parser.Node
import parser.Type
import resource.Content

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
                            .forEach {
                                cache = it
                            }
                }
                ARRAY_INDEX -> {
                    cache = if(value.contains("[+\\-*/#]+".toRegex())) {
                        val index = Expr.parseCalExpr(value, cache.children.size)
                        if(index >= cache.children.size || index < 0) Content.getNullNode(cache)
                        else if(cache.children[index].type != Type.STRING) Node(cache, getInsideString(cache.children[index])) else cache.children[index]
                    }
                    else if(cache.children[value.toInt()].type != Type.STRING) Node(cache, getInsideString(cache.children[value.toInt()])) else cache.children[value.toInt()]
                }
                ARRAY_ALL_NODE -> {
                    val buffer = StringBuffer()
                    for(n in cache.children) {
                        buffer.append(
                                if(n.isKid()) n.value
                                else {
                                    when {
                                        n.type == Type.STRING -> "${n.value}:${n.getKid().value}"
                                        n.type == Type.ARRAY -> getInsideString(n)
                                        n.type == Type.OBJECT -> getInsideString(n)
                                        else -> ""
                                    }
                                }
                        )
                        buffer.append(" ")
                    }
                    buffer.deleteCharAt(buffer.lastIndex)
                    cache = Node(cache, Type.STRING, buffer.toString())
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

    fun checkForDoubleArray(key: String): ArrayList<Double> {
        val keys = Expr.parseExpr(key)
        var cache = root
        var list = ArrayList<Double>()
        var index = 0
        for((key, value) in keys) {
            index++
            when(key) {
                ROOT -> while(cache.parent != null) {cache = cache.parent!!}
                CHECK_STRING -> {
                    cache.children
                            .asSequence()
                            .filter { it.value == value }
                            .forEach {
                                if(it.type == Type.OBJECT || it.type == Type.ARRAY) {
                                    if(index == keys.size) {
                                        cache = it
                                        list =  getDoubleArray(cache)
                                    } else {
                                        cache = it
                                    }
                                } else if(it.type == Type.STRING) {
                                    cache = it.getKid()
                                }
                            }
                }
                ARRAY_INDEX -> {
                    cache = if(value.contains("[+\\-*/#]+".toRegex())) {
                        val index = Expr.parseCalExpr(value, cache.children.size)
                        if(index >= cache.children.size || index < 0) Content.getNullNode(cache)
                        else if(cache.children[index].type != Type.STRING) Node(cache, getInsideString(cache.children[index])) else cache.children[index]
                    }
                    else if(cache.children[value.toInt()].type != Type.STRING) Node(cache, getInsideString(cache.children[value.toInt()])) else cache.children[value.toInt()]
                }
            }
        }
        return list
    }

    private fun isDouble(str: String): Boolean {
        return "[0-9]+\\.[0-9]+".toRegex().matches(str)
    }

    private fun getDoubleArray(node: Node): ArrayList<Double> {
        val list = ArrayList<Double>()
        node.children.filter {
            isDouble(it.value)
        }.mapTo(list) {
                    it.value.toDouble()
                }
        return list
    }

    private fun getInsideString(start: Node): String {
        val buffer = StringBuffer()
        buffer.append(start.value)
        buffer.append(":")
        for(n in start.children) {
            buffer.append(if(n.isKid()) n.value else getInsideString(n))
            buffer.append(',')
        }
        buffer.deleteCharAt(buffer.lastIndex)
        return buffer.toString()
    }

}
