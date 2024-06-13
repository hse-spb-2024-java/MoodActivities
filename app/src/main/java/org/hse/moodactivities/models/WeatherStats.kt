package org.hse.moodactivities.models

import android.util.Log

class WeatherStats(stats: org.hse.moodactivities.common.proto.responses.stats.WeatherStats) {
    private var score: Int
    private var temperature: Double
    private var humidity: Double

    init {
        Log.i("weather", stats.weather.description)
        score = stats.score
        temperature = stats.weather.temperature
        humidity = stats.weather.humidity
    }

    fun getScore(): Int {
        return score
    }

    fun getTemperature(): Double {
        return temperature
    }

    fun getHumidity(): Double {
        return humidity
    }
}
