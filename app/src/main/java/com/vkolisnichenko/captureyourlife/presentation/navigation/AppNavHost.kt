package com.vkolisnichenko.captureyourlife.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vkolisnichenko.addmedia.presentation.AddMediaScreen
import com.vkolisnichenko.core.domain.repository.CameraController
import com.vkolisnichenko.currentdaymedia.presentation.CurrentDayMediaScreen
import com.vkolisnichenko.mainscreen.presentation.MainScreen
import com.vkolisnichenko.mediaconfirmation.MediaConfirmationScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier,
    cameraController: CameraController
) {
    NavHost(navController = navController, startDestination = Screens.MainScreen.route) {
        composable(route = Screens.MainScreen.route) {
            MainScreen(
                modifier = modifier,
                onAddClick = {
                    navController.navigate(Screens.AddMediaScreen.route)
                },
                onItemClick = {
                    if(it.media.size > 1) {
                        navController.navigate("${Screens.CurrentDayMedia.route}/${it.date}")
                    } else {
                        navController.navigate("${Screens.MediaConfirmation.route}/${true}?mediaId=${it.media[0].id}")
                    }
                })
        }
        composable(route = Screens.AddMediaScreen.route) {
            AddMediaScreen({
                cameraController.openCameraForPhoto()
            }, {
                cameraController.openCameraForVideo()
            }) {
                navController.popBackStackOrIgnore()
            }
        }
        composable(
            route = "${Screens.MediaConfirmation.route}/{isUploadFromDB}?mediaId={mediaId}",
            arguments = listOf(navArgument("isUploadFromDB") {
                type = NavType.BoolType
            }, navArgument("mediaId") {
                    type = NavType.StringType
                    nullable = true
                })
        ) { backStackEntry ->
            val isUploadFromDB = backStackEntry.arguments?.getBoolean("isUploadFromDB") ?: false
            val mediaId = backStackEntry.arguments?.getString("mediaId")

            MediaConfirmationScreen(
                onBackClick = { navController.popBackStackOrIgnore() },
                onFinishScreen = {
                    navController.popBackStack(route = Screens.MainScreen.route, inclusive = false)
                },
                isUploadFromDB = isUploadFromDB,
                mediaId = mediaId?.toLong() ?: -1
            )
        }
        composable(
            route = "${Screens.CurrentDayMedia.route}/{day}",
            arguments = listOf(navArgument("day") { type = NavType.StringType })
        ) {
            val day = it.arguments?.getString("day")
            day?.let {
                CurrentDayMediaScreen(
                    onBackClick = { navController.popBackStackOrIgnore() },
                    currentDay = day,
                    onMediaItemClick = { mediaId ->
                        navController.navigate("${Screens.MediaConfirmation.route}/${true}?mediaId=${mediaId}")
                    }
                )
            }
        }
    }
}

fun NavController.popBackStackOrIgnore() {
    if (currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
        popBackStack()
    }
}