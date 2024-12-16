package com.abhilash.whereismybus

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.abhilash.whereismybus.ui.theme.WhereIsMyBusTheme
import com.abhilash.whereismybus.ui.theme.components.MMapScreen
import com.abhilash.whereismybus.ui.theme.components.MainScreen
import com.abhilash.whereismybus.ui.theme.firebase.ReadLocationFromFirebase
import com.abhilash.whereismybus.ui.theme.testing.NmapScreen


class MainActivity : ComponentActivity() {

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            // Handle the result of the permission request
            val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            if (fineLocationGranted && coarseLocationGranted) {
                Toast.makeText(this, "Location permissions granted", Toast.LENGTH_SHORT).show()
                // Permissions granted: Perform location-dependent actions
            } else {
                Toast.makeText(this, "Location permissions denied", Toast.LENGTH_SHORT).show()
                // Permissions denied: Notify the user
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check and request location permissions
        if (!hasLocationPermission()) {
            requestLocationPermission()
        }

        setContent {
            MaterialTheme {
                ReadLocationFromFirebase()
                //MainScreen()
               // BusStatusScreen()
               // MMapScreen()
              //  NmapScreen()
            }
        }
    }

    private fun hasLocationPermission(): Boolean {
        val fineLocation = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED

        val coarseLocation = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED

        return fineLocation && coarseLocation
    }

    private fun requestLocationPermission() {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WhereIsMyBusTheme {

    }
}
