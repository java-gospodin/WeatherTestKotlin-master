package com.example.alex.weathertestkotlin.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WeatherMain(@SerializedName("weather")
                  @Expose
                  val weather  : List<Weather>?,
                  @SerializedName("base")
                  @Expose
                  var base: String,
                  @SerializedName("main")
                  @Expose
                  var main: MainModel,
                  @SerializedName("dt")
                  @Expose
                  var dt: Int,
                  @SerializedName("sys")
                  @Expose
                  var sys: Sys,
                  @SerializedName("id")
                  @Expose
                  var id: Int,
                  @SerializedName("name")
                  @Expose
                  var name: String,
                  @SerializedName("cod")
                  @Expose
                  var cod: Int)
