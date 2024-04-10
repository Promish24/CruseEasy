@file:OptIn(
    ExperimentalFoundationApi::class
)

package com.centennial.cruiseease.ui.screens.unauth

import com.centennial.cruiseease.data.onboardingData
import com.centennial.cruiseease.utils.SecurePreferences

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.centennial.cruiseease.constants.Strings
import com.centennial.cruiseease.models.datamodel.OnboardingDataModel
import com.centennial.cruiseease.sealed.Screen
import com.centennial.cruiseease.ui.theme.Metrics
import com.centennial.cruiseease.ui.theme.urbanistFont
import com.centennial.cruiseease.utils.getScreenHeightPercentage
import com.centennial.cruiseease.utils.getScreenWidthPercentage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

@Composable
fun OnboardingScreen(navController
                     : NavController) {
    PagerStepThree(navController = navController)
}

@Composable
fun PagerStepThree(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val items = onboardingData
        val pageCount = items.size
        val pagerState = rememberPagerState(
            pageCount = { pageCount },
        )
        val context = LocalContext.current
        val indicatorScrollState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()


        LaunchedEffect(key1 = pagerState.currentPage, block = {
            val currentPage = pagerState.currentPage
            val size = indicatorScrollState.layoutInfo.visibleItemsInfo.size
            val lastVisibleIndex =
                indicatorScrollState.layoutInfo.visibleItemsInfo.last().index
            val firstVisibleItemIndex = indicatorScrollState.firstVisibleItemIndex

            if (currentPage > lastVisibleIndex - 1) {
                indicatorScrollState.animateScrollToItem(currentPage - size + 2)
            } else if (currentPage <= firstVisibleItemIndex + 1) {
                indicatorScrollState.animateScrollToItem((currentPage - 1).coerceAtLeast(0))
            }
        })

        val seventyFivePercentOfScreenHeight =
            getScreenHeightPercentage(percentage = Metrics.Percemtages.seventyFive).dp
        val ninetyPercentageOfScreenWidth =
            getScreenWidthPercentage(percentage = Metrics.Percemtages.ninety).dp

        Spacer(modifier = Modifier.height(25.dp))
        PagerSection(
            pagerState,
            seventyFivePercentOfScreenHeight,
            ninetyPercentageOfScreenWidth,
            items
        )
        IndicatorSection(indicatorScrollState, pageCount, pagerState)

        ButtonSection(coroutineScope, pagerState, items, navController, context)

    }
}

@Composable
private fun PagerSection(
    pagerState: PagerState,
    seventyFivePercentOfScreenHeight: Dp,
    ninetyPercentageOfScreenWidth: Dp,
    items: ArrayList<OnboardingDataModel>
) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .height(seventyFivePercentOfScreenHeight)
    ) { page ->
        Column(
            modifier = Modifier
                .padding(25.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(0.7f)
                    .width(ninetyPercentageOfScreenWidth)
                    .align(Alignment.CenterHorizontally)

            ) {
                Image(
                    painter = painterResource(id = items[page].image),
                    contentDescription = items[page].id,
                    modifier = Modifier
                        .height(300.dp)
                        .width(ninetyPercentageOfScreenWidth)
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            Box(modifier = Modifier.weight(0.2f), contentAlignment = Alignment.Center) {
                Text(
                    text = items[page].title,
                    style = TextStyle(
                        fontFamily = urbanistFont,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontSize = 30.sp,
                        color = Color(0, 0, 0)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            Box(modifier = Modifier.weight(0.2f), contentAlignment = Alignment.Center) {
                Text(
                    text = items[page].description,
                    style = TextStyle(
                        fontFamily = urbanistFont,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        color = Color(0, 0, 0)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun IndicatorSection(
    indicatorScrollState: LazyListState,
    pageCount: Int,
    pagerState: PagerState
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LazyRow(
            state = indicatorScrollState,
            modifier = Modifier
                .height(50.dp)
                .width(((6 + 16) * 2 + 3 * (10 + 16)).dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(pageCount) { iteration ->
                item(key = "item$iteration") {
                    val currentPage = pagerState.currentPage
                    val firstVisibleIndex by remember { derivedStateOf { indicatorScrollState.firstVisibleItemIndex } }
                    val lastVisibleIndex =
                        indicatorScrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                            ?: 0

                    val height = when (iteration) {
                        currentPage -> 10.dp
                        in firstVisibleIndex + 1 until lastVisibleIndex -> 10.dp
                        else -> 10.dp
                    }

                    val width = when (iteration) {
                        currentPage -> 35.dp
                        in firstVisibleIndex + 1 until lastVisibleIndex -> 10.dp
                        else -> 10.dp
                    }

                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .size(width, height)
                            .background(
                                brush = if (currentPage == iteration) {
                                    Brush.linearGradient(
                                        colors = listOf(
                                            Color(142, 64, 255),
                                            Color(114, 16, 255)
                                        ),
                                        start = Offset(0f, 0f),
                                        end = Offset(width.value, height.value)
                                    )
                                } else {
                                    Brush.linearGradient(
                                        colors = listOf(
                                            Color(224, 224, 224),
                                            Color(224, 224, 224)
                                        ),
                                        start = Offset(0f, 0f),
                                        end = Offset(width.value, height.value)
                                    )
                                }, CircleShape
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun ButtonSection(
    coroutineScope: CoroutineScope,
    pagerState: PagerState,
    items: ArrayList<OnboardingDataModel>,
    navController: NavController,
    context: Context
) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .padding(bottom = 20.dp)
            .padding(horizontal = 10.dp)
            .fillMaxHeight()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                coroutineScope.launch {
                    if (pagerState.currentPage == items.size - 1) {
                        getStarted(navController = navController, context = context)
                    } else {
                        val nextPage = (pagerState.currentPage + 1) % items.size
                        pagerState.animateScrollToPage(nextPage)
                    }
                }
            },
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 3.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(
                    red = 114,
                    green = 16,
                    blue = 255
                )
            )
        ) {
            Text(
                if (pagerState.currentPage == items.size - 1) Strings.GET_STARTED else Strings.NEXT,
                style = TextStyle(
                    fontFamily = urbanistFont,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    letterSpacing = 0.5.sp,
                    color = Color(255, 255, 255)
                )
            )

        }
    }
}

private fun getStarted(navController: NavController, context: Context) {
    val securePreferences = SecurePreferences(context)
    runBlocking { securePreferences.setHasBeenToOnboarding(true) }
    navController.navigate(Screen.UnauthNavigatorScreen.route) {
        popUpTo(Screen.OnboardNavigatorScreen.route) {
            inclusive = true
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    val mockNavController = rememberNavController()
    OnboardingScreen(mockNavController)
}