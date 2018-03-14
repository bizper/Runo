package log

import resource.Sp

object Log {

    enum class Level {
        ERROR,
        WARN,
        NORMAL,
        INFO
    }

    fun record(level: Level, str: String) {
        println("[${level.name}] $str")
    }

    fun record(level: Level, sp: Sp) {
        println("[${level.name}] ${sp.state} ${sp.string}")
    }

}