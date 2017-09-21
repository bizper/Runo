package log

object Level{

    val FATAL = 0//导致程序崩溃的错误
    val ERROR = 1//程序无法修复的错误，但不会导致程序崩溃
    val WARN = 2//可能会导致错误的风险，程序可以自行修复的错误
    val NORMAL = 3//正常运行的状态输出
    val INFO = 4//提醒，可以使用的选项或信息

    enum class Inside{
        FATAL,
        ERROR,
        WARN,
        NORMAL,
        INFO
    }

    fun getTypeName(count: Int): String {
        return when(count) {
            FATAL -> Inside.FATAL.name
            ERROR -> Inside.ERROR.name
            WARN -> Inside.WARN.name
            INFO -> Inside.INFO.name
            else -> Inside.NORMAL.name
        }
    }

}