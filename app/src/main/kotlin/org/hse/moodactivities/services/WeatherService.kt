package org.hse.moodactivities.services

import org.hse.moodactivities.common.proto.requests.stats.WeatherStatsRequest
import org.hse.moodactivities.common.proto.services.StatsServiceGrpc
import org.hse.moodactivities.responses.WeatherResponse

class WeatherService {
    companion object {
        fun getWeatherStats(
            stub: StatsServiceGrpc.StatsServiceBlockingStub,
            timePeriod: TimePeriod.Value
        ): WeatherResponse {
            val request = WeatherStatsRequest.newBuilder()
                .setPeriod(ServiceUtils.toPeriodType(timePeriod)).build()
            val serverResponse = stub.getWeatherStats(request)
            return WeatherResponse(serverResponse)
        }
    }
}
