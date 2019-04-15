package com.example.alex.weathertestkotlin

import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.alex.weathertestkotlin.model.WeatherMain
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.DateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    internal lateinit var handler: Handler
    internal lateinit var city: TextView
    internal lateinit var temp: TextView
    internal lateinit var sky: TextView
    internal lateinit var gradus: TextView
    internal lateinit var detailsText: TextView
    internal lateinit var data: TextView

    val UNITS_CONST : String = "metric"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handler = Handler()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        temp = findViewById<TextView>(R.id.temperature)
        temp.typeface = Typeface.createFromAsset(assets, "fonts/HelveticaNeueCyr-UltraLight.otf")
        sky = findViewById<TextView>(R.id.sky)
        sky.typeface = Typeface.createFromAsset(assets, "fonts/weather.ttf")
        gradus = findViewById<TextView>(R.id.gradus)
        gradus.typeface = Typeface.createFromAsset(assets, "fonts/HelveticaNeueCyr-UltraLight.otf")
        gradus.text = "\u00b0C"
        detailsText = findViewById<TextView>(R.id.details)
        city = findViewById<TextView>(R.id.city)
        data = findViewById<TextView>(R.id.data)

        updateWeatherData(CityPreference(this@MainActivity).getCity()!!)
    }

    private fun updateWeatherData(city: String) {
        var  api : Api.WeatherServices = Api.getClient()!!.create(Api.WeatherServices::class.java)
        api.getWeatherInfo(city, UNITS_CONST).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({response ->
                renderWeather(response)
                Toast.makeText(this, "sff", Toast.LENGTH_LONG).show()
            },
                { error ->
                    Toast.makeText(this, error.message,Toast.LENGTH_SHORT).show()
                })
    }


    private fun renderWeather(response: WeatherMain){
        city.text = response.name.toUpperCase() + ", " + response.sys.country
        val details = response.weather?.get(0)
        val main = response.main
        detailsText.text = (details?.description!!.toUpperCase() + "\n" + resources.getString(R.string.humidity)
                + ": " + main.humidity + "%" + "\n" + resources.getString(R.string.pressure)
                + ": " + main.pressure + " hPa")
        detailsText.textAlignment = View.TEXT_ALIGNMENT_CENTER
        detailsText.setLineSpacing(0f, 1.4f)

        temp.text = String.format("%.1f", main.temp)

        val df = DateFormat.getDateTimeInstance()
        val updatedOn = df.format(Date( response.dt.toLong() * 1000))
        data.text = resources.getString(R.string.last_update) + " " + updatedOn


        setWeatherIcon(details.id, response.sys.sunrise * 1000,
                response.sys.sunset * 1000)
    }


    private fun setWeatherIcon(actualId: Int, sunrise: Int, sunset: Int) {
        val id = actualId / 100
        var icon = ""
        if (actualId == 800) {
            val currentTime = Date().time
            if (currentTime >= sunrise && currentTime < sunset) {
                icon = this@MainActivity.getString(R.string.weather_sunny)
            } else {
                icon = this@MainActivity.getString(R.string.weather_clear_night)
            }
        } else {
            Log.d("SimpleWeather", "id $id")
            when (id) {
                2 -> icon = this@MainActivity.getString(R.string.weather_thunder)
                3 -> icon = this@MainActivity.getString(R.string.weather_drizzle)
                5 -> icon = this@MainActivity.getString(R.string.weather_rainy)
                6 -> icon = this@MainActivity.getString(R.string.weather_snowy)
                7 -> icon = this@MainActivity.getString(R.string.weather_foggy)
                8 -> icon = this@MainActivity.getString(R.string.weather_cloudy)
            }
        }
        sky.text = icon
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) showInputDialog()
        return true
    }

    private fun showInputDialog() {
        val chooseCity = AlertDialog.Builder(this)
        chooseCity.setIcon(R.mipmap.ic_launcher)
        chooseCity.setTitle(R.string.choose_city)
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        chooseCity.setView(input)
        chooseCity.setPositiveButton(
            resources.getString(R.string.ok)
        ) { dialog, which ->
            val city = input.text.toString()
            if(TextUtils.isEmpty(city)) {
                dialog.dismiss()
            }else {
                updateWeatherData(city)
                CityPreference(this@MainActivity).setCity(city)
            }
        }
        chooseCity.show()
    }
}

