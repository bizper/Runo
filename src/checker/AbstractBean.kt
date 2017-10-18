package checker

import Expr.*
import parser.Node
import parser.Type

open abstract class AbstractBean {

    lateinit var root: Node

    fun check(key: String): String {
        var index = 0
        var length = key.split(".").size
        for((type, value) in Expr.parseExpr(key)) {
            println("$type $value")
            if(index > length) break
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
                            .filter { it.value == value }
                            .forEach { root = it.children[0] }
                }
            }
            index ++
        }
        return root.value
    }

}