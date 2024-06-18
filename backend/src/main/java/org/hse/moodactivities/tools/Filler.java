package org.hse.moodactivities.tools;

import static org.hse.moodactivities.services.StatsService.getUser;

import static java.lang.Math.abs;

import org.hse.moodactivities.common.proto.requests.defaults.ActivityRecord;
import org.hse.moodactivities.data.entities.mongodb.Activity;
import org.hse.moodactivities.data.entities.mongodb.DailyActivity;
import org.hse.moodactivities.data.entities.mongodb.FitnessData;
import org.hse.moodactivities.data.entities.mongodb.Mood;
import org.hse.moodactivities.data.entities.mongodb.MoodFlowRecord;
import org.hse.moodactivities.data.entities.mongodb.RecordQuestion;
import org.hse.moodactivities.data.entities.mongodb.User;
import org.hse.moodactivities.data.entities.mongodb.UserDayMeta;
import org.hse.moodactivities.services.WeatherService;
import org.hse.moodactivities.utils.MongoDBSingleton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Filler {
    private static final Random random = new Random();
    private static ArrayList<String> emotions;
    private static ArrayList<String> activities;

    private static ArrayList<String> questions;
    private static ArrayList<String> actions;
    private static ArrayList<String> answers;

    private static ArrayList<String> summaries;

    private static double getRandomDouble(double min, double max) {
        return min + random.nextDouble() * (max - min);
    }

    private static int getRandomInt(int base, int add) {
        return base + abs(random.nextInt()) % add;
    }

    private static String getRandomWeather() {
        double dist = random.nextDouble();
        if (dist < 0.2) {
            return "Clear weather.";
        } else if (dist < 0.4) {
            return "Cloudy weather.";
        } else if (dist < 0.6) {
            return "Rainy weather.";
        } else if (dist < 0.8) {
            return "Snowy weather.";
        } else {
            return "Windy weather.";
        }
    }

    static {
        emotions = new ArrayList<>();
        emotions.add("joy");
        emotions.add("sadness");
        emotions.add("satisfaction");
        emotions.add("inspiration");
        emotions.add("anxiety");
        emotions.add("love");
        emotions.add("optimism");
        emotions.add("anger");
        emotions.add("disappointment");
        emotions.add("fear");
        emotions.add("drowsiness");
        emotions.add("awareness");
        emotions.add("thoughtfulness");
        emotions.add("gratitude");
        emotions.add("lonely");
        emotions.add("irritation");
        emotions.add("trust");
        emotions.add("enthusiasm");
        emotions.add("astonishment");
        emotions.add("irony");
        emotions.add("shock");
        emotions.add("indifference");
        emotions.add("determination");
        emotions.add("energy");
        emotions.add("admiration");

        activities = new ArrayList<>();
        activities.add("read");
        activities.add("family");
        activities.add("work");
        activities.add("walk");
        activities.add("sport");
        activities.add("study");
        activities.add("watch movie");
        activities.add("friends");
        activities.add("cook");
        activities.add("meditation");
        activities.add("listen music");
        activities.add("art");
        activities.add("photo");
        activities.add("dance");
        activities.add("play with pet");
        activities.add("museum");
        activities.add("learn new language");
        activities.add("board games");
        activities.add("video games");
        activities.add("play music");
        activities.add("date");
        activities.add("shopping");
        activities.add("attractions");
        activities.add("holiday");
        activities.add("concert");

        questions = new ArrayList<>();
        questions.add("How's your day going?");
        questions.add("What's been happening with you today?");
        questions.add("How are things going for you?");
        questions.add("How did your day unfold?");
        questions.add("Can you share how your day has been?");

        actions = new ArrayList<>();
        actions.add("Take a Walk in the Park");
        actions.add("Engage in a Hobby");
        actions.add("Watch a Comedy Show or Movie");
        actions.add("Practice Yoga or Meditation");
        actions.add("Spend Time with Loved Ones");

        answers = new ArrayList<>();
        answers.add("I loved it!");
        answers.add("It was fantastic!");
        answers.add("Superb experience!");
        answers.add("Superb experience!");
        answers.add("Couldn't be better!");

        summaries = new ArrayList<>();
        summaries.add("Today felt bright and hopeful.");
        summaries.add("Felt downcast and tired all day.");
        summaries.add("Had a wonderful day full of joy.");
        summaries.add("Day was stressful and overwhelming.");
        summaries.add("Felt calm and peaceful throughout the day.");
    }

    public static void runFiller(String userId) {
        User user = new User();
        user.setId(userId);
        int number = 30;
        LocalDate today = LocalDate.now();
        ArrayList<UserDayMeta> metas = new ArrayList<>();
        for (int daysDiff = number - 1; daysDiff >= 0; daysDiff--) {
            LocalDate daysAgo = today.minusDays(daysDiff);
            int mfCount = getRandomInt(2, 3);
            UserDayMeta newMeta = new UserDayMeta(daysAgo);
            for (int j = 0; j < mfCount; j++) {
                int rate = getRandomInt(0, 5);
                int sizes = getRandomInt(1, 3);
                ArrayList<Mood> moods = new ArrayList<>();
                ArrayList<Activity> internalActivities = new ArrayList<>();
                for (int counter = 0; counter < sizes; counter++) {
                    Mood newMood = new Mood();
                    newMood.setType(emotions.get(getRandomInt(0, emotions.size())));
                    moods.add(newMood);
                    Activity newActivity = new Activity();
                    newActivity.setType(activities.get(getRandomInt(0, activities.size())));
                    internalActivities.add(newActivity);
                }

                RecordQuestion question = new RecordQuestion(questions.get(getRandomInt(0, questions.size())), answers.get(getRandomInt(0, answers.size())));

                String summary = summaries.get(getRandomInt(0, summaries.size()));
                newMeta.addRecord(new MoodFlowRecord(moods, internalActivities, LocalTime.now().minusMinutes(daysDiff), question, rate, summary));
            }
            for (int j = 0; j < mfCount; j++) {
                UserDayMeta.Weather weather = new UserDayMeta.Weather(false, getRandomWeather(), getRandomDouble(15, 25), getRandomInt(75, 20), (int) newMeta.getRecords().getLast().getScore());
                newMeta.addWeather(weather);
            }
            newMeta.setFitnessData(new FitnessData(getRandomInt(5000, 15000)));
            DailyActivity activity = new DailyActivity(LocalTime.now().minusMinutes(daysDiff), actions.get(getRandomInt(0, actions.size())), answers.get(getRandomInt(0, answers.size())));
            newMeta.setActivity(activity);
            metas.add(newMeta);
        }
        user.setMetas(metas);
        MongoDBSingleton.getInstance().getConnection().saveEntity(user);
    }
}
