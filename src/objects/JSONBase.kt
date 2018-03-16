package objects

import objects.error.TypeErrorException

interface JSONBase {

    fun toArray(): Array<JSONBase>

    fun toInt(): Int

    fun toDouble(): Double

    override fun toString(): String

    fun toBoolean(): Boolean

    fun toNull(): String

    fun forEach(action:(JSONBase) -> Unit)

}