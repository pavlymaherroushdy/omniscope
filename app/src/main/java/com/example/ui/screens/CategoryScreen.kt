package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Category
import com.example.model.SubTopic
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.AccentEmerald
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.PrimaryCyan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    category: Category,
    completedSubTopicIds: Set<String> = emptySet(),
    passedFinalExams: Set<String> = emptySet(),
    onBackToMainMenu: () -> Unit,
    onTopicClick: (String) -> Unit,
    onStartFinalExam: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val totalSubtopics = category.subtopics.size
    val completedCount = category.subtopics.count { completedSubTopicIds.contains(it.id) }
    val allCompleted = totalSubtopics > 0 && completedCount == totalSubtopics
    val isExamPassed = passedFinalExams.contains(category.id)

    var selectedHubId by remember { mutableStateOf("all") }
    val filteredSubtopics = remember(selectedHubId, category.subtopics) {
        if (selectedHubId == "all") category.subtopics
        else category.subtopics.filter { it.hubId == selectedHubId }
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize().background(DarkBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top App Bar with prominent "رجوع" button
            TopAppBar(
                title = {
                    Text(
                        text = category.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    Button(
                        onClick = onBackToMainMenu,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryCyan),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .testTag("back_to_main_menu_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "القائمة الرئيسية",
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFF0F172A)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "رجوع للرئيسية",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                    }
                },
                actions = {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (allCompleted) AccentEmerald.copy(alpha = 0.2f) else DarkSurfaceVariant,
                        border = BorderStroke(1.dp, if (allCompleted) AccentEmerald else Color.Transparent),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (allCompleted) Icons.Default.CheckCircle else Icons.Default.Tune,
                                contentDescription = null,
                                tint = if (allCompleted) AccentEmerald else PrimaryCyan,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$completedCount / $totalSubtopics مكتمل",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (allCompleted) AccentEmerald else Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("category_subtopics_list"),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Category Banner Header
                item {
                    CategoryHeaderCard(category = category)
                }

                // Section Title
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "الموضوعات والمحاور الفرعية",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = category.primaryColor,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "اختر موضوعاً لبدء الاستكشاف التفاعلي",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = DarkSurfaceVariant
                        ) {
                            Text(
                                text = "$totalSubtopics مواضيع",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // Sub-Hubs Filter Chips (e.g. [الكل], [الهاردوير], [السوفتوير])
                if (category.subHubs.isNotEmpty()) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = selectedHubId == "all",
                                onClick = { selectedHubId = "all" },
                                label = { Text("الكل", fontWeight = FontWeight.Bold) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = category.primaryColor,
                                    selectedLabelColor = Color(0xFF0F172A),
                                    containerColor = DarkSurfaceVariant,
                                    labelColor = Color.White
                                )
                            )
                            category.subHubs.forEach { hub ->
                                FilterChip(
                                    selected = selectedHubId == hub.id,
                                    onClick = { selectedHubId = hub.id },
                                    label = { Text(hub.title, fontWeight = FontWeight.Bold) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = category.primaryColor,
                                        selectedLabelColor = Color(0xFF0F172A),
                                        containerColor = DarkSurfaceVariant,
                                        labelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }

                // List of Sub-Topics with Checkmarks
                items(filteredSubtopics) { subtopic ->
                    val isCompleted = completedSubTopicIds.contains(subtopic.id)
                    SubTopicCard(
                        subtopic = subtopic,
                        categoryColor = category.primaryColor,
                        isCompleted = isCompleted,
                        onClick = { onTopicClick(subtopic.id) }
                    )
                }

                // Final Category Exam Section (Locked until all sub-topics have checkmarks)
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    FinalExamUnlockCard(
                        category = category,
                        completedCount = completedCount,
                        totalCount = totalSubtopics,
                        isUnlocked = allCompleted,
                        isPassed = isExamPassed,
                        onStartExam = { onStartFinalExam(category.id) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun CategoryHeaderCard(category: Category) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, category.primaryColor.copy(alpha = 0.4f), RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                Image(
                    painter = painterResource(id = category.imageRes),
                    contentDescription = category.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, DarkSurface.copy(alpha = 0.95f)),
                                startY = 40f
                            )
                        )
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = category.tagline,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = category.primaryColor
                )
                Text(
                    text = category.title,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = category.description,
                    fontSize = 12.sp,
                    color = Color(0xFFCBD5E1),
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun SubTopicCard(
    subtopic: SubTopic,
    categoryColor: Color,
    isCompleted: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .border(
                1.dp,
                if (isCompleted) AccentEmerald.copy(alpha = 0.6f) else Color(0xFF1E293B),
                RoundedCornerShape(18.dp)
            )
            .clickable { onClick() }
            .testTag("subtopic_card_${subtopic.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) Color(0xFF0D1E1A) else DarkSurface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = categoryColor.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, categoryColor.copy(alpha = 0.35f))
                ) {
                    Text(
                        text = subtopic.badge,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = categoryColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = subtopic.readTime,
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Sub-Topic Title + Permanent Checkmark if completed
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = subtopic.title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )

                if (isCompleted) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = AccentEmerald.copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, AccentEmerald),
                        modifier = Modifier.testTag("checkmark_${subtopic.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "✔️ مكتمل",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = AccentEmerald
                            )
                        }
                    }
                }
            }

            Text(
                text = subtopic.subtitle,
                fontSize = 12.sp,
                color = Color(0xFF94A3B8),
                lineHeight = 17.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 10.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = DarkSurfaceVariant
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            tint = PrimaryCyan,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${subtopic.components.size} أجزاء تفاعلية",
                            fontSize = 10.sp,
                            color = Color(0xFFCBD5E1),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                if (subtopic.comparisons.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = DarkSurfaceVariant
                    ) {
                        Text(
                            text = "مقارنة تفاعلية حية",
                            fontSize = 10.sp,
                            color = PrimaryCyan,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = DarkSurfaceVariant
                ) {
                    Text(
                        text = "اختبار من 3 أسئلة",
                        fontSize = 10.sp,
                        color = if (isCompleted) AccentEmerald else Color(0xFF94A3B8),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCompleted) categoryColor.copy(alpha = 0.85f) else categoryColor
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("open_topic_${subtopic.id}")
            ) {
                Text(
                    text = if (isCompleted) "مراجعة الشرح والمحاكي التفاعلي ←" else "فتح الشرح والمحاكي التفاعلي ←",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFF0F172A)
                )
            }
        }
    }
}

@Composable
fun FinalExamUnlockCard(
    category: Category,
    completedCount: Int,
    totalCount: Int,
    isUnlocked: Boolean,
    isPassed: Boolean,
    onStartExam: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(
                1.5.dp,
                if (isUnlocked) AccentAmber else Color(0xFF334155),
                RoundedCornerShape(20.dp)
            )
            .testTag("final_exam_unlock_card"),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) Color(0xFF1E170A) else DarkSurface
        )
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isUnlocked) Icons.Default.EmojiEvents else Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (isUnlocked) AccentAmber else Color(0xFF94A3B8),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "الامتحان النهائي الشامل للقسم",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isUnlocked) Color.White else Color(0xFF94A3B8)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isUnlocked) AccentAmber.copy(alpha = 0.2f) else DarkSurfaceVariant,
                    border = BorderStroke(1.dp, if (isUnlocked) AccentAmber else Color.Transparent)
                ) {
                    Text(
                        text = if (isUnlocked) {
                            if (isPassed) "🏆 تم الاجتياز" else "🔓 مفتوح الآن"
                        } else {
                            "🔒 مغلق"
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isUnlocked) AccentAmber else Color(0xFF94A3B8),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (!isUnlocked) {
                Text(
                    text = "أكمل جميع اختبارات الموضوعات الفرعية أولاً للحصول على شارات الإكمال (✔️) وفتح الامتحان النهائي لهذا القسم! تم إكمال $completedCount من أصل $totalCount موضوعات.",
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8),
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                val progress = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = category.primaryColor,
                    trackColor = DarkSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {},
                    enabled = false,
                    colors = ButtonDefaults.buttonColors(disabledContainerColor = DarkSurfaceVariant),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("locked_final_exam_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "الامتحان النهائي مغلق (${completedCount}/${totalCount})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            } else {
                Text(
                    text = if (isPassed) {
                        "رائع جداً! لقد أكملت كل الموضوعات واجتزت الامتحان النهائي لهذا القسم بنجاح. يمكنك إعادة خوض الامتحان في أي وقت لاختبار وتحديث معلوماتك."
                    } else {
                        "تهانينا! لقد حصلت على جميع شارات الإكمال (✔️) في هذا القسم. لقد تم إلغاء القفل وأصبح بإمكانك الآن خوض الامتحان النهائي الشامل المكون من سؤال واحد لكل موضوع!"
                    },
                    fontSize = 12.sp,
                    color = Color(0xFFE2E8F0),
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(14.dp))
                Button(
                    onClick = onStartExam,
                    colors = ButtonDefaults.buttonColors(containerColor = AccentAmber),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("start_final_exam_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isPassed) "إعادة خوض الامتحان النهائي الشامل 🏆" else "ابدأ الامتحان النهائي الشامل الآن 🏆",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp,
                        color = Color(0xFF0F172A)
                    )
                }
            }
        }
    }
}
