package org.hse.moodactivities.responses

class WeekAnalyticsResponse(private val analytics: String, private val recommendations: String) {
    fun getAnalytics() : String {
        return analytics
    }

    fun getRecommendations() : String {
        return recommendations
    }
}
