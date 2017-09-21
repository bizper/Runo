package parser

data class Node(var type: Type,var value: String) {

    var parent: Node? = null

    constructor(parent: Node, type: Type, value: String): this(type, value) {
        this.parent = parent
    }

    var children = ArrayList<Node>()

    fun delete(): Node {
        return children.removeAt(children.size - 1)
    }

}