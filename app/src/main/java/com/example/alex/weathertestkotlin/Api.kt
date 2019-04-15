package com.example.alex.weathertestkotlin

import com.example.alex.weathertestkotlin.model.WeatherMain
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


class Api {

    companion object {
        var retrofit: Retrofit? = null

        fun getClient(): Retrofit? {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl("http://api.openweathermap.org/data/2.5/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            }

            return retrofit
        }
    }

    interface WeatherServices {

        @Headers("x-api-key: 4428776ae883966d29f6f283dbe08ba6")
        @GET("weather?")
        fun  getWeatherInfo (
            @Query("q") city: String, @Query("units") units: String): Observable<WeatherMain>
    }
}