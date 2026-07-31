package cu.youchat.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import cu.youchat.app.R

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onFinalizar: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { 5 })
    var mostrarSkip by remember { mutableStateOf(true) }
    val colores = listOf(Color(63, 72, 204), Color(244, 67, 54), Color(76, 175, 80), Color(255, 193, 7), Color(25, 118, 210))
    val colorFondo by animateColorAsState(targetValue = colores[pagerState.currentPage], animationSpec = tween(300))
    LaunchedEffect(pagerState.currentPage) { mostrarSkip = pagerState.currentPage != 4 }

    Box(Modifier.fillMaxSize().background(colorFondo)) {
        Column(Modifier.fillMaxSize()) {
            AnimatedVisibility(visible = mostrarSkip, enter = fadeIn(), exit = fadeOut(), modifier = Modifier.align(Alignment.End).padding(top = 48.dp, end = 16.dp)) {
                TextButton(onClick = onFinalizar) { Text("SALTAR", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp) }
            }
            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth().weight(1f)) { page ->
                when (page) {
                    0 -> Pagina1()
                    1 -> Pagina2()
                    2 -> Pagina3()
                    3 -> Pagina4()
                    4 -> Pagina5(onEntrar = onFinalizar)
                }
            }
            IndicadorPuntos(5, pagerState.currentPage, Modifier.align(Alignment.CenterHorizontally).padding(bottom = 32.dp))
        }
    }
}

@Composable
private fun Pagina1() {
    Column(Modifier.fillMaxSize().padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(12.dp))
        Text("Bienvenido a", fontSize = 30.sp, color = Color.White, textAlign = TextAlign.Center)
        Spacer(Modifier.weight(1f))
        Icon(painterResource(R.drawable.iconycvector9_2_1), null, Modifier.size(150.dp), tint = Color.Unspecified)
        Spacer(Modifier.weight(1f))
        Text("YouChat la nueva aplicación de mensajería instantánea", fontSize = 20.sp, color = Color.White, textAlign = TextAlign.Center, modifier = Modifier.padding(bottom = 60.dp))
    }
}

@Composable
private fun Pagina2() = PaginaRojo("Seguridad", R.raw.new_login, "YouChat es seguro, su privacidad está asegurada con nosotros")
@Composable
private fun Pagina3() = PaginaRojo("Moderno", R.raw.aasweeping_floor, "Converse bajo una interfaz cómoda, moderna y agradable")
@Composable
private fun Pagina4() = PaginaRojo("Rapidez", R.raw.aafast_chat, "Comuníquese con las personas de manera rápida y en tiempo real")

@Composable
private fun PaginaRojo(titulo: String, @androidx.annotation.RawRes animacionRes: Int, texto: String) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animacionRes))
    val progress by animateLottieCompositionAsState(composition = composition, iterations = LottieConstants.IterateForever)
    Column(Modifier.fillMaxSize().padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(12.dp))
        Text(titulo, fontSize = 30.sp, color = Color.White, textAlign = TextAlign.Center)
        LottieAnimation(composition = composition, progress = { progress }, modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp))
        Text(texto, fontSize = 20.sp, color = Color.White, textAlign = TextAlign.Center, modifier = Modifier.padding(bottom = 60.dp))
    }
}

@Composable
private fun Pagina5(onEntrar: () -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.aaanimation))
    val progress by animateLottieCompositionAsState(composition = composition, iterations = LottieConstants.IterateForever)
    Column(Modifier.fillMaxSize().padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(12.dp))
        Text("¡Todo listo!", fontSize = 30.sp, color = Color.White, textAlign = TextAlign.Center)
        LottieAnimation(composition = composition, progress = { progress }, modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp))
        OutlinedButton(onClick = onEntrar, modifier = Modifier.fillMaxWidth().padding(horizontal = 36.dp).padding(bottom = 8.dp), colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent), shape = RoundedCornerShape(50)) {
            Text("ENTRAR", color = Color.White, fontSize = 13.sp)
        }
        Text("Adelante, comience a usar YouChat", fontSize = 20.sp, color = Color.White, textAlign = TextAlign.Center, modifier = Modifier.padding(bottom = 60.dp))
    }
}

@Composable
private fun IndicadorPuntos(total: Int, actual: Int, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        repeat(total) { index ->
            val activo = index == actual
            val size by animateDpAsState(targetValue = if (activo) 12.dp else 8.dp, animationSpec = spring(dampingRatio = 0.6f))
            val alpha by animateFloatAsState(targetValue = if (activo) 1f else 0.5f)
            Box(Modifier.size(size).alpha(alpha).clip(CircleShape).background(Color.White))
        }
    }
}
