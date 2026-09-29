package com.example.viewmodel

import androidx.lifecycle.ViewModel
import com.example.data.ExplorationRepository
import com.example.model.Category
import com.example.model.SubTopic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

sealed interface ScreenState {
    data object Dashboard : ScreenState
    data class CategoryDetail(val categoryId: String) : ScreenState
    data class TopicDetail(val topicId: String, val fromCategoryId: String? = null) : ScreenState
    data class FinalExam(val categoryId: String) : ScreenState
}

data class ExplorationUiState(
    val currentScreen: ScreenState = ScreenState.Dashboard,
    val searchQuery: String = "",
    val searchResults: List<SubTopic> = emptyList(),
    val selectedCategory: Category? = null,
    val selectedTopic: SubTopic? = null,
    val selectedComponentId: String? = null,
    val selectedTab: Int = 0, // 0: Components, 1: How It Works, 2: Fun Facts, 3: Quiz
    val bookmarkedTopicIds: Set<String> = emptySet(),
    val completedQuizzes: Map<String, Int> = emptyMap(), // topicId -> score
    val completedSubTopicIds: Set<String> = emptySet(), // Permanent green checkmarks ✔️
    val passedFinalExams: Set<String> = emptySet(), // categoryId -> passed
    val selectedQuizAnswers: Map<Int, Int> = emptyMap(), // questionIndex -> chosenOptionIndex
    val isQuizSubmitted: Boolean = false,
    val quizScore: Int = 0,
    val isQuizPassed: Boolean = false,
    val finalExamAnswers: Map<Int, Int> = emptyMap(),
    val isFinalExamSubmitted: Boolean = false,
    val finalExamScore: Int = 0,
    val exploredTopicIds: Set<String> = emptySet()
)

class ExplorationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ExplorationUiState())
    val uiState: StateFlow<ExplorationUiState> = _uiState.asStateFlow()

    fun navigateToCategory(categoryId: String) {
        val category = ExplorationRepository.getCategoryById(categoryId)
        _uiState.update {
            it.copy(
                currentScreen = ScreenState.CategoryDetail(categoryId),
                selectedCategory = category,
                searchQuery = "",
                searchResults = emptyList()
            )
        }
    }

    fun navigateToTopic(topicId: String, fromCategoryId: String? = null) {
        val topic = ExplorationRepository.getTopicById(topicId)
        val defaultComponentId = topic?.components?.firstOrNull()?.id
        _uiState.update {
            it.copy(
                currentScreen = ScreenState.TopicDetail(topicId, fromCategoryId),
                selectedTopic = topic,
                selectedComponentId = defaultComponentId,
                selectedTab = 0,
                selectedQuizAnswers = emptyMap(),
                isQuizSubmitted = false,
                quizScore = 0,
                isQuizPassed = false,
                exploredTopicIds = it.exploredTopicIds + topicId
            )
        }
    }

    fun navigateToDashboard() {
        _uiState.update {
            it.copy(
                currentScreen = ScreenState.Dashboard,
                selectedCategory = null,
                selectedTopic = null,
                selectedComponentId = null,
                searchQuery = "",
                searchResults = emptyList()
            )
        }
    }

    fun navigateToFinalExam(categoryId: String) {
        val category = ExplorationRepository.getCategoryById(categoryId)
        _uiState.update {
            it.copy(
                currentScreen = ScreenState.FinalExam(categoryId),
                selectedCategory = category,
                finalExamAnswers = emptyMap(),
                isFinalExamSubmitted = false,
                finalExamScore = 0
            )
        }
    }

    fun handleBackNavigation() {
        when (val screen = _uiState.value.currentScreen) {
            is ScreenState.FinalExam -> {
                navigateToCategory(screen.categoryId)
            }
            is ScreenState.TopicDetail -> {
                if (screen.fromCategoryId != null) {
                    navigateToCategory(screen.fromCategoryId)
                } else {
                    navigateToDashboard()
                }
            }
            is ScreenState.CategoryDetail -> {
                navigateToDashboard()
            }
            ScreenState.Dashboard -> {
                // Root
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        val filtered = if (query.isBlank()) {
            emptyList()
        } else {
            ExplorationRepository.getAllTopics().filter {
                it.title.contains(query, ignoreCase = true) ||
                it.subtitle.contains(query, ignoreCase = true) ||
                it.overview.contains(query, ignoreCase = true) ||
                it.badge.contains(query, ignoreCase = true)
            }
        }
        _uiState.update {
            it.copy(
                searchQuery = query,
                searchResults = filtered
            )
        }
    }

    fun selectComponent(componentId: String) {
        _uiState.update { it.copy(selectedComponentId = componentId) }
    }

    fun selectDetailTab(tabIndex: Int) {
        _uiState.update { it.copy(selectedTab = tabIndex) }
    }

    fun toggleBookmark(topicId: String) {
        _uiState.update {
            val updated = if (it.bookmarkedTopicIds.contains(topicId)) {
                it.bookmarkedTopicIds - topicId
            } else {
                it.bookmarkedTopicIds + topicId
            }
            it.copy(bookmarkedTopicIds = updated)
        }
    }

    fun selectQuizAnswer(questionIndex: Int, optionIndex: Int) {
        if (_uiState.value.isQuizSubmitted) return
        _uiState.update {
            it.copy(
                selectedQuizAnswers = it.selectedQuizAnswers + (questionIndex to optionIndex)
            )
        }
    }

    fun submitQuiz() {
        val topic = _uiState.value.selectedTopic ?: return
        var correctCount = 0
        topic.quizQuestions.forEachIndexed { idx, q ->
            if (_uiState.value.selectedQuizAnswers[idx] == q.correctIndex) {
                correctCount++
            }
        }
        // Passing threshold: at least 2 out of 3 (or 100% of questions if fewer)
        val isPassed = correctCount >= (topic.quizQuestions.size * 2 / 3).coerceAtLeast(1)

        _uiState.update {
            val updatedCompletedTopics = if (isPassed) {
                it.completedSubTopicIds + topic.id
            } else {
                it.completedSubTopicIds
            }
            it.copy(
                isQuizSubmitted = true,
                quizScore = correctCount,
                isQuizPassed = isPassed,
                completedQuizzes = it.completedQuizzes + (topic.id to correctCount),
                completedSubTopicIds = updatedCompletedTopics
            )
        }
    }

    fun resetQuiz() {
        _uiState.update {
            it.copy(
                selectedQuizAnswers = emptyMap(),
                isQuizSubmitted = false,
                quizScore = 0,
                isQuizPassed = false
            )
        }
    }

    fun selectFinalExamAnswer(questionIndex: Int, optionIndex: Int) {
        if (_uiState.value.isFinalExamSubmitted) return
        _uiState.update {
            it.copy(
                finalExamAnswers = it.finalExamAnswers + (questionIndex to optionIndex)
            )
        }
    }

    fun submitFinalExam(categoryId: String) {
        val category = ExplorationRepository.getCategoryById(categoryId) ?: return
        var correctCount = 0
        category.finalExamQuestions.forEachIndexed { idx, q ->
            if (_uiState.value.finalExamAnswers[idx] == q.correctIndex) {
                correctCount++
            }
        }
        // Passing threshold: 60% or higher
        val totalQuestions = category.finalExamQuestions.size
        val isPassed = totalQuestions > 0 && (correctCount.toFloat() / totalQuestions >= 0.6f)

        _uiState.update {
            val updatedPassedExams = if (isPassed) {
                it.passedFinalExams + categoryId
            } else {
                it.passedFinalExams
            }
            it.copy(
                isFinalExamSubmitted = true,
                finalExamScore = correctCount,
                passedFinalExams = updatedPassedExams
            )
        }
    }

    fun resetFinalExam() {
        _uiState.update {
            it.copy(
                finalExamAnswers = emptyMap(),
                isFinalExamSubmitted = false,
                finalExamScore = 0
            )
        }
    }
}
