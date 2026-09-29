package com.example.model

import androidx.compose.ui.graphics.Color

enum class SimulationType {
    STORAGE_COMPARISON,
    CPU_ARCHITECTURE,
    GPU_ARCHITECTURE,
    MOTHERBOARD_FLOW,
    RAM_BUS,
    PSU_SIMULATOR,
    COOLING_SIMULATOR,
    SOLAR_SYSTEM,
    BLACK_HOLE,
    ROCKET_STAGING,
    GALAXIES_UNIVERSE,
    CAR_ENGINE,
    PLANETARY_GEARS,
    HYDRAULIC_PRESS,
    BRAKES_SUSPENSION,
    BRAKES_SYSTEM,
    SUSPENSION_SYSTEM
}

data class Category(
    val id: String,
    val title: String,
    val tagline: String,
    val description: String,
    val imageRes: Int,
    val primaryColor: Color,
    val accentColor: Color,
    val glowColor: Color,
    val subtopicCount: Int,
    val subtopics: List<SubTopic>,
    val finalExamQuestions: List<FinalExamQuestion> = emptyList()
)

data class SubTopic(
    val id: String,
    val categoryId: String,
    val title: String,
    val subtitle: String,
    val badge: String,
    val readTime: String,
    val difficulty: String, // e.g., "أساسي", "متوسط", "متقدم"
    val overview: String,
    val simulationType: SimulationType,
    val components: List<TopicComponent>,
    val funFacts: List<FunFact>,
    val processSteps: List<ProcessStep>,
    val quizQuestions: List<QuizQuestion>,
    val comparisons: List<ComparisonItem> = emptyList()
)

data class TopicComponent(
    val id: String,
    val name: String,
    val role: String,
    val shortSummary: String,
    val detailedExplanation: String,
    val statLabel: String,
    val statValue: String
)

data class ProcessStep(
    val stepNumber: Int,
    val title: String,
    val description: String,
    val highlight: String
)

data class FunFact(
    val title: String,
    val fact: String,
    val category: String
)

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class FinalExamQuestion(
    val subTopicId: String,
    val subTopicTitle: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class ComparisonItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val speedValue: String,
    val latencyValue: String,
    val mechanism: String,
    val pros: List<String>,
    val cons: List<String>,
    val bestFor: String
)
