package com.abhilash.whereismybus.ui.theme.components

import com.google.firebase.database.*

class FirebaseRepository {
    private val database = FirebaseDatabase.getInstance()
    private val busStatusRef = database.getReference("busStatus")

    fun getBusStatus(onSuccess: (BusStatus) -> Unit) {
        busStatusRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val busStatus = snapshot.getValue(BusStatus::class.java)
                busStatus?.let { onSuccess(it) }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    fun updateBusStatus(busStatus: BusStatus) {
        busStatusRef.setValue(busStatus)
    }
}
