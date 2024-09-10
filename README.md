![Static Badge](https://img.shields.io/badge/Android-green) 
![Static Badge](https://img.shields.io/badge/Kotlin-mauve)
![Static Badge](https://img.shields.io/badge/latest%20release:-1.2.0-red)
![Static Badge](https://img.shields.io/badge/jitpackio-black)


## Network connectivity: Effortlessly Track network status (Avaialble or not) -LIVE- in Your Android App

This developer-friendly library simplifies check network status in your android app if it is connect to network or not to prevent app from crashing if you don't have internet connection as you fetching data from Internet or using internet in your app.

Key Features:

 - Effortless Integration: Add the library to your project with just a few lines in your gradle files.
 - Change Network status with live mode when connection is lost it automatically notify 

# Getting Started:

## Set up Gradle:
 - Add this at the end of your repositories (**settings.gradle**)
    ```
    repositories {
        ....
        maven {
            url = uri("https://jitpack.io")
        }
    }
    ```
 - Add the dependency 
 
     ```
      dependencies {
            implementation "com.github.MohamedKandel:Network-Connetivity:latest-release"
        }
    ```
# Simple Usage:

To use this library in your code you need to call it in your activity or fragment as follow
```
class MainActivity : AppCompatActivity() {

    private lateinit var connectivity: ConnectionManager
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        connectivity = ConnectionManager.getInstance()

        connectivity.listenConnection(owner = this, context = this, listener =  object : OnConnectionChangedListener {
            override fun onConnectionAvailable() {
                // network connection available (do something need internet connection)
                fetchDataAPI()
            }

            override fun onConnectionLosing() {
                // network connection losing (don't do any operation need internet connection)
                noInternetDialog()
            }

            override fun onConnectionUnAvailable() {
                // network connection UnAvailable (don't do any operation need internet connection)
                noInternetDialog()
            }

            override fun onConnectionLost() {
                // network connection completely lost (don't do any operation need internet connection)
                noInternetDialog()
            }
        })
    }
}
```
# Compatibility:

This library is compatible with Android versions from API 24 (Android 7) to API 34 (Android 14).

# Contact:

Developed by Mohamed Kandeel. Feel free to reach out via email (mohamed.hossam7799@gmail.com) with suggestions or feature requests.

Thank you for choosing Network connectivity!
