package com.example.domain.content

object QuestionValidator {
    data class Result(val valid: Boolean, val errors: List<String>)

    fun validate(question: String, options: List<String>, correctIndex: Int): Result {
        val errors = buildList {
            if (question.trim().length < 10) add("Soru en az 10 karakter olmalı.")
            if (options.size != 4) add("Tam olarak 4 şık gerekli.")
            if (options.any { it.trim().isEmpty() }) add("Tüm şıklar doldurulmalı.")
            if (options.map { normalize(it) }.distinct().size != options.size) add("Şıklar birbirinden farklı olmalı.")
            if (correctIndex !in 0..3) add("Doğru cevap A-D arasında olmalı.")
        }
        return Result(errors.isEmpty(), errors)
    }

    private fun normalize(value: String): String = value
        .trim()
        .lowercase()
        .replace("\\s+".toRegex(), " ")
}
