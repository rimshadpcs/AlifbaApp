package com.alifba.alifba.presentation.lessonScreens

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.alifba.alifba.R
import com.alifba.alifba.models.LessonScreenViewModel
import com.alifba.alifba.models.LessonSegment
import com.alifba.alifba.presentation.dialogs.LottieAnimationDialog
import com.alifba.alifba.presentation.lessonPath.LessonPathViewModel
import com.alifba.alifba.utils.PlayAudio
import kotlinx.coroutines.delay
@Composable
fun LessonScreen(lessonId: Int, navigateToLessonPathScreen: () -> Unit,viewModel: LessonScreenViewModel) {
    val lessonPathViewModel: LessonPathViewModel = viewModel()
    val lesson = viewModel.getLessonContentById(lessonId)
    var currentSegmentIndex by remember { mutableStateOf(0) }
    val showDialog = remember { mutableStateOf(false) }
    var currentCommonLessonSegment: LessonSegment.CommonLesson? by remember { mutableStateOf(null) }

    val introductionLessons by lessonPathViewModel.introductionLessons.observeAsState(initial = emptyList())

    // Show LottieAnimationDialog based on showDialog state
    if (showDialog.value) {
        LottieAnimationDialog(showDialog = showDialog, lottieFileRes = R.raw.celebration)
        // Play audio
        PlayAudio(audioResId = R.raw.yay)
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        if (lesson != null) {
            when (val currentSegment = lesson.segments[currentSegmentIndex]) {
                is LessonSegment.CommonLesson -> {
                    DisposableEffect(currentSegment) {
                        viewModel.stopAudio() // Stop any currently playing audio
                        viewModel.startAudio(currentSegment.speech) // Start new audio
                        onDispose {
                            viewModel.stopAudio()
                        }

                    }
                    CommonLessonSegment(segment = currentSegment,
                        onNextClicked = {
                            if (currentSegmentIndex < lesson.segments.size - 1) {
                                currentSegmentIndex++
                            } else {
                                showDialog.value = true
                            }
                        }
                    )
                }

                is LessonSegment.MCQLessonItem -> {
                    DisposableEffect(currentSegment) {
                        viewModel.stopAudio() // Stop any currently playing audio
                        viewModel.startAudio(currentSegment.speech) // Start new audio
                        onDispose {
                            viewModel.stopAudio()
                        }
                    }
                    MCQSegment(
                        segment = currentSegment,
                        onNextClicked = {
                            if (currentSegmentIndex < lesson.segments.size - 1) {
                                currentSegmentIndex++
                            } else {
                                showDialog.value = true
                            }
                        }
                    )
                }
            }
            println("Current CommonLesson Segment: $currentCommonLessonSegment")
            //println("Current Segment: $currentSegment")


            if (showDialog.value) {
                lessonPathViewModel.completeLesson(lessonId)
                LaunchedEffect(key1 = showDialog) {
                    delay(2000)
                    showDialog.value = false
                    navigateToLessonPathScreen()
                }

            }

        } else

        {
            Text("Lesson not found. ID: $lessonId") // Display the ID for debugging
        }
    }

}

