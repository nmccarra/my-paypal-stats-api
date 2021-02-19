# my-paypal-stats-api

This API is a client of the existing [Paypal Transactions Search API](https://developer.paypal.com/docs/api/transaction-search/v1#transactions_get).
The objective of this application is to generate summary information from paypal user transactions.


Use Java 11 with this application.
 
```sh
brew tap AdoptOpenJDK/openjdk
brew cask install adoptopenjdk11
```

## Build, Test, Run
Gradle is used to build, test, and run this application.

```sh
brew install gradle
```

### build & test
```sh
gradle clean build
```

### run
This API uses the Spring Boot framework. 
Run using:

```sh
gradle bootRun
```