package com.mkandeel.networkconnectivity

import kotlinx.coroutines.flow.Flow

interface ConnectionListener {
    fun onConnectionStatusChangedListener(): Flow<Status>

    enum class Status {
        AVAILABLE, UNAVAILABLE, LOST, LOSING
    }
}