package mock.space.data.utils


fun String.applyRulesIfExists(
    bodyParams: String?,
    bodyRules: List<String>?,
    urlParams: String?,
    urlRules: List<String>?
): String {
    bodyParams?.let {
        if (bodyRules != null) {
            for (differentiator in bodyRules) {
                val indexOfMethod = bodyParams.indexOf(differentiator)
                if (indexOfMethod > 0) {
                    return this + bodyParams.substring(indexOfMethod + differentiator.length, indexOfMethod + 20)
                }
            }
        }
    }

    urlParams?.let {
        if (urlRules != null) {
            for (differentiator in urlRules) {
                return this.substringBefore(differentiator)
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