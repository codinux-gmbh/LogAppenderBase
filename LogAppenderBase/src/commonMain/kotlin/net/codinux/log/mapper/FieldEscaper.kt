package net.codinux.log.mapper

interface FieldEscaper {

    fun escapeFieldName(fieldName: String): String

}