package mock.space.data.utils


fun String.applyRulesIfExists(param: String?, rules: List<String>): String {
    param?.let {
        for (differentiator in rules) {
            val indexOfMethod = param.indexOf(differentiator)
            if (indexOfMethod > 0) {
                return this + param.substring(indexOfMethod + differentiator.length, indexOfMethod + 20)
            }
        }
    }
    return this
}

fun String.cleanPath(): String =
    if (last() == '/') {
        this.dropLast(1)
    } else {
        this
    }
