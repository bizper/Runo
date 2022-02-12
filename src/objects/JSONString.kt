package objects

class JSONString(private val str: String): JSONBase {

    fun getString(): String = str

}