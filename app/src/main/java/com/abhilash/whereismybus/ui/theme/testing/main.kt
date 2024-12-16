package com.abhilash.whereismybus.ui.theme.testing

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier


import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState


import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline

@Composable
fun MapScreen() {
    val atasehir = LatLng(40.9971, 29.1007)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(atasehir, 15f)
    }

    var uiSettings by remember {
        mutableStateOf(MapUiSettings(zoomControlsEnabled = true))
    }
    var properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.SATELLITE))
    }

    val routeCoordinates = listOf(
        LatLng(40.9967,29.0570), // Starting point
        LatLng(40.9900,30.0570), // Waypoint 1
        LatLng(41.0322,29.0216), // Waypoint 2
        LatLng(41.0333,29.0910)  // Ending point
    )

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings
    ) {
        Polyline(
            points = routeCoordinates,
            clickable = true,
            color = Color.Blue,
            width = 5f
        )
    }
}


@Composable
fun NmapScreen() {
    val atasehir = LatLng(40.9971, 29.1007)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(atasehir, 15f)
    }

    var uiSettings by remember {
        mutableStateOf(MapUiSettings(zoomControlsEnabled = true))
    }
    var properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.SATELLITE))
    }

    val routeCoordinates = listOf(
        LatLng(40.9967,29.0570), // Starting point
        LatLng(41.0322,29.0216), // Waypoint 1
        LatLng(41.0333,29.0910)  // Ending point
    )

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings
    ) {
        Polygon(
            points = routeCoordinates,
            clickable = true,
            fillColor = Color.Green,
            strokeColor = Color.Blue,
            strokeWidth = 5f
        )
    }
}