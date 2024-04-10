package com.centennial.cruiseease.utils

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import android.content.SharedPreferences
import com.centennial.cruiseease.constants.AppSecrets
import com.centennial.cruiseease.models.datamodel.Cruise
import com.google.common.reflect.TypeToken
import com.google.gson.Gson;
import java.lang.reflect.Type

class SecurePreferences(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val gson = Gson()

    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        AppSecrets.SECURE_PREFRENCES,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )

    fun setHasBeenToOnboarding(value: Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean(AppSecrets.ONBOARDED, value)
            apply()
        }
    }

    fun getHasBeenToOnboarding(): Boolean {
        return sharedPreferences.getBoolean(AppSecrets.ONBOARDED, false)
    }

    fun setToken(value: String){
        with(sharedPreferences.edit()){
            putString(AppSecrets.TOKEN, value)
            apply()
        }
    }

    fun removeToken() {
        with(sharedPreferences.edit()) {
            remove(AppSecrets.TOKEN)
            apply()
        }
    }


    fun getToken(): String? {
        return sharedPreferences.getString(AppSecrets.TOKEN, null)
    }


    fun setCruiseData(cruise: Cruise) {
        val cruiseJson = gson.toJson(cruise)
        with(sharedPreferences.edit()) {
            putString(AppSecrets.CRUISE_DATA, cruiseJson)
            apply()
        }
    }

    fun setWishlist(wishlist: List<Cruise>) {
        val existingWishlist: MutableList<Cruise> = getWishlist()?.toMutableList() ?: mutableListOf()
        val newCruises: List<Cruise> = wishlist.filter { newCruise ->
            existingWishlist.none { existingCruise -> existingCruise.id == newCruise.id }
        }
        existingWishlist.addAll(newCruises)
        val cruiseListJson = gson.toJson(existingWishlist)
        with(sharedPreferences.edit()) {
            putString(AppSecrets.WISHLIST_DATA, cruiseListJson)
            apply()
        }
    }



    fun getWishlist(): List<Cruise>? {
        val wishlistJson = sharedPreferences.getString(AppSecrets.WISHLIST_DATA, null)
        return if (wishlistJson != null) {
            val type: Type = object : TypeToken<List<Cruise>>() {}.type
            gson.fromJson(wishlistJson, type)
        } else {
            null
        }
    }

    fun removeFromWishlist(cruise: Cruise) {
        val existingWishlist: MutableList<Cruise> = getWishlist()?.toMutableList() ?: mutableListOf()
        existingWishlist.remove(cruise)
        val cruiseListJson = gson.toJson(existingWishlist)
        with(sharedPreferences.edit()) {
            putString(AppSecrets.WISHLIST_DATA, cruiseListJson)
            apply()
        }
    }



    fun getCruiseData(): Cruise? {
        val cruiseJson = sharedPreferences.getString(AppSecrets.CRUISE_DATA, null)
        return if (cruiseJson != null) {
            gson.fromJson(cruiseJson, Cruise::class.java)
        } else {
            null
        }
    }

    fun deleteCruiseData() {
        with(sharedPreferences.edit()) {
            remove(AppSecrets.CRUISE_DATA)
            apply()
        }
    }
}