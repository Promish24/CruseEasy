package com.centennial.cruiseease.models.viewmodel.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.centennial.cruiseease.models.datamodel.Cruise
import com.centennial.cruiseease.models.datamodel.UserData
import com.centennial.cruiseease.sealed.CruiseDataState
import com.centennial.cruiseease.sealed.ProfileDataState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class HomeViewModel:ViewModel() {
    val response: MutableState<CruiseDataState> = mutableStateOf(CruiseDataState.Empty)
    private val _userDataState = MutableLiveData<ProfileDataState>()
    val userDataState: LiveData<ProfileDataState> = _userDataState


    init {
        fetchUserInfoFromFirebase()
        fetchDataFromFirebase()
    }

    private fun fetchUserInfoFromFirebase() {
        _userDataState.value = ProfileDataState.Loading

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val firstName = document.getString("firstName")
                        val lastName = document.getString("lastName")
                        val email = document.getString("email")
                        val userPhotoUrl = document.getString("userPhoto")

                        val userData = UserData(
                            email ?: "",
                            firstName ?: "",
                            lastName ?: "",
                            userPhotoUrl ?: ""
                        )
                        _userDataState.value = ProfileDataState.Success(userData)
                    } else {
                        _userDataState.value = ProfileDataState.Empty
                    }
                }
                .addOnFailureListener { exception ->
                    _userDataState.value =
                        ProfileDataState.Error(exception.message ?: "Unknown error")
                }
        }
    }

    private fun fetchDataFromFirebase(){
        val tempList = mutableListOf<Cruise>()
        response.value = CruiseDataState.Loading
        FirebaseDatabase.getInstance().getReference("cruises")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dataSnap in snapshot.children) {
                        val cruiseItem = dataSnap.getValue(Cruise::class.java)
                        if (cruiseItem != null)
                            tempList.add(cruiseItem)
                    }
                    response.value = CruiseDataState.Success(tempList)
                }

                override fun onCancelled(error: DatabaseError) {
                    response.value = CruiseDataState.Failure(error.message)
                }

            })
    }
}