package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.BrightnessHigh
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storm
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.SpaceAccent
import com.example.ui.theme.SpacePrimary
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// =================================================================
// 1. EARTH SIMULATION (كوكب الأرض - ليل ونهار وغلاف مغناطيسي وطبقات)
// =================================================================
@Composable
fun EarthSimulation(selectedComponentId: String?) {
    var isNightMode by remember { mutableStateOf(false) }
    var showMagneticShield by remember { mutableStateOf(true) }
    var showInteriorLayers by remember { mutableStateOf(false) }
    var timeOfDaySlider by remember { mutableFloatStateOf(12f) } // 0 to 24 hours

    val infiniteTransition = rememberInfiniteTransition(label = "earth_spin")
    val spinPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spin_phase"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "محاكي كوكب الأرض التفاعلي",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SpaceAccent
                )
                Text(
                    text = if (showInteriorLayers) "تشريح باطن الأرض واللب والوشاح" else "الكرة الأرضية: دورة الليل والنهار والدرع المغناطيسي",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Control Toggles
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = !isNightMode,
                onClick = { isNightMode = false; timeOfDaySlider = 12f },
                label = { Text("نهار مشرق ☀️", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF0284C7),
                    selectedLabelColor = Color.White,
                    containerColor = DarkSurfaceVariant,
                    labelColor = Color.LightGray
                )
            )
            FilterChip(
                selected = isNightMode,
                onClick = { isNightMode = true; timeOfDaySlider = 2f },
                label = { Text("ليل وأضواء المدن 🌙", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF4338CA),
                    selectedLabelColor = Color.White,
                    containerColor = DarkSurfaceVariant,
                    labelColor = Color.LightGray
                )
            )
            FilterChip(
                selected = showMagneticShield,
                onClick = { showMagneticShield = !showMagneticShield },
                label = { Text("الدرع المغناطيسي 🛡️", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF0D9488),
                    selectedLabelColor = Color.White,
                    containerColor = DarkSurfaceVariant,
                    labelColor = Color.LightGray
                )
            )
            FilterChip(
                selected = showInteriorLayers,
                onClick = { showInteriorLayers = !showInteriorLayers },
                label = { Text("طبقات الباطن 🌋", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFE11D48),
                    selectedLabelColor = Color.White,
                    containerColor = DarkSurfaceVariant,
                    labelColor = Color.LightGray
                )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Visual Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF030712))
                .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f
                val globeRadius = 70f

                // Distant Stars
                drawCircle(Color.White.copy(alpha = 0.5f), radius = 1.5f, center = Offset(cx - 110f, cy - 60f))
                drawCircle(Color.White.copy(alpha = 0.7f), radius = 2f, center = Offset(cx + 120f, cy - 70f))
                drawCircle(Color.White.copy(alpha = 0.4f), radius = 1.5f, center = Offset(cx - 90f, cy + 70f))
                drawCircle(Color.White.copy(alpha = 0.6f), radius = 2f, center = Offset(cx + 90f, cy + 60f))

                if (showInteriorLayers) {
                    // Crust (قشرة)
                    drawCircle(Color(0xFF64748B), radius = globeRadius, center = Offset(cx, cy))
                    // Mantle (وشاح الحمم الصخرية)
                    drawCircle(Color(0xFFEA580C), radius = globeRadius * 0.85f, center = Offset(cx, cy))
                    // Outer Core (اللب الخارجي السائل)
                    drawCircle(Color(0xFFFBBF24), radius = globeRadius * 0.55f, center = Offset(cx, cy))
                    // Inner Core (اللب الداخلي الصلب)
                    drawCircle(Color(0xFFFEF08A), radius = globeRadius * 0.28f, center = Offset(cx, cy))
                } else {
                    // Atmospheric Halo Glow
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFF38BDF8).copy(alpha = 0.4f), Color.Transparent),
                            center = Offset(cx, cy),
                            radius = globeRadius + 18f
                        ),
                        radius = globeRadius + 18f,
                        center = Offset(cx, cy)
                    )

                    // Earth Sphere Base (Ocean blue or Dark navy on night)
                    val baseOcean = if (isNightMode) Color(0xFF0A192F) else Color(0xFF0284C7)
                    drawCircle(baseOcean, radius = globeRadius, center = Offset(cx, cy))

                    // Continents Representation
                    val continentColor = if (isNightMode) Color(0xFF1E293B) else Color(0xFF16A34A)
                    val offsetContinents = (spinPhase * 80f) % 80f

                    // Draw continent patches
                    drawOval(
                        color = continentColor,
                        topLeft = Offset(cx - 45f + offsetContinents - 40f, cy - 35f),
                        size = Size(40f, 30f)
                    )
                    drawOval(
                        color = continentColor,
                        topLeft = Offset(cx - 10f + offsetContinents - 40f, cy + 5f),
                        size = Size(35f, 25f)
                    )
                    drawOval(
                        color = continentColor,
                        topLeft = Offset(cx + 25f - offsetContinents, cy - 25f),
                        size = Size(30f, 35f)
                    )

                    // Night Shadow Overlay & City Lights
                    if (isNightMode) {
                        drawCircle(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0x99030712), Color(0xDD030712)),
                                startX = cx - globeRadius,
                                endX = cx + globeRadius
                            ),
                            radius = globeRadius,
                            center = Offset(cx, cy)
                        )
                        // Twinkling Golden City Lights
                        val lightAlpha = 0.7f + 0.3f * sin(spinPhase * 2 * PI.toFloat())
                        drawCircle(Color(0xFFFDE047).copy(alpha = lightAlpha), radius = 2.5f, center = Offset(cx - 20f, cy - 15f))
                        drawCircle(Color(0xFFFDE047).copy(alpha = lightAlpha), radius = 2f, center = Offset(cx + 10f, cy + 8f))
                        drawCircle(Color(0xFFFDE047).copy(alpha = lightAlpha), radius = 3f, center = Offset(cx - 5f, cy - 25f))
                        drawCircle(Color(0xFFFDE047).copy(alpha = lightAlpha), radius = 2.5f, center = Offset(cx + 25f, cy - 5f))
                    }

                    // Magnetic Field Lines (Shield)
                    if (showMagneticShield) {
                        val shieldColor = Color(0xFF14B8A6).copy(alpha = 0.75f)
                        val shieldGlow = Color(0xFF2DD4BF).copy(alpha = 0.35f)

                        // Dipole curves around Earth
                        drawArc(
                            color = shieldColor,
                            startAngle = -140f,
                            sweepAngle = 100f,
                            useCenter = false,
                            topLeft = Offset(cx - globeRadius * 1.5f, cy - globeRadius * 1.3f),
                            size = Size(globeRadius * 3f, globeRadius * 2.6f),
                            style = Stroke(width = 2.5f, cap = StrokeCap.Round)
                        )
                        drawArc(
                            color = shieldColor,
                            startAngle = 40f,
                            sweepAngle = 100f,
                            useCenter = false,
                            topLeft = Offset(cx - globeRadius * 1.5f, cy - globeRadius * 1.3f),
                            size = Size(globeRadius * 3f, globeRadius * 2.6f),
                            style = Stroke(width = 2.5f, cap = StrokeCap.Round)
                        )
                        // Polar Auroral Cusps
                        drawCircle(Color(0xFF22C55E).copy(alpha = 0.6f), radius = 9f, center = Offset(cx, cy - globeRadius))
                        drawCircle(Color(0xFF22C55E).copy(alpha = 0.6f), radius = 9f, center = Offset(cx, cy + globeRadius))
                    }
                }

                // Moon Orbiting Earth
                val moonDist = globeRadius + 45f
                val moonAngleRad = Math.toRadians((spinPhase * 360.0)).toFloat()
                val moonX = cx + moonDist * cos(moonAngleRad)
                val moonY = cy + moonDist * sin(moonAngleRad) * 0.45f
                drawCircle(Color(0xFFCBD5E1), radius = 7.5f, center = Offset(moonX, moonY))
            }

            // Overlay Info Badge
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xCC0F172A),
                border = BorderStroke(1.dp, Color(0xFF334155))
            ) {
                Text(
                    text = if (showInteriorLayers) "🌋 اللب الداخلي: 6,000°C حديد ونيكل صلب" else "🌍 8.2 مليار نسمة | واحة الحياة | 1.00 AU",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF38BDF8),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

// =================================================================
// 2. MARS SIMULATION (كوكب المريخ - عاصفة ترابية وبركان أوليمبوس)
// =================================================================
@Composable
fun MarsSimulation(selectedComponentId: String?) {
    var isDustStormActive by remember { mutableStateOf(false) }
    var showOlympusMons by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "mars_storm")
    val stormOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "storm_flow"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "محاكي كوكب المريخ التفاعلي",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFB7185)
                )
                Text(
                    text = if (showOlympusMons) "فحص بركان أوليمبوس مونس (ارتفاع 22 كم)" else "سطح الصدأ الأحمر وعواصف الغبار العالمية",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Interaction Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = isDustStormActive,
                onClick = { isDustStormActive = !isDustStormActive },
                label = { Text(if (isDustStormActive) "إيقاف العاصفة الترابية 🌪️" else "تفعيل عاصفة ترابية Fe2O3 🌪️", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFEA580C),
                    selectedLabelColor = Color.White,
                    containerColor = DarkSurfaceVariant,
                    labelColor = Color.LightGray
                )
            )
            FilterChip(
                selected = showOlympusMons,
                onClick = { showOlympusMons = !showOlympusMons },
                label = { Text("تكبير بركان أوليمبوس 🌋", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFDC2626),
                    selectedLabelColor = Color.White,
                    containerColor = DarkSurfaceVariant,
                    labelColor = Color.LightGray
                )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF05050A))
                .border(1.dp, Color(0xFF271515), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f
                val marsRadius = if (showOlympusMons) 100f else 72f

                if (showOlympusMons) {
                    // Close-up on Olympus Mons Volcano
                    // Giant shield volcano base
                    drawCircle(Color(0xFF991B1B), radius = 95f, center = Offset(cx, cy))
                    drawCircle(Color(0xFFB91C1C), radius = 70f, center = Offset(cx, cy))
                    drawCircle(Color(0xFFDC2626), radius = 45f, center = Offset(cx, cy))
                    // Central Caldera (فوهة البركان العملاقة)
                    drawCircle(Color(0xFF450A0A), radius = 22f, center = Offset(cx, cy))
                    drawCircle(Color(0xFF1C0303), radius = 14f, center = Offset(cx, cy))
                } else {
                    // Mars Planetary Disc (Rust-red & orange)
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFF97316), Color(0xFFDC2626), Color(0xFF991B1B)),
                            center = Offset(cx - 15f, cy - 15f),
                            radius = marsRadius
                        ),
                        radius = marsRadius,
                        center = Offset(cx, cy)
                    )

                    // Northern Polar Ice Cap (جليد جاف CO2 ناصع البياض)
                    drawOval(
                        color = Color.White.copy(alpha = 0.9f),
                        topLeft = Offset(cx - 24f, cy - marsRadius + 3f),
                        size = Size(48f, 15f)
                    )

                    // Olympus Mons dot locator
                    drawCircle(Color(0xFF7F1D1D), radius = 10f, center = Offset(cx - 25f, cy - 10f))
                    drawCircle(Color(0xFFEF4444), radius = 4f, center = Offset(cx - 25f, cy - 10f))

                    // Valles Marineris Canyon Line (أعمق وادٍ في المنظومة)
                    drawLine(
                        color = Color(0xFF450A0A),
                        start = Offset(cx - 30f, cy + 15f),
                        end = Offset(cx + 35f, cy + 22f),
                        strokeWidth = 3f,
                        cap = StrokeCap.Round
                    )

                    // Phobos & Deimos Moons
                    val phobosDist = marsRadius + 28f
                    val phobosAngle = Math.toRadians((stormOffset * 2.0)).toFloat()
                    drawCircle(Color(0xFF94A3B8), radius = 4.5f, center = Offset(cx + phobosDist * cos(phobosAngle), cy + phobosDist * sin(phobosAngle) * 0.5f))

                    val deimosDist = marsRadius + 50f
                    val deimosAngle = Math.toRadians((stormOffset * 0.8)).toFloat()
                    drawCircle(Color(0xFF64748B), radius = 3.5f, center = Offset(cx + deimosDist * cos(deimosAngle), cy + deimosDist * sin(deimosAngle) * 0.5f))

                    // Dust Storm Particles Overlay
                    if (isDustStormActive) {
                        for (i in 0..18) {
                            val pPhase = ((stormOffset + i * 20) % 360) / 360f
                            val px = cx - marsRadius + (pPhase * marsRadius * 2f)
                            val py = cy - marsRadius * 0.7f + ((i * 19) % (marsRadius * 1.4f))
                            drawCircle(
                                color = Color(0xFFFDBA74).copy(alpha = 0.75f),
                                radius = 4f + (i % 3),
                                center = Offset(px, py)
                            )
                        }
                    }
                }
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xCC0F172A),
                border = BorderStroke(1.dp, Color(0xFF334155))
            ) {
                Text(
                    text = if (showOlympusMons) "🌋 ارتفاع أوليمبوس: 22 كم (3 أضعاف قمة إيفرست!)" else "🔴 صدأ الحديد Fe2O3 | قمران: فوبوس وديموس",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFB7185),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

// =================================================================
// 3. ASTEROID BELT SIMULATION (حزام الكويكبات والصخور التفاعلية)
// =================================================================
@Composable
fun AsteroidBeltSimulation(selectedComponentId: String?) {
    var selectedAsteroid by remember { mutableStateOf("ceres") } // ceres, vesta, pallas, ida
    var orbitSpeed by remember { mutableFloatStateOf(1f) }

    val infiniteTransition = rememberInfiniteTransition(label = "belt_spin")
    val beltAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = (8000 / orbitSpeed).toInt().coerceAtLeast(1000), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "belt_angle"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "محاكي حزام الكويكبات التفاعلي",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFBBF24)
        )
        Text(
            text = "اضغط على أي كويكب لتكبيره وفحص حجمه وتركيبه المعدني",
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Asteroid Selector Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedAsteroid == "ceres",
                onClick = { selectedAsteroid = "ceres" },
                label = { Text("كوكب سيريس (Ceres 940km) ⭐", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFF59E0B),
                    selectedLabelColor = Color(0xFF0F172A),
                    containerColor = DarkSurfaceVariant,
                    labelColor = Color.LightGray
                )
            )
            FilterChip(
                selected = selectedAsteroid == "vesta",
                onClick = { selectedAsteroid = "vesta" },
                label = { Text("فيستا (Vesta 525km)", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFF59E0B),
                    selectedLabelColor = Color(0xFF0F172A),
                    containerColor = DarkSurfaceVariant,
                    labelColor = Color.LightGray
                )
            )
            FilterChip(
                selected = selectedAsteroid == "pallas",
                onClick = { selectedAsteroid = "pallas" },
                label = { Text("بالاس (Pallas 512km)", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFF59E0B),
                    selectedLabelColor = Color(0xFF0F172A),
                    containerColor = DarkSurfaceVariant,
                    labelColor = Color.LightGray
                )
            )
            FilterChip(
                selected = selectedAsteroid == "ida",
                onClick = { selectedAsteroid = "ida" },
                label = { Text("إيدا وقمشه داكتيل", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFF59E0B),
                    selectedLabelColor = Color(0xFF0F172A),
                    containerColor = DarkSurfaceVariant,
                    labelColor = Color.LightGray
                )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF030712))
                .border(1.dp, Color(0xFF2E2413), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f

                // Orbit Rings (Mars inside, Jupiter outside)
                drawCircle(Color(0x33EF4444), radius = 45f, center = Offset(cx, cy), style = Stroke(1f))
                drawCircle(Color(0x33F59E0B), radius = 105f, center = Offset(cx, cy), style = Stroke(1f))

                // Sun at center
                drawCircle(Color(0xFFFFD54F), radius = 12f, center = Offset(cx, cy))

                // Dense Asteroid Belt Cloud (Floating rocks in orbit 2.2 to 3.2 AU)
                for (i in 0..45) {
                    val baseRadius = 60f + (i % 38)
                    val angleOffset = i * 28f + (beltAngle * (0.8f + (i % 4) * 0.1f))
                    val rad = Math.toRadians(angleOffset.toDouble()).toFloat()
                    val ax = cx + baseRadius * cos(rad)
                    val ay = cy + baseRadius * sin(rad) * 0.6f
                    drawCircle(
                        color = Color(0xFF94A3B8).copy(alpha = 0.7f),
                        radius = 1.8f + (i % 2),
                        center = Offset(ax, ay)
                    )
                }

                // Spotlight Zoom on Selected Asteroid
                val zoomX = cx + 80f
                val zoomY = cy - 40f
                drawCircle(Color(0x33F59E0B), radius = 38f, center = Offset(zoomX, zoomY))
                drawCircle(Color(0xFFF59E0B), radius = 38f, center = Offset(zoomX, zoomY), style = Stroke(1.5f))

                when (selectedAsteroid) {
                    "ceres" -> {
                        // Ceres: spherical dwarf planet with Occator white salt spots
                        drawCircle(Color(0xFF64748B), radius = 26f, center = Offset(zoomX, zoomY))
                        drawCircle(Color.White, radius = 3.5f, center = Offset(zoomX - 5f, zoomY - 4f))
                        drawCircle(Color.White, radius = 2.5f, center = Offset(zoomX - 2f, zoomY - 1f))
                    }
                    "vesta" -> {
                        // Vesta: giant Rheasilvia south crater
                        drawOval(Color(0xFF78716C), topLeft = Offset(zoomX - 22f, zoomY - 18f), size = Size(44f, 36f))
                        drawCircle(Color(0xFF44403C), radius = 11f, center = Offset(zoomX + 6f, zoomY + 4f))
                    }
                    "pallas" -> {
                        // Pallas: highly irregular carbonaceous
                        drawOval(Color(0xFF475569), topLeft = Offset(zoomX - 20f, zoomY - 19f), size = Size(40f, 38f))
                    }
                    "ida" -> {
                        // Ida potato shape + tiny moon Dactyl
                        drawOval(Color(0xFF57534E), topLeft = Offset(zoomX - 24f, zoomY - 12f), size = Size(38f, 24f))
                        drawCircle(Color(0xFFD6D3D1), radius = 4f, center = Offset(zoomX + 26f, zoomY - 16f))
                    }
                }
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xDD0F172A),
                border = BorderStroke(1.dp, Color(0xFF334155))
            ) {
                val detailText = when (selectedAsteroid) {
                    "ceres" -> "⭐ كوكب سيريس القزم: قطر 940 كم | يمثل ثلث كتلة الحزام وحدها!"
                    "vesta" -> "🪨 فيستا: قطر 525 كم | صخور بازلتية وفوهة صدمية عملاقة"
                    "pallas" -> "☄️ بالاس: قطر 512 كم | مدار مائل بشدة 34 درجة"
                    else -> "🌙 الكويكب إيدا وقمشه داكتيل: أول كويكب يُكتشف له قمر خاص"
                }
                Text(
                    text = detailText,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFBBF24),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

// =================================================================
// 4. SUN & STARS SIMULATION (الشمس والتوهجات وسلايدر الحرارة واللون)
// =================================================================
@Composable
fun SunAndStarsSimulation(selectedComponentId: String?) {
    var starTempKelvin by remember { mutableFloatStateOf(5800f) } // 3000K to 12000K
    var isSolarFlareActive by remember { mutableStateOf(false) }
    var showNebulaCloud by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "star_plasma")
    val plasmaPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "plasma_phase"
    )

    // Compute star color based on Kelvin temperature
    val (starColor, starGlow, starLabel) = remember(starTempKelvin) {
        when {
            starTempKelvin < 4000f -> Triple(
                Color(0xFFEF4444),
                Color(0xFFDC2626),
                "عملاق أحمر فائق (مثل بيتلجوز Betelgeuse ~3,500K)"
            )
            starTempKelvin < 7000f -> Triple(
                Color(0xFFFBBF24),
                Color(0xFFF59E0B),
                "قزم أصفر متزن (مثل شمسنا Sun ~5,800K)"
            )
            else -> Triple(
                Color(0xFF67E8F9),
                Color(0xFF06B6D4),
                "نجم أزرق-أبيض عملاق (مثل الشعرى اليمانية Sirius ~9,940K)"
            )
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
                    text = "محاكي الشمس والنجوم والتوهجات",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = starColor
                )
                Text(
                    text = starLabel,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Interaction Buttons & Sliders
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { isSolarFlareActive = !isSolarFlareActive },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSolarFlareActive) Color(0xFFEF4444) else DarkSurfaceVariant
                ),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Bolt, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (isSolarFlareActive) "توهج شمسي نشط! 🔥" else "إطلاق توهج شمسي ⚡", fontSize = 11.sp)
            }

            Button(
                onClick = { showNebulaCloud = !showNebulaCloud },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (showNebulaCloud) Color(0xFF9333EA) else DarkSurfaceVariant
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text(if (showNebulaCloud) "إخفاء السديم 🌌" else "سديم الجبار M42 🌌", fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Temperature Slider
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("الحرارة: ${starTempKelvin.toInt()}K", fontSize = 11.sp, color = Color.LightGray, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Slider(
                value = starTempKelvin,
                onValueChange = { starTempKelvin = it },
                valueRange = 3000f..12000f,
                colors = SliderDefaults.colors(thumbColor = starColor, activeTrackColor = starColor),
                modifier = Modifier.weight(1f)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF030712))
                .border(1.dp, starColor.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f
                val starRadius = if (starTempKelvin < 4000f) 65f else 50f

                // Nebula Background Clouds if active (سديم غازي متوهج)
                if (showNebulaCloud) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFC084FC).copy(alpha = 0.4f), Color(0xFFE879F9).copy(alpha = 0.2f), Color.Transparent),
                            center = Offset(cx - 70f, cy - 30f),
                            radius = 120f
                        ),
                        radius = 120f,
                        center = Offset(cx - 70f, cy - 30f)
                    )
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFF38BDF8).copy(alpha = 0.35f), Color.Transparent),
                            center = Offset(cx + 80f, cy + 40f),
                            radius = 100f
                        ),
                        radius = 100f,
                        center = Offset(cx + 80f, cy + 40f)
                    )
                }

                // Corona Glow
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(starGlow.copy(alpha = 0.5f), Color.Transparent),
                        center = Offset(cx, cy),
                        radius = starRadius + 35f
                    ),
                    radius = starRadius + 35f,
                    center = Offset(cx, cy)
                )

                // Stellar Core Disc
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color.White, starColor, starGlow),
                        center = Offset(cx - 10f, cy - 10f),
                        radius = starRadius
                    ),
                    radius = starRadius,
                    center = Offset(cx, cy)
                )

                // Solar Flares / Prominences (توهجات بلازمية مقوسة)
                if (isSolarFlareActive) {
                    val flareSweep = 60f + 25f * sin(plasmaPhase * 2 * PI.toFloat())
                    drawArc(
                        color = Color(0xFFFEF08A),
                        startAngle = -70f,
                        sweepAngle = flareSweep,
                        useCenter = false,
                        topLeft = Offset(cx - starRadius - 25f, cy - starRadius - 35f),
                        size = Size(starRadius * 2f + 50f, starRadius * 2f + 70f),
                        style = Stroke(width = 4f, cap = StrokeCap.Round)
                    )
                    drawArc(
                        color = starColor,
                        startAngle = 110f,
                        sweepAngle = 45f,
                        useCenter = false,
                        topLeft = Offset(cx - starRadius - 30f, cy - starRadius - 20f),
                        size = Size(starRadius * 2f + 60f, starRadius * 2f + 40f),
                        style = Stroke(width = 3.5f, cap = StrokeCap.Round)
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xCC0F172A),
                border = BorderStroke(1.dp, Color(0xFF334155))
            ) {
                Text(
                    text = "🔥 اندماج الهيدروجين إلى هيليوم | مفاعل نووي طبيعي",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = starColor,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

// =================================================================
// 5. GAS GIANTS SIMULATION (المشتري والبقعة الحمراء / زحل والحلقات)
// =================================================================
@Composable
fun GasGiantsSimulation(topicId: String, selectedComponentId: String?) {
    val isSaturn = topicId == "planet_saturn"
    var ringTiltAngle by remember { mutableFloatStateOf(20f) } // for Saturn
    var showRedSpotDetail by remember { mutableStateOf(true) }

    val infiniteTransition = rememberInfiniteTransition(label = "gas_spin")
    val stormSpin by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spot_spin"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = if (isSaturn) "محاكي كوكب زحل وحلقاته الجليدية" else "محاكي كوكب المشتري والبقعة الحمراء العظيمة",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSaturn) Color(0xFFFBBF24) else Color(0xFFF97316)
        )
        Text(
            text = if (isSaturn) "تحكم في زاوية الحلقات وفجوة كاسيني (146 قمراً)" else "عاصفة البقعة الحمراء المستمرة >350 سنة وتتسع لـ 1.3 كوكب أرض",
            fontSize = 13.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (isSaturn) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("زاوية ميلان الحلقات: ${ringTiltAngle.toInt()}°", fontSize = 11.sp, color = Color.LightGray, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Slider(
                    value = ringTiltAngle,
                    onValueChange = { ringTiltAngle = it },
                    valueRange = 0f..35f,
                    colors = SliderDefaults.colors(thumbColor = Color(0xFFFBBF24), activeTrackColor = Color(0xFFFBBF24)),
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = showRedSpotDetail,
                    onClick = { showRedSpotDetail = !showRedSpotDetail },
                    label = { Text("البقعة الحمراء (>350 سنة) 🌀", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFEA580C),
                        selectedLabelColor = Color.White,
                        containerColor = DarkSurfaceVariant,
                        labelColor = Color.LightGray
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF030712))
                .border(1.dp, Color(0xFF292524), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f
                val planetRadius = if (isSaturn) 52f else 68f

                if (isSaturn) {
                    // Back half of Saturn's Rings
                    val ringTiltRad = Math.toRadians(ringTiltAngle.toDouble()).toFloat()
                    val ringWidth = 240f
                    val ringHeight = 70f * sin(ringTiltRad.coerceAtLeast(0.15f))

                    drawOval(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFEAB308).copy(alpha = 0.8f), Color(0xFFCA8A04), Color(0xFF713F12)),
                            center = Offset(cx, cy),
                            radius = ringWidth / 2f
                        ),
                        topLeft = Offset(cx - ringWidth / 2f, cy - ringHeight / 2f),
                        size = Size(ringWidth, ringHeight),
                        style = Stroke(width = 24f)
                    )
                    // Cassini Division (فجوة كاسيني الداكنة)
                    drawOval(
                        color = Color(0xFF030712),
                        topLeft = Offset(cx - ringWidth * 0.44f, cy - ringHeight * 0.44f),
                        size = Size(ringWidth * 0.88f, ringHeight * 0.88f),
                        style = Stroke(width = 3.5f)
                    )

                    // Saturn Body
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFFEF08A), Color(0xFFFBBF24), Color(0xFFB45309)),
                            center = Offset(cx - 10f, cy - 10f),
                            radius = planetRadius
                        ),
                        radius = planetRadius,
                        center = Offset(cx, cy)
                    )

                    // Front half of Saturn's Rings (drawn on top of body to give 3D depth)
                    drawArc(
                        color = Color(0xFFFDE047).copy(alpha = 0.9f),
                        startAngle = 0f,
                        sweepAngle = 180f,
                        useCenter = false,
                        topLeft = Offset(cx - ringWidth / 2f, cy - ringHeight / 2f),
                        size = Size(ringWidth, ringHeight),
                        style = Stroke(width = 22f)
                    )
                } else {
                    // Jupiter: Giant gas bands
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFFED7AA), Color(0xFFFB923C), Color(0xFFC2410C)),
                            center = Offset(cx - 15f, cy - 15f),
                            radius = planetRadius
                        ),
                        radius = planetRadius,
                        center = Offset(cx, cy)
                    )

                    // Atmospheric Belts & Zones (أحزمة غازية ملونة)
                    drawRect(Color(0x559A3412), topLeft = Offset(cx - planetRadius + 3f, cy - 30f), size = Size(planetRadius * 2f - 6f, 16f))
                    drawRect(Color(0x447C2D12), topLeft = Offset(cx - planetRadius + 3f, cy - 5f), size = Size(planetRadius * 2f - 6f, 18f))
                    drawRect(Color(0x559A3412), topLeft = Offset(cx - planetRadius + 3f, cy + 20f), size = Size(planetRadius * 2f - 6f, 14f))

                    // Great Red Spot (البقعة الحمراء العظيمة)
                    val spotX = cx + 20f
                    val spotY = cy + 12f
                    drawOval(
                        color = Color(0xFFDC2626),
                        topLeft = Offset(spotX - 16f, spotY - 10f),
                        size = Size(32f, 20f)
                    )
                    // Swirl inside Great Red Spot
                    val spotAngleRad = Math.toRadians(stormSpin.toDouble()).toFloat()
                    drawCircle(
                        color = Color(0xFFFEF08A),
                        radius = 3.5f,
                        center = Offset(spotX + 5f * cos(spotAngleRad), spotY + 4f * sin(spotAngleRad))
                    )

                    // 4 Galilean Moons (غانيميد، كاليستو، إيو، وأوروبا)
                    drawCircle(Color(0xFFCBD5E1), radius = 4f, center = Offset(cx - 85f, cy - 10f)) // Europa
                    drawCircle(Color(0xFFFDE047), radius = 4.5f, center = Offset(cx - 105f, cy + 5f)) // Io
                    drawCircle(Color(0xFFE2E8F0), radius = 7f, center = Offset(cx + 88f, cy - 15f)) // Ganymede
                    drawCircle(Color(0xFF94A3B8), radius = 6f, center = Offset(cx + 115f, cy + 10f)) // Callisto
                }
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xDD0F172A),
                border = BorderStroke(1.dp, Color(0xFF334155))
            ) {
                Text(
                    text = if (isSaturn) "🪐 146 قمراً معتمداً | سمك الحلقات 10-100 متر فقط من الجليد" else "🌀 عاصفة البقعة الحمراء تتسع لـ 1.3 كوكب أرض | 95 قمراً",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSaturn) Color(0xFFFBBF24) else Color(0xFFF97316),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

// =================================================================
// 6. NEUTRON STARS & PULSARS (النجوم النيوترونية والنباضة)
// =================================================================
@Composable
fun NeutronStarsPulsarsSimulation(selectedComponentId: String?) {
    var spinFreqHz by remember { mutableFloatStateOf(30f) } // 10Hz to 120Hz

    val infiniteTransition = rememberInfiniteTransition(label = "pulsar_beam")
    val pulsarAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = (1000 / (spinFreqHz / 10f)).toInt().coerceAtLeast(100), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulsar_rotation"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "محاكي النجم النيوتروني والنباض (Pulsar)",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF38BDF8)
        )
        Text(
            text = "أعلى كثافة مادية: ملعقة شاي تزن مليار طن تدور بنبضات راديوية",
            fontSize = 13.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Rotation speed slider
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("سرعة الدوران: ${spinFreqHz.toInt()} هرتز (دورة/ثانية)", fontSize = 11.sp, color = Color.LightGray, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Slider(
                value = spinFreqHz,
                onValueChange = { spinFreqHz = it },
                valueRange = 10f..100f,
                colors = SliderDefaults.colors(thumbColor = Color(0xFF38BDF8), activeTrackColor = Color(0xFF38BDF8)),
                modifier = Modifier.weight(1f)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF02040A))
                .border(1.dp, Color(0xFF0369A1), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f
                val coreRadius = 24f

                // Sweeping Lighthouse Cones (Pulsar Beams)
                val rad = Math.toRadians(pulsarAngle.toDouble()).toFloat()
                val beamLength = 110f

                // Beam 1
                val bx1 = cx + beamLength * cos(rad)
                val by1 = cy + beamLength * sin(rad)
                val path1 = Path().apply {
                    moveTo(cx, cy)
                    lineTo(bx1 - 25f * sin(rad), by1 + 25f * cos(rad))
                    lineTo(bx1 + 25f * sin(rad), by1 - 25f * cos(rad))
                    close()
                }
                drawPath(path1, Brush.radialGradient(colors = listOf(Color(0xFF38BDF8).copy(alpha = 0.8f), Color.Transparent), center = Offset(cx, cy), radius = beamLength))

                // Beam 2 (Opposite pole)
                val radOpposite = rad + PI.toFloat()
                val bx2 = cx + beamLength * cos(radOpposite)
                val by2 = cy + beamLength * sin(radOpposite)
                val path2 = Path().apply {
                    moveTo(cx, cy)
                    lineTo(bx2 - 25f * sin(radOpposite), by2 + 25f * cos(radOpposite))
                    lineTo(bx2 + 25f * sin(radOpposite), by2 - 25f * cos(radOpposite))
                    close()
                }
                drawPath(path2, Brush.radialGradient(colors = listOf(Color(0xFF38BDF8).copy(alpha = 0.8f), Color.Transparent), center = Offset(cx, cy), radius = beamLength))

                // Dense Magnetic Field Rings
                drawCircle(Color(0xFF0284C7).copy(alpha = 0.35f), radius = 55f, center = Offset(cx, cy), style = Stroke(1.5f))
                drawCircle(Color(0xFF38BDF8).copy(alpha = 0.2f), radius = 75f, center = Offset(cx, cy), style = Stroke(1f))

                // Ultra-Dense Neutron Core
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color.White, Color(0xFFBAE6FD), Color(0xFF0284C7)),
                        center = Offset(cx, cy),
                        radius = coreRadius
                    ),
                    radius = coreRadius,
                    center = Offset(cx, cy)
                )
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xDD0F172A),
                border = BorderStroke(1.dp, Color(0xFF334155))
            ) {
                Text(
                    text = "📡 حزم إشعاعية كهرومغناطيسية | نبضات أسرع من الساعات الذرية",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF38BDF8),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

// =================================================================
// 7. QUASARS & BLACK HOLES (الكوازارات والثقوب السوداء وأفق الحدث)
// =================================================================
@Composable
fun QuasarsBlackHolesSimulation(selectedComponentId: String?) {
    var selectedHoleMass by remember { mutableStateOf("ton618") } // sgrA, m87, ton618, cygnus

    val infiniteTransition = rememberInfiniteTransition(label = "black_hole_spin")
    val diskSpin by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "disk_rotation"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "محاكي الثقوب السوداء والكوازارات الفائقة",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFA855F7)
        )
        Text(
            text = "قرص التسامي المتوهج، انحناء الضوء الجاذبي، ونفاثات البلازما النسبية",
            fontSize = 13.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Mass Selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedHoleMass == "ton618",
                onClick = { selectedHoleMass = "ton618" },
                label = { Text("TON 618 (66 مليار شمس) 👑", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF9333EA),
                    selectedLabelColor = Color.White,
                    containerColor = DarkSurfaceVariant,
                    labelColor = Color.LightGray
                )
            )
            FilterChip(
                selected = selectedHoleMass == "m87",
                onClick = { selectedHoleMass = "m87" },
                label = { Text("ثقب M87* (6.5 مليار شمس)", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF9333EA),
                    selectedLabelColor = Color.White,
                    containerColor = DarkSurfaceVariant,
                    labelColor = Color.LightGray
                )
            )
            FilterChip(
                selected = selectedHoleMass == "sgrA",
                onClick = { selectedHoleMass = "sgrA" },
                label = { Text("الرامي A* (4.3 مليون شمس)", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF9333EA),
                    selectedLabelColor = Color.White,
                    containerColor = DarkSurfaceVariant,
                    labelColor = Color.LightGray
                )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF030308))
                .border(1.dp, Color(0xFF581C87), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f

                // Relativistic Polar Plasma Jets (نفاثات جسيمات نسبية من القطبين)
                val jetHeight = 100f
                drawLine(
                    brush = Brush.verticalGradient(colors = listOf(Color(0xFFC084FC), Color.White, Color.Transparent), startY = cy - jetHeight, endY = cy),
                    start = Offset(cx, cy - jetHeight),
                    end = Offset(cx, cy),
                    strokeWidth = 6f,
                    cap = StrokeCap.Round
                )
                drawLine(
                    brush = Brush.verticalGradient(colors = listOf(Color.Transparent, Color.White, Color(0xFFC084FC)), startY = cy, endY = cy + jetHeight),
                    start = Offset(cx, cy),
                    end = Offset(cx, cy + jetHeight),
                    strokeWidth = 6f,
                    cap = StrokeCap.Round
                )

                // Gravitational Lensing Halo (الانحناء الجاذبي للضوء)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFF97316).copy(alpha = 0.5f), Color(0xFFA855F7).copy(alpha = 0.3f), Color.Transparent),
                        center = Offset(cx, cy),
                        radius = 85f
                    ),
                    radius = 85f,
                    center = Offset(cx, cy)
                )

                // Accretion Disk (قرص التنامي المتوهج الساخن)
                val diskWidth = 190f
                val diskHeight = 55f
                drawOval(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFFEF08A), Color(0xFFF97316), Color(0xFFDC2626)),
                        center = Offset(cx - 20f, cy), // Doppler beaming brighter on left
                        radius = diskWidth / 2f
                    ),
                    topLeft = Offset(cx - diskWidth / 2f, cy - diskHeight / 2f),
                    size = Size(diskWidth, diskHeight)
                )

                // Event Horizon (أفق الحدث - نقطة اللاعودة المطلقة)
                drawCircle(Color.Black, radius = 34f, center = Offset(cx, cy))
                drawCircle(Color(0xFFFDE047), radius = 34f, center = Offset(cx, cy), style = Stroke(2f))

                // Singularity hint at center
                drawCircle(Color.Black, radius = 5f, center = Offset(cx, cy))
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xDD0F172A),
                border = BorderStroke(1.dp, Color(0xFF334155))
            ) {
                val massLabel = when (selectedHoleMass) {
                    "ton618" -> "👑 TON 618: 66 مليار كتلة شمسية | أفق حدث يتسع لعشرات المجموعات الشمسية!"
                    "m87" -> "📸 ثقب M87*: 6.5 مليار شمس | صاحب أول صورة لظل ثقب أسود 2019"
                    else -> "🌌 الرامي A*: 4.3 مليون شمس | الوحش الرابض في قلب مجرتنا درب التبانة"
                }
                Text(
                    text = massLabel,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFC084FC),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

// =================================================================
// 8. GAMMA-RAY BURSTS & SUPERNOVAS (انفجارات غاما والمستعرات العظمى)
// =================================================================
@Composable
fun SupernovaGammaRayBurstSimulation(selectedComponentId: String?) {
    var isExploding by remember { mutableStateOf(false) }

    val explosionProgress by animateFloatAsState(
        targetValue = if (isExploding) 1f else 0f,
        animationSpec = tween(durationMillis = 2500, easing = LinearEasing),
        label = "blast_anim"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "محاكي انفجارات أشعة غاما (GRB) والمستعر الأعظم",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF43F5E)
                )
                Text(
                    text = "طاقة شمسنا في 10 مليارات سنة تطلق في أجزاء من الثانية!",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { isExploding = !isExploding },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isExploding) Color(0xFF059669) else Color(0xFFE11D48)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Bolt, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = if (isExploding) "إعادة تعيين النجم 🔄" else "إطلاق الانفجار الكوني الخارق 💥 (Supernova & GRB)",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF050207))
                .border(1.dp, Color(0xFF881337), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f

                if (explosionProgress > 0.05f) {
                    // Expanding Multi-colored Shockwave Remnant (السديم المتوسع)
                    val shockRadius = 30f + explosionProgress * 110f
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 1f - explosionProgress * 0.4f),
                                Color(0xFFF43F5E).copy(alpha = 0.8f - explosionProgress * 0.5f),
                                Color(0xFF8B5CF6).copy(alpha = 0.7f - explosionProgress * 0.4f),
                                Color(0xFF06B6D4).copy(alpha = 0.5f - explosionProgress * 0.5f),
                                Color.Transparent
                            ),
                            center = Offset(cx, cy),
                            radius = shockRadius
                        ),
                        radius = shockRadius,
                        center = Offset(cx, cy)
                    )

                    // Relativistic GRB Laser-like Jet Beams
                    val jetBeamDist = explosionProgress * 150f
                    drawLine(
                        color = Color(0xFFFEF08A),
                        start = Offset(cx, cy),
                        end = Offset(cx, cy - jetBeamDist),
                        strokeWidth = 8f * (1f - explosionProgress * 0.5f),
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = Color(0xFFFEF08A),
                        start = Offset(cx, cy),
                        end = Offset(cx, cy + jetBeamDist),
                        strokeWidth = 8f * (1f - explosionProgress * 0.5f),
                        cap = StrokeCap.Round
                    )

                    // Forging Heavy Elements particles (تخليق الذهب والبلاتين)
                    for (i in 0..20) {
                        val pAngle = i * (2 * PI.toFloat() / 20f)
                        val pDist = explosionProgress * (50f + (i % 5) * 15f)
                        val px = cx + pDist * cos(pAngle)
                        val py = cy + pDist * sin(pAngle)
                        drawCircle(
                            color = Color(0xFFFACC15).copy(alpha = 1f - explosionProgress * 0.3f),
                            radius = 3f,
                            center = Offset(px, py)
                        )
                    }
                } else {
                    // Pre-explosion Giant Star pulsating
                    drawCircle(Color(0xFFE11D48).copy(alpha = 0.4f), radius = 55f, center = Offset(cx, cy))
                    drawCircle(
                        brush = Brush.radialGradient(colors = listOf(Color.White, Color(0xFFF43F5E), Color(0xFF9F1239)), center = Offset(cx, cy), radius = 40f),
                        radius = 40f,
                        center = Offset(cx, cy)
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xDD0F172A),
                border = BorderStroke(1.dp, Color(0xFF334155))
            ) {
                Text(
                    text = if (isExploding) "💥 ومضة غاما خارقة + ولادة الذهب والبلاتين في السديم" else "⚡ انقر على الزر أعلاه لإطلاق الانفجار الكوني ورؤية الحزم النسبية",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFDA4AF),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

// =================================================================
// 9. EXOPLANETS SIMULATION (الكواكب الخارجية - أمطار الحديد والألماس)
// =================================================================
@Composable
fun ExoplanetsSimulation(selectedComponentId: String?) {
    var selectedExo by remember { mutableStateOf("wasp76b") } // wasp76b, kepler186f, cancri55e

    val infiniteTransition = rememberInfiniteTransition(label = "exo_orbit")
    val orbitPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbit_cycle"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "محاكي الكواكب الخارجية وعوالم الغرائب",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2DD4BF)
        )
        Text(
            text = "اكتشاف العوالم عبر طريقة العبور (Transit) وأغرب المناخات الكونية",
            fontSize = 13.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedExo == "wasp76b",
                onClick = { selectedExo = "wasp76b" },
                label = { Text("أمطار الحديد (WASP-76b) 🌧️", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFE11D48),
                    selectedLabelColor = Color.White,
                    containerColor = DarkSurfaceVariant,
                    labelColor = Color.LightGray
                )
            )
            FilterChip(
                selected = selectedExo == "kepler186f",
                onClick = { selectedExo = "kepler186f" },
                label = { Text("توأم الأرض (Kepler-186f) 🌍", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF059669),
                    selectedLabelColor = Color.White,
                    containerColor = DarkSurfaceVariant,
                    labelColor = Color.LightGray
                )
            )
            FilterChip(
                selected = selectedExo == "cancri55e",
                onClick = { selectedExo = "cancri55e" },
                label = { Text("كوكب الألماس (55 Cancri e) 💎", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF0284C7),
                    selectedLabelColor = Color.White,
                    containerColor = DarkSurfaceVariant,
                    labelColor = Color.LightGray
                )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF030712))
                .border(1.dp, Color(0xFF134E4A), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f

                when (selectedExo) {
                    "wasp76b" -> {
                        // WASP-76b: Tidally locked iron rain planet
                        // Day side 2400C (yellow/red), Night side 1500C with molten iron drops
                        drawCircle(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFFFBBF24), Color(0xFFDC2626), Color(0xFF1E1B4B)),
                                startX = cx - 55f,
                                endX = cx + 55f
                            ),
                            radius = 55f,
                            center = Offset(cx, cy)
                        )
                        // Molten iron droplets on night side
                        for (i in 0..12) {
                            val dy = cy - 40f + ((orbitPhase * 80f + i * 20f) % 80f)
                            val dx = cx + 15f + (i % 4) * 8f
                            drawLine(Color(0xFFEF4444), start = Offset(dx, dy), end = Offset(dx, dy + 7f), strokeWidth = 2.5f)
                        }
                    }
                    "cancri55e" -> {
                        // 55 Cancri e: Diamond planet cross section
                        // Lava oceans outside, pure diamond crystal mantle
                        drawCircle(Color(0xFFEF4444), radius = 55f, center = Offset(cx, cy))
                        drawCircle(
                            brush = Brush.radialGradient(colors = listOf(Color.White, Color(0xFFBAE6FD), Color(0xFF38BDF8)), center = Offset(cx, cy), radius = 42f),
                            radius = 42f,
                            center = Offset(cx, cy)
                        )
                    }
                    else -> {
                        // Kepler-186f: Goldilocks Zone Habitable Planet
                        // Host Red Dwarf star
                        drawCircle(Color(0xFFDC2626), radius = 25f, center = Offset(cx - 90f, cy))
                        // Habitable green zone ring
                        drawCircle(Color(0x3322C55E), radius = 90f, center = Offset(cx - 90f, cy), style = Stroke(16f))
                        // Earth-like planet with ocean and green land
                        val kx = cx + 20f
                        drawCircle(Color(0xFF0284C7), radius = 30f, center = Offset(kx, cy))
                        drawCircle(Color(0xFF16A34A), radius = 12f, center = Offset(kx - 6f, cy - 6f))
                    }
                }
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xDD0F172A),
                border = BorderStroke(1.dp, Color(0xFF334155))
            ) {
                val exoText = when (selectedExo) {
                    "wasp76b" -> "🌧️ حرارة 2,400°C تبخر الحديد نهاراً ليسيل كأمطار حديد مصهور ليلاً!"
                    "cancri55e" -> "💎 كوكب الألماس: ضغط وحرارة مهولة حولت ثلث وشاحه لألماس نقي وجرافيت"
                    else -> "🌿 في النطاق الصالح للسكن (Goldilocks) بدرجات حرارة معتدلة تسمح بالمياه السائلة"
                }
                Text(
                    text = exoText,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2DD4BF),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

// =================================================================
// 10. OTHER CELESTIAL BODIES (عطارد، الزهرة، أورانوس، نبتون، بلوتو)
// =================================================================
@Composable
fun PlanetAtmosphereSimulation(topicId: String, selectedComponentId: String?) {
    var mercuryTempPhase by remember { mutableFloatStateOf(0.5f) } // 0=night, 1=day
    var uranusTiltSlider by remember { mutableFloatStateOf(98f) }

    val infiniteTransition = rememberInfiniteTransition(label = "neptune_winds")
    val windOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(animation = tween(2000, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "wind_speed"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        val (planetTitle, planetSubtitle, planetColor) = when (topicId) {
            "planet_mercury" -> Triple("محاكي كوكب عطارد والتباين الحراري", "نهار حارق يصهر الرصاص 430°C وصقيع ليلي -180°C لغياب الغلاف", Color(0xFF94A3B8))
            "planet_venus" -> Triple("محاكي كوكب الزهرة والاحتباس الحراري", "جحيم 465°C وضغط 90 ضعف الأرض تحت غلاف 96% CO2 وحمض الكبريتيك", Color(0xFFFBBF24))
            "planet_uranus" -> Triple("محاكي كوكب أورانوس والميلان الشاذ", "ميلان محوري استثنائي 98° يجعله يتدحرج على جنبه (-224°C أبرد كوكب)", Color(0xFF22D3EE))
            "planet_neptune" -> Triple("محاكي كوكب نبتون والرياح فوق الصوتية", "أسرع رياح في المنظومة بسرعة 2,100 كم/س تفوق سرعة الصوت!", Color(0xFF38BDF8))
            "planet_pluto" -> Triple("محاكي كوكب بلوتو وشارون الثنائي", "كوكب قزم وسهل النيتروجين الجليدي (قلب بلوتو) ونظام مداري مقفول", Color(0xFFCBD5E1))
            else -> Triple("محاكي أطراف المنظومة وسحابة أورت", "أجرام حزام كايبر وسحابة أورت الجليدية حتى 100,000 AU", Color(0xFF818CF8))
        }

        Text(text = planetTitle, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = planetColor)
        Text(text = planetSubtitle, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

        Spacer(modifier = Modifier.height(10.dp))

        if (topicId == "planet_mercury") {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(if (mercuryTempPhase > 0.5f) "النهار: +430°C 🔥" else "الليل: -180°C ❄️", fontSize = 11.sp, color = planetColor, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Slider(
                    value = mercuryTempPhase,
                    onValueChange = { mercuryTempPhase = it },
                    colors = SliderDefaults.colors(thumbColor = planetColor, activeTrackColor = planetColor),
                    modifier = Modifier.weight(1f)
                )
            }
        } else if (topicId == "planet_uranus") {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("زاوية الميلان: ${uranusTiltSlider.toInt()}°", fontSize = 11.sp, color = planetColor, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Slider(
                    value = uranusTiltSlider,
                    onValueChange = { uranusTiltSlider = it },
                    valueRange = 0f..98f,
                    colors = SliderDefaults.colors(thumbColor = planetColor, activeTrackColor = planetColor),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF030712))
                .border(1.dp, planetColor.copy(alpha = 0.35f), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f

                when (topicId) {
                    "planet_mercury" -> {
                        // Cratered Mercury with extreme day/night gradient
                        val dayColor = Color(0xFFF97316)
                        val nightColor = Color(0xFF1E293B)
                        val blend = if (mercuryTempPhase > 0.5f) dayColor else nightColor
                        drawCircle(blend, radius = 55f, center = Offset(cx, cy))
                        drawCircle(Color(0xFF0F172A), radius = 10f, center = Offset(cx - 15f, cy - 10f))
                        drawCircle(Color(0xFF0F172A), radius = 7f, center = Offset(cx + 12f, cy + 18f))
                    }
                    "planet_venus" -> {
                        // Thick yellowish-white runaway greenhouse acid clouds
                        drawCircle(
                            brush = Brush.radialGradient(colors = listOf(Color(0xFFFEF08A), Color(0xFFFBBF24), Color(0xFFD97706)), center = Offset(cx - 10f, cy - 10f), radius = 60f),
                            radius = 60f,
                            center = Offset(cx, cy)
                        )
                        // Acid cloud swirl lines
                        drawArc(Color(0x55B45309), startAngle = 30f, sweepAngle = 120f, useCenter = false, topLeft = Offset(cx - 50f, cy - 30f), size = Size(100f, 60f), style = Stroke(4f))
                    }
                    "planet_uranus" -> {
                        // Cyan planet tilted by uranusTiltSlider
                        val tiltRad = Math.toRadians(uranusTiltSlider.toDouble()).toFloat()
                        drawCircle(Color(0xFF22D3EE), radius = 56f, center = Offset(cx, cy))
                        // Tilted ring and equator line
                        val rx = 80f * cos(tiltRad)
                        val ry = 80f * sin(tiltRad)
                        drawLine(Color.White.copy(alpha = 0.8f), start = Offset(cx - rx, cy - ry), end = Offset(cx + rx, cy + ry), strokeWidth = 2.5f)
                    }
                    "planet_neptune" -> {
                        // Deep azure blue Neptune with 2100km/h wind stream vectors
                        drawCircle(Color(0xFF0284C7), radius = 58f, center = Offset(cx, cy))
                        // Great Dark Spot
                        drawOval(Color(0xFF0C4A6E), topLeft = Offset(cx + 10f, cy - 12f), size = Size(26f, 16f))
                        // Supersonic wind streamlines
                        for (i in 0..4) {
                            val wy = cy - 35f + i * 18f
                            val wx = cx - 50f + ((windOffset * 2f + i * 25f) % 100f)
                            drawLine(Color.White.copy(alpha = 0.6f), start = Offset(wx, wy), end = Offset(wx + 18f, wy), strokeWidth = 2f)
                        }
                    }
                    "planet_pluto" -> {
                        // Pluto with Sputnik Planitia Heart and Charon binary orbit
                        drawCircle(Color(0xFF78716C), radius = 42f, center = Offset(cx - 20f, cy))
                        // White Nitrogen Ice Heart
                        drawCircle(Color.White.copy(alpha = 0.9f), radius = 12f, center = Offset(cx - 15f, cy + 5f))
                        // Charon Moon
                        drawCircle(Color(0xFF475569), radius = 22f, center = Offset(cx + 55f, cy - 20f))
                    }
                    else -> {
                        // Kuiper Belt / Oort Cloud deep icy bodies
                        for (i in 0..30) {
                            val r = 30f + i * 3f
                            val a = i * 24f
                            val px = cx + r * cos(Math.toRadians(a.toDouble())).toFloat()
                            val py = cy + r * sin(Math.toRadians(a.toDouble())).toFloat() * 0.6f
                            drawCircle(Color(0xFF93C5FD).copy(alpha = 0.7f), radius = 2f, center = Offset(px, py))
                        }
                    }
                }
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xDD0F172A),
                border = BorderStroke(1.dp, Color(0xFF334155))
            ) {
                val statText = when (topicId) {
                    "planet_mercury" -> "🌡️ أكبر تباين حراري: فارق 610 درجات بين النهار والليل!"
                    "planet_venus" -> "🔥 أسخن كوكب بالمنظومة (465°C) | ضغط 90 ضعف ضغط سطح الأرض"
                    "planet_uranus" -> "🔄 ميلان 98° يجعله يدور كبرميل متدحرج قطباه يواجهان الشمس لـ 42 عاماً"
                    "planet_neptune" -> "💨 أسرع رياح بالكون الشمسي (2,100 كم/س) تفوق سرعة الصوت"
                    "planet_pluto" -> "🤍 قلب نيتروجين جليدي (Tombaugh Regio) ونظام ثنائي مع شارون"
                    else -> "🧊 جليد أزلي وخزان تريليونات المذنبات حتى سنتين ضوئيتين"
                }
                Text(
                    text = statText,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = planetColor,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

// =================================================================
// 11. GALAXIES SIMULATION (محاكي المجرات والتركيب الحلزوني والمغزلي)
// =================================================================
@Composable
fun GalaxiesSimulation(selectedComponentId: String?) {
    var selectedGalaxy by remember { mutableStateOf("milky_way") }
    var rotationSpeedMultiplier by remember { mutableFloatStateOf(1f) }

    androidx.compose.runtime.LaunchedEffect(selectedComponentId) {
        if (!selectedComponentId.isNullOrEmpty()) {
            when (selectedComponentId) {
                "milky_way", "andromeda", "triangulum", "sombrero" -> selectedGalaxy = selectedComponentId
            }
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "galaxy_spin")
    val spinAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = (14000 / rotationSpeedMultiplier).toInt().coerceAtLeast(1000), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "galaxy_rot"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "محاكي المجرات والتركيب الحلزوني والأذرع",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFA855F7)
        )
        Text(
            text = when (selectedGalaxy) {
                "andromeda" -> "مجرة أندروميدا (M31): عملاق حلزوني بتريليون نجم يتجه نحونا بسرعة 110 كم/ث"
                "sombrero" -> "مجرة السومبريرو (M104): حلقة غبار داكنة كلاسيكية وانتفاخ مركزي كروي ضخم"
                "triangulum" -> "مجرة المثلث (M33): مجرة حلزونية ندفية (Flocculent) وغنية بسدم ولادة النجوم"
                else -> "درب التبانة: مجرة حلزونية ضلعية (Barred Spiral) تحتضن 200-400 مليار نجم"
            },
            fontSize = 13.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Galaxy Selector Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val galaxies = listOf(
                "milky_way" to "درب التبانة 🌌",
                "andromeda" to "أندروميدا (M31) ✨",
                "triangulum" to "المثلث (M33) 🔭",
                "sombrero" to "السومبريرو (M104) 🪐"
            )
            galaxies.forEach { (id, label) ->
                FilterChip(
                    selected = selectedGalaxy == id,
                    onClick = { selectedGalaxy = id },
                    label = { Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFA855F7),
                        selectedLabelColor = Color.White,
                        containerColor = Color(0xFF1E1B4B),
                        labelColor = Color(0xFFC084FC)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Rotation speed slider
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("سرعة الدوران: ${"%.1f".format(rotationSpeedMultiplier)}x", fontSize = 11.sp, color = Color(0xFFC084FC), fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Slider(
                value = rotationSpeedMultiplier,
                onValueChange = { rotationSpeedMultiplier = it },
                valueRange = 0.5f..3f,
                colors = SliderDefaults.colors(thumbColor = Color(0xFFA855F7), activeTrackColor = Color(0xFFA855F7)),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF06030F))
                .border(1.dp, Color(0xFF6B21A8), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f

                // Distant background galaxies / stars
                for (i in 0..40) {
                    val sx = (cx * 0.2f + (i * 37) % size.width)
                    val sy = (cy * 0.2f + (i * 59) % size.height)
                    drawCircle(Color.White.copy(alpha = 0.35f), radius = 1.2f, center = Offset(sx, sy))
                }

                if (selectedGalaxy == "sombrero") {
                    // Sombrero: Massive luminous elliptical bulge + prominent dark dust lane
                    drawOval(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFFFFAF0), Color(0xFFFDE68A), Color(0xFFCA8A04).copy(alpha = 0.4f), Color.Transparent),
                            center = Offset(cx, cy),
                            radius = 110f
                        ),
                        topLeft = Offset(cx - 100f, cy - 65f),
                        size = Size(200f, 130f)
                    )

                    // Brilliant nucleus
                    drawCircle(Color.White, radius = 16f, center = Offset(cx, cy))

                    // Outer thin disk
                    drawOval(
                        color = Color(0xFFE9D5FF).copy(alpha = 0.25f),
                        topLeft = Offset(cx - 135f, cy - 25f),
                        size = Size(270f, 50f)
                    )

                    // Sharp dark absorption dust lane (Ring) across the middle
                    val dustY = cy + 4f
                    drawLine(
                        color = Color(0xFF1E102F).copy(alpha = 0.95f),
                        start = Offset(cx - 130f, dustY),
                        end = Offset(cx + 130f, dustY),
                        strokeWidth = 6f,
                        cap = StrokeCap.Round
                    )
                    // Finer subtle dust thread
                    drawLine(
                        color = Color(0xFF090412),
                        start = Offset(cx - 120f, dustY - 1f),
                        end = Offset(cx + 120f, dustY - 1f),
                        strokeWidth = 2.5f
                    )
                } else {
                    // Spiral Galaxies (Milky Way, Andromeda, Triangulum)
                    val baseAngle = Math.toRadians(spinAngle.toDouble())
                    val numArms = if (selectedGalaxy == "triangulum") 3 else 2
                    val armColor = if (selectedGalaxy == "andromeda") Color(0xFF67E8F9) else Color(0xFFC084FC)

                    // Central Galactic Bulge
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color.White, Color(0xFFFDE047), Color(0xFFA855F7).copy(alpha = 0.3f), Color.Transparent),
                            center = Offset(cx, cy),
                            radius = 45f
                        ),
                        radius = 45f,
                        center = Offset(cx, cy)
                    )

                    // Central Bar for Milky Way
                    if (selectedGalaxy == "milky_way") {
                        val barLen = 50f
                        val barAngle = baseAngle + Math.PI / 4
                        val bx = (cos(barAngle) * barLen).toFloat()
                        val by = (sin(barAngle) * barLen * 0.6f).toFloat()
                        drawLine(
                            brush = Brush.linearGradient(listOf(Color(0xFFFEF08A), Color.White, Color(0xFFFEF08A))),
                            start = Offset(cx - bx, cy - by),
                            end = Offset(cx + bx, cy + by),
                            strokeWidth = 10f,
                            cap = StrokeCap.Round
                        )
                    }

                    // Spiral Arms using Archimedean / Logarithmic spirals
                    for (arm in 0 until numArms) {
                        val armOffset = (arm * (2 * PI / numArms))
                        for (step in 1..40) {
                            val r = 18f + step * 2.8f
                            val theta = baseAngle + armOffset + (step * 0.16)
                            val px = cx + (r * cos(theta)).toFloat()
                            val py = cy + (r * sin(theta) * 0.55f).toFloat() // perspective tilt

                            val particleSize = (1.5f + (step % 3) * 0.8f)
                            val alpha = ((45 - step) / 45f).coerceIn(0.2f, 0.9f)
                            drawCircle(
                                color = armColor.copy(alpha = alpha),
                                radius = particleSize,
                                center = Offset(px, py)
                            )

                            // H-II star forming regions (Pink / Magenta nebulae)
                            if (step % 6 == 0) {
                                drawCircle(
                                    color = Color(0xFFF43F5E).copy(alpha = 0.85f),
                                    radius = particleSize + 1.2f,
                                    center = Offset(px + 2f, py)
                                )
                            }
                        }
                    }
                }
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xDD0F172A),
                border = BorderStroke(1.dp, Color(0xFF4C1D95))
            ) {
                val infoText = when (selectedGalaxy) {
                    "andromeda" -> "🌌 أندروميدا: القطر 220,000 سنة ضوئية | تصادم حتمي مع درب التبانة بعد 4.5 مليار عام"
                    "sombrero" -> "🪐 السومبريرو: حزام غبار كوني أسود وثقب أسود مركزي بكتلة مليار شمس!"
                    "triangulum" -> "🔭 المثلث (M33): ثالث كبرى مجرات المجموعة المحلية وتبعد 2.7 مليون سنة ضوئية"
                    else -> "🌌 درب التبانة: ذراع الجبار يضم شمسنا على بعد 26,000 سنة ضوئية من ثقب الرامي A*"
                }
                Text(
                    text = infoText,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE9D5FF),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

// =================================================================
// 12. COSMIC PHENOMENA LAB (المختبر المتعدد للظواهر الكونية الخارقة)
// =================================================================
@Composable
fun CosmicPhenomenaInteractiveLab(selectedComponentId: String?) {
    var activePhenomenon by remember { mutableStateOf("gamma_ray_bursts") }

    androidx.compose.runtime.LaunchedEffect(selectedComponentId) {
        if (!selectedComponentId.isNullOrEmpty()) {
            when (selectedComponentId) {
                "neutron_pulsars" -> activePhenomenon = "neutron_pulsars"
                "quasars_phenom" -> activePhenomenon = "quasars_phenom"
                "gamma_ray_bursts" -> activePhenomenon = "gamma_ray_bursts"
            }
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Phenomenon Mode Selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val modes = listOf(
                "gamma_ray_bursts" to "انفجارات غاما والمستعر 💥",
                "neutron_pulsars" to "النجوم النباضة النيوترونية ⏱️",
                "quasars_phenom" to "الكوازارات الفائقة ⚡"
            )
            modes.forEach { (id, title) ->
                FilterChip(
                    selected = activePhenomenon == id,
                    onClick = { activePhenomenon = id },
                    label = { Text(title, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = when (id) {
                            "gamma_ray_bursts" -> Color(0xFFF43F5E)
                            "neutron_pulsars" -> Color(0xFF0284C7)
                            else -> Color(0xFFE11D48)
                        },
                        selectedLabelColor = Color.White,
                        containerColor = Color(0xFF1E293B),
                        labelColor = Color.LightGray
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Render the respective dedicated simulation
        when (activePhenomenon) {
            "neutron_pulsars" -> NeutronStarsPulsarsSimulation(selectedComponentId = selectedComponentId)
            "quasars_phenom" -> QuasarsBlackHolesSimulation(selectedComponentId = selectedComponentId)
            else -> SupernovaGammaRayBurstSimulation(selectedComponentId = selectedComponentId)
        }
    }
}

