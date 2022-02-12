package adapter

@FunctionalInterface
interface TypeAdapter<T> {

    fun accept(str: String): T

}