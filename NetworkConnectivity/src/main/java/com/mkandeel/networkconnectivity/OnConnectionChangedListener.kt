package com.mkandeel.networkconnectivity

interface OnConnectionChangedListener {
    fun onConnectionAvailable()
    fun onConnectionLosing()
    fun onConnectionUnAvailable()
    fun onConnectionLost()
}