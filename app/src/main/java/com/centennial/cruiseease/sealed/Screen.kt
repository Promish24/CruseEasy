package com.centennial.cruiseease.sealed

import com.centennial.cruiseease.constants.Routes
import com.centennial.cruiseease.models.datamodel.Cruise

sealed class Screen(val route: String) {
    data object HomeScreen : Screen(Routes.HOME)
    data object DetailScreen : Screen(Routes.DETAIL)
    data object LoginScreen : Screen(Routes.LOGIN)
    data object SignUpScreen : Screen(Routes.SIGN_UP)
    data object SavedScreen : Screen(Routes.SAVED)
    data object BookingsScreen : Screen(Routes.BOOKINGS)
    data object OnboardScreen : Screen(Routes.ONBOARD)
    data object AuthNavigatorScreen : Screen(Routes.AUTH_NAVIGATOR)
    data object UnauthNavigatorScreen : Screen(Routes.UNAUTH_NAVIGATOR)
    data object OnboardNavigatorScreen : Screen(Routes.ONBOARD_NAVIGATOR)
    data object TermsAndConditionsScreen : Screen(Routes.TERMS_AND_CONDITIONS)
    data object ProfileScreen : Screen(Routes.PROFILE)
    data object Payment: Screen(Routes.PAYMENT)

    fun withArgs(vararg cruise: Cruise): String {
        return "$route/${cruise.toString()}"
    }
}
