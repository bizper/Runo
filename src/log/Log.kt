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
        if(level == Level.ERROR || level == Level.WARN) System.err.println("[${level.name}] $str")
        else println("[${level.name}] $str")
    }

    fun record(level: Level, sp: Sp) {
        if(level == Level.ERROR || level == Level.WARN) System.err.println("[${level.name}] ${sp.state} ${sp.string}")
        else println("[${level.name}] ${sp.state} ${sp.string}")

    }

    fun info(sp: Sp) {
        record(Level.INFO, sp)
    }

    fun info(str: String) {
        record(Level.INFO, str)
    }

    fun record(obj: Any, str: String) {
        println("[$obj] str")
    }

}