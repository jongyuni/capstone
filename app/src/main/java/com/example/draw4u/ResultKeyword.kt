package com.example.draw4u

data class ResultKeyword(
    var message: String,
    var keywords: Array<String?> = arrayOfNulls<String>(3)
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ResultKeyword

        if (message != other.message) return false
        if (!keywords.contentEquals(other.keywords)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + keywords.contentHashCode()
        return result
    }
}
