package Expr

object Expr {

    private val       root_name = "root"
    private val     root_symbol = "$"
    private val all_node_symbol = "*"
    private val    current_node = "@"
    private val  element_length = "#"
    private val     array_regex = "([\\w\\W]+)\\[([\\w]+)]".intern().toRegex()

    fun parseExpr(expr: String): ArrayList<ExprPack> {
        var command = ArrayList<ExprPack>()
        var exprList = expr.split(".")
        for(i in exprList) {
            when(true) {
                i == root_symbol -> command.add(ExprPack(ExprType.ROOT, Expr.root_name))
                i.matches(array_regex) -> {
                    val arrIndex = parseArrayIndex(i)
                    command.add(ExprPack(ExprType.ARRAY, arrIndex[1].toString()))
                    command.add(ExprPack(ExprType.ARRAY_INDEX, arrIndex[2].toString()))
                }
                i == all_node_symbol -> command.add(ExprPack(ExprType.ALL_NODE, Expr.all_node_symbol))
                else -> command.add(ExprPack(ExprType.CHECK_STRING, i))
            }
        }
        return command
    }

    private fun parseArrayIndex(string: String): Array<Any> {
        return array_regex.findAll(string).toList().flatMap(MatchResult::groupValues).toTypedArray()
    }

}
