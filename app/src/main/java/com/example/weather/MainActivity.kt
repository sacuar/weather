package com.example.weather

import android.app.DownloadManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.media3.common.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {


        private val apiKey = "7d6a73a841ac29cbb90013209d7c808d"
        private val baseUrl = "https://api.openweathermap.org/data/2.5/weather"
        private val city = "London" // Replace with your desired city

        private lateinit var textViewCity: TextView
        private lateinit var textViewDescription: TextView
        private lateinit var textViewTemperature: TextView

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            textViewCity = findViewById(R.id.textViewCity)
            textViewDescription = findViewById(R.id.textViewDescription)
            textViewTemperature = findViewById(R.id.textViewTemperature)

            val url = "$baseUrl?q=$city&appid=$apiKey"
            val request = Request.Builder().url(url).build()

            val client = OkHttpClient()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseData = response.body?.string()
                    runOnUiThread {
                        try {
                            val json = JSONObject(responseData)
                            val weatherArray = json.getJSONArray("weather")
                            val weatherObject = weatherArray.getJSONObject(0)
                            val description = weatherObject.getString("description")
                            val temperature = json.getJSONObject("main").getDouble("temp")

                            Log.d("Weather", "Description: $description")
                            Log.d("Weather", "Temperature: $temperature")

                            textViewCity.text = city
                            textViewDescription.text = description
                            textViewTemperature.text = temperature.toString()

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }
            })
        }
    }
