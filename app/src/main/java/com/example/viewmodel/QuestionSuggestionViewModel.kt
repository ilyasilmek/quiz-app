package com.example.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class QuestionSuggestionViewModel : ViewModel() {
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

    fun setCategory(value: String) { _category.value = value }
    fun setQuestion(value: String) { _question.value = value }
    fun setOption(index: Int, value: String) {
        _options.update { current -> current.toMutableList().also { it[index] = value } }
    }
    fun setCorrect(index: Int) { _correctIndex.value = index }
    fun setExplanation(value: String) { _explanation.value = value }
    fun setSource(value: String) { _source.value = value }

    fun submit() {
        // Production: replace with Public API repository call.
        _submitted.value = true
    }

    fun reset() {
        _question.value = ""
        _options.value = listOf("", "", "", "")
        _correctIndex.value = 0
        _explanation.value = ""
        _source.value = ""
        _submitted.value = false
    }
}
