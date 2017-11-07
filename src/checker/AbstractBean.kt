package checker

import Expr.*
import Expr.ExprType.*
import parser.Node
import parser.Type

open abstract class AbstractBean {

    lateinit var root: Node

    fun check(key: String): String {
        val keys = Expr.parseExpr(key)
        for((type, value) in keys) {
            when(type) {
                ROOT -> while(root.parent != null) {root = root.parent!!}
                ARRAY -> {
                    root.children
                            .asSequence()
                            .filter { it.value == value && it.type == Type.ARRAY}
                            .forEach { root = it }
                }
                ARRAY_INDEX -> {
                    root = root.children[value.toInt()]
                }
                CHECK_STRING -> {
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
        }
        return root.value
    }

}