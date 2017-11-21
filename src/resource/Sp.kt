package resource

import parser.Type

data class Sp(var state: State, var type: Type, var string: String) {

    constructor(state: State, char: Char): this(state, Type.STRING, char.toString())

    constructor(state: State, type: Type, char: Char): this(state, type, char.toString())

    constructor(state: State, string: String): this(state, Type.STRING, string)

    constructor(state: State): this(state, Type.STRING, "")

    constructor(state: State, type: Type): this(state, type, "")

    override fun toString(): String {
        return "$state $type $string"
    }

}