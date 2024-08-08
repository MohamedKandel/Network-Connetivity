package com.mkandeel.networkconnetivity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.mkandeel.networkconnectivity.ConnectionManager
import com.mkandeel.networkconnectivity.OnConnectionChangedListener
import com.mkandeel.networkconnetivity.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var connectivity: ConnectionManager
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        connectivity = ConnectionManager(this)

        connectivity.listenConnection(this, object : OnConnectionChangedListener {
            override fun onConnectionAvailable() {
                binding.textView.text = "Available"
            }

            override fun onConnectionLosing() {
                binding.textView.text = "Losing"
            }

            override fun onConnectionUnAvailable() {
                binding.textView.text = "UnAvailable"
            }

            override fun onConnectionLost() {
                binding.textView.text = "Lost"
            }
        })
    }
}