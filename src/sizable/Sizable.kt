package sizable

object Sizable {

    private val separator = System.lineSeparator()

    fun toJSON(name: String, any: Any, height: Int): String {
        val cls = any.javaClass
        val buffer = StringBuffer()
        (0..height).filter { it > 1 }.forEach { buffer.append("    ") }
        buffer.append("${if(name == "") "" else "\"$name\":"}{$separator")
        for(field in cls.declaredFields) {
            field.isAccessible = true
            val type = field.type.toString()
            val inside = field.get(any)
            (0..height).forEach { buffer.append("    ") }
            when {
                isArray(type) -> {
                    buffer.append("\"${field.name}\":[$separator")
                    val arr = inside as Array<*>
                    val arr_name = arr.javaClass.componentType.canonicalName
                    println(arr_name)
                    when {
                        string_class.contains(arr_name) -> {
                            for(a in arr) {
                                (0..height + 1).forEach { buffer.append("    ") }
                                buffer.append("\"$a\",$separator")
                            }
                        }
                        non_quote_class.contains(arr_name) -> {
                            for(a in arr) {
                                (0..height + 1).forEach { buffer.append("    ") }
                                buffer.append("$a,$separator")
                            }
                        }
                        else -> {
                            for(a in arr) {
                                buffer.append(toJSON("", a!!, height + 1))
                                buffer.deleteCharAt(buffer.lastIndex - 1)
                                buffer.append(",")
                            }

                        }
                    }
                    buffer.deleteCharAt(buffer.lastIndex - 2)
                    (0..height).forEach { buffer.append("    ") }
                    buffer.append("],$separator")
                }
                string_type.contains(type) -> buffer.append("\"${field.name}\":\"${inside?:"Null"}\",$separator")
                non_quote_type.contains(type) -> buffer.append("\"${field.name}\":${inside?:"Null"},$separator")
                else -> buffer.append(toJSON(field.name, inside, height + 1))
            }
        }
        buffer.deleteCharAt(buffer.lastIndex - 2)
        (0..height).filter { it != 0 }.forEach { buffer.append("    ") }
        buffer.append("}$separator")
        return buffer.toString()
    }

    private val string_type = arrayOf("char", "class java.lang.String")
    private val non_quote_type = arrayOf("boolean", "int", "double", "float", "long")
    private val non_quote_class = arrayOf("java.lang.Boolean", "java.lang.Integer", "java.lang.Double", "java.lang.Float", "java.lang.Long")
    private val string_class = arrayOf("java.lang.String", "java.lang.Character")


    private fun isArray(key: String): Boolean = "class \\[L[\\w\\W]+".toRegex().matches(key)


}