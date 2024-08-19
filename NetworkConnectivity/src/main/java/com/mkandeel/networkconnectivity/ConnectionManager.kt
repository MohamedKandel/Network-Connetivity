package com.mkandeel.networkconnectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class ConnectionManager(private val context: Context) {

    private class Connectivity(context: Context): ConnectionListener {
        private val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        private val _statusLiveData = MutableLiveData<ConnectionListener.Status>()
        val statusLiveData: LiveData<ConnectionListener.Status> get() = _statusLiveData

        override fun onConnectionStatusChangedListener(): Flow<ConnectionListener.Status> {
            return callbackFlow {
                val callback = object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        launch {
                            trySend(ConnectionListener.Status.AVAILABLE)
                        }
                    }

                    override fun onLosing(network: Network, maxMsToLive: Int) {
                        super.onLosing(network, maxMsToLive)
                        launch {
                            trySend(ConnectionListener.Status.LOSING)
                        }
                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        launch {
                            trySend(ConnectionListener.Status.LOST)
                        }
                    }

                    override fun onUnavailable() {
                        super.onUnavailable()
                        launch {
                            trySend(ConnectionListener.Status.UNAVAILABLE)
                        }
                    }
                }
                connectivityManager.registerDefaultNetworkCallback(callback)
                awaitClose {
                    connectivityManager.unregisterNetworkCallback(callback)
                }
            }.distinctUntilChanged()
        }

        fun observe() {
            CoroutineScope(Dispatchers.IO).launch {
                onConnectionStatusChangedListener().collect {
                    _statusLiveData.postValue(it)
                }
            }
        }

    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    fun listenConnection(owner: LifecycleOwner, listener: OnConnectionChangedListener) {
        val connectivity = Connectivity(context)
        connectivity.observe()
        connectivity.statusLiveData.observe(owner) {
            when(it) {
                ConnectionListener.Status.AVAILABLE ->{
                    Log.i("Connection Listener mohamed", "Available")
                    listener.onConnectionAvailable()
                }
                ConnectionListener.Status.UNAVAILABLE -> {
                    Log.i("Connection Listener mohamed", "UnAvailable")
                    listener.onConnectionUnAvailable()
                }
                ConnectionListener.Status.LOST -> {
                    Log.i("Connection Listener mohamed", "Lost")
                    listener.onConnectionLost()
                }
                ConnectionListener.Status.LOSING -> {
                    Log.i("Connection Listener mohamed", "Losing")
                    listener.onConnectionLosing()
                }
            }
        }
    }
}