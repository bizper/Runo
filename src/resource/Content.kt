package resource

object Content {

    fun isWhiteSpace(ws: Char): Boolean {
        return when(ws) {
            '\n' -> true//换行符
            '\t' -> true//制表符
            '\b' -> true
            '\r' -> true
            ' ' -> true
            else -> false
        }
    }

    fun isNumber(num: Char): Boolean {
        return num in '0'..'9'
    }

    fun isNumber(number: String): Boolean {
        return "[\\-\\d\\\\.]+".toRegex().matches(number)
    }

}