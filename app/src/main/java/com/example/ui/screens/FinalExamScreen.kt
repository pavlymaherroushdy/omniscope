package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
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
import com.example.model.Category
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.AccentEmerald
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.PrimaryCyan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinalExamScreen(
    category: Category,
    selectedAnswers: Map<Int, Int>,
    isSubmitted: Boolean,
    score: Int,
    onSelectAnswer: (Int, Int) -> Unit,
    onSubmitExam: () -> Unit,
    onResetExam: () -> Unit,
    onBackToCategory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val questions = category.finalExamQuestions
    val totalQuestions = questions.size
    val isPassed = totalQuestions > 0 && (score.toFloat() / totalQuestions >= 0.6f)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "الامتحان النهائي: ${category.title}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = onBackToCategory,
                    modifier = Modifier.testTag("final_exam_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "رجوع للقسم",
                        tint = PrimaryCyan
                    )
                }
            },
            actions = {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = category.primaryColor.copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, category.primaryColor.copy(alpha = 0.4f)),
                    modifier = Modifier.padding(end = 12.dp)
                ) {
                    Text(
                        text = "🏆 الامتحان الشامل",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = category.primaryColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("final_exam_questions_list"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Info Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = BorderStroke(1.dp, category.primaryColor.copy(alpha = 0.35f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = AccentAmber,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "تحدي التخرج وإتقان القسم",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "يحتوي هذا الامتحان على سؤال شامل ومكثف من كل موضوع فرعي قمت بدراسته في هذا القسم (${totalQuestions} أسئلة). اجتياز هذا الامتحان يمنحك وسام الإتقان النهائي للقسم!",
                            fontSize = 12.sp,
                            color = Color(0xFFCBD5E1),
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "الإجابات المكتملة: ${selectedAnswers.size} / $totalQuestions",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryCyan
                            )
                            val progress = if (totalQuestions > 0) selectedAnswers.size.toFloat() / totalQuestions else 0f
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .width(120.dp)
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = category.primaryColor,
                                trackColor = DarkSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Results Banner if Submitted
            if (isSubmitted) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("final_exam_result_card"),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isPassed) AccentEmerald.copy(alpha = 0.15f) else Color(0xFFEF4444).copy(alpha = 0.15f)
                        ),
                        border = BorderStroke(1.dp, if (isPassed) AccentEmerald else Color(0xFFEF4444))
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (isPassed) "🎉 مبارك! اجتزت الامتحان النهائي بنجاح!" else "⚡ نتيجة الامتحان النهائي",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isPassed) AccentEmerald else Color(0xFFEF4444)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "درجتك النهائية: $score من أصل $totalQuestions (${(score * 100) / totalQuestions.coerceAtLeast(1)}%)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (isPassed) {
                                    "لقد أثبتت تميزك وفهمك العميق لكافة أسرار ${category.title}! تم تتويجك بلقب الخبير المعتمد في هذا المجال. 🏆"
                                } else {
                                    "تحتاج إلى نسبة 60% على الأقل للاجتياز والحصول على وسام القسم. راجع الشروحات المفصلة بالأسفل وحاول مجدداً!"
                                },
                                fontSize = 12.sp,
                                color = Color(0xFFE2E8F0),
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            // Questions List
            itemsIndexed(questions) { qIdx, question ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("exam_question_card_$qIdx"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = BorderStroke(1.dp, Color(0xFF1E293B))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = category.primaryColor.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = question.subTopicTitle,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = category.primaryColor,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            Text(
                                text = "سؤال ${qIdx + 1} من $totalQuestions",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = question.question,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        question.options.forEachIndexed { optIdx, option ->
                            val isSelected = selectedAnswers[qIdx] == optIdx
                            val isCorrect = question.correctIndex == optIdx

                            val optionBg = when {
                                !isSubmitted -> if (isSelected) category.primaryColor.copy(alpha = 0.15f) else Color.Transparent
                                isCorrect -> AccentEmerald.copy(alpha = 0.2f)
                                isSelected && !isCorrect -> Color(0xFFEF4444).copy(alpha = 0.2f)
                                else -> Color.Transparent
                            }

                            val optionBorder = when {
                                !isSubmitted -> if (isSelected) category.primaryColor else Color(0xFF334155)
                                isCorrect -> AccentEmerald
                                isSelected && !isCorrect -> Color(0xFFEF4444)
                                else -> Color(0xFF1E293B)
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = optionBg,
                                border = BorderStroke(1.dp, optionBorder),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable(enabled = !isSubmitted) { onSelectAnswer(qIdx, optIdx) }
                                    .testTag("exam_q_${qIdx}_opt_$optIdx")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = if (!isSubmitted) { { onSelectAnswer(qIdx, optIdx) } } else null,
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = category.primaryColor,
                                            unselectedColor = Color.Gray
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = option,
                                        fontSize = 12.sp,
                                        color = if (isSelected) Color.White else Color(0xFFCBD5E1),
                                        lineHeight = 17.sp
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
                                    modifier = Modifier.padding(10.dp),
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }

            // Action Buttons
            item {
                Spacer(modifier = Modifier.height(8.dp))
                if (!isSubmitted) {
                    Button(
                        onClick = onSubmitExam,
                        enabled = selectedAnswers.size == totalQuestions,
                        colors = ButtonDefaults.buttonColors(containerColor = category.primaryColor),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("submit_final_exam_button")
                    ) {
                        Text(
                            text = "تسليم الامتحان النهائي واعتماد النتيجة 🏆",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF0F172A)
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onResetExam,
                            colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("reset_final_exam_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "إعادة الامتحان",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 13.sp
                            )
                        }

                        Button(
                            onClick = onBackToCategory,
                            colors = ButtonDefaults.buttonColors(containerColor = category.primaryColor),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("back_from_exam_button")
                        ) {
                            Text(
                                text = "العودة للقسم",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A),
                                fontSize = 13.sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
