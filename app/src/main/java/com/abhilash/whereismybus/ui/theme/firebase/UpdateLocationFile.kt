package com.abhilash.whereismybus.ui.theme.firebase

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.delay

fun updateLocationToFirebase(userId: String, latitude: Double, longitude: Double) {
    val database = FirebaseDatabase.getInstance("https://whereismybus-673fa-default-rtdb.firebaseio.com/")
    val ref = database.getReference("locations").child(userId)

    val locationData = mapOf(
        "latitude" to latitude,
        "longitude" to longitude,
        "timestamp" to System.currentTimeMillis()
    )

    ref.setValue(locationData)
        .addOnSuccessListener {
            Log.d("FirebaseTest", "Data written successfully!")
        }
        .addOnFailureListener { e ->
            Log.e("FirebaseTest", "Failed to write data: ${e.message}")
        }

    //ref.setValue(locationData)
}


@Composable
fun ShareBusLocation(busId: String="RNE796") {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    // Check for location permissions
    val hasFineLocationPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    // Continuously fetch and update location
    if (hasFineLocationPermission || hasCoarseLocationPermission) {
        LaunchedEffect(Unit) {
            while (true) {
                try {
                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        if (location != null) {
                            val latitude = location.latitude
                            val longitude = location.longitude
                           // updateLocationToFirebase("TestBus", 12.971598, 77.594566) // Sample LatLng for Bengaluru

                             updateLocationToFirebase(busId, latitude, longitude)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                delay(5000) // Update location every 5 seconds
            }
        }
    } else {

        //latitude
        //:8.6467473
    //   {
        //  "rules": {
        //    "locations": {
        //      ".read": true,  // Allow everyone to read
        //      ".write": "auth != null"  // Allow only logged-in users to write
        //    }
        //  }
        //
    // }
        //longitude
        //:76.9016858
        //timestamp
        //:1734221715631
        // Permission is not granted; Handle permission request outside this Composable
        // Use a proper mechanism like Accompanist Permissions or system dialogs
    }
}


fun fetchCurrentLocation(
    context: Context,
    onLocationReceived: (Location?) -> Unit
) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    try {
        fusedLocationClient.lastLocation.addOnSuccessListener(onLocationReceived)
    } catch (e: SecurityException) {
        e.printStackTrace()
    }
}
