package core

import core.Sizable
import java.net.Socket

class MediaEncoding {

    companion object {
        val head = "0x0f 0xaf 0xad"
        val end = "0x0f 0x0f 0x0e"
    }

    fun getInfo(): String {
        return "$head $end"
    }

}