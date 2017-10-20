package checker

import Expr.*
import parser.Node
import parser.Type

open abstract class AbstractBean {

    lateinit var root: Node

    fun check(key: String): String {
        var index = 0
        var keys = Expr.parseExpr(key)
        var length = key.split(".").size
        for((type, value) in keys) {
            if(index > length) break
            println("$type $value")
            println("${root.type} ${root.value}")
            when(type) {
                ExprType.ROOT -> while(root.parent != null) {root = root.parent!!}
                ExprType.ARRAY -> {
                    root.children
                            .asSequence()
                            .filter { it.value == value && it.type == Type.ARRAY}
                            .forEach { root = it }
                }
                ExprType.ARRAY_INDEX -> {
                    root = root.children[value.toInt()]
                }
                ExprType.CHECK_STRING -> {
                    root.children
                            .asSequence()
                            .filter { it.value == value && (it.type == Type.STRING || it.type == Type.OBJECT) }
                            .forEach {
                                println(it.value)
                                root = it.getKid()
                            }
                }
            }
            index ++
            println("${root.type} ${root.value}")
            println("*************************")
        }
        return root.value
    }

}