package mock.space.data.utils


fun String.applyRulesIfExists(
    bodyParams: String?,
    bodyRules: List<String>?,
    url: String,
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

    urlRules?.forEach { rule ->
        if (url.contains(rule)) {
            return url.substringBefore(rule)
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
