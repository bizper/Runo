package objects

import objects.errors.TypeErrorException
import parser.Node
import parser.Type

class JSONObject private constructor() : JSONBase {

    private constructor(node: Node) : this() {
        if(node.type != Type.OBJECT_ROOT) throw TypeErrorException("Not OBJECT_ROOT Type with this node ${node.value}")
    }

    private val map = HashMap<String, JSONBase>()

    fun getJSONObject(key: String): JSONObject {
        val base = map[key]
        return if(base is JSONObject) base
        else throw TypeErrorException("Not JSONObject Type with key $key")
    }

    fun getJSONArray(key: String): JSONArray {
        val base = map[key]
        return if(base is JSONArray) base
        else throw TypeErrorException("Not JSONArray Type with key $key")
    }

    fun getString(key: String): String {
        val base = map[key]
        return if(base is JSONString) base.getString()
        else throw TypeErrorException("Not JSONString Type with key $key")
    }

    fun getInt(key: String): Int {
        val base = map[key]
        return if(base is JSONNumber) (map[key] as JSONNumber).getInt()
        else throw TypeErrorException("Not JSONNumber Type with key $key")
    }

    fun getDouble(key: String): Double {
        val base = map[key]
        return if(base is JSONNumber) (map[key] as JSONNumber).getDouble()
        else throw TypeErrorException("Not JSONNumber Type with key $key")
    }

    companion object {
        fun getJSONObject(): JSONObject {
            return JSONObject()
        }

        fun getJSONObject(node: Node): JSONObject {
            return JSONObject(node)
        }
    }

}