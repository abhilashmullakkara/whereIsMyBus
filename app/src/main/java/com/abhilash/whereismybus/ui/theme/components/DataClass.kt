package com.abhilash.whereismybus.ui.theme.components

//data class BusStatus(
//    val isStarted: Boolean = false,
//    val currentLocation: Location = Location(),
//    val reachedPlaces: List<String> = emptyList(),
//    val hasBreakdown: Boolean = false,
//    val breakdownMessage: String? = null
//)
//
//data class Location(
//    val latitude: Double = 0.0,
//    val longitude: Double = 0.0,
//    val placeName: String = ""
//)
data class BusStatus(
    val isStarted: Boolean = false,
    val currentLocation: Location = Location(),
    val reachedPlaces: List<String> = emptyList(),
    val hasBreakdown: Boolean = false,
    val breakdownMessage: String? = null
)

data class Location(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val placeName: String = ""
)
