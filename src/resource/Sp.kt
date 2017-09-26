package resource

data class Sp(var state: State, var string: String) {

    constructor(state: State, char: Char): this(state, char.toString())

    constructor(state: State): this(state, "")

}