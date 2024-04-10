package com.centennial.cruiseease.ui.components.templates

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.centennial.cruiseease.constants.Routes
import com.centennial.cruiseease.ui.components.organisms.BottomTab
import com.centennial.cruiseease.ui.components.organisms.Header

@Composable
fun Layout(
    header: @Composable (() -> Unit)? = null,
    footer: @Composable (() -> Unit)? = null,
    content: @Composable (() -> Unit)? = null
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.height(60.dp)) {
            if (header != null) {
                header()
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            if (content != null) {
                content()
            }
        }

        footer?.let {
            Box(modifier = Modifier.height(60.dp)) {
                footer.invoke()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LayoutPreview() {
    val mockNavController = rememberNavController()
    Layout(
        header = { Header(navController=mockNavController, currentRoute = Routes.HOME) },
        content = null,
        footer = { BottomTab(mockNavController, Routes.HOME) }
    )
}
