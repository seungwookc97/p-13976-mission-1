package com.standard.util.json

object JsonUtil {
    // JSON 문자열을 Map으로 변환
    fun jsonStrToMap(jsonStr: String): Map<String, Any> {
        return jsonStr
            .removeSurrounding("{", "}")
            .split(",")
            .mapNotNull {
                val (key, value) = it.split(":", limit = 2).map(String::trim).takeIf { it.size == 2 }
                    ?: return@mapNotNull null

                val cleanedKey = key.removeSurrounding("\"")

                val cleanedValue = if (value.startsWith("\"") && value.endsWith("\"")) {
                    value.removeSurrounding("\"")
                } else {
                    value.toInt()
                }

                cleanedKey to cleanedValue
            }.toMap()
    }

    // Map 리스트를 JSON 문자열로 변환
    fun toString(mapList: List<Map<String, Any?>>): String {
        return mapList.joinToString(
            prefix = "[\n", separator = ",\n", postfix = "\n]"
        ) { map -> toString(map).prependIndent("    ") }
    }

    // Map을 JSON 문자열로 변환
    fun toString(map: Map<String, Any?>): String {
        return map.entries.joinToString(
            prefix = "{\n", separator = ",\n", postfix = "\n}"
        ) { (key, value) ->
            val formattedKey = "\"$key\""
            val formattedValue = when (value) {
                is String -> "\"$value\""
                else -> value
            }
            "    $formattedKey: $formattedValue"
        }
    }
}