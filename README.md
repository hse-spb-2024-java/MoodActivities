# Mood Activities

## Overview

Mood Activities is a special Android application that helps people understand and improve their emotions, mood and physical condition and get an analysis of this from ChatGPT 3.5.

With the app you can:
- Record how you feel every day
- Answer questions from ChatGPT
- Get some useful insights based on your answers
- Interact with an AI assistant to find answers to your questions
- Perform various activities
- Analyze the dependence of your mood on various factors


<p align="middle">
<img src="img/home_screen.png" width="30%" />
<img src="img/analytics.png" width="30%" />
<img src="img/calendar.png" width="30%" />
</p>


## Building

The whole project is built by Gradle with latest Android SDK and Java SDK 21

### Prerequisites

In order to get the app going you need several things:
- Get ChatGPT 3.5 API key
- Setup the app in Google Cloud Console:
- - Setup Android client and one WEB Client in OAuth Consent screen to get OAuth going
- - Enable access to Fitness API
- Generate RSA4096 keypair for JWT signing
- Get OpenWeather API key to get weather services going

Before building you need to prepare `.env` file in `resources` source directory with following keys:

- `JWT_PRIVATE_KEY` - private part of JWT certificate keypair
- `JWT_PUBLIC_KEY` - public part of JWT certificate keypair
- `OPEN_WEATHER_MAP_KEY` - OpenWeather API key
- `GPT_KEY` - API key for ChatGPT 3.5
- `CLIENT_ID` - Client ID of **WEB** client of APP on Google Cloud Console
- `MONGO_HOST` - hostname of MongoDB instance
- `MONGO_PORT` - port of which Mongo is running
- `MONGO_USERS_DBNAME` - name for Mongo DB for user metadata
- `MONGO_QUESTIONS_DBNAME` - name for Mongo DB for questions metadata
- `ADMIN` - username of default user in app
- `EMAIL` - email of default user in app
- `PASSWORD` - password of default user in app

### Running

To run the whole project you need to start additional services like PostgreSQL and MongoDB. 
There is a sample configuration for `docker-compose`:

```shell
docker-compose -f docker-compose.dev.yml up
```

Then build and run backend:

```shell
gradlew backend:run
```

And then build and run Android app in an emulator:

```shell
gradlew installDebug
```
