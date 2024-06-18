package org.hse.moodactivities.responses

import org.hse.moodactivities.common.proto.responses.stats.WeatherStatsResponse
import org.hse.moodactivities.models.WeatherStats

class WeatherResponse(response: WeatherStatsResponse) {
    private var weatherStats : ArrayList<WeatherStats>

    init {
        val weatherServerStats = response.weatherStatsList
        weatherStats = ArrayList(response.weatherStatsCount)
        for (stats in weatherServerStats) {
            weatherStats.add(WeatherStats(stats))
        }
    }

    fun getWeatherStats(): ArrayList<WeatherStats> {
        return weatherStats
    }
}
