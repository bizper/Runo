package objects

import objects.error.TypeErrorException
import parser.Node

class JSONBoolean: JSONBase {

    private var flag: Boolean

    constructor(flag: Boolean) {
        this.flag = flag
    }

    constructor(n: Node): this(n.value.toBoolean())

    override fun toInt(): Int {
        throw TypeErrorException("boolean can not cast to Int")
    }

    override fun toDouble(): Double {
        throw TypeErrorException("boolean can not cast to Double")
    }

    override fun toString(): String {
        return flag.toString()
    }

    override fun toBoolean(): Boolean {
        return flag
    }

    override fun toNull(): String {
        throw TypeErrorException("boolean can not cast to Null")
    }

    override fun toArray(): Array<JSONBase> {
        throw TypeErrorException("boolean can not cast to Array")
    }


}