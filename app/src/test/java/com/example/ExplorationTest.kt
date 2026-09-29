package com.example

import com.example.data.ExplorationRepository
import com.example.viewmodel.ExplorationViewModel
import com.example.viewmodel.ScreenState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExplorationTest {

    @Test
    fun repository_containsThreePrimaryCategories() {
        val categories = ExplorationRepository.allCategories
        assertEquals(3, categories.size)
        assertEquals("technology", categories[0].id)
        assertEquals("space", categories[1].id)
        assertEquals("mechanics", categories[2].id)
    }

    @Test
    fun repository_containsExpectedSubTopicsIncludingPsuAndCooling() {
        // Technology subtopics (7 in total)
        assertNotNull(ExplorationRepository.getTopicById("cpu_processor"))
        assertNotNull(ExplorationRepository.getTopicById("gpu_graphics"))
        assertNotNull(ExplorationRepository.getTopicById("motherboard_hub"))
        assertNotNull(ExplorationRepository.getTopicById("storage_drives"))
        assertNotNull(ExplorationRepository.getTopicById("ram_memory"))
        assertNotNull(ExplorationRepository.getTopicById("psu_power_supply"))
        assertNotNull(ExplorationRepository.getTopicById("cooling_systems"))

        // Space subtopics (4 in total)
        assertNotNull(ExplorationRepository.getTopicById("solar_system"))
        assertNotNull(ExplorationRepository.getTopicById("stars_black_holes"))
        assertNotNull(ExplorationRepository.getTopicById("space_rockets"))
        assertNotNull(ExplorationRepository.getTopicById("galaxies_universe"))

        // Mechanics subtopics (5 in total)
        assertNotNull(ExplorationRepository.getTopicById("car_engine"))
        assertNotNull(ExplorationRepository.getTopicById("gears_transmission"))
        assertNotNull(ExplorationRepository.getTopicById("hydraulic_systems"))
        assertNotNull(ExplorationRepository.getTopicById("brakes_system"))
        assertNotNull(ExplorationRepository.getTopicById("suspension_system"))
    }

    @Test
    fun repository_everySubTopicHasThreeQuizQuestions() {
        val allTopics = ExplorationRepository.getAllTopics()
        assertEquals(16, allTopics.size) // 7 + 4 + 5

        for (topic in allTopics) {
            assertEquals("Topic ${topic.id} should have exactly 3 questions", 3, topic.quizQuestions.size)
            for (q in topic.quizQuestions) {
                assertEquals(4, q.options.size)
                assertTrue(q.correctIndex in 0..3)
                assertTrue(q.explanation.isNotBlank())
            }
        }
    }

    @Test
    fun repository_everyCategoryHasFinalExamQuestions() {
        for (category in ExplorationRepository.allCategories) {
            assertTrue(category.finalExamQuestions.isNotEmpty())
            assertEquals(
                "Category ${category.id} final exam should have 1 question per subtopic",
                category.subtopics.size,
                category.finalExamQuestions.size
            )
        }
    }

    @Test
    fun repository_coolingSystemsHasComparisonTable() {
        val cooling = ExplorationRepository.getTopicById("cooling_systems")
        assertNotNull(cooling)
        assertTrue(cooling!!.comparisons.isNotEmpty())
        assertEquals(2, cooling.comparisons.size) // Air vs Liquid
    }

    @Test
    fun viewModel_quizCompletionRewardsPermanentCheckmark() {
        val vm = ExplorationViewModel()
        vm.navigateToTopic("psu_power_supply", fromCategoryId = "technology")
        val topic = vm.uiState.value.selectedTopic!!

        // Answer correctly (at least 2 questions out of 3)
        vm.selectQuizAnswer(0, topic.quizQuestions[0].correctIndex)
        vm.selectQuizAnswer(1, topic.quizQuestions[1].correctIndex)
        vm.selectQuizAnswer(2, topic.quizQuestions[2].correctIndex)

        vm.submitQuiz()

        assertTrue(vm.uiState.value.isQuizSubmitted)
        assertTrue(vm.uiState.value.isQuizPassed)
        assertEquals(3, vm.uiState.value.quizScore)
        assertTrue(vm.uiState.value.completedSubTopicIds.contains("psu_power_supply"))
    }

    @Test
    fun viewModel_finalExamFlow() {
        val vm = ExplorationViewModel()
        vm.navigateToFinalExam("space")
        assertTrue(vm.uiState.value.currentScreen is ScreenState.FinalExam)

        val category = vm.uiState.value.selectedCategory!!
        assertEquals("space", category.id)

        // Select all correct answers for final exam
        category.finalExamQuestions.forEachIndexed { idx, q ->
            vm.selectFinalExamAnswer(idx, q.correctIndex)
        }

        vm.submitFinalExam("space")
        assertTrue(vm.uiState.value.isFinalExamSubmitted)
        assertEquals(category.finalExamQuestions.size, vm.uiState.value.finalExamScore)
        assertTrue(vm.uiState.value.passedFinalExams.contains("space"))

        // Back navigation from final exam leads to category
        vm.handleBackNavigation()
        assertTrue(vm.uiState.value.currentScreen is ScreenState.CategoryDetail)
    }

    @Test
    fun viewModel_navigationFlow() {
        val vm = ExplorationViewModel()
        assertTrue(vm.uiState.value.currentScreen is ScreenState.Dashboard)

        // Navigate to category
        vm.navigateToCategory("technology")
        assertTrue(vm.uiState.value.currentScreen is ScreenState.CategoryDetail)
        assertEquals("technology", (vm.uiState.value.currentScreen as ScreenState.CategoryDetail).categoryId)

        // Navigate to subtopic
        vm.navigateToTopic("storage_drives", fromCategoryId = "technology")
        assertTrue(vm.uiState.value.currentScreen is ScreenState.TopicDetail)
        assertEquals("storage_drives", (vm.uiState.value.currentScreen as ScreenState.TopicDetail).topicId)
        assertNotNull(vm.uiState.value.selectedComponentId)

        // Back to category
        vm.handleBackNavigation()
        assertTrue(vm.uiState.value.currentScreen is ScreenState.CategoryDetail)

        // Back to dashboard (Main Menu)
        vm.handleBackNavigation()
        assertTrue(vm.uiState.value.currentScreen is ScreenState.Dashboard)
    }
}
