package core

class MediaEncoding {

    companion object {
        val head = "0x0f 0xaf 0xad"
    }

    fun getInfo(): String {
        return "$head"
    }

}