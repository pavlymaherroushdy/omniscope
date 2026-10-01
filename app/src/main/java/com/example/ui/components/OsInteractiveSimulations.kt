package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SimulationType
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun OsInteractiveLab(
    simulationType: SimulationType,
    selectedComponentId: String?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        when (simulationType) {
            SimulationType.OS_ANDROID -> AndroidInteractiveSim()
            SimulationType.OS_IOS -> IosInteractiveSim()
            SimulationType.OS_HARMONY -> HarmonyInteractiveSim()
            SimulationType.OS_GOOGLE_TV -> GoogleTvInteractiveSim()
            SimulationType.OS_WEBOS -> WebosInteractiveSim()
            SimulationType.OS_TIZEN -> TizenInteractiveSim()
            SimulationType.OS_WINDOWS -> WindowsInteractiveSim()
            SimulationType.OS_MACOS -> MacosInteractiveSim()
            SimulationType.OS_LINUX -> LinuxInteractiveSim()
            else -> GenericInteractiveLabArabic(simulationType, selectedComponentId)
        }
    }
}

// =========================================================================
// 1. محاكي أندرويد (Android: APK Sideloading, Customization, Google Services)
// =========================================================================
@Composable
fun AndroidInteractiveSim() {
    var activeMode by remember { mutableStateOf("sideload") } // sideload, launcher, gms
    var apkStep by remember { mutableIntStateOf(0) } // 0: Idle, 1: Scanning, 2: Installing, 3: Completed
    var allowUnknownSources by remember { mutableStateOf(false) }
    var selectedThemeColor by remember { mutableStateOf(Color(0xFF10B981)) }
    var gridColumns by remember { mutableIntStateOf(4) }
    var gmsEnabled by remember { mutableStateOf(true) }

    LaunchedEffect(apkStep) {
        if (apkStep == 1) {
            delay(1200)
            apkStep = 2
        } else if (apkStep == 2) {
            delay(1400)
            apkStep = 3
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "⚡ محاكي بيئة أندرويد التفاعلية",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color(0xFF34D399)
            )
            Surface(
                color = Color(0xFF064E3B),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "AOSP + GMS",
                    fontSize = 10.sp,
                    color = Color(0xFF34D399),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Mode switchers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(
                selected = activeMode == "sideload",
                onClick = { activeMode = "sideload" },
                label = { Text("تثبيت APK خارجي", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF059669),
                    selectedLabelColor = Color.White
                )
            )
            FilterChip(
                selected = activeMode == "launcher",
                onClick = { activeMode = "launcher" },
                label = { Text("تخصيص الواجهة", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF059669),
                    selectedLabelColor = Color.White
                )
            )
            FilterChip(
                selected = activeMode == "gms",
                onClick = { activeMode = "gms" },
                label = { Text("خدمات جوجل", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF059669),
                    selectedLabelColor = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (activeMode) {
            "sideload" -> {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF062C22)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "السماح بالتثبيت من مصادر غير معروفة",
                                fontSize = 12.sp,
                                color = Color.White
                            )
                            Switch(
                                checked = allowUnknownSources,
                                onCheckedChange = {
                                    allowUnknownSources = it
                                    if (!it) apkStep = 0
                                },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF10B981))
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                                .background(Color(0xFF021E17), RoundedCornerShape(8.dp))
                                .border(1.dp, Color(0xFF059669).copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            when (apkStep) {
                                0 -> {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            imageVector = Icons.Default.Android,
                                            contentDescription = null,
                                            tint = if (allowUnknownSources) Color(0xFF34D399) else Color.Gray,
                                            modifier = Modifier.size(32.dp)
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = if (allowUnknownSources) "الحزمة RetroLab_v2.4.apk جاهزة للتثبيت" else "فعّل خيار السماح بالتثبيت للمتابعة",
                                            fontSize = 11.sp,
                                            color = if (allowUnknownSources) Color.White else Color.Gray
                                        )
                                    }
                                }
                                1 -> {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        CircularProgressIndicator(
                                            color = Color(0xFF34D399),
                                            modifier = Modifier.size(28.dp),
                                            strokeWidth = 3.dp
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "فحص الحزمة عبر Google Play Protect...",
                                            fontSize = 11.sp,
                                            color = Color(0xFF34D399)
                                        )
                                    }
                                }
                                2 -> {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        LinearProgressIndicator(
                                            color = Color(0xFF10B981),
                                            modifier = Modifier.fillMaxWidth(0.7f)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "جاري فك الحزمة وتثبيت ملفات APK في /data/app...",
                                            fontSize = 11.sp,
                                            color = Color.White
                                        )
                                    }
                                }
                                3 -> {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = Color(0xFF10B981),
                                            modifier = Modifier.size(32.dp)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "تم تثبيت التطبيق بنجاح! حرية كاملة خارج المتجر",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF34D399)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                if (allowUnknownSources) {
                                    apkStep = 1
                                }
                            },
                            enabled = allowUnknownSources && (apkStep == 0 || apkStep == 3),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (apkStep == 3) "إعادة تجربة التثبيت الخارجي" else "تثبيت ملف APK الجانبي (Sideload)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
            "launcher" -> {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0B1D28)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "لوحة ألوان Material You التكيفية:", fontSize = 12.sp, color = Color.White)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            listOf(
                                Color(0xFF10B981),
                                Color(0xFF3B82F6),
                                Color(0xFFEC4899),
                                Color(0xFFF59E0B),
                                Color(0xFF8B5CF6)
                            ).forEach { color ->
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .border(
                                            width = if (selectedThemeColor == color) 3.dp else 1.dp,
                                            color = if (selectedThemeColor == color) Color.White else Color.Transparent,
                                            shape = CircleShape
                                        )
                                        .clickable { selectedThemeColor = color }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(text = "تخطيط شبكة الشاشة الرئيسية (Grid):", fontSize = 12.sp, color = Color.White)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf(4 to "4 × 5", 5 to "5 × 6").forEach { (cols, label) ->
                                Button(
                                    onClick = { gridColumns = cols },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (gridColumns == cols) selectedThemeColor else Color(0xFF1E293B)
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(label, fontSize = 11.sp, color = Color.White)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Preview Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                                .background(Color(0xFF020B14), RoundedCornerShape(8.dp))
                                .border(1.dp, selectedThemeColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                repeat(gridColumns) { idx ->
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(selectedThemeColor),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = when (idx % 4) {
                                                    0 -> Icons.Default.Call
                                                    1 -> Icons.Default.Chat
                                                    2 -> Icons.Default.CameraAlt
                                                    else -> Icons.Default.Settings
                                                },
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Box(
                                            modifier = Modifier
                                                .width(24.dp)
                                                .height(4.dp)
                                                .background(Color.Gray.copy(alpha = 0.4f), RoundedCornerShape(2.dp))
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            "gms" -> {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF131E29)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "حالة خدمات Google Play (GMS Core):",
                                fontSize = 12.sp,
                                color = Color.White
                            )
                            Switch(
                                checked = gmsEnabled,
                                onCheckedChange = { gmsEnabled = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF38BDF8))
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        val statusText = if (gmsEnabled) {
                            "المتجر وخرائط جوجل والإشعارات السحابية (FCM) تعمل بكفاءة تامة"
                        } else {
                            "وضع AOSP النقي (بدون تتبع جوجل، استهلاك بطارية أقل، وتوافق عبر MicroG)"
                        }

                        Surface(
                            color = if (gmsEnabled) Color(0xFF0369A1).copy(alpha = 0.3f) else Color(0xFFD97706).copy(alpha = 0.3f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = statusText,
                                fontSize = 11.sp,
                                color = if (gmsEnabled) Color(0xFF7DD3FC) else Color(0xFFFDE68A),
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// =========================================================================
// 2. محاكي آبل (Apple iOS: Sandbox, Secure Enclave, AirDrop Ecosystem)
// =========================================================================
@Composable
fun IosInteractiveSim() {
    var activeTab by remember { mutableStateOf("sandbox") } // sandbox, enclave, airdrop
    var attemptAccessPhotos by remember { mutableStateOf(false) }
    var faceIdScanning by remember { mutableStateOf(false) }
    var faceIdSuccess by remember { mutableStateOf(false) }
    var airdropProgress by remember { mutableFloatStateOf(0f) }
    var airdropDevice by remember { mutableStateOf("MacBook Pro M3") }

    LaunchedEffect(faceIdScanning) {
        if (faceIdScanning) {
            faceIdSuccess = false
            delay(1500)
            faceIdScanning = false
            faceIdSuccess = true
        }
    }

    LaunchedEffect(airdropProgress) {
        if (airdropProgress in 0.01f..0.99f) {
            delay(80)
            airdropProgress = (airdropProgress + 0.15f).coerceAtMost(1f)
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🛡️ محاكي أمان وتناغم نظام Apple iOS",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color(0xFF60A5FA)
            )
            Surface(
                color = Color(0xFF1E3A8A),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Sandbox & Enclave",
                    fontSize = 10.sp,
                    color = Color(0xFF93C5FD),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(
                selected = activeTab == "sandbox",
                onClick = { activeTab = "sandbox" },
                label = { Text("صندوق الرمل (Sandbox)", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF2563EB),
                    selectedLabelColor = Color.White
                )
            )
            FilterChip(
                selected = activeTab == "enclave",
                onClick = { activeTab = "enclave" },
                label = { Text("شريحة Secure Enclave", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF2563EB),
                    selectedLabelColor = Color.White
                )
            )
            FilterChip(
                selected = activeTab == "airdrop",
                onClick = { activeTab = "airdrop" },
                label = { Text("تناغم AirDrop", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF2563EB),
                    selectedLabelColor = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (activeTab) {
            "sandbox" -> {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF111827)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "محاكاة محاولة تطبيق خارجي الوصول للصور بدون إذن:",
                            fontSize = 12.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .background(Color(0xFF030712), RoundedCornerShape(8.dp))
                                .border(1.dp, Color(0xFF374151), RoundedCornerShape(8.dp))
                                .padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (!attemptAccessPhotos) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = Color(0xFF60A5FA),
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Text(
                                        text = "التطبيق معزول في بيئة Container مغلقة تماماً عن ملفات النظام",
                                        fontSize = 11.sp,
                                        color = Color.LightGray
                                    )
                                }
                            } else {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.Shield,
                                        contentDescription = null,
                                        tint = Color(0xFFEF4444),
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "⛔ تم حظر الوصول العشوائي تلقائياً بواسطة iOS Kernel",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFF87171)
                                    )
                                    Text(
                                        text = "يجب موافقة صريحة من المستخدم عبر نافذة أمان النظام الموحدة",
                                        fontSize = 10.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = { attemptAccessPhotos = !attemptAccessPhotos },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (attemptAccessPhotos) Color(0xFF374151) else Color(0xFF2563EB)
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (attemptAccessPhotos) "إعادة ضبط العزل" else "محاكاة اختراق حدود التطبيق",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
            "enclave" -> {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "فحص وتشفير البصمة الحيوية (Face ID Biometrics):",
                            fontSize = 12.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1E293B))
                                .border(
                                    width = 2.dp,
                                    color = if (faceIdSuccess) Color(0xFF10B981) else Color(0xFF3B82F6),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (faceIdScanning) {
                                CircularProgressIndicator(
                                    color = Color(0xFF60A5FA),
                                    modifier = Modifier.size(50.dp),
                                    strokeWidth = 3.dp
                                )
                            } else if (faceIdSuccess) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF10B981),
                                    modifier = Modifier.size(46.dp)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Face,
                                    contentDescription = null,
                                    tint = Color(0xFF94A3B8),
                                    modifier = Modifier.size(46.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = if (faceIdSuccess) "تم التحقق داخل شريحة Secure Enclave المشفرة عتادياً" else "شريحة مستقلة لا يمكن حتى لنواة النظام قراءة مفاتيحها",
                            fontSize = 11.sp,
                            color = if (faceIdSuccess) Color(0xFF34D399) else Color.Gray
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = { faceIdScanning = true },
                            enabled = !faceIdScanning,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("محاكاة مطابقة الوجه (Face ID Scan)", fontSize = 12.sp, color = Color.White)
                        }
                    }
                }
            }
            "airdrop" -> {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "الإرسال الفوري عبر منظومة آبل المتناغمة:", fontSize = 12.sp, color = Color.White)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("MacBook Pro M3", "iPad Pro", "Apple Watch").forEach { dev ->
                                Surface(
                                    color = if (airdropDevice == dev) Color(0xFF1D4ED8) else Color(0xFF1E293B),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.clickable { airdropDevice = dev }
                                ) {
                                    Text(
                                        text = dev,
                                        fontSize = 11.sp,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LinearProgressIndicator(
                            progress = { airdropProgress },
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFF38BDF8),
                            trackColor = Color(0xFF1E293B),
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (airdropProgress >= 1f) "✅ تم تسليم الملف إلى $airdropDevice عبر شبكة نظير لنظير P2P" else "السرعة: 120MB/s عبر بروتوكول مشفر مباشر",
                            fontSize = 11.sp,
                            color = if (airdropProgress >= 1f) Color(0xFF34D399) else Color.Gray
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = { airdropProgress = 0.05f },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("إرسال ملف فائق الدقة عبر AirDrop", fontSize = 12.sp, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

// =========================================================================
// 3. محاكي هارموني (HarmonyOS: Super Device Radar, DSoftBus, Microkernel)
// =========================================================================
@Composable
fun HarmonyInteractiveSim() {
    var connectedTablet by remember { mutableStateOf(false) }
    var connectedTv by remember { mutableStateOf(false) }
    var connectedWatch by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🌐 محاكي الجهاز الفائق (Super Device)",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color(0xFFF43F5E)
            )
            Surface(
                color = Color(0xFF881337),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "DSoftBus 0.8ms",
                    fontSize = 10.sp,
                    color = Color(0xFFFECDD3),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "انقر على الأجهزة المحيطة بالهاتف لربطها فورياً كجهاز حوسبة فائق وموحد:",
            fontSize = 12.sp,
            color = Color.LightGray
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Radar Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .background(Color(0xFF150A10), RoundedCornerShape(12.dp))
                .border(1.dp, Color(0xFFE11D48).copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f

                // Draw radar concentric circles
                drawCircle(Color(0xFFE11D48).copy(alpha = 0.15f), radius = 45f, center = Offset(cx, cy), style = Stroke(1.5f))
                drawCircle(Color(0xFFE11D48).copy(alpha = 0.12f), radius = 80f, center = Offset(cx, cy), style = Stroke(1.5f))
                drawCircle(Color(0xFFE11D48).copy(alpha = 0.08f), radius = 120f, center = Offset(cx, cy), style = Stroke(1.5f))

                // If connected, draw connecting energy rays
                if (connectedTablet) {
                    drawLine(
                        color = Color(0xFFF43F5E),
                        start = Offset(cx, cy),
                        end = Offset(cx - 85f, cy - 35f),
                        strokeWidth = 3f
                    )
                }
                if (connectedTv) {
                    drawLine(
                        color = Color(0xFFF43F5E),
                        start = Offset(cx, cy),
                        end = Offset(cx + 85f, cy - 35f),
                        strokeWidth = 3f
                    )
                }
                if (connectedWatch) {
                    drawLine(
                        color = Color(0xFFF43F5E),
                        start = Offset(cx, cy),
                        end = Offset(cx, cy + 60f),
                        strokeWidth = 3f
                    )
                }
            }

            // Central Phone Node
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE11D48)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PhoneIphone,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Satellite Devices Buttons
            // 1. Tablet (Top Left)
            Box(
                modifier = Modifier
                    .offset(x = (-85).dp, y = (-35).dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (connectedTablet) Color(0xFFBE123C) else Color(0xFF27131B))
                    .border(1.dp, if (connectedTablet) Color.White else Color.Gray, CircleShape)
                    .clickable { connectedTablet = !connectedTablet },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Tablet,
                    contentDescription = null,
                    tint = if (connectedTablet) Color.White else Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }

            // 2. Smart Screen TV (Top Right)
            Box(
                modifier = Modifier
                    .offset(x = 85.dp, y = (-35).dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (connectedTv) Color(0xFFBE123C) else Color(0xFF27131B))
                    .border(1.dp, if (connectedTv) Color.White else Color.Gray, CircleShape)
                    .clickable { connectedTv = !connectedTv },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Tv,
                    contentDescription = null,
                    tint = if (connectedTv) Color.White else Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }

            // 3. Watch (Bottom)
            Box(
                modifier = Modifier
                    .offset(x = 0.dp, y = 60.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (connectedWatch) Color(0xFFBE123C) else Color(0xFF27131B))
                    .border(1.dp, if (connectedWatch) Color.White else Color.Gray, CircleShape)
                    .clickable { connectedWatch = !connectedWatch },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Watch,
                    contentDescription = null,
                    tint = if (connectedWatch) Color.White else Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Device Connection Details
        Surface(
            color = Color(0xFF1E0E17),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                val count = listOf(connectedTablet, connectedTv, connectedWatch).count { it }
                Text(
                    text = "الأجهزة المتصلة بالناقل الموزع: $count أجهزة",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color.White
                )
                Text(
                    text = when {
                        count == 0 -> "المجال جاهز للاكتشاف التلقائي عبر بروتوكول DSoftBus اللاسلكي"
                        connectedTv && connectedTablet -> "مشاركة مهام متعددة: الشاشة كعرض سينمائي واللوحي كلوحة رسم فورية"
                        connectedTv -> "بث محتوى 4K فوري دون أي تأخير مع تزامن الصوت ومكبرات التلفاز"
                        else -> "مشاركة المعالج والذاكرة عبر الأجهزة بسلاسة النواة المصغرة"
                    },
                    fontSize = 11.sp,
                    color = Color(0xFFFDA4AF)
                )
            }
        }
    }
}

// =========================================================================
// 4. محاكي Google TV (Chromecast 4K, Voice Assistant, Recommendation Carousel)
// =========================================================================
@Composable
fun GoogleTvInteractiveSim() {
    var isCasting by remember { mutableStateOf(false) }
    var castResolution by remember { mutableStateOf("4K HDR (60fps)") }
    var isVoiceSearching by remember { mutableStateOf(false) }
    var voiceQuery by remember { mutableStateOf("أفضل أفلام الخيال العلمي") }

    LaunchedEffect(isVoiceSearching) {
        if (isVoiceSearching) {
            delay(1500)
            isVoiceSearching = false
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "📺 محاكي نظام Google TV / Android TV",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color(0xFF38BDF8)
            )
            Surface(
                color = Color(0xFF0369A1),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Google Cast 4K",
                    fontSize = 10.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // TV Screen Mockup
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .background(
                    Brush.verticalGradient(listOf(Color(0xFF0B192C), Color(0xFF020617))),
                    RoundedCornerShape(12.dp)
                )
                .border(2.dp, Color(0xFF1E3A8A), RoundedCornerShape(12.dp))
                .padding(10.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // TV Top Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Google TV Home", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Surface(
                            color = if (isCasting) Color(0xFF10B981) else Color(0xFF334155),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = if (isCasting) "Cast: شغال" else "Cast: خامل",
                                fontSize = 9.sp,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (isVoiceSearching) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(Color(0xFF1E293B).copy(alpha = 0.8f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            CircularProgressIndicator(color = Color(0xFF38BDF8), modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                            Text(text = "المساعد يستمع: '$voiceQuery'...", fontSize = 11.sp, color = Color.White)
                        }
                    }
                } else if (isCasting) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(Color(0xFF064E3B), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "جاري بث فيديو يوتيوب بدقة $castResolution", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF34D399))
                            Text(text = "التحكم في الصوت والتقديم يتم مباشرة من هاتفك عبر Chromecast", fontSize = 10.sp, color = Color.LightGray)
                        }
                    }
                } else {
                    // App rows
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("YouTube" to Color(0xFFDC2626), "Netflix" to Color(0xFFB91C1C), "Prime" to Color(0xFF0284C7), "Play Store" to Color(0xFF059669)).forEach { (name, col) ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .background(col.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                                    .border(1.dp, col, RoundedCornerShape(6.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(name, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Control Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { isCasting = !isCasting },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCasting) Color(0xFF059669) else Color(0xFF0284C7)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(imageVector = Icons.Default.Cast, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isCasting) "إيقاف البث" else "بث من الهاتف", fontSize = 11.sp)
            }

            Button(
                onClick = { isVoiceSearching = true },
                enabled = !isVoiceSearching,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(imageVector = Icons.Default.Mic, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("أمر صوتي ذكي", fontSize = 11.sp)
            }
        }
    }
}

// =========================================================================
// 5. محاكي LG webOS (Magic Remote Air Mouse, Card Deck, OLED Care)
// =========================================================================
@Composable
fun WebosInteractiveSim() {
    var pointerX by remember { mutableFloatStateOf(150f) }
    var pointerY by remember { mutableFloatStateOf(60f) }
    var selectedCardIndex by remember { mutableIntStateOf(1) }
    var oledCareActive by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🪄 محاكي LG webOS والريموت السحري",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color(0xFFA855F7)
            )
            Surface(
                color = Color(0xFF581C87),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Magic Remote Pointer",
                    fontSize = 10.sp,
                    color = Color(0xFFE9D5FF),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "اسحب إصبعك داخل مساحة الشاشة لتحريك مؤشر الفأرة الهوائي فوق كروت التطبيقات:",
            fontSize = 11.sp,
            color = Color.LightGray
        )

        Spacer(modifier = Modifier.height(10.dp))

        // webOS Screen with Interactive Pointer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color(0xFF0F0A1C), RoundedCornerShape(12.dp))
                .border(1.dp, Color(0xFF9333EA).copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        pointerX = (pointerX + dragAmount.x).coerceIn(20f, size.width.toFloat() - 20f)
                        pointerY = (pointerY + dragAmount.y).coerceIn(20f, size.height.toFloat() - 20f)
                    }
                }
        ) {
            // Cards Bar at bottom
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val cards = listOf("الرئيسية", "Netflix", "YouTube", "HDMI 1", "Game Optimizer")
                cards.forEachIndexed { index, name ->
                    val isHovered = (pointerX >= index * 55f && pointerX <= (index + 1) * 65f && pointerY > 70f)
                    val isSelected = selectedCardIndex == index || isHovered
                    val cardHeight = if (isSelected) 46.dp else 36.dp

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(cardHeight)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isSelected) Color(0xFFA855F7) else Color(0xFF2E1065)
                            )
                            .clickable { selectedCardIndex = index },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = name,
                            fontSize = if (isSelected) 10.sp else 9.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = Color.White
                        )
                    }
                }
            }

            // Magic Remote Pointer Cursor
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Drop shadow
                drawCircle(Color.Black.copy(alpha = 0.5f), radius = 9f, center = Offset(pointerX + 2f, pointerY + 2f))
                // Magic pointer body (Magenta/Violet teardrop)
                drawCircle(Color(0xFFE879F9), radius = 8f, center = Offset(pointerX, pointerY))
                drawCircle(Color.White, radius = 3.5f, center = Offset(pointerX, pointerY))
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // OLED Care Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "ميزة العناية بشاشات OLED (Pixel Refresher)", fontSize = 12.sp, color = Color.White)
                Text(text = "إزاحة البكسلات غير المحسوسة لحماية الشاشة من التطبيع (Burn-in)", fontSize = 10.sp, color = Color.Gray)
            }
            Switch(
                checked = oledCareActive,
                onCheckedChange = { oledCareActive = it },
                colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFA855F7))
            )
        }
    }
}

// =========================================================================
// 6. محاكي سامسونج Tizen OS (SmartThings, Gaming Hub, Multi-View)
// =========================================================================
@Composable
fun TizenInteractiveSim() {
    var tizenTab by remember { mutableStateOf("smartthings") } // smartthings, gaming, multiview
    var livingRoomLight by remember { mutableStateOf(true) }
    var acTemperature by remember { mutableIntStateOf(22) }
    var multiViewRatio by remember { mutableStateOf("50/50") }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "⚡ محاكي نظام Samsung Tizen OS",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color(0xFF38BDF8)
            )
            Surface(
                color = Color(0xFF0369A1),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Neo QLED & Tizen 8",
                    fontSize = 10.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(
                selected = tizenTab == "smartthings",
                onClick = { tizenTab = "smartthings" },
                label = { Text("منزل SmartThings", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF0284C7),
                    selectedLabelColor = Color.White
                )
            )
            FilterChip(
                selected = tizenTab == "gaming",
                onClick = { tizenTab = "gaming" },
                label = { Text("Gaming Hub السحابي", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF0284C7),
                    selectedLabelColor = Color.White
                )
            )
            FilterChip(
                selected = tizenTab == "multiview",
                onClick = { tizenTab = "multiview" },
                label = { Text("العرض المتعدد (Multi-View)", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF0284C7),
                    selectedLabelColor = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (tizenTab) {
            "smartthings" -> {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "لوحة التحكم بالأجهزة المنزلية من شاشة سامسونج:", fontSize = 12.sp, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Light Tile
                            Surface(
                                color = if (livingRoomLight) Color(0xFFF59E0B).copy(alpha = 0.2f) else Color(0xFF1E293B),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { livingRoomLight = !livingRoomLight }
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Icon(
                                        imageVector = Icons.Default.Lightbulb,
                                        contentDescription = null,
                                        tint = if (livingRoomLight) Color(0xFFF59E0B) else Color.Gray,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("إضاءة الصالة", fontSize = 11.sp, color = Color.White)
                                    Text(if (livingRoomLight) "مضاءة (100%)" else "مطفأة", fontSize = 10.sp, color = Color.Gray)
                                }
                            }

                            // AC Tile
                            Surface(
                                color = Color(0xFF0284C7).copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Icon(imageVector = Icons.Default.AcUnit, contentDescription = null, tint = Color(0xFF38BDF8), modifier = Modifier.size(24.dp))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("المكيف: $acTemperature°C", fontSize = 11.sp, color = Color.White)
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Text(
                                            text = "[-] ",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF38BDF8),
                                            modifier = Modifier.clickable { if (acTemperature > 18) acTemperature-- }
                                        )
                                        Text(
                                            text = "[+]",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF38BDF8),
                                            modifier = Modifier.clickable { if (acTemperature < 28) acTemperature++ }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            "gaming" -> {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "مركز ألعاب Xbox Cloud Gaming بدون كونسول:", fontSize = 12.sp, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                                .background(Color(0xFF064E3B), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(imageVector = Icons.Default.SportsEsports, contentDescription = null, tint = Color(0xFF34D399), modifier = Modifier.size(32.dp))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("اتصال يد التحكم عبر Bluetooth جاهز", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("زمن الاستجابة: 12ms | جودة البث: 60fps 1080p", fontSize = 10.sp, color = Color(0xFF6EE7B7))
                            }
                        }
                    }
                }
            }
            "multiview" -> {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "تقسيم شاشة التلفاز لمتابعة محتويين في آن واحد:", fontSize = 12.sp, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .background(Color(0xFF1E293B), RoundedCornerShape(8.dp))
                                    .border(1.dp, Color(0xFF38BDF8), RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("بث مباراة كرة القدم ⚽ (HDMI 1)", fontSize = 10.sp, color = Color.White)
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .background(Color(0xFF1E293B), RoundedCornerShape(8.dp))
                                    .border(1.dp, Color(0xFFF59E0B), RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("مطابقة شاشة الهاتف 📱 (Smart View)", fontSize = 10.sp, color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}

// =========================================================================
// 7. محاكيات أنظمة الحواسب (Windows, macOS, Linux)
// =========================================================================
@Composable
fun WindowsInteractiveSim() {
    Surface(
        color = Color(0xFF0F172A),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("🪟 محاكي نواة Windows NT و DirectStorage", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF60A5FA))
            Spacer(modifier = Modifier.height(6.dp))
            Text("نظام ويندوز يرتكز على نواة NT الحديثة مع دعم عتادي هائل وتقنيات ألعاب متقدمة مثل DirectX 12 Ultimate و DirectStorage لتحميل الألعاب مباشرة من NVMe إلى كارت الشاشة.", fontSize = 11.sp, color = Color.LightGray)
        }
    }
}

@Composable
fun MacosInteractiveSim() {
    Surface(
        color = Color(0xFF0F172A),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("🍎 محاكي بيئة macOS ونواة Darwin UNIX", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF93C5FD))
            Spacer(modifier = Modifier.height(6.dp))
            Text("يعتمد macOS على معايير POSIX ونواة Darwin المستقرة مع معمارية Apple Silicon الموحدة (Unified Memory Architecture) ومكتبات رسوميات Metal 3 فائقة الكفاءة.", fontSize = 11.sp, color = Color.LightGray)
        }
    }
}

@Composable
fun LinuxInteractiveSim() {
    var selectedDistro by remember { mutableStateOf("SteamOS") }

    Surface(
        color = Color(0xFF0F172A),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("🐧 محاكي توزيعات نواة لينكس (Distros)", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFFFBBF24))
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("SteamOS" to "ألعاب", "Ubuntu" to "برمجة", "Arch" to "تخصيص", "Mint" to "شخصي").forEach { (distro, desc) ->
                    Surface(
                        color = if (selectedDistro == distro) Color(0xFFD97706) else Color(0xFF1E293B),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.clickable { selectedDistro = distro }
                    ) {
                        Text("$distro ($desc)", fontSize = 10.sp, color = Color.White, modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = when (selectedDistro) {
                    "SteamOS" -> "توزيعة Valve للألعاب مع طبقة Proton لتشغيل ألعاب Windows بسلاسة."
                    "Ubuntu" -> "البيئة القياسية العالمية للسيرفرات وتطوير البرمجيات والذكاء الاصطناعي."
                    "Arch" -> "توزيعة Rolling Release موجهة للمحترفين للتحكم الدقيق بكل سطر برمجي."
                    else -> "واجهة سهلة تشبه ويندوز مثالية للمبتدئين وخفيفة على الموارد."
                },
                fontSize = 11.sp,
                color = Color.LightGray
            )
        }
    }
}
