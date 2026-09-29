package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.ExplorationRepository
import com.example.ui.screens.CategoryScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.FinalExamScreen
import com.example.ui.screens.TopicDetailScreen
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.ExplorationViewModel
import com.example.viewmodel.ScreenState

class MainActivity : ComponentActivity() {

    private val viewModel: ExplorationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    OmniScopeApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun OmniScopeApp(
    viewModel: ExplorationViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Android System Back navigation handling
    BackHandler(enabled = uiState.currentScreen !is ScreenState.Dashboard) {
        viewModel.handleBackNavigation()
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .safeDrawingPadding(),
        containerColor = DarkBackground
    ) { innerPadding ->
        AnimatedContent(
            targetState = uiState.currentScreen,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "screen_transition"
        ) { screen ->
            when (screen) {
                is ScreenState.Dashboard -> {
                    DashboardScreen(
                        categories = ExplorationRepository.allCategories,
                        searchQuery = uiState.searchQuery,
                        searchResults = uiState.searchResults,
                        exploredCount = uiState.exploredTopicIds.size,
                        onSearchChanged = viewModel::onSearchQueryChanged,
                        onCategoryClick = viewModel::navigateToCategory,
                        onTopicClick = { topicId -> viewModel.navigateToTopic(topicId) }
                    )
                }
                is ScreenState.CategoryDetail -> {
                    val category = uiState.selectedCategory
                        ?: ExplorationRepository.getCategoryById(screen.categoryId)
                    if (category != null) {
                        CategoryScreen(
                            category = category,
                            completedSubTopicIds = uiState.completedSubTopicIds,
                            passedFinalExams = uiState.passedFinalExams,
                            onBackToMainMenu = viewModel::navigateToDashboard,
                            onTopicClick = { topicId -> viewModel.navigateToTopic(topicId, fromCategoryId = category.id) },
                            onStartFinalExam = { catId -> viewModel.navigateToFinalExam(catId) }
                        )
                    } else {
                        viewModel.navigateToDashboard()
                    }
                }
                is ScreenState.TopicDetail -> {
                    val topic = uiState.selectedTopic
                        ?: ExplorationRepository.getTopicById(screen.topicId)
                    if (topic != null) {
                        TopicDetailScreen(
                            topic = topic,
                            selectedComponentId = uiState.selectedComponentId,
                            selectedTab = uiState.selectedTab,
                            isBookmarked = uiState.bookmarkedTopicIds.contains(topic.id),
                            selectedQuizAnswers = uiState.selectedQuizAnswers,
                            isQuizSubmitted = uiState.isQuizSubmitted,
                            quizScore = uiState.quizScore,
                            isQuizPassed = uiState.isQuizPassed,
                            onBackToMainMenu = viewModel::navigateToDashboard,
                            onBackToCategory = {
                                if (screen.fromCategoryId != null) {
                                    viewModel.navigateToCategory(screen.fromCategoryId)
                                } else {
                                    viewModel.navigateToDashboard()
                                }
                            },
                            onComponentSelect = viewModel::selectComponent,
                            onTabSelect = viewModel::selectDetailTab,
                            onBookmarkToggle = { viewModel.toggleBookmark(topic.id) },
                            onSelectQuizAnswer = viewModel::selectQuizAnswer,
                            onSubmitQuiz = viewModel::submitQuiz,
                            onResetQuiz = viewModel::resetQuiz
                        )
                    } else {
                        viewModel.navigateToDashboard()
                    }
                }
                is ScreenState.FinalExam -> {
                    val category = uiState.selectedCategory
                        ?: ExplorationRepository.getCategoryById(screen.categoryId)
                    if (category != null) {
                        FinalExamScreen(
                            category = category,
                            selectedAnswers = uiState.finalExamAnswers,
                            isSubmitted = uiState.isFinalExamSubmitted,
                            score = uiState.finalExamScore,
                            onSelectAnswer = viewModel::selectFinalExamAnswer,
                            onSubmitExam = { viewModel.submitFinalExam(category.id) },
                            onResetExam = viewModel::resetFinalExam,
                            onBackToCategory = { viewModel.navigateToCategory(category.id) }
                        )
                    } else {
                        viewModel.navigateToDashboard()
                    }
                }
            }
        }
    }
}
