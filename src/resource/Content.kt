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
        return (num in '0'..'9') || (num == '-') || (num == '.') || (num == '+') || (num == 'E') || (num == 'e')
    }

    fun isNumber(number: String): Boolean {
        return "^(-)?(0|[1-9][0-9]*)+(.[0-9]+)?([Ee][+-]?[\\d]+)?$".toRegex().matches(number)
    }

    fun isEnd(char: Char): Boolean {
        return (char == ',') || (char == '}') || (char == ']')
    }

}