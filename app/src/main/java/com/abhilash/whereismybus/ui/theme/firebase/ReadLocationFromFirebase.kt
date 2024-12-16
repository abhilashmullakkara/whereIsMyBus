package com.abhilash.whereismybus.ui.theme.firebase

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.abhilash.whereismybus.R
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
@Composable
fun ReadLocationFromFirebase(userId: String = "RNE796") {
    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState()
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }

    // Firebase initialization
    val database = FirebaseDatabase.getInstance("https://whereismybus-673fa-default-rtdb.firebaseio.com/")
    val ref = database.getReference("locations").child(userId)

    // Listen for Firebase data changes
    DisposableEffect(Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val locationData = snapshot.value as? Map<*, *>
                    val latitude = locationData?.get("latitude") as? Double
                    val longitude = locationData?.get("longitude") as? Double
                    currentLocation = if (latitude != null && longitude != null) {
                        LatLng(latitude, longitude)
                    } else {
                        null
                    }

                    Log.d("FirebaseTest", "Updated Latitude: $latitude, Longitude: $longitude")
                } else {
                    Log.d("FirebaseTest", "No location data available for userId: $userId")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseTest", "Failed to read data: ${error.message}")
            }
        }

        // Add the listener to Firebase
        ref.addValueEventListener(valueEventListener)

        // Cleanup listener when composable is disposed
        onDispose {
            ref.removeEventListener(valueEventListener)
        }
    }

    // Display map or a loading message
    Box(modifier = Modifier.fillMaxSize()) {
        if (currentLocation != null) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState.apply {
                    position = CameraPosition.fromLatLngZoom(currentLocation!!, 15f)
                },
                properties = MapProperties(isMyLocationEnabled = true),
                uiSettings = MapUiSettings(zoomControlsEnabled = true)
            ) {
                val markerState = remember { MarkerState(position = currentLocation!!) }
                val busIcon = BitmapDescriptorFactory.fromResource(R.drawable.mybus)
                Marker(
                    state = markerState,
                    icon = busIcon,
                    title = "Bus Location",
                    snippet = "Your live location"
                )
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Fetching location... Please wait.")
            }
        }
    }
}
