package objects

import objects.error.TypeErrorException
import parser.Node

class JSONString: JSONBase {

    private var str: String

    constructor(str: String) {
        this.str = str
    }

    constructor(n: Node): this(n.value)

    override fun toArray(): Array<JSONBase> {
        throw TypeErrorException("string can not cast to Array")
    }

    override fun toInt(): Int {
        return str.toInt()
    }

    override fun toDouble(): Double {
        return str.toDouble()
    }

    override fun toString(): String {
        return str
    }

    override fun toBoolean(): Boolean {
        return str.toBoolean()
    }

    override fun toNull(): String {
        throw TypeErrorException("string can not cast to Null")
    }

    override fun forEach(action: (JSONBase) -> Unit) {
        action(JSONString(str))
    }

}