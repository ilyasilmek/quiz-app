package com.example.data.validation

object QuestionSubmissionValidator {
    fun validate(
        categoryId: String,
        question: String,
        options: List<String>,
        correctIndex: Int
    ) {
        require(categoryId.isNotBlank()) { "Kategori seçilmelidir." }
        require(question.trim().length >= 10) { "Soru en az 10 karakter olmalıdır." }
        require(options.size == 4) { "Tam olarak 4 şık gereklidir." }
        require(options.all { it.trim().isNotEmpty() }) { "Tüm şıklar doldurulmalıdır." }
        require(options.map { it.trim().lowercase() }.distinct().size == 4) {
            "Şıklar birbirinden farklı olmalıdır."
        }
        require(correctIndex in options.indices) { "Doğru cevap geçersiz." }
    }
}
