package parser

class Node {

    var value: String = ""
    var type: Type = Type.READY_FOR_CHANGE
    var parent: Node? = null
    var children = ArrayList<Node>()

    constructor(parent: Node, type: Type, value: String) {
        this.parent = parent
        this.type = type
        this.value = value
    }

    fun deleteLastOne() {
        children.removeAt(children.size - 1)
    }

}