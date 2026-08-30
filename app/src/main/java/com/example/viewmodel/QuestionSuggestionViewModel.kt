package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.remote.QuestionSubmissionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuestionSuggestionViewModel(
    private val repository: QuestionSubmissionRepository,
) : ViewModel() {
    private val _category = MutableStateFlow("Genel Kültür")
    val category: StateFlow<String> = _category.asStateFlow()
    private val _question = MutableStateFlow("")
    val question: StateFlow<String> = _question.asStateFlow()
    private val _options = MutableStateFlow(listOf("", "", "", ""))
    val options: StateFlow<List<String>> = _options.asStateFlow()
    private val _correctIndex = MutableStateFlow(0)
    val correctIndex: StateFlow<Int> = _correctIndex.asStateFlow()
    private val _explanation = MutableStateFlow("")
    val explanation: StateFlow<String> = _explanation.asStateFlow()
    private val _source = MutableStateFlow("")
    val source: StateFlow<String> = _source.asStateFlow()
    private val _submitted = MutableStateFlow(false)
    val submitted: StateFlow<Boolean> = _submitted.asStateFlow()
    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()
    private val _submissionError = MutableStateFlow<String?>(null)
    val submissionError: StateFlow<String?> = _submissionError.asStateFlow()

    fun setCategory(value: String) { _category.value = value }
    fun setQuestion(value: String) { _question.value = value }
    fun setOption(index: Int, value: String) {
        _options.update { current -> current.toMutableList().also { it[index] = value } }
    }
    fun setCorrect(index: Int) { _correctIndex.value = index }
    fun setExplanation(value: String) { _explanation.value = value }
    fun setSource(value: String) { _source.value = value }

    fun submit() {
        if (_isSubmitting.value) return

        viewModelScope.launch {
            _isSubmitting.value = true
            _submissionError.value = null

            repository.submit(
                categoryId = _category.value,
                question = _question.value,
                options = _options.value,
                correctIndex = _correctIndex.value,
                explanation = _explanation.value,
                sourceUrl = _source.value,
            ).onSuccess {
                _submitted.value = true
            }.onFailure { error ->
                _submissionError.value = error.message ?: "Soru gönderilemedi. Lütfen tekrar deneyin."
            }

            _isSubmitting.value = false
        }
    }

    fun reset() {
        _question.value = ""
        _options.value = listOf("", "", "", "")
        _correctIndex.value = 0
        _explanation.value = ""
        _source.value = ""
        _submitted.value = false
        _isSubmitting.value = false
        _submissionError.value = null
    }

    companion object {
        fun factory(repository: QuestionSubmissionRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    QuestionSuggestionViewModel(repository) as T
            }
    }
}
