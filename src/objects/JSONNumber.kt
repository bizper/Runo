package objects

import objects.error.TypeErrorException
import parser.Node

class JSONNumber: JSONBase {

    private val num: Number

    constructor(num: Number) {
        this.num = num
    }

    constructor(str: String) {
        this.num = str.toDouble()
    }

    constructor(n: Node): this(n.value)

    override fun toArray(): Array<JSONBase> {
        throw TypeErrorException("number can not cast to Array")
    }

    override fun toInt(): Int {
        return num.toInt()
    }

    override fun toDouble(): Double {
        return num.toDouble()
    }

    override fun toString(): String {
        return num.toString()
    }

    override fun toBoolean(): Boolean {
        throw TypeErrorException("number can not cast to Boolean")
    }

    override fun toNull(): String {
        throw TypeErrorException("number can not cast to Null")
    }

    override fun forEach(action: (JSONBase) -> Unit) {
        action(JSONNumber(num))
    }

}