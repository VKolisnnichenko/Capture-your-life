package com.vkolisnichenko.currentdaymedia.presentation

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.vkolisnichenko.currentdaymedia.R
import com.vkolisnichenko.currentdaymedia.model.CurrentDayModel
import com.vkolisnichenko.currentdaymedia.model.SpecificDayRecordModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun CurrentDayMediaScreen(
    onBackClick: () -> Unit,
    currentDay: String,
    modifier: Modifier = Modifier,
    currentDayMediaViewModel: CurrentDayMediaViewModel = hiltViewModel(),
    onMediaItemClick: (id: Long) -> Unit
) {
    LaunchedEffect(Unit) {
        currentDayMediaViewModel.getCurrentDay(currentDay)
        currentDayMediaViewModel.fetchCurrentDayRecords(currentDay)
    }

    val mediaState by currentDayMediaViewModel.currentDayMediaState.collectAsState()
    when (mediaState) {
        is CurrentDayMediaState.Loading -> LoadingState()
        is CurrentDayMediaState.Error -> ErrorState(
            modifier = modifier,
            error = (mediaState as CurrentDayMediaState.Error).message
        )

        is CurrentDayMediaState.Success -> SuccessState(
            modifier = modifier,
            onBackClick = onBackClick,
            currentDayModel = (mediaState as CurrentDayMediaState.Success).currentDayModel,
            onEditClick = onMediaItemClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessState(
    modifier: Modifier,
    onBackClick: () -> Unit,
    currentDayModel: CurrentDayModel,
    onEditClick: (Long) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back"
                        )
                    }
                },
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = currentDayModel.currentDayOfTheWeek,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = colorResource(id = R.color.color_primary)
                )
            )
        },

        modifier = modifier
            .fillMaxSize(),
        containerColor = colorResource(id = R.color.background_color)
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp)
        ) {

            items(currentDayModel.currentDayRecords) { mediaModel ->
                UserMediaItem(
                    modifier = Modifier.padding(bottom = 8.dp),
                    specificDayRecordModel = mediaModel,
                    onEditClick = onEditClick
                )
            }

        }
    }
}

@Composable
fun UserMediaItem(
    modifier: Modifier = Modifier,
    specificDayRecordModel: SpecificDayRecordModel,
    onEditClick: (Long) -> Unit
) {

    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.secondary_color)),
        onClick = {
            onEditClick.invoke(specificDayRecordModel.mediaId)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = specificDayRecordModel.currentDate,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        if (specificDayRecordModel.isPhoto) {
                            Image(
                                painter = rememberAsyncImagePainter(specificDayRecordModel.mediaUri),
                                contentDescription = "User Media",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            LaunchedEffect(specificDayRecordModel.mediaUri) {
                                bitmap =
                                    extractVideoFrameAsync(context, specificDayRecordModel.mediaUri)
                            }

                            bitmap?.let {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = "Video Thumbnail",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play Video",
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .size(32.dp),
                                    tint = Color.White
                                )
                            }

                            if (bitmap == null) {
                                Text(text = stringResource(R.string.no_preview), color = Color.White, fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = "Notes:",
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 3,
                            fontSize = 16.sp,
                            overflow = TextOverflow.Ellipsis,
                            color = colorResource(R.color.black),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        Text(
                            text = specificDayRecordModel.userNotes.ifBlank { stringResource(R.string.don_t_have_any_notes) },
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            color = if (specificDayRecordModel.userNotes.isNotBlank()) {
                                colorResource(R.color.text_color)
                            } else {
                                colorResource(R.color.text_color_disabled)
                            }
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.TopEnd)
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorResource(R.color.color_primary))
                    .clickable(
                        onClick = {
                            onEditClick.invoke(specificDayRecordModel.mediaId)
                        },
                    )
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "Edit Media",
                    tint = Color.Black
                )
            }
        }
    }
}


@Composable
fun ErrorState(modifier: Modifier, error: String) {
    Text(
        text = error,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    )
}

@Composable
fun LoadingState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}


suspend fun extractVideoFrameAsync(context: Context, uri: Uri): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(context, uri)
            val frame = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
            retriever.release()
            frame
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SuccessStatePreview() {
    val mockRecords = listOf(
        SpecificDayRecordModel(
            mediaId = 1,
            isPhoto = true,
            mediaUri = Uri.parse("https://via.placeholder.com/100"),
            currentDate = "2024-12-19",
            userNotes = "Photo notes"
        ),
        SpecificDayRecordModel(
            mediaId = 2,
            isPhoto = false,
            mediaUri = Uri.parse("https://via.placeholder.com/100"),
            currentDate = "2024-12-18",
            userNotes = "Video notes"
        ),
        SpecificDayRecordModel(
            mediaId = 3,
            isPhoto = true,
            mediaUri = Uri.parse("https://via.placeholder.com/100"),
            currentDate = "2024-12-17",
            userNotes = ""
        )
    )
    val mockCurrentDayModel = CurrentDayModel(
        currentDayOfTheWeek = "Thursday",
        currentDayRecords = mockRecords
    )

    SuccessState(
        modifier = Modifier.fillMaxSize(),
        onBackClick = { },
        currentDayModel = mockCurrentDayModel,
        onEditClick = { }
    )
}


