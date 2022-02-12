package objects

class JSONNumber(private val number: String): JSONBase {

    fun getInt(): Int {
        return number.toInt()
    }

    fun getDouble(): Double {
        return number.toDouble()
    }

}