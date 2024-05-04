package org.hse.moodactivities.responses

import org.hse.moodactivities.common.proto.requests.defaults.MoodRecord
import org.hse.moodactivities.common.proto.responses.stats.AllDayResponse
import org.hse.moodactivities.models.DailyActivityItemModel
import org.hse.moodactivities.models.DailyQuestionItemModel
import org.hse.moodactivities.models.MoodEvent

class FullDayReportResponse() {
    private lateinit var dailyQuestion: DailyQuestionItemModel
    private lateinit var dailyActivity: DailyActivityItemModel
    private lateinit var moodEvents: ArrayList<MoodEvent>
    fun init(response: AllDayResponse) {
        val question = response.question.question
        val answer = response.question.answer
        val dailyQuestionTime = response.question.time
        dailyQuestion = DailyQuestionItemModel(question, answer, dailyQuestionTime)
        val activity = response.activity.activity
        val impressions = response.activity.report
        val dailyActivityTime = response.activity.time
        dailyActivity = DailyActivityItemModel(activity, impressions, dailyActivityTime)
        moodEvents = ArrayList()
        moodEvents.ensureCapacity(response.recordsCount)
        for (i in 0..response.recordsCount - 1) {
            val record = response.getRecords(i)
            val moodEvent = MoodEvent()
            moodEvent.setMoodRate(record.moodsCount)
            moodEvent.setQuestion(record.question)
            moodEvent.setUserAnswer(record.answer)
            moodEvent.setChosenActivities(initActivities(record))
            moodEvent.setChosenEmotions(initEmotions(record))
            moodEvents.add(moodEvent)
        }
    }

    private fun initActivities(record: MoodRecord): HashSet<String> {
        val activities = HashSet<String>()
        val activitiesCount = record.activitiesCount - 1
        for (pos in 0..activitiesCount) {
            activities.add(record.getActivities(pos))
        }
        return activities
    }

    private fun initEmotions(record: MoodRecord): HashSet<String> {
        val emotions = HashSet<String>()
        val emotionsCount = record.moodsCount - 1
        for (pos in 0..emotionsCount) {
            emotions.add(record.getMoods(pos))
        }
        return emotions
    }

    fun getDailyActivity(): DailyActivityItemModel {
        return dailyActivity
    }

    fun getDailyQuestion(): DailyQuestionItemModel {
        return dailyQuestion
    }

    fun getMoodEvents(): ArrayList<MoodEvent> {
        return moodEvents
    }
}