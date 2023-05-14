package com.yeolsimee.moneysaving.data.data_store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences


interface DataStoreService {
    fun getDataStore(): DataStore<Preferences>
}