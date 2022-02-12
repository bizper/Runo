package objects

import objects.errors.TypeErrorException

class JSONArray: JSONBase {

    private val list = ArrayList<JSONBase>()

    fun add(base: JSONBase) {
        list.add(base)
    }

    fun get(index: Int): JSONBase {
        return list[index]
    }

}