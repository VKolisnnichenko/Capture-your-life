package com.vkolisnichenko.captureyourlife.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vkolisnichenko.captureyourlife.R


@Composable
fun PermissionDeniedDialog(onDismiss: () -> Unit, onOpenSettings : () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(stringResource(R.string.permission_denied)) },
        text = {
            Text(stringResource(R.string.camera_permission_is_required_to_take_photos_or_record_videos_please_enable_it_in_settings))
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = {
                    onOpenSettings()
                    onDismiss()
                }) {
                    Text(stringResource(R.string.ok))
                }
                TextButton(onClick = { onDismiss() }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        }
    )
}
