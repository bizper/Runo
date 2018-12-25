package adapter

interface TypeAdapter<T> {

    fun accept(str: String): T

}