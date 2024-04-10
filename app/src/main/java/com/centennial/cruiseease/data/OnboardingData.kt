package com.centennial.cruiseease.data

import com.centennial.cruiseease.R
import com.centennial.cruiseease.models.datamodel.OnboardingDataModel

val onboardingData = ArrayList<OnboardingDataModel>().apply {
    add(
        OnboardingDataModel(
            id = "1",
            image = R.drawable.welcome,
            title = "Welcome Aboard!",
            description = "Embark on an unforgettable journey across serene seas and breathtaking destinations. Let's set sail together!"
        )
    )
    add(
        OnboardingDataModel(
            id = "2",
            image = R.drawable.discover,
            title = "Discover Your Dream Destinations",
            description = "Explore a world of possibilities as you plan your perfect cruise getaway. From exotic beaches to vibrant cities, the adventure awaits!"
        )
    )
    add(
        OnboardingDataModel(
            id = "3",
            image = R.drawable.booking,
            title = "Seamless Booking Experience",
            description = "Experience hassle-free booking with our user-friendly platform. Your dream cruise is just a few taps away. Bon voyage!"
        )
    )
}
