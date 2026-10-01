package com.example.data

import com.example.R
import com.example.model.Category
import com.example.model.SubTopic
import com.example.ui.theme.MechAccent
import com.example.ui.theme.MechGlow
import com.example.ui.theme.MechPrimary
import com.example.ui.theme.SpaceAccent
import com.example.ui.theme.SpaceGlow
import com.example.ui.theme.SpacePrimary
import com.example.ui.theme.TechAccent
import com.example.ui.theme.TechGlow
import com.example.ui.theme.TechPrimary

object ExplorationRepository {

    // =========================================================================
    // 1. التكنولوجيا (Technology: Hardware & Software - 15 Subtopics)
    // =========================================================================
    val cpuTopic = TechTopics.cpuTopic
    val gpuTopic = TechTopics.gpuTopic
    val motherboardTopic = TechTopics.motherboardTopic
    val storageTopic = TechTopics.storageTopic
    val ramTopic = TechTopics.ramTopic
    val psuTopic = TechTopics.psuTopic
    val coolingTopic = TechTopics.coolingTopic

    // Mobile Hardware
    val mobileSocsTopic = TechAdditions.mobileSocsTopic
    val mobileGpusTopic = TechAdditions.mobileGpusTopic
    val batteriesChargingTopic = TechAdditions.batteriesChargingTopic

    // Software & Operating Systems
    val osWindowsTopic = TechAdditions.osWindowsTopic
    val osMacosTopic = TechAdditions.osMacosTopic
    val osLinuxTopic = TechAdditions.osLinuxTopic
    val osAndroidTopic = TechAdditions.osAndroidTopic
    val osIosTopic = TechAdditions.osIosTopic
    val osHarmonyTopic = TechAdditions.osHarmonyTopic
    val osGoogleTvTopic = TechAdditions.osGoogleTvTopic
    val osWebosTopic = TechAdditions.osWebosTopic
    val osTizenTopic = TechAdditions.osTizenTopic

    // =========================================================================
    // 2. علوم الفضاء والفلك (Space & Astronomy - 4 Subtopics)
    // =========================================================================
    val solarSystemTopic = SpaceTopics.solarSystemTopic
    val starsBlackHolesTopic = SpaceTopics.starsBlackHolesTopic
    val rocketsTopic = SpaceTopics.rocketsTopic
    val galaxiesUniverseTopic = SpaceTopics.galaxiesUniverseTopic

    // =========================================================================
    // 3. الميكانيكا والهندسة (Mechanics & Engineering - 5 Subtopics)
    // =========================================================================
    val engineTopic = MechanicsTopics.engineTopic
    val transmissionTopic = MechanicsTopics.transmissionTopic
    val hydraulicsTopic = MechanicsTopics.hydraulicsTopic
    val brakesTopic = MechanicsTopics.brakesTopic
    val suspensionTopic = MechanicsTopics.suspensionTopic

    // =========================================================================
    // LIST OF MAIN CATEGORIES
    // =========================================================================
    val allCategories: List<Category> = listOf(
        Category(
            id = "technology",
            title = "التكنولوجيا",
            tagline = "الهاردوير (عتاد الكمبيوتر والهاتف) والسوفتوير (أنظمة التشغيل)",
            description = "اكتشف الأسرار الخفية لعتاد الحاسوب والهواتف الذكية وأنظمة التشغيل: المعالجات، كروت الشاشة، الذواكر، البطاريات، وأنظمة ويندوز، ماك، لينكس وتوزيعاته، والأندرويد والتلفزيونات الذكية.",
            imageRes = R.drawable.img_cat_tech,
            primaryColor = TechPrimary,
            accentColor = TechAccent,
            glowColor = TechGlow,
            subtopicCount = 19,
            subtopics = listOf(
                cpuTopic, gpuTopic, motherboardTopic, storageTopic, ramTopic, psuTopic, coolingTopic,
                mobileSocsTopic, mobileGpusTopic, batteriesChargingTopic,
                osWindowsTopic, osMacosTopic, osLinuxTopic,
                osAndroidTopic, osIosTopic, osHarmonyTopic,
                osGoogleTvTopic, osWebosTopic, osTizenTopic
            ),
            finalExamQuestions = FinalExamsData.techFinalExam,
            subHubs = listOf(
                com.example.model.SubHub("hardware", "الهاردوير"),
                com.example.model.SubHub("software", "السوفتوير")
            )
        ),
        Category(
            id = "space",
            title = "علوم الفضاء والفلك",
            tagline = "داخل مجموعتنا الشمسية (13 موضوعاً) وخارجها (5 مجالات كونية)",
            description = "انطلق في رحلة كونية ملهمة تبدأ من عوالم مجموعتنا الشمسية الـ 13 وصولاً إلى عوالم ما وراء المنظومة: المجرات، الثقوب السوداء، النجوم والسدم، الكواكب الخارجية، والظواهر الكونية الخارقة.",
            imageRes = R.drawable.img_cat_space,
            primaryColor = SpacePrimary,
            accentColor = SpaceAccent,
            glowColor = SpaceGlow,
            subtopicCount = 18,
            subtopics = SpaceTopics.allSpaceTopics,
            finalExamQuestions = FinalExamsData.spaceFinalExam,
            subHubs = listOf(
                com.example.model.SubHub("inside", "داخل مجموعتنا الشمسية (13)"),
                com.example.model.SubHub("outside", "خارج مجموعتنا الشمسية (5)")
            )
        ),
        Category(
            id = "mechanics",
            title = "الميكانيكا والهندسة",
            tagline = "المحركات، الفتيس، الهيدروليك، الفرامل، والعفشة",
            description = "استكشف أسرار الهندسة والميكانيكا: أسرار محرك السيارة والعزم مقابل الهورس باور، أنواع نواقل الحركة والفتيس، قوة الأنظمة الهيدروليكية، منظومة الفرامل الذكية (ABS/ESP)، وأنظمة التعليق والعفشة.",
            imageRes = R.drawable.img_cat_mechanics,
            primaryColor = MechPrimary,
            accentColor = MechAccent,
            glowColor = MechGlow,
            subtopicCount = 5,
            subtopics = listOf(engineTopic, transmissionTopic, hydraulicsTopic, brakesTopic, suspensionTopic),
            finalExamQuestions = FinalExamsData.mechanicsFinalExam
        )
    )

    fun getCategoryById(id: String): Category? {
        return allCategories.find { it.id == id }
    }

    fun getTopicById(id: String): SubTopic? {
        return allCategories.flatMap { it.subtopics }.find { it.id == id }
    }

    fun getAllTopics(): List<SubTopic> {
        return allCategories.flatMap { it.subtopics }
    }
}
