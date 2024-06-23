# Mood Activities

An App for tracking your mood, analyzed by ChatGPT

## Building

The whole project is built by Gradle with latest Android SDK and Java SDK 21

### Prerequisites

Before building you need to prepare `.env` file in `resources` source directory with following keys:

- `JWT_PRIVATE_KEY` - private part of JWT certificate keypair
- `JWT_PUBLIC_KEY` - public part of JWT certificate keypair
- `MONGO_HOST` - hostname of MongoDB instance
- `MONGO_PORT` - port of which Mongo is running
- `MONGO_USERS_DBNAME` - name for Mongo DB for user metadata
- `MONGO_QUESTIONS_DBNAME` - name for Mongo DB for questions metadata
- `CLIENT_ID` - Client ID of **WEB** client of APP on Google Cloud API
- `ADMIN` - username of default user in app
- `EMAIL` - email of default user in app
- `PASSWORD` - password of default user in app

### Running

To run the whole project you need to start additional services like PostgreSQL and MongoDB. 
There is a sample configuraion for `docker-compose`:

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
