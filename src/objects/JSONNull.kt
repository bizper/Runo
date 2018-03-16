package objects

import objects.error.TypeErrorException

class JSONNull: JSONBase {

    override fun toArray(): Array<JSONBase> {
        throw TypeErrorException("null can not cast to Array")
    }

    override fun toInt(): Int {
        throw TypeErrorException("null can not cast to Int")
    }

    override fun toDouble(): Double {
        throw TypeErrorException("null can not cast to Double")
    }

    override fun toString(): String {
        return "null"
    }

    override fun toBoolean(): Boolean {
        throw TypeErrorException("null can not cast to Boolean")
    }

    override fun toNull(): String {
        return "null"
    }

    override fun forEach(action: (JSONBase) -> Unit) {
        throw TypeErrorException("Null don't have any elements for action")
    }

}