package com.vkolisnichenko.mediaconfirmation

import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaConfirmationScreen(
    onBackClick: () -> Unit,
    onFinishScreen: () -> Unit,
    viewModel: MediaConfirmationViewModel = hiltViewModel(),
    isUploadFromDB: Boolean = false,
    mediaId: Long
) {
    val mediaState by viewModel.mediaConfirmationScreenState.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit) {
        if (!isUploadFromDB) {
            viewModel.fetchMedia()
        } else {
            viewModel.fetchMediaCaptureById(mediaId)
        }
    }


    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.remove_media)) },
            text = { Text(stringResource(R.string.are_you_sure_that_you_want_to_remove_the_media_you_created)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        if (isUploadFromDB) {
                            viewModel.onRemoveMedia(mediaId)
                        } else {
                            onFinishScreen.invoke()
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.remove), color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.confirmation),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    TextButton(onClick = {
                        if (!isUploadFromDB) {
                            viewModel.onSaveMedia()
                        } else {
                            viewModel.onUpdateMedia(mediaId)
                        }
                    }) {
                        Text(
                            text = if (isUploadFromDB) stringResource(R.string.confirm) else stringResource(R.string.save),
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = colorResource(id = R.color.color_primary)
                )
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(colorResource(id = R.color.secondary_color))
                .padding(16.dp)
        ) {
            when (val state = mediaState) {
                is MediaConfirmationState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is MediaConfirmationState.Success -> {
                    val media = state.mediaConfirmationModel
                    if (media.onFinishScreen) {
                        LaunchedEffect(Unit) {
                            onFinishScreen.invoke()
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        if (media.isPhoto) {
                            AsyncImage(
                                model = media.mediaUri,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            AndroidView(
                                factory = { context ->
                                    VideoView(context).apply {
                                        setVideoURI(media.mediaUri)
                                        setMediaController(MediaController(context).apply {
                                            setAnchorView(this@apply)
                                        })
                                        start()
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .clip(RoundedCornerShape(16.dp))
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = media.currentDate,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = stringResource(R.string.want_to_note_something_special),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        var noteText by remember { mutableStateOf(media.userNotes) }
                        media.userNotes = noteText

                        TextField(
                            value = noteText,
                            onValueChange = {
                                noteText = it
                            },
                            placeholder = { Text(stringResource(R.string.enter_your_notes_here)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .border(
                                    BorderStroke(1.dp, Color.LightGray),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(8.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = colorResource(id = R.color.background_color),
                                unfocusedContainerColor = colorResource(id = R.color.background_color),
                                disabledContainerColor = colorResource(id = R.color.background_color),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                        )

                        Spacer(modifier = Modifier.weight(1f))
                    }
                }

                is MediaConfirmationState.Error -> {
                    //todo
                }
            }

            Button(
                onClick = { showDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .height(65.dp)
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.red)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = stringResource(R.string.remove), color = Color.White, fontSize = 20.sp)
            }
        }
    }
}
