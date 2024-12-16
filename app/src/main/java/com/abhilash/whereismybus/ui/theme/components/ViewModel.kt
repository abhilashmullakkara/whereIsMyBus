package com.abhilash.whereismybus.ui.theme.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BusViewModel : ViewModel() {
    private val repository = FirebaseRepository()

    private val _busStatus = MutableStateFlow<BusStatus?>(null)
    val busStatus: StateFlow<BusStatus?> = _busStatus

    init {
        fetchBusStatus()
    }

    private fun fetchBusStatus() {
        repository.getBusStatus { status ->
            viewModelScope.launch {
                _busStatus.emit(status)
            }
        }
    }

    fun updateBusStatus(status: BusStatus) {
        repository.updateBusStatus(status)
    }
}
