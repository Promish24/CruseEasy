package com.centennial.cruiseease.models.viewmodel.mybookings

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.centennial.cruiseease.models.datamodel.Booking
import com.centennial.cruiseease.sealed.BookingsDataState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyBookingsModel : ViewModel() {
    val response: MutableState<BookingsDataState> = mutableStateOf(BookingsDataState.Empty)

    init {
        fetchDataFromFirebase()
    }

    private fun fetchDataFromFirebase() {
        val tempList = mutableListOf<Booking>()
        response.value = BookingsDataState.Loading

        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

        FirebaseDatabase.getInstance().getReference("bookings")
            .orderByChild("userId").equalTo(currentUserUid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (datasnapshot in snapshot.children) {
                        val bookingItem = datasnapshot.getValue(Booking::class.java)
                        if (bookingItem != null)
                            tempList.add(bookingItem)
                        Log.d("FirebaseData", "Received Booking: $bookingItem")
                    }

                    if (tempList.isEmpty()) {
                        response.value = BookingsDataState.Empty
                    } else {
                        response.value = BookingsDataState.Success(tempList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    response.value = BookingsDataState.Failure(error.message)
                }
            })
    }
}
