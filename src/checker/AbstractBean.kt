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
                            .forEach {
                                if(it.type == Type.OBJECT) {
                                    root = it
                                } else if(it.type == Type.STRING) {
                                    root = it.getKid()
                                }
                            }
                }
            }
            index ++
        }
        return root.value
    }

}