package org.hse.moodactivities.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class GoogleUtils {
    public static String APP_ID = Dotenv.load().get("CLIENT_ID");
}
