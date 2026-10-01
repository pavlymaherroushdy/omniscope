package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SimulationType
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.AccentEmerald
import com.example.ui.theme.AccentViolet
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.MechAccent
import com.example.ui.theme.MechGlow
import com.example.ui.theme.MechPrimary
import com.example.ui.theme.PrimaryCyan
import com.example.ui.theme.PrimaryIndigo
import com.example.ui.theme.SpaceAccent
import com.example.ui.theme.SpaceGlow
import com.example.ui.theme.SpacePrimary
import com.example.ui.theme.TechAccent
import com.example.ui.theme.TechGlow
import com.example.ui.theme.TechPrimary
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun InteractiveSimulationViewer(
    simulationType: SimulationType,
    selectedComponentId: String?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            when (simulationType) {
                SimulationType.STORAGE_COMPARISON -> StorageComparisonSimulation(selectedComponentId = selectedComponentId)
                SimulationType.CPU_ARCHITECTURE -> CpuArchitectureSimulation(selectedComponentId = selectedComponentId)
                SimulationType.CAR_ENGINE -> EngineSimulation(selectedComponentId = selectedComponentId)
                SimulationType.BLACK_HOLE -> BlackHoleSimulation(selectedComponentId = selectedComponentId)
                SimulationType.PLANETARY_GEARS -> GearsSimulation(selectedComponentId = selectedComponentId)
                SimulationType.HYDRAULIC_PRESS -> HydraulicPressSimulation(selectedComponentId = selectedComponentId)
                SimulationType.ROCKET_STAGING -> RocketStagingSimulation(selectedComponentId = selectedComponentId)
                SimulationType.BRAKES_SUSPENSION,
                SimulationType.BRAKES_SYSTEM -> BrakesSuspensionSimulation(selectedComponentId = selectedComponentId)
                SimulationType.SUSPENSION_SYSTEM -> SuspensionSimulation(selectedComponentId = selectedComponentId)
                SimulationType.SOLAR_SYSTEM,
                SimulationType.EXOPLANETS,
                SimulationType.STARS_NEBULAE,
                SimulationType.COSMIC_PHENOMENA,
                SimulationType.GALAXIES_UNIVERSE -> SolarSystemSimulation(selectedComponentId = selectedComponentId)
                SimulationType.PSU_SIMULATOR -> PsuSimulation(selectedComponentId = selectedComponentId)
                SimulationType.COOLING_SIMULATOR -> CoolingSimulation(selectedComponentId = selectedComponentId)
                SimulationType.OS_WINDOWS,
                SimulationType.OS_MACOS,
                SimulationType.OS_LINUX,
                SimulationType.OS_ANDROID,
                SimulationType.OS_IOS,
                SimulationType.OS_HARMONY,
                SimulationType.OS_GOOGLE_TV,
                SimulationType.OS_WEBOS,
                SimulationType.OS_TIZEN,
                SimulationType.OS_MOBILE,
                SimulationType.OS_SMART_TV -> OsInteractiveLab(simulationType = simulationType, selectedComponentId = selectedComponentId)
                else -> GenericInteractiveLabArabic(simulationType, selectedComponentId)
            }
        }
    }
}

// -------------------------------------------------------------
// 1. مختبر مقارنة وسائط التخزين (HDD vs SATA SSD vs NVMe M.2)
// -------------------------------------------------------------
@Composable
fun StorageComparisonSimulation(selectedComponentId: String?) {
    var selectedFileSizeGb by remember { mutableIntStateOf(50) } // 10GB, 50GB, 100GB
    var isTesting by remember { mutableStateOf(false) }

    var progressHdd by remember { mutableFloatStateOf(0f) }
    var progressSsd by remember { mutableFloatStateOf(0f) }
    var progressNvme by remember { mutableFloatStateOf(0f) }

    var timeHdd by remember { mutableFloatStateOf(0f) }
    var timeSsd by remember { mutableFloatStateOf(0f) }
    var timeNvme by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(isTesting) {
        if (isTesting) {
            progressHdd = 0f
            progressSsd = 0f
            progressNvme = 0f
            val totalSecondsHdd = (selectedFileSizeGb * 1024f) / 140f // ~366s scaled
            val totalSecondsSsd = (selectedFileSizeGb * 1024f) / 550f // ~93s scaled
            val totalSecondsNvme = (selectedFileSizeGb * 1024f) / 7000f // ~7.3s scaled

            // Simulation step loop over 60 ticks
            for (step in 1..60) {
                delay(50)
                // NVMe finishes very rapidly (in ~10 steps)
                progressNvme = (step / 10f).coerceAtMost(1f)
                timeNvme = (progressNvme * totalSecondsNvme)

                // SATA SSD finishes in ~28 steps
                progressSsd = (step / 28f).coerceAtMost(1f)
                timeSsd = (progressSsd * totalSecondsSsd)

                // HDD finishes in 60 steps (or partial)
                progressHdd = (step / 60f).coerceAtMost(1f)
                timeHdd = (progressHdd * totalSecondsHdd)
            }
            isTesting = false
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "مختبر المقارنة التفاعلي لسرعات التخزين",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TechAccent,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "سباق نقل البيانات: HDD مقابل SSD و NVMe",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = TechGlow.copy(alpha = 0.25f),
                border = BorderStroke(1.dp, TechAccent)
            ) {
                Text(
                    text = "$selectedFileSizeGb جيجابايت",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TechAccent,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // File size selector buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(10 to "10 GB (فيديو 4K)", 50 to "50 GB (لعبة ضخمة)", 100 to "100 GB (ويندوز كامل)").forEach { (size, label) ->
                val isSelected = selectedFileSizeGb == size
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) PrimaryCyan else DarkSurfaceVariant,
                    border = BorderStroke(1.dp, if (isSelected) PrimaryCyan else Color(0xFF334155)),
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            selectedFileSizeGb = size
                            progressHdd = 0f
                            progressSsd = 0f
                            progressNvme = 0f
                        }
                        .testTag("file_size_${size}gb")
                ) {
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color(0xFF0F172A) else Color.White,
                        modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
                        maxLines = 1
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 3 Live Benchmark Bars
        StorageSpeedBar(
            name = "NVMe M.2 (Gen 4/5 فائق السرعة)",
            speed = "7,000+ MB/s",
            latency = "0.02 ms (فوري)",
            progress = progressNvme,
            barColor = PrimaryCyan,
            status = if (progressNvme >= 1f) "اكتمل النقل في ${timeNvme.toInt()} ثوانٍ!" else if (isTesting) "جاري الضخ عبر PCIe..." else "جاهز",
            isHighlighted = selectedComponentId == "nvme_m2_comp"
        )

        Spacer(modifier = Modifier.height(10.dp))

        StorageSpeedBar(
            name = "SATA SSD (حالة صلبة صامتة)",
            speed = "550 MB/s",
            latency = "0.1 ms",
            progress = progressSsd,
            barColor = AccentEmerald,
            status = if (progressSsd >= 1f) "اكتمل النقل في ${timeSsd.toInt()} ثانية" else if (isTesting) "نقل عبر كابل SATA..." else "جاهز",
            isHighlighted = selectedComponentId == "sata_ssd_comp"
        )

        Spacer(modifier = Modifier.height(10.dp))

        StorageSpeedBar(
            name = "الهارد الميكانيكي التقليدي (HDD)",
            speed = "140 MB/s",
            latency = "15 ms (بطيء نسبياً)",
            progress = progressHdd,
            barColor = AccentAmber,
            status = if (progressHdd >= 1f) "اكتمل بعد ${timeHdd.toInt()} ثانية!" else if (isTesting) "الإبرة والأسطوانة تدور..." else "جاهز",
            isHighlighted = selectedComponentId == "hdd_comp"
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Start Benchmark Button
        Button(
            onClick = { isTesting = true },
            enabled = !isTesting,
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryCyan),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .testTag("start_storage_benchmark")
        ) {
            Icon(
                imageVector = Icons.Default.Speed,
                contentDescription = null,
                tint = Color(0xFF0F172A),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = if (isTesting) "جاري اختبار نقل ملف $selectedFileSizeGb جيجابايت..." else "بدء اختبار السرعة ونقل الملف ⚡",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun StorageSpeedBar(
    name: String,
    speed: String,
    latency: String,
    progress: Float,
    barColor: Color,
    status: String,
    isHighlighted: Boolean
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isHighlighted) barColor.copy(alpha = 0.15f) else DarkSurfaceVariant,
        border = BorderStroke(1.dp, if (isHighlighted) barColor else Color(0xFF334155)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color.White
                )
                Text(
                    text = speed,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.sp,
                    color = barColor
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = barColor,
                trackColor = Color(0xFF0F172A)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = status,
                    fontSize = 11.sp,
                    color = Color(0xFFCBD5E1)
                )
                Text(
                    text = "زمن الاستجابة: $latency",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

// -------------------------------------------------------------
// 2. محاكي المعالج المركزي (CPU Architecture & Clock Speed)
// -------------------------------------------------------------
@Composable
fun CpuArchitectureSimulation(selectedComponentId: String?) {
    var clockGhz by remember { mutableFloatStateOf(4.8f) }
    var activeCoreCount by remember { mutableIntStateOf(8) }

    val infiniteTransition = rememberInfiniteTransition(label = "cpu_pulse")
    val pipelinePulse by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = (1000 / (clockGhz / 2f)).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "clock_cycle"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "محاكي نبضات المعالج ودورة الأوامر",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TechAccent,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "معمارية ${String.format("%.1f", clockGhz)} GHz | $activeCoreCount أنوية نشطة",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF070F1E))
                .border(1.dp, Color(0xFF162D4A), RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f

                // CPU Silicon Die outline
                drawRoundRect(
                    color = Color(0xFF1E293B),
                    topLeft = Offset(cx - 150f, cy - 70f),
                    size = Size(300f, 140f),
                    cornerRadius = CornerRadius(12f, 12f),
                    style = Stroke(width = 3f)
                )

                // 8 Cores grid
                val coreW = 55f
                val coreH = 45f
                for (row in 0..1) {
                    for (col in 0..3) {
                        val coreLeft = (cx - 130f) + (col * 68f)
                        val coreTop = (cy - 55f) + (row * 60f)
                        val isFired = pipelinePulse > 0.4f

                        drawRoundRect(
                            color = if (isFired) TechAccent.copy(alpha = 0.3f) else Color(0xFF0F172A),
                            topLeft = Offset(coreLeft, coreTop),
                            size = Size(coreW, coreH),
                            cornerRadius = CornerRadius(6f, 6f)
                        )
                        drawRoundRect(
                            color = if (isFired) TechAccent else Color(0xFF334155),
                            topLeft = Offset(coreLeft, coreTop),
                            size = Size(coreW, coreH),
                            cornerRadius = CornerRadius(6f, 6f),
                            style = Stroke(width = 1.5f)
                        )
                    }
                }

                // Central L3 Cache Bar
                val cacheColor = if (selectedComponentId == "cache_memory") PrimaryCyan else AccentViolet
                drawLine(
                    color = cacheColor,
                    start = Offset(cx - 130f, cy),
                    end = Offset(cx + 130f, cy),
                    strokeWidth = 6f
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "تردد الساعة (GHz):",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.width(130.dp)
            )
            Slider(
                value = clockGhz,
                onValueChange = { clockGhz = it },
                valueRange = 2.0f..6.0f,
                modifier = Modifier
                    .weight(1f)
                    .testTag("cpu_ghz_slider"),
                colors = SliderDefaults.colors(
                    thumbColor = TechAccent,
                    activeTrackColor = TechAccent
                )
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            EngineMetricBadge("دورات في الثانية", "${String.format("%.1f", clockGhz)} مليار دورة")
            EngineMetricBadge("كفاءة الأوامر (IPC)", "4.2 تعليمات/دورة")
            EngineMetricBadge("حرارة السيليكون", "${(45 + (clockGhz * 6)).toInt()}°C")
        }
    }
}

// -------------------------------------------------------------
// 3. محرك الاحتراق الداخلي الرباعي (Four-Stroke ICE Engine)
// -------------------------------------------------------------
@Composable
fun EngineSimulation(selectedComponentId: String?) {
    var isRunning by remember { mutableStateOf(true) }
    var rpm by remember { mutableFloatStateOf(1500f) }

    val infiniteTransition = rememberInfiniteTransition(label = "engine_cycle")
    val animatedAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 720f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = (60000 / rpm.coerceAtLeast(100f) * 2).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "crank_rotation"
    )

    val currentAngle = if (isRunning) animatedAngle else 0f
    val cycleDeg = currentAngle % 720f
    val (strokeName, strokeColor) = when (cycleDeg) {
        in 0f..<180f -> "1. شوط السحب (دخول الهواء والبنزين)" to PrimaryCyan
        in 180f..<360f -> "2. شوط الضغط (كبس الخليط لأعلى)" to PrimaryIndigo
        in 360f..<540f -> "3. شوط القدرة والاشتعال (انفجار البوجيه!)" to Color(0xFFFF5722)
        else -> "4. شوط العادم (طرد الدخان المحترق)" to AccentAmber
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "محاكي دورة أوتو الرباعية للمحرك",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MechAccent,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = strokeName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = strokeColor
                )
            }
            IconButton(
                onClick = { isRunning = !isRunning },
                modifier = Modifier
                    .size(36.dp)
                    .background(DarkSurfaceVariant, CircleShape)
                    .testTag("toggle_engine_simulation")
            ) {
                Icon(
                    imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isRunning) "إيقاف مؤقت" else "تشغيل",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF090D16))
                .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f

                val cylinderTop = 20f
                val cylinderBottom = cy + 50f
                val cylinderWidth = 140f
                val cylinderLeft = cx - cylinderWidth / 2f
                val cylinderRight = cx + cylinderWidth / 2f

                // Draw Cylinder walls
                drawRect(
                    color = Color(0xFF2A374D),
                    topLeft = Offset(cylinderLeft - 10f, cylinderTop),
                    size = Size(10f, cylinderBottom - cylinderTop)
                )
                drawRect(
                    color = Color(0xFF2A374D),
                    topLeft = Offset(cylinderRight, cylinderTop),
                    size = Size(10f, cylinderBottom - cylinderTop)
                )

                // Crankshaft rotation
                val crankRadius = 38f
                val crankCenterY = cylinderBottom + 35f
                val crankAngleRad = Math.toRadians((currentAngle % 360.0)).toFloat()
                val crankPinX = cx + crankRadius * sin(crankAngleRad)
                val crankPinY = crankCenterY + crankRadius * cos(crankAngleRad)

                // Piston position
                val rodLength = 95f
                val pistonY = crankPinY - (rodLength * 0.9f)
                val pistonHeight = 35f

                // In-cylinder gas color
                val isSparkActive = cycleDeg in 360f..<420f
                val chamberGasColor = when {
                    isSparkActive -> Color(0xFFFF9800)
                    cycleDeg in 0f..<180f -> Color(0x3300D1FF) // Fresh air
                    cycleDeg in 180f..<360f -> Color(0x556366F1) // Compressed
                    cycleDeg in 360f..<540f -> Color(0x88FF5722) // Fireball
                    else -> Color(0x448D6E63) // Exhaust
                }
                drawRect(
                    color = chamberGasColor,
                    topLeft = Offset(cylinderLeft, cylinderTop),
                    size = Size(cylinderWidth, pistonY - cylinderTop)
                )

                // Spark plug (البوجيه)
                val sparkX = cx
                val sparkY = cylinderTop
                drawLine(
                    color = Color.LightGray,
                    start = Offset(sparkX, sparkY - 14f),
                    end = Offset(sparkX, sparkY + 6f),
                    strokeWidth = 6f
                )
                if (isSparkActive || selectedComponentId == "spark_plug") {
                    drawCircle(
                        color = Color(0xFFFFEB3B),
                        radius = 12f,
                        center = Offset(sparkX, sparkY + 6f)
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 6f,
                        center = Offset(sparkX, sparkY + 6f)
                    )
                }

                // Piston body (البستم)
                val pistonColor = if (selectedComponentId == "piston") PrimaryCyan else Color(0xFF64748B)
                drawRoundRect(
                    color = pistonColor,
                    topLeft = Offset(cylinderLeft + 2f, pistonY),
                    size = Size(cylinderWidth - 4f, pistonHeight),
                    cornerRadius = CornerRadius(6f, 6f)
                )

                // Connecting rod (ذراع التوصيل)
                val wristPin = Offset(cx, pistonY + pistonHeight / 2f)
                drawCircle(Color.LightGray, radius = 6f, center = wristPin)

                val rodColor = if (selectedComponentId == "connecting_rod") AccentAmber else Color(0xFF94A3B8)
                drawLine(
                    color = rodColor,
                    start = wristPin,
                    end = Offset(crankPinX, crankPinY),
                    strokeWidth = 8f,
                    cap = StrokeCap.Round
                )

                // Crankshaft (عمود الكرنك)
                val crankColor = if (selectedComponentId == "crankshaft") AccentViolet else Color(0xFF334155)
                drawCircle(
                    color = crankColor,
                    radius = crankRadius,
                    center = Offset(cx, crankCenterY),
                    style = Stroke(width = 8f)
                )
                drawCircle(
                    color = Color(0xFF00D1FF),
                    radius = 8f,
                    center = Offset(crankPinX, crankPinY)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "سرعة الدوران (RPM): ${rpm.toInt()}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.width(160.dp)
            )
            Slider(
                value = rpm,
                onValueChange = { rpm = it },
                valueRange = 800f..6500f,
                modifier = Modifier
                    .weight(1f)
                    .testTag("engine_rpm_slider"),
                colors = SliderDefaults.colors(
                    thumbColor = strokeColor,
                    activeTrackColor = strokeColor
                )
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            EngineMetricBadge("زاوية الكرنك", "${(cycleDeg).toInt()}°")
            EngineMetricBadge("ضغط الغرفة", if (cycleDeg in 360f..460f) "1,250 PSI" else "120 PSI")
            EngineMetricBadge("حرارة اللهب", if (cycleDeg in 360f..460f) "2,150°C" else "320°C")
        }
    }
}

// -------------------------------------------------------------
// 4. محاكي الثقب الأسود والنسبية العامة
// -------------------------------------------------------------
@Composable
fun BlackHoleSimulation(selectedComponentId: String?) {
    var blackHoleMass by remember { mutableFloatStateOf(10f) }

    val infiniteTransition = rememberInfiniteTransition(label = "accretion_swirl")
    val swirlAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "swirl"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "محاكي النسبية العامة والثقوب السوداء",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SpaceAccent,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "ثقب أسود دوار بكتلة ${blackHoleMass.toInt()} شمس",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF030509)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f

                // Gravitational lensing grid
                for (r in 30..120 step 25) {
                    drawCircle(
                        color = Color(0x2238BDF8),
                        radius = r.toFloat(),
                        center = Offset(cx, cy),
                        style = Stroke(width = 1f)
                    )
                }

                // Relativistic Accretion Disk (Swirling plasma)
                val diskWidth = 190f
                val diskHeight = 60f
                drawOval(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFF38BDF8), Color(0xFFFF9800), Color(0x00FF5722)),
                        center = Offset(cx, cy),
                        radius = diskWidth
                    ),
                    topLeft = Offset(cx - diskWidth, cy - diskHeight),
                    size = Size(diskWidth * 2f, diskHeight * 2f),
                    style = Stroke(width = 24f)
                )

                // Event Horizon
                val eventHorizonRadius = 26f + (blackHoleMass * 0.5f)
                drawCircle(
                    color = Color.Black,
                    radius = eventHorizonRadius,
                    center = Offset(cx, cy)
                )
                drawCircle(
                    color = if (selectedComponentId == "event_horizon") SpaceAccent else Color(0x66818CF8),
                    radius = eventHorizonRadius,
                    center = Offset(cx, cy),
                    style = Stroke(width = 3f)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "كتلة الثقب (كتل شمسية):",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.width(160.dp)
            )
            Slider(
                value = blackHoleMass,
                onValueChange = { blackHoleMass = it },
                valueRange = 3f..50f,
                modifier = Modifier
                    .weight(1f)
                    .testTag("black_hole_mass_slider"),
                colors = SliderDefaults.colors(
                    thumbColor = SpaceAccent,
                    activeTrackColor = SpaceAccent
                )
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            EngineMetricBadge("نصف قطر الحدث", "${(blackHoleMass * 2.95).toInt()} كم")
            EngineMetricBadge("سرعة الإفلات", "300,000 كم/ثانية")
            EngineMetricBadge("تباطؤ الزمن", "قريب من التوقف التام")
        }
    }
}

// -------------------------------------------------------------
// 5. محاكي التروس الكوكبية (Planetary Gears)
// -------------------------------------------------------------
@Composable
fun GearsSimulation(selectedComponentId: String?) {
    var gearSpeed by remember { mutableFloatStateOf(1f) }

    val infiniteTransition = rememberInfiniteTransition(label = "gears_mesh")
    val sunRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = (3000 / gearSpeed.coerceAtLeast(0.2f)).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sun_spin"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "مجموعة التروس الكوكبية وناقل الحركة",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MechAccent,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "مضاعفة العزم بنسبة 4:1 في حجم مدمج",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF0F172A)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f

                val sunRadius = 28f
                val planetRadius = 26f
                val orbitRadius = 60f
                val ringRadius = 90f

                // Outer Ring Gear (Stationary)
                val ringColor = if (selectedComponentId == "ring_gear") MechAccent else Color(0xFF334155)
                drawCircle(
                    color = ringColor,
                    radius = ringRadius,
                    center = Offset(cx, cy),
                    style = Stroke(width = 12f)
                )

                // 3 Planet Gears
                val carrierAngle = sunRotation * 0.25f
                for (p in 0 until 3) {
                    val pBaseAngle = Math.toRadians((p * 120.0 + carrierAngle)).toFloat()
                    val px = cx + orbitRadius * cos(pBaseAngle)
                    val py = cy + orbitRadius * sin(pBaseAngle)

                    drawLine(
                        color = Color(0x6664748B),
                        start = Offset(cx, cy),
                        end = Offset(px, py),
                        strokeWidth = 5f
                    )

                    drawCircle(
                        color = Color(0xFF0284C7),
                        radius = planetRadius,
                        center = Offset(px, py)
                    )
                }

                // Central Sun Gear
                drawCircle(
                    color = MechAccent,
                    radius = sunRadius,
                    center = Offset(cx, cy)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "سرعة دوران المحرك:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.width(130.dp)
            )
            Slider(
                value = gearSpeed,
                onValueChange = { gearSpeed = it },
                valueRange = 0.2f..3.0f,
                modifier = Modifier
                    .weight(1f)
                    .testTag("gear_speed_slider"),
                colors = SliderDefaults.colors(
                    thumbColor = MechAccent,
                    activeTrackColor = MechAccent
                )
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            EngineMetricBadge("سرعة ترس الشمس", "${(gearSpeed * 1000).toInt()} RPM")
            EngineMetricBadge("سرعة الخرج للعجلات", "${(gearSpeed * 250).toInt()} RPM")
            EngineMetricBadge("مضاعفة العزم", "4.0x أضعاف القوة")
        }
    }
}

// -------------------------------------------------------------
// 6. محاكي الأنظمة الهيدروليكية ومبدأ باسكال
// -------------------------------------------------------------
@Composable
fun HydraulicPressSimulation(selectedComponentId: String?) {
    var inputStroke by remember { mutableFloatStateOf(0.4f) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "مختبر مبدأ باسكال ومضاعفة القوة",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MechAccent,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "ضغط سائل الزيت (F1/A1 = F2/A2)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF0F172A)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val inputColX = size.width * 0.25f
                val outputColX = size.width * 0.72f
                val baseY = size.height * 0.85f

                val inputWidth = 36f
                val outputWidth = 110f

                val inputPistonY = (size.height * 0.30f) + (inputStroke * 80f)
                val outputLiftY = (size.height * 0.60f) - (inputStroke * 25f)

                // High pressure red oil
                val fluidPath = Path().apply {
                    moveTo(inputColX - inputWidth / 2f, inputPistonY)
                    lineTo(inputColX - inputWidth / 2f, baseY)
                    lineTo(outputColX + outputWidth / 2f, baseY)
                    lineTo(outputColX + outputWidth / 2f, outputLiftY)
                    lineTo(outputColX - outputWidth / 2f, outputLiftY)
                    lineTo(outputColX - outputWidth / 2f, baseY - 30f)
                    lineTo(inputColX + inputWidth / 2f, baseY - 30f)
                    lineTo(inputColX + inputWidth / 2f, inputPistonY)
                    close()
                }
                drawPath(fluidPath, color = Color(0xFFEF4444))

                // Input Master Piston (Small)
                drawRect(
                    color = Color(0xFF38BDF8),
                    topLeft = Offset(inputColX - inputWidth / 2f, inputPistonY - 14f),
                    size = Size(inputWidth, 14f)
                )

                // Output Slave Ram (Heavy Anvil)
                drawRect(
                    color = Color(0xFFF59E0B),
                    topLeft = Offset(outputColX - outputWidth / 2f, outputLiftY - 24f),
                    size = Size(outputWidth, 24f)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "مستوى ضغط الذراع:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.width(130.dp)
            )
            Slider(
                value = inputStroke,
                onValueChange = { inputStroke = it },
                valueRange = 0.05f..0.95f,
                modifier = Modifier
                    .weight(1f)
                    .testTag("hydraulic_stroke_slider"),
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFFEF4444),
                    activeTrackColor = Color(0xFFEF4444)
                )
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            EngineMetricBadge("قوة الإدخال F1", "50 نيوتن (بسيطة)")
            EngineMetricBadge("قوة الرفع F2", "500 نيوتن (10 أضعاف)")
            EngineMetricBadge("ضغط الزيت", "3,500 PSI")
        }
    }
}

// -------------------------------------------------------------
// 7. محاكي إطلاق وانفصال مراحل الصواريخ
// -------------------------------------------------------------
@Composable
fun RocketStagingSimulation(selectedComponentId: String?) {
    var stageStep by remember { mutableIntStateOf(1) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "محاكي الإطلاق المداري والانفصال",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SpaceAccent,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = when (stageStep) {
                        1 -> "المرحلة الأولى: اشتعال المعزز الأساسي"
                        2 -> "المرحلة الثانية: انفصال المعزز وتخفيف الوزن"
                        else -> "المرحلة الثالثة: الوصول لسرعة المدار (ماخ 25)"
                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
            Button(
                onClick = { stageStep = if (stageStep >= 3) 1 else stageStep + 1 },
                colors = ButtonDefaults.buttonColors(containerColor = SpaceGlow),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("next_stage_button")
            ) {
                Text(if (stageStep >= 3) "إعادة الإطلاق" else "المرحلة التالية", fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF030712)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f

                val rocketWidth = 44f
                val fairingHeight = 45f
                val secondStageHeight = 55f
                val boosterHeight = 85f

                val boosterOffset = if (stageStep >= 2) 45f else 0f
                val noseY = cy - 80f

                // Fairing
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(cx - rocketWidth / 2f, noseY),
                    size = Size(rocketWidth, fairingHeight),
                    cornerRadius = CornerRadius(16f, 16f)
                )

                // Upper stage
                val s2Y = noseY + fairingHeight
                drawRect(
                    color = Color(0xFFCBD5E1),
                    topLeft = Offset(cx - rocketWidth / 2f, s2Y),
                    size = Size(rocketWidth, secondStageHeight)
                )

                // Booster stage
                val bY = s2Y + secondStageHeight + 6f + boosterOffset
                drawRect(
                    color = Color(0xFF64748B),
                    topLeft = Offset(cx - rocketWidth / 2f, bY),
                    size = Size(rocketWidth, boosterHeight)
                )

                // Flame
                if (stageStep == 1) {
                    val flamePath = Path().apply {
                        moveTo(cx - 16f, bY + boosterHeight)
                        lineTo(cx + 16f, bY + boosterHeight)
                        lineTo(cx, bY + boosterHeight + 40f)
                        close()
                    }
                    drawPath(flamePath, color = Color(0xFFFF5722))
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            EngineMetricBadge("السرعة الحالية", when (stageStep) { 1 -> "ماخ 3.2"; 2 -> "ماخ 8.5"; else -> "28,000 كم/س (ماخ 25)" })
            EngineMetricBadge("الارتفاع", when (stageStep) { 1 -> "35 كم"; 2 -> "90 كم"; else -> "200 كم مداري" })
            EngineMetricBadge("قوة الدفع", when (stageStep) { 1 -> "16 مليون رطل"; 2 -> "250 ألف رطل"; else -> "انعدام الجاذبية" })
        }
    }
}

// -------------------------------------------------------------
// 8. محاكي نظام الفرامل مانع الانغلاق ABS
// -------------------------------------------------------------
@Composable
fun BrakesSuspensionSimulation(selectedComponentId: String?) {
    var isAbsActive by remember { mutableStateOf(true) }
    var isEbdActive by remember { mutableStateOf(true) }
    var isEspActive by remember { mutableStateOf(true) }
    var isBaActive by remember { mutableStateOf(true) }
    var isBraking by remember { mutableStateOf(false) }
    var brakingProgress by remember { mutableFloatStateOf(0f) }

    val stoppingDistanceMeters = remember(isAbsActive, isEbdActive, isEspActive, isBaActive) {
        var base = 65
        if (isAbsActive) base -= 12
        if (isEbdActive) base -= 8
        if (isBaActive) base -= 6
        if (isEspActive) base -= 3
        base
    }

    LaunchedEffect(isBraking) {
        if (isBraking) {
            brakingProgress = 0f
            val totalSteps = (stoppingDistanceMeters * 0.9f).toInt().coerceIn(25, 65)
            for (i in 1..totalSteps) {
                delay(25)
                brakingProgress = i / totalSteps.toFloat()
            }
            isBraking = false
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "🛑 محاكي منظومات الفرامل الذكية (ABS, EBD, ESP, BA)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MechAccent,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = if (isAbsActive) "تحكم كامل في التوجيه أثناء الفرملة" else "⚠️ خطر: انغلاق العجلات وانزلاق المركبة!",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isAbsActive) AccentEmerald else Color(0xFFEF4444)
                )
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (isAbsActive) AccentEmerald.copy(alpha = 0.15f) else Color(0xFFEF4444).copy(alpha = 0.15f),
                border = BorderStroke(1.dp, if (isAbsActive) AccentEmerald else Color(0xFFEF4444))
            ) {
                Text(
                    text = "مسافة التوقف: $stoppingDistanceMeters م",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isAbsActive) AccentEmerald else Color(0xFFEF4444),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // System Toggles
        Text(text = "تفعيل / تعطيل أنظمة الفرملة الإلكترونية:", fontSize = 11.sp, color = Color.White)
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(
                selected = isAbsActive,
                onClick = { isAbsActive = !isAbsActive },
                label = { Text("ABS", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = AccentEmerald,
                    selectedLabelColor = Color(0xFF0F172A),
                    containerColor = DarkSurfaceVariant,
                    labelColor = Color.White
                ),
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                selected = isEbdActive,
                onClick = { isEbdActive = !isEbdActive },
                label = { Text("EBD", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = PrimaryCyan,
                    selectedLabelColor = Color(0xFF0F172A),
                    containerColor = DarkSurfaceVariant,
                    labelColor = Color.White
                ),
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                selected = isEspActive,
                onClick = { isEspActive = !isEspActive },
                label = { Text("ESP", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = AccentAmber,
                    selectedLabelColor = Color(0xFF0F172A),
                    containerColor = DarkSurfaceVariant,
                    labelColor = Color.White
                ),
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                selected = isBaActive,
                onClick = { isBaActive = !isBaActive },
                label = { Text("BA", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF8B5CF6),
                    selectedLabelColor = Color.White,
                    containerColor = DarkSurfaceVariant,
                    labelColor = Color.White
                ),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Simulation Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF0B132B)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val startX = 50f
                val endX = size.width - 50f
                val carY = size.height / 2f

                // Road lanes
                drawLine(Color(0xFF334155), Offset(startX, carY + 25f), Offset(endX, carY + 25f), strokeWidth = 3f)
                drawLine(Color(0xFF1E293B), Offset(startX, carY - 25f), Offset(endX, carY - 25f), strokeWidth = 2f)

                // Stop line
                drawLine(Color(0xFFEF4444), Offset(endX, carY - 35f), Offset(endX, carY + 35f), strokeWidth = 4f)

                // Car position during braking
                val carX = startX + (endX - startX) * brakingProgress
                val carColor = if (isAbsActive) PrimaryCyan else Color(0xFFFF5722)

                // Car body
                drawRoundRect(
                    color = carColor,
                    topLeft = Offset(carX - 25f, carY - 14f),
                    size = Size(50f, 24f),
                    cornerRadius = CornerRadius(6f, 6f)
                )

                // Wheels
                val wheelColor = if (isBraking && !isAbsActive) Color.Red else Color.Black
                drawCircle(wheelColor, radius = 7f, center = Offset(carX - 15f, carY + 12f))
                drawCircle(wheelColor, radius = 7f, center = Offset(carX + 15f, carY + 12f))

                // Brake glow effect on disks during braking
                if (isBraking) {
                    drawCircle(Color(0xFFFF9800).copy(alpha = 0.8f), radius = 4f, center = Offset(carX - 15f, carY + 12f))
                    drawCircle(Color(0xFFFF9800).copy(alpha = 0.8f), radius = 4f, center = Offset(carX + 15f, carY + 12f))
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Metrics badges
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            EngineMetricBadge(label = "القدرة على التوجيه", value = if (isAbsActive) "متاحة 100%" else "منعدمة (انزلاق)")
            EngineMetricBadge(label = "توزيع الوزن (EBD)", value = if (isEbdActive) "متزن تلقائياً" else "حمولة على الأمام")
            EngineMetricBadge(label = "الثبات بالمنعطف (ESP)", value = if (isEspActive) "محمي من الدوران" else "معرض للعوم")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { isBraking = true },
            enabled = !isBraking,
            colors = ButtonDefaults.buttonColors(containerColor = MechAccent),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().height(42.dp)
        ) {
            Text(
                text = if (isBraking) "جاري كبح المركبة بالاحتكاك والأنظمة الذكية..." else "اختبار التوقف الطارئ من سرعة 100 كم/س 🚨",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = Color(0xFF0F172A)
            )
        }
    }
}

// -------------------------------------------------------------
// 9. محاكي مدارات الكواكب
// -------------------------------------------------------------
@Composable
fun SolarSystemSimulation(selectedComponentId: String?) {
    val infiniteTransition = rememberInfiniteTransition(label = "orbits")
    val orbitAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(animation = tween(durationMillis = 6000, easing = LinearEasing)),
        label = "orbit_motion"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "محاكي مدارات المجموعة الشمسية",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = SpaceAccent
        )
        Text(
            text = "دوران الكواكب حول جاذبية الشمس المركزية",
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF030712)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f

                // Sun
                drawCircle(Color(0xFFFFD54F), radius = 22f, center = Offset(cx, cy))

                // Orbit 1: Mercury / Earth
                drawCircle(Color(0x3338BDF8), radius = 55f, center = Offset(cx, cy), style = Stroke(1f))
                val a1 = Math.toRadians(orbitAngle.toDouble() * 1.5).toFloat()
                drawCircle(Color(0xFF38BDF8), radius = 6f, center = Offset(cx + 55f * cos(a1), cy + 55f * sin(a1)))

                // Orbit 2: Mars
                drawCircle(Color(0x33EF4444), radius = 80f, center = Offset(cx, cy), style = Stroke(1f))
                val a2 = Math.toRadians(orbitAngle.toDouble() * 1.0).toFloat()
                drawCircle(Color(0xFFFF5722), radius = 7f, center = Offset(cx + 80f * cos(a2), cy + 80f * sin(a2)))

                // Orbit 3: Jupiter
                drawCircle(Color(0x33F59E0B), radius = 115f, center = Offset(cx, cy), style = Stroke(1f))
                val a3 = Math.toRadians(orbitAngle.toDouble() * 0.4).toFloat()
                drawCircle(Color(0xFFF59E0B), radius = 14f, center = Offset(cx + 115f * cos(a3), cy + 115f * sin(a3)))
            }
        }
    }
}

@Composable
fun GenericInteractiveLabArabic(simulationType: SimulationType, selectedComponentId: String?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .background(Color(0xFF0F172A), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Storage,
                contentDescription = null,
                tint = PrimaryCyan,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "مختبر التجارب التفاعلية الشامل",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "اختر أحد المكونات بالأسفل لفحص أدائه وعزله ومحاكاته",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun EngineMetricBadge(label: String, value: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = DarkSurfaceVariant
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, fontSize = 10.sp, color = Color.Gray)
            Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

// -------------------------------------------------------------
// 10. محاكي الباور سبلاي وحساب الكفاءة (PSU Simulation)
// -------------------------------------------------------------
@Composable
fun PsuSimulation(selectedComponentId: String?) {
    var cpuWatts by remember { mutableFloatStateOf(125f) }
    var gpuWatts by remember { mutableFloatStateOf(250f) }
    var efficiencyRating by remember { mutableStateOf("Gold") }
    var testProtection by remember { mutableStateOf(false) }
    var isCheapPsu by remember { mutableStateOf(false) }

    val baseOtherWatts = 60f
    val totalDcWatts = cpuWatts + gpuWatts + baseOtherWatts
    val efficiencyFactor = when (efficiencyRating) {
        "Bronze" -> 0.85f
        "Gold" -> 0.90f
        "Titanium" -> 0.94f
        else -> 0.80f
    }
    val wallAcWatts = totalDcWatts / efficiencyFactor
    val wastedHeatWatts = wallAcWatts - totalDcWatts

    val recommendedWattage = when {
        totalDcWatts <= 400 -> 550
        totalDcWatts <= 520 -> 650
        totalDcWatts <= 650 -> 750
        totalDcWatts <= 750 -> 850
        else -> 1000
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "⚡ محاكي استهلاك الطاقة والكفاءة (PSU Lab)",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = AccentAmber
            )
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = AccentAmber.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, AccentAmber)
            ) {
                Text(
                    text = "موصى به: ${recommendedWattage}W",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentAmber,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // CPU Wattage Slider
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "سحب المعالج (CPU TDP):", fontSize = 11.sp, color = Color.White)
            Text(text = "${cpuWatts.toInt()}W", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PrimaryCyan)
        }
        Slider(
            value = cpuWatts,
            onValueChange = { cpuWatts = it },
            valueRange = 65f..250f,
            steps = 5,
            colors = SliderDefaults.colors(thumbColor = PrimaryCyan, activeTrackColor = PrimaryCyan),
            modifier = Modifier.fillMaxWidth()
        )

        // GPU Wattage Slider
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "سحب كارت الشاشة (GPU Power):", fontSize = 11.sp, color = Color.White)
            Text(text = "${gpuWatts.toInt()}W", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentAmber)
        }
        Slider(
            value = gpuWatts,
            onValueChange = { gpuWatts = it },
            valueRange = 75f..450f,
            steps = 7,
            colors = SliderDefaults.colors(thumbColor = AccentAmber, activeTrackColor = AccentAmber),
            modifier = Modifier.fillMaxWidth()
        )

        // Efficiency Rating Selector
        Text(text = "شهادة كفاءة الطاقة (80 Plus):", fontSize = 11.sp, color = Color.White)
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Bronze", "Gold", "Titanium").forEach { rating ->
                val isSelected = efficiencyRating == rating
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) AccentAmber else DarkSurfaceVariant,
                    border = BorderStroke(1.dp, if (isSelected) AccentAmber else Color(0xFF334155)),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { efficiencyRating = rating }
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = when (rating) {
                                "Bronze" -> "80+ برونز (85%)"
                                "Gold" -> "80+ ذهبي (90%)"
                                "Titanium" -> "80+ تيتانيوم (94%)"
                                else -> rating
                            },
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color(0xFF0F172A) else Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Power Readouts Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            EngineMetricBadge(label = "الحمل الفعلي (DC)", value = "${totalDcWatts.toInt()} واط")
            EngineMetricBadge(label = "المسحوب من الجدار", value = "${wallAcWatts.toInt()} واط")
            EngineMetricBadge(label = "الهدر الحراري", value = "${wastedHeatWatts.toInt()} واط")
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Protection Circuit Test
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            border = BorderStroke(1.dp, Color(0xFF1E293B))
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isCheapPsu) "النوع: باور رخيص غير معتمد ⚠️" else "النوع: باور سبلاي أصلي معتمد ✅",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isCheapPsu) Color(0xFFEF4444) else AccentEmerald
                    )
                    Button(
                        onClick = { isCheapPsu = !isCheapPsu; testProtection = false },
                        colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(text = "تبديل النوع", fontSize = 10.sp, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Button(
                    onClick = { testProtection = true },
                    colors = ButtonDefaults.buttonColors(containerColor = if (isCheapPsu) Color(0xFFEF4444) else PrimaryCyan),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "محاكاة صدمة كهربائية وماس (Short Circuit Test)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                }

                if (testProtection) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isCheapPsu) {
                            "💥 كارثة! الباور الرخيص يفتقر لدارات (SCP/OVP)؛ تسرب الجهد العالي واحترقت اللوحة الأم وكارت الشاشة فوراً!"
                        } else {
                            "🛡️ نجاح الأمان! استشعرت دارة SCP الماس الكهربائي وقطعت التيار في 0.2 ملي ثانية؛ كافة مكونات الحاسوب سالمة تماماً!"
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isCheapPsu) Color(0xFFEF4444) else AccentEmerald,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 11. محاكي التبريد الهوائي ضد المائي (Cooling Systems Lab)
// -------------------------------------------------------------
@Composable
fun CoolingSimulation(selectedComponentId: String?) {
    var isLiquidMode by remember { mutableStateOf(false) }
    var loadLevel by remember { mutableIntStateOf(1) }

    val tempCelsius = when (loadLevel) {
        0 -> if (isLiquidMode) 32 else 36
        1 -> if (isLiquidMode) 58 else 68
        else -> if (isLiquidMode) 74 else 86
    }

    val fanRpm = when (loadLevel) {
        0 -> if (isLiquidMode) 750 else 800
        1 -> if (isLiquidMode) 1200 else 1450
        else -> if (isLiquidMode) 1650 else 1950
    }

    val noiseDb = when (loadLevel) {
        0 -> if (isLiquidMode) 25 else 22
        1 -> if (isLiquidMode) 32 else 34
        else -> if (isLiquidMode) 36 else 42
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "❄️ مختبر مقارنة التبريد (Air vs. Liquid)",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = PrimaryCyan
            )
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (tempCelsius < 70) AccentEmerald.copy(alpha = 0.2f) else AccentAmber.copy(alpha = 0.2f),
                border = BorderStroke(1.dp, if (tempCelsius < 70) AccentEmerald else AccentAmber)
            ) {
                Text(
                    text = "حرارة المعالج: ${tempCelsius}°C",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (tempCelsius < 70) AccentEmerald else AccentAmber,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Cooler Type Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (!isLiquidMode) PrimaryCyan else DarkSurfaceVariant,
                border = BorderStroke(1.dp, if (!isLiquidMode) PrimaryCyan else Color(0xFF334155)),
                modifier = Modifier
                    .weight(1f)
                    .clickable { isLiquidMode = false }
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "💨 التبريد الهوائي (Air)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (!isLiquidMode) Color(0xFF0F172A) else Color.White
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (isLiquidMode) PrimaryCyan else DarkSurfaceVariant,
                border = BorderStroke(1.dp, if (isLiquidMode) PrimaryCyan else Color(0xFF334155)),
                modifier = Modifier
                    .weight(1f)
                    .clickable { isLiquidMode = true }
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "💧 التبريد المائي المغلق (AIO)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isLiquidMode) Color(0xFF0F172A) else Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Workload Toggle
        Text(text = "مستوى ضغط وتشغيل المعالج:", fontSize = 11.sp, color = Color.White)
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("خمول وتصفح (45W)", "ألعاب عادية (130W)", "ضغط أقصى (250W)").forEachIndexed { idx, label ->
                val isSelected = loadLevel == idx
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) AccentEmerald else DarkSurfaceVariant,
                    border = BorderStroke(1.dp, if (isSelected) AccentEmerald else Color(0xFF334155)),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { loadLevel = idx }
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = label,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color(0xFF0F172A) else Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Metrics Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            EngineMetricBadge(label = "الحرارة الناتجة", value = "$tempCelsius °C")
            EngineMetricBadge(label = "سرعة المروحة", value = "$fanRpm RPM")
            EngineMetricBadge(label = "مستوى الضجيج", value = "$noiseDb dB")
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Comparison Diagnostics Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            border = BorderStroke(1.dp, Color(0xFF1E293B))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = if (isLiquidMode) "مواصفات التبريد المائي المغلق (AIO 360mm):" else "مواصفات التبريد الهوائي (Dual Tower):",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryCyan
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isLiquidMode) {
                        "• خطر تسريب السوائل: ضئيل (2-3% بعد 4 سنوات)\n• العمر الافتراضي: 4-6 سنوات (عمر المضخة الميكانيكية)\n• المظهر: أنيق ومفتوح بالكامل مع إضاءة RGB مذهلة"
                    } else {
                        "• خطر تسريب السوائل: 0% معدوم تماماً (لا توجد سوائل خارجية)\n• العمر الافتراضي: 10+ سنوات (مجرد معدن ومروحة قابلة للاستبدال)\n• المظهر: كتلة معدنية ضخمة قد تغطي جزءاً من شريحتي الرام"
                    },
                    fontSize = 11.sp,
                    color = Color(0xFFCBD5E1),
                    lineHeight = 16.sp
                )
            }
        }
    }
}

// -------------------------------------------------------------
// 12. محاكي نظام التعليق والعفشة (Suspension Lab)
// -------------------------------------------------------------
@Composable
fun SuspensionSimulation(selectedComponentId: String?) {
    var selectedType by remember { mutableStateOf("Multilink") } // "MacPherson", "Multilink", "Air"
    var isTestingBump by remember { mutableStateOf(false) }
    var airHeightLevel by remember { mutableIntStateOf(1) } // 0: منخفض, 1: عادي, 2: مرتفع (للطرق الوعرة)
    var bumpProgress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(isTestingBump) {
        if (isTestingBump) {
            bumpProgress = 0f
            for (step in 1..40) {
                delay(30)
                bumpProgress = step / 40f
            }
            isTestingBump = false
            bumpProgress = 0f
        }
    }

    val comfortScore = when (selectedType) {
        "MacPherson" -> "75% (متوسط واقتصادي)"
        "Multilink" -> "92% (راحة وثبات ممتاز)"
        else -> "98% (سجادة هوائية فاخرة)"
    }

    val dampingSpeed = when (selectedType) {
        "MacPherson" -> "1.2 ثانية (ارتداد خفيف)"
        "Multilink" -> "0.5 ثانية (استقرار سريع)"
        else -> "0.2 ثانية (امتصاص فوري)"
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🛞 مختبر محاكاة العفشة وامتصاص الصدمات",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MechAccent
            )
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MechAccent.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, MechAccent)
            ) {
                Text(
                    text = "ثبات الإطارات: 100%",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MechAccent,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Suspension Type Switcher
        Text(text = "اختر نوع نظام التعليق للاختبار:", fontSize = 11.sp, color = Color.White)
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                "MacPherson" to "ماكفرسون (بسيط)",
                "Multilink" to "Multi-link (مستقل)",
                "Air" to "تعليق هوائي (Air)"
            ).forEach { (typeKey, label) ->
                val isSelected = selectedType == typeKey
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) MechAccent else DarkSurfaceVariant,
                    border = BorderStroke(1.dp, if (isSelected) MechAccent else Color(0xFF334155)),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedType = typeKey }
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = label,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color(0xFF0F172A) else Color.White
                        )
                    }
                }
            }
        }

        // Air Suspension Height Control
        if (selectedType == "Air") {
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "التحكم في ارتفاع السيارة (Air Suspension Level):", fontSize = 11.sp, color = AccentEmerald)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("منخفض (Sport)", "طبيعي (Comfort)", "مرتفع (Off-road)").forEachIndexed { idx, title ->
                    val isLvlSelected = airHeightLevel == idx
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (isLvlSelected) AccentEmerald else DarkSurfaceVariant,
                        border = BorderStroke(1.dp, if (isLvlSelected) AccentEmerald else Color(0xFF334155)),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { airHeightLevel = idx }
                    ) {
                        Text(
                            text = title,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isLvlSelected) Color(0xFF0F172A) else Color.White,
                            modifier = Modifier.padding(vertical = 4.dp),
                            maxLines = 1
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Visual Canvas for Bump Test
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF0F172A)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val groundY = size.height - 35f

                // Road
                drawLine(Color(0xFF334155), Offset(20f, groundY), Offset(size.width - 20f, groundY), strokeWidth = 3f)

                // Bump in center
                drawArc(
                    color = Color(0xFFE2E8F0),
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = true,
                    topLeft = Offset(cx - 30f, groundY - 18f),
                    size = Size(60f, 36f)
                )

                // Oscillation offset
                val bounce = if (isTestingBump) {
                    val freq = when (selectedType) {
                        "MacPherson" -> 4f
                        "Multilink" -> 2.5f
                        else -> 1.5f
                    }
                    val amplitude = when (selectedType) {
                        "MacPherson" -> 20f
                        "Multilink" -> 10f
                        else -> 4f
                    }
                    (sin(bumpProgress * Math.PI.toFloat() * freq) * amplitude * (1f - bumpProgress))
                } else {
                    0f
                }

                val baseWheelY = groundY - 18f
                val wheelY = baseWheelY - bounce
                val chassisHeightOffset = if (selectedType == "Air") (airHeightLevel - 1) * 10f else 0f
                val chassisY = wheelY - 45f - chassisHeightOffset

                // Chassis body
                drawRoundRect(
                    color = MechAccent,
                    topLeft = Offset(cx - 70f, chassisY),
                    size = Size(140f, 24f),
                    cornerRadius = CornerRadius(6f, 6f)
                )

                // Spring & Shock Absorber
                drawLine(
                    color = AccentAmber,
                    start = Offset(cx, chassisY + 24f),
                    end = Offset(cx, wheelY),
                    strokeWidth = 5f
                )

                // Wheel
                drawCircle(color = Color.Black, radius = 18f, center = Offset(cx, wheelY))
                drawCircle(color = Color.Gray, radius = 8f, center = Offset(cx, wheelY))
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Metrics
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            EngineMetricBadge(label = "مستوى الراحة والعزل", value = comfortScore)
            EngineMetricBadge(label = "زمن إخماد الارتداد", value = dampingSpeed)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { isTestingBump = true },
            enabled = !isTestingBump,
            colors = ButtonDefaults.buttonColors(containerColor = MechAccent),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().height(42.dp)
        ) {
            Text(
                text = if (isTestingBump) "جاري امتصاص واخماد الصدمة..." else "اختبار المرور فوق مطب صلب 💥",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
        }
    }
}
