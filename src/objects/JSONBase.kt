package objects

interface JSONBase {

    fun toArray(): Array<JSONBase>

    fun toInt(): Int

    fun toDouble(): Double

    override fun toString(): String

    fun toBoolean(): Boolean

    fun toNull(): String


}