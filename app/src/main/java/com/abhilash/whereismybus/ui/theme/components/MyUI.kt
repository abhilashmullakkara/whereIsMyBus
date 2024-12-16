package com.abhilash.whereismybus.ui.theme.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusStatusScreen(viewModel: BusStatusViewModel = viewModel(factory = BusStatusViewModelFactory())) {
    val busStatus by viewModel.busStatus.observeAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Bus Status Tracker") }
            )
        }
    ) { padding ->
        if (busStatus == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Loading...")
            }
        } else {
            val status = busStatus!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = if (status.isStarted) "Bus Started" else "Bus Not Started")
                Text(text = "Current Location: ${status.currentLocation.placeName}")
                Text(text = "Coordinates: ${status.currentLocation.latitude}, ${status.currentLocation.longitude}")
                Text(text = "Reached Places: ${status.reachedPlaces.joinToString(", ")}")
                if (status.hasBreakdown) {
                    Text(
                        text = "Breakdown: ${status.breakdownMessage ?: "No details available"}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
                val newStatus = BusStatus(
                    isStarted = true,
                    currentLocation = Location(13.0827, 80.2707, "Chennai"),
                    reachedPlaces = listOf("Stop A", "Stop B", "Stop C"),
                    hasBreakdown = false,
                    breakdownMessage = null
                )
                viewModel.updateStatus(newStatus)
            }
        }
    }
}

class BusStatusViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BusStatusViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BusStatusViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class BusStatusViewModel : ViewModel() {

    private val _busStatus = MutableLiveData<BusStatus>()
    val busStatus: LiveData<BusStatus> = _busStatus

    init {
        // Initialize with mock or default data
        _busStatus.value = BusStatus(
            isStarted = true,
            currentLocation = Location(12.9716, 77.5946, "Bangalore"),
            reachedPlaces = listOf("Stop A", "Stop B"),
            hasBreakdown = false,
            breakdownMessage = null
        )
    }

    // Method to update the bus status dynamically
    fun updateStatus(newStatus: BusStatus) {
        _busStatus.value = newStatus
    }
}
