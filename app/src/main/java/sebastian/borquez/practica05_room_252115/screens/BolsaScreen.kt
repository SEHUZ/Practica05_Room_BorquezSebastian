package sebastian.borquez.practica05_room_252115.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigationevent.NavigationEventInfo
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.flow.collectLatest
import sebastian.borquez.practica05_room_252115.data.PokemonEntity
import sebastian.borquez.practica05_room_252115.viewModel.PokemonViewModel

fun primaryTypeColor(type: String): Color {
    return when (type.split("/").first().trim().lowercase()) {
        "fire"     -> Color(0xFFFF9741)
        "water"    -> Color(0xFF3692DC)
        "grass"    -> Color(0xFF38BF4B)
        "electric" -> Color(0xFFFBD100)
        "psychic"  -> Color(0xFFFF6675)
        "ice"      -> Color(0xFF4CD1C0)
        "dragon"   -> Color(0xFF006FC9)
        "dark"     -> Color(0xFF5B5466)
        "fairy"    -> Color(0xFFFF76A4)
        "normal"   -> Color(0xFF9FA19F)
        "fighting" -> Color(0xFFE0306A)
        "flying"   -> Color(0xFF89AAE3)
        "poison"   -> Color(0xFFB567CE)
        "ground"   -> Color(0xFFE87236)
        "rock"     -> Color(0xFFC5B78C)
        "bug"      -> Color(0xFF83C300)
        "ghost"    -> Color(0xFF4C6AB2)
        "steel"    -> Color(0xFF60A1B8)
        else       -> Color(0xFF9FA19F)
    }
}

private fun typeEmoji(type: String): String {
    return when (type.split("/").first().trim().lowercase()) {
        "fire"     -> "🔥"
        "water"    -> "💧"
        "grass"    -> "🌿"
        "electric" -> "⚡"
        "psychic"  -> "🔮"
        "ice"      -> "❄️"
        "dragon"   -> "🐉"
        "dark"     -> "🌑"
        "fairy"    -> "✨"
        "normal"   -> "⭐"
        "fighting" -> "👊"
        "flying"   -> "🌪️"
        "poison"   -> "☠️"
        "ground"   -> "🌍"
        "rock"     -> "🪨"
        "bug"      -> "🐛"
        "ghost"    -> "👻"
        "steel"    -> "⚙️"
        else       -> "❓"
    }
}

fun spriteUrl(number: String): String {
    val id = number.trimStart('0').toIntOrNull() ?: 1
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
}

@Composable
fun BolsaScreen(pokemonViewModel: PokemonViewModel) {

    val searchQuery by pokemonViewModel.searchQuery.collectAsStateWithLifecycle()
    val typeFilter  by pokemonViewModel.typeFilter.collectAsStateWithLifecycle()
    val minLevel    by pokemonViewModel.minLevel.collectAsStateWithLifecycle()
    val pokemons    by pokemonViewModel.pokemonsState.collectAsStateWithLifecycle()

    var pokemonToDelete by remember { mutableStateOf<PokemonEntity?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        pokemonViewModel.mensaje.collectLatest { msg ->
            snackbarHostState.showSnackbar(message = msg, duration = SnackbarDuration.Short)
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFF1A1A2E)
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF16213E))
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PixelTextField(
                    value = searchQuery,
                    onValueChange = { pokemonViewModel.updateSearchQuery(it) },
                    label = "Buscar nombre o tipo…",
                    leadingIcon = {
                        Icon(Icons.Default.Search, null,
                            tint = Color(0xFFE94560), modifier = Modifier.size(18.dp))
                    }
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    PixelTextField(
                        value = typeFilter,
                        onValueChange = { pokemonViewModel.updateTypeFilter(it) },
                        label = "Tipo",
                        modifier = Modifier.weight(1f)
                    )
                    PixelTextField(
                        value = if (minLevel == 1) "" else minLevel.toString(),
                        onValueChange = { pokemonViewModel.updateMinLevel(it.toIntOrNull() ?: 1) },
                        label = "Nv. min",
                        modifier = Modifier.weight(1f),
                        keyboardType = KeyboardType.Number
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "MI BOLSA",
                    color = Color(0xFFE94560),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 3.sp
                )
                Text(
                    "${pokemons.size} Pokémon",
                    color = Color(0xFF53D8FB),
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(pokemons, key = { it.id }) { pokemon ->
                    PokemonCard(
                        pokemon = pokemon,
                        onLevelUp = { pokemonViewModel.levelUpPokemon(pokemon) },
                        onDelete  = { pokemonToDelete = pokemon }
                    )
                }
            }
        }
    }

    pokemonToDelete?.let { poke ->
        PokedexDialog(
            pokemon = poke,
            onConfirm = {
                pokemonViewModel.deletePokemon(poke)
                pokemonToDelete = null
            },
            onDismiss = { pokemonToDelete = null }
        )
    }
}

@Composable
private fun PokedexHeader(scanY: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFFDC0A2D), Color(0xFFB20000))
                )
            )
            .drawBehind {
                val y = scanY * size.height
                drawLine(
                    color = Color.White.copy(alpha = 0.15f),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 2.dp.toPx()
                )
                drawLine(
                    color = Color.Black.copy(alpha = 0.4f),
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 3.dp.toPx()
                )
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1A1A2E))
                    .border(3.dp, Color.White.copy(alpha = 0.6f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF53D8FB))
                )
            }
            Column {
                Text(
                    "POKÉDEX",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp
                )
                Text(
                    "Edición Nacional",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
private fun PokemonCard(
    pokemon: PokemonEntity,
    onLevelUp: () -> Unit,
    onDelete: () -> Unit
) {
    val typeColor = primaryTypeColor(pokemon.type)
    val lightTypeColor = typeColor.copy(alpha = 0.15f)
    val levelFraction = (pokemon.level / 100f).coerceIn(0f, 1f)

    val pulseAnim = rememberInfiniteTransition(label = "pulse")
    val glowAlpha by pulseAnim.animateFloat(
        initialValue = 0.3f, targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            tween(1200, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ), label = "glow"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = typeColor.copy(alpha = 0.4f),
                spotColor = typeColor.copy(alpha = 0.4f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F3460))
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(Brush.horizontalGradient(listOf(typeColor, typeColor.copy(alpha = 0.4f))))
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(lightTypeColor)
                        .border(
                            width = 2.dp,
                            brush = Brush.linearGradient(
                                listOf(typeColor, typeColor.copy(alpha = 0.3f))
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val step = 8.dp.toPx()
                        var x = 0f
                        while (x < size.width) {
                            var y = 0f
                            while (y < size.height) {
                                drawCircle(
                                    color = typeColor.copy(alpha = 0.12f),
                                    radius = 1.dp.toPx(),
                                    center = Offset(x, y)
                                )
                                y += step
                            }
                            x += step
                        }
                    }
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(spriteUrl(pokemon.number))
                            .crossfade(false)
                            .build(),
                        contentDescription = pokemon.name,
                        contentScale = ContentScale.Fit,
                        filterQuality = FilterQuality.None,
                        modifier = Modifier.size(64.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = pokemon.name,
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "#${pokemon.number}",
                            color = typeColor,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        pokemon.type.split("/").forEach { t ->
                            val tColor = primaryTypeColor(t.trim())
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(tColor.copy(alpha = 0.25f))
                                    .border(1.dp, tColor.copy(alpha = 0.8f), RoundedCornerShape(50))
                                    .padding(horizontal = 7.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "${typeEmoji(t.trim())} ${t.trim().uppercase()}",
                                    color = tColor,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Nv.",
                            color = Color(0xFF9BA3AF),
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "${pokemon.level}",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color.White.copy(alpha = 0.1f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(levelFraction)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(3.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(typeColor, typeColor.copy(alpha = 0.6f))
                                    )
                                )
                        )
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PixelActionButton(
                        onClick = onLevelUp,
                        color = Color(0xFF38BF4B),
                        contentDescription = "Subir nivel"
                    ) {
                        Icon(Icons.Default.Add, null,
                            tint = Color.White, modifier = Modifier.size(18.dp))
                    }

                    PixelActionButton(
                        onClick = onDelete,
                        color = Color(0xFFE94560),
                        contentDescription = "Liberar"
                    ) {
                        Text("✕", color = Color.White, fontSize = 14.sp,
                            fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }
    }
}

@Composable
fun AsyncImage(
    model: HexFormat.Builder,
    contentDescription: String,
    contentScale: ContentScale.Companion,
    filterQuality: NavigationEventInfo.None,
    modifier: Modifier
) {
    TODO("Not yet implemented")
}

@Composable
private fun PixelActionButton(
    onClick: () -> Unit,
    color: Color,
    contentDescription: String,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.2f))
            .border(2.dp, color.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
private fun PixelTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(label, fontSize = 11.sp, fontFamily = FontFamily.Monospace,
                color = Color(0xFF9BA3AF))
        },
        leadingIcon = leadingIcon,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor    = Color(0xFFE94560),
            unfocusedBorderColor  = Color(0xFF2D3748),
            focusedTextColor      = Color.White,
            unfocusedTextColor    = Color(0xFFD1D5DB),
            cursorColor           = Color(0xFFE94560),
            focusedContainerColor = Color(0xFF0F3460).copy(alpha = 0.6f),
            unfocusedContainerColor = Color(0xFF0F3460).copy(alpha = 0.4f),
            focusedLabelColor     = Color(0xFFE94560),
        ),
        shape = RoundedCornerShape(10.dp),
        textStyle = LocalTextStyle.current.copy(
            fontFamily = FontFamily.Monospace,
            fontSize = 13.sp
        ),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun PokedexDialog(
    pokemon: PokemonEntity,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF0F3460),
        shape = RoundedCornerShape(16.dp),
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()) {
                Text(
                    "⚠ LIBERAR",
                    color = Color(0xFFE94560),
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp,
                    fontSize = 16.sp
                )
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(spriteUrl(pokemon.number)).crossfade(false).build(),
                    contentDescription = null,
                    filterQuality = FilterQuality.None,
                    modifier = Modifier.size(72.dp)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "¿Liberar a ${pokemon.name}?\nEsta acción no se puede deshacer.",
                    color = Color(0xFFD1D5DB),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("LIBERAR", color = Color(0xFFE94560),
                    fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCELAR", color = Color(0xFF53D8FB),
                    fontFamily = FontFamily.Monospace)
            }
        }
    )
}