package objects

import objects.error.TypeErrorException
import parser.Node
import parser.Type.*
import java.util.*

class JSONObject: JSONBase {

    val table = Hashtable<JSONBase, JSONBase>()

    constructor(n: Node) {
        if(n.type != OBJECT) throw TypeErrorException("non-object node can not cast to JSONObject")
        n.children.forEach {
            when(it.type) {
                OBJECT -> table[JSONString(it.value)] = JSONObject(it)
                ARRAY -> table[JSONString(it.value)] = JSONArray(it)
                STRING -> table[JSONString(it.value)] = getRightJSONObject(it.getKid())
            }
        }
    }

    override fun toArray(): Array<JSONBase> {
        return table.keys.toTypedArray()
    }

    override fun toInt(): Int {
        throw TypeErrorException("object can not cast to int")
    }

    override fun toDouble(): Double {
        throw TypeErrorException("object can not cast to double")
    }

    override fun toString(): String {
        val sb = StringBuffer()
        table.forEach {
            sb.append(it.key.toString()).append(":").append(it.value).appendln()
        }
        return sb.toString()
    }

    override fun toBoolean(): Boolean {
        throw TypeErrorException("object can not cast to boolean")
    }

    override fun toNull(): String {
        throw TypeErrorException("object can not cast to null")
    }

    private fun getRightJSONObject(n: Node): JSONBase {
        return when(n.type) {
            STRING -> JSONString(n.value)
            OBJECT -> JSONObject(n)
            ARRAY -> JSONArray(n)
            NUMBER -> JSONNumber(n)
            BOOLEAN -> JSONBoolean(n)
            NULL -> JSONNull()
        }
    }

    override fun forEach(action:(JSONBase) -> Unit) {
        table.keys.forEach(action)
    }

}