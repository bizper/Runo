package parser

class Node {

    var parent: Node? = null
    var value = ""
    var type: Type

    constructor(parent: Node?, type: Type, value: String) {
        this.parent = parent
        this.type = type
        this.value = value
    }

    constructor(type: Type,value: String): this(null, type, value)
    constructor(parent: Node, value: String): this(parent, Type.NULL, value)

    var children = ArrayList<Node>()

    fun delete(): Node = children.removeAt(children.size - 1)

    fun getChildren(): Array<Node> = children.toTypedArray()

    fun add(node: Node) {
        children.add(node)
    }

    fun getKid(): Node = children[0]

    fun isKid(): Boolean = children.isEmpty()

}