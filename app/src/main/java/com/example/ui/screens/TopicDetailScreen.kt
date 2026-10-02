package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ComparisonItem
import com.example.model.FunFact
import com.example.model.ProcessStep
import com.example.model.QuizQuestion
import com.example.model.SubTopic
import com.example.model.TopicComponent
import com.example.ui.components.InteractiveSimulationViewer
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.AccentEmerald
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.PrimaryCyan
import com.example.ui.theme.PrimaryIndigo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicDetailScreen(
    topic: SubTopic,
    selectedComponentId: String?,
    selectedTab: Int,
    isBookmarked: Boolean,
    selectedQuizAnswers: Map<Int, Int>,
    isQuizSubmitted: Boolean,
    quizScore: Int = 0,
    isQuizPassed: Boolean = false,
    onBackToMainMenu: () -> Unit,
    onBackToCategory: () -> Unit,
    onComponentSelect: (String) -> Unit,
    onTabSelect: (Int) -> Unit,
    onBookmarkToggle: () -> Unit,
    onSelectQuizAnswer: (Int, Int) -> Unit,
    onSubmitQuiz: () -> Unit,
    onResetQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedComponent = topic.components.find { it.id == selectedComponentId }
        ?: topic.components.firstOrNull()

    val firstTabName = when {
        topic.categoryId == "space" && topic.hubId == "outside" -> "الأجرام والظواهر"
        topic.categoryId == "space" -> "البيانات الفلكية"
        else -> "المكونات"
    }

    val tabs = if (topic.comparisons.isNotEmpty()) {
        listOf(firstTabName, "كيف يعمل", "المقارنة", "معلومات ممتعة", "اختبار التحدي")
    } else {
        listOf(firstTabName, "كيف يعمل", "معلومات ممتعة", "اختبار التحدي")
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize().background(DarkBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Bar with prominent "رجوع" (Back to Main Menu) button
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = topic.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White,
                            maxLines = 1
                        )
                        Text(
                            text = topic.badge,
                            fontSize = 11.sp,
                            color = PrimaryCyan
                        )
                    }
                },
                navigationIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        // Prominent "رجوع" (Back to Main Menu) Button
                        Button(
                            onClick = onBackToMainMenu,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryCyan),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("detail_back_to_main_menu_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "القائمة الرئيسية",
                                modifier = Modifier.size(15.dp),
                                tint = Color(0xFF0F172A)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "رجوع للرئيسية",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        // Back to Category
                        IconButton(
                            onClick = onBackToCategory,
                            modifier = Modifier.testTag("detail_back_to_category_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "رجوع للقسم",
                                tint = Color.LightGray
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = onBookmarkToggle,
                        modifier = Modifier.testTag("bookmark_toggle_button")
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "حفظ",
                            tint = if (isBookmarked) PrimaryCyan else Color.Gray
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("topic_detail_scroll"),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Interactive Simulation Lab
                item {
                    InteractiveSimulationViewer(
                        simulationType = topic.simulationType,
                        selectedComponentId = selectedComponentId
                    )
                }

                // Overview Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        border = BorderStroke(1.dp, Color(0xFF1E293B))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = PrimaryCyan,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "نظرة عامة وشرح المفهوم",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryCyan,
                                    letterSpacing = 0.5.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = topic.overview,
                                fontSize = 13.sp,
                                color = Color(0xFFCBD5E1),
                                lineHeight = 20.sp
                            )
                        }
                    }
                }

                // Tabs
                item {
                    val safeTabIndex = selectedTab.coerceIn(0, tabs.size - 1)
                    PrimaryScrollableTabRow(
                        selectedTabIndex = safeTabIndex,
                        containerColor = DarkSurface,
                        contentColor = PrimaryCyan,
                        indicator = {
                            TabRowDefaults.PrimaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(safeTabIndex),
                                color = PrimaryCyan
                            )
                        },
                        edgePadding = 8.dp,
                        modifier = Modifier.clip(RoundedCornerShape(12.dp))
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = safeTabIndex == index,
                                onClick = { onTabSelect(index) },
                                text = {
                                    Text(
                                        text = title,
                                        fontSize = 12.sp,
                                        fontWeight = if (safeTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                        color = if (safeTabIndex == index) PrimaryCyan else Color.Gray
                                    )
                                },
                                modifier = Modifier.testTag("tab_$index")
                            )
                        }
                    }
                }

                // Tab Content Switcher
                val hasComparisons = topic.comparisons.isNotEmpty()
                val currentTabName = tabs.getOrNull(selectedTab.coerceIn(0, tabs.size - 1)) ?: "المكونات"

                when (currentTabName) {
                    firstTabName, "المكونات" -> {
                        item {
                            ComponentInspectorSection(
                                components = topic.components,
                                selectedComponentId = selectedComponentId,
                                selectedComponent = selectedComponent,
                                onComponentSelect = onComponentSelect,
                                title = when {
                                    topic.categoryId == "space" && topic.hubId == "outside" -> "الأجرام والظواهر المختارة (اضغط لتفحص كل جرم)"
                                    topic.categoryId == "space" -> "البيانات الفلكية الأساسية (اضغط للتفاصيل)"
                                    else -> "المكونات الرئيسية (اضغط على أي جزء لتفحصه)"
                                }
                            )
                        }
                    }
                    "كيف يعمل" -> {
                        items(topic.processSteps) { step ->
                            ProcessStepCard(step = step)
                        }
                    }
                    "المقارنة" -> {
                        items(topic.comparisons) { item ->
                            ComparisonCard(comparison = item)
                        }
                    }
                    "معلومات ممتعة" -> {
                        items(topic.funFacts) { fact ->
                            FunFactCard(fact = fact)
                        }
                    }
                    "اختبار التحدي" -> {
                        item {
                            QuizSection(
                                questions = topic.quizQuestions,
                                selectedAnswers = selectedQuizAnswers,
                                isSubmitted = isQuizSubmitted,
                                score = quizScore,
                                isPassed = isQuizPassed,
                                onSelectAnswer = onSelectQuizAnswer,
                                onSubmit = onSubmitQuiz,
                                onReset = onResetQuiz
                            )
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Clickable Key Components Inspector (فاحص المكونات التفاعلي)
// -------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ComponentInspectorSection(
    components: List<TopicComponent>,
    selectedComponentId: String?,
    selectedComponent: TopicComponent?,
    onComponentSelect: (String) -> Unit,
    title: String = "المكونات الرئيسية (اضغط على أي جزء لتفحصه)"
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryCyan
        )
        Spacer(modifier = Modifier.height(10.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            components.forEach { comp ->
                val isSelected = comp.id == selectedComponentId
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) PrimaryCyan else DarkSurfaceVariant,
                    border = BorderStroke(
                        1.dp,
                        if (isSelected) PrimaryCyan else Color(0xFF334155)
                    ),
                    modifier = Modifier
                        .clickable { onComponentSelect(comp.id) }
                        .testTag("component_chip_${comp.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Extension,
                            contentDescription = null,
                            tint = if (isSelected) Color(0xFF0F172A) else Color.LightGray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = comp.name,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color(0xFF0F172A) else Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        selectedComponent?.let { comp ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, PrimaryCyan.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = comp.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = comp.role,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = PrimaryCyan
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = DarkSurfaceVariant,
                            border = BorderStroke(1.dp, Color(0xFF334155))
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = comp.statLabel,
                                    fontSize = 9.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = comp.statValue,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentAmber
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = comp.shortSummary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFE2E8F0)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = comp.detailedExplanation,
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8),
                        lineHeight = 19.sp
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Comparison Card (بطاقة المقارنة التفاعلية المفصلة)
// -------------------------------------------------------------
@Composable
fun ComparisonCard(comparison: ComparisonItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = comparison.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Text(
                        text = comparison.subtitle,
                        fontSize = 11.sp,
                        color = PrimaryCyan
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = PrimaryCyan.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, PrimaryCyan.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = comparison.speedValue,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryCyan,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = DarkSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "آلية العمل: ",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = comparison.mechanism,
                        fontSize = 11.sp,
                        color = Color(0xFFCBD5E1)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Pros
            Text(
                text = "المميزات:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = AccentEmerald
            )
            comparison.pros.forEach { pro ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = AccentEmerald,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = pro,
                        fontSize = 11.sp,
                        color = Color(0xFFCBD5E1)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Cons
            Text(
                text = "العيوب أو المحددات:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFEF4444)
            )
            comparison.cons.forEach { con ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = con,
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = AccentAmber.copy(alpha = 0.12f),
                border = BorderStroke(1.dp, AccentAmber.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "🎯 الاستخدام الأنسب: ${comparison.bestFor}",
                    fontSize = 11.sp,
                    color = AccentAmber,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

// -------------------------------------------------------------
// Process Step Card (خطوات كيف يعمل)
// -------------------------------------------------------------
@Composable
fun ProcessStepCard(step: ProcessStep) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = BorderStroke(1.dp, Color(0xFF1E293B))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                shape = CircleShape,
                color = PrimaryIndigo.copy(alpha = 0.25f),
                border = BorderStroke(1.dp, PrimaryIndigo),
                modifier = Modifier.size(34.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "${step.stepNumber}",
                        fontWeight = FontWeight.Bold,
                        color = PrimaryCyan,
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = step.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = DarkSurfaceVariant
                    ) {
                        Text(
                            text = step.highlight,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AccentAmber,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = step.description,
                    fontSize = 12.sp,
                    color = Color(0xFFCBD5E1),
                    lineHeight = 18.sp
                )
            }
        }
    }
}

// -------------------------------------------------------------
// Fun Fact Card (حقائق علمية ممتعة)
// -------------------------------------------------------------
@Composable
fun FunFactCard(fact: FunFact) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = BorderStroke(1.dp, AccentAmber.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = AccentAmber.copy(alpha = 0.15f),
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = AccentAmber,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = fact.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = DarkSurfaceVariant
                    ) {
                        Text(
                            text = fact.category,
                            fontSize = 10.sp,
                            color = AccentAmber,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = fact.fact,
                    fontSize = 12.sp,
                    color = Color(0xFFCBD5E1),
                    lineHeight = 18.sp
                )
            }
        }
    }
}

// -------------------------------------------------------------
// Quiz Section (اختبار التحدي التفاعلي)
// -------------------------------------------------------------
@Composable
fun QuizSection(
    questions: List<QuizQuestion>,
    selectedAnswers: Map<Int, Int>,
    isSubmitted: Boolean,
    score: Int,
    isPassed: Boolean,
    onSelectAnswer: (Int, Int) -> Unit,
    onSubmit: () -> Unit,
    onReset: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "تحدي قياس الفهم والاستيعاب (3 أسئلة)",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryCyan
        )
        Spacer(modifier = Modifier.height(10.dp))

        if (isSubmitted) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .testTag("quiz_result_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isPassed) AccentEmerald.copy(alpha = 0.15f) else Color(0xFFEF4444).copy(alpha = 0.15f)
                ),
                border = BorderStroke(1.dp, if (isPassed) AccentEmerald else Color(0xFFEF4444))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (isPassed) "🎉 أحسنت! تم اجتياز الاختبار بنجاح!" else "⚡ نتيجة الاختبار",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isPassed) AccentEmerald else Color(0xFFEF4444)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "الدرجة: $score من ${questions.size}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isPassed) {
                            "✔️ مبروك! لقد حصلت على علامة الصح الخضراء الدائمة في قائمة القسم وتقدمت نحو فتح الامتحان النهائي!"
                        } else {
                            "تحتاج إلى إجابتين صحيحتين على الأقل لنيل شارة الإكمال (✔️). راجع التفسيرات العلمية أدناه وأعد المحاولة!"
                        },
                        fontSize = 11.sp,
                        color = Color(0xFFCBD5E1),
                        lineHeight = 16.sp
                    )
                }
            }
        }

        questions.forEachIndexed { qIdx, question ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = BorderStroke(1.dp, Color(0xFF1E293B))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "السؤال ${qIdx + 1}: ${question.question}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    question.options.forEachIndexed { optIdx, option ->
                        val isSelected = selectedAnswers[qIdx] == optIdx
                        val isCorrect = question.correctIndex == optIdx
                        val optionBg = when {
                            !isSubmitted -> if (isSelected) PrimaryCyan.copy(alpha = 0.15f) else Color.Transparent
                            isCorrect -> AccentEmerald.copy(alpha = 0.2f)
                            isSelected && !isCorrect -> Color(0xFFEF4444).copy(alpha = 0.2f)
                            else -> Color.Transparent
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = optionBg,
                            border = BorderStroke(
                                1.dp,
                                when {
                                    !isSubmitted -> if (isSelected) PrimaryCyan else Color(0xFF334155)
                                    isCorrect -> AccentEmerald
                                    isSelected && !isCorrect -> Color(0xFFEF4444)
                                    else -> Color(0xFF1E293B)
                                }
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable(enabled = !isSubmitted) { onSelectAnswer(qIdx, optIdx) }
                                .testTag("quiz_${qIdx}_opt_$optIdx")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = if (!isSubmitted) { { onSelectAnswer(qIdx, optIdx) } } else null,
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = PrimaryCyan,
                                        unselectedColor = Color.Gray
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = option,
                                    fontSize = 12.sp,
                                    color = if (isSelected) Color.White else Color(0xFFCBD5E1)
                                )
                            }
                        }
                    }

                    if (isSubmitted) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = DarkSurfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "💡 التفسير العلمي: ${question.explanation}",
                                fontSize = 11.sp,
                                color = PrimaryCyan,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        if (!isSubmitted) {
            Button(
                onClick = onSubmit,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryCyan),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .testTag("submit_quiz_button"),
                enabled = selectedAnswers.size == questions.size
            ) {
                Text(
                    text = "تأكيد الإجابات ومعرفة النتيجة والشرح",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    fontSize = 13.sp
                )
            }
        } else {
            Button(
                onClick = onReset,
                colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .testTag("reset_quiz_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "إعادة التحدي مرة أخرى",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 13.sp
                )
            }
        }
    }
}
