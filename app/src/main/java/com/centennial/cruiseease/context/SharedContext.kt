package com.centennial.cruiseease.context

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf
import com.centennial.cruiseease.models.datamodel.Cruise

val LocalSelectedCruise = staticCompositionLocalOf { mutableStateOf<Cruise?>(null) }
