package com.example.alex.weathertestkotlin.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MainModel( @SerializedName("temp")
                 @Expose
                 var temp: Double,
                 @SerializedName("pressure")
                 @Expose
                 var pressure: Int,
                 @SerializedName("humidity")
                 @Expose
                 var humidity: Int,
                 @SerializedName("temp_min")
                 @Expose
                 var tempMin: Double,
                 @SerializedName("temp_max")
                 @Expose
                 var tempMax: Double)
