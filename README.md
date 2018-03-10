# Gradle Slack Plugin

![Example Message](/assets/example-message.png)

## Example Usage:
```
slack {
    webhookUrl "https://hooks.slack.com/services/"
    username "Gradle"
    shouldMonitor "compileJava", "build"
}
```

## Installation:
```
plugins {
    id "com.alexleventer.slack" version "1.0.3"
}

slack {
    webhookUrl "https://hooks.slack.com/services/
    username "Gradle"
    shouldMonitor "build", "test"
}
```

## Extension Properties:
| Property Name      | Type          | Default Value                       |
| ------------------ |:-------------:| -----------------------------------:|
| `webhookUrl`       | String        | ""                                  |
| `username`         | String?       | Gradle                              |
| `iconUrl`          | String?       | Gradlephant.png                     |
| `shouldMonitor`    | List<String>  | []                                  |
| `introText`        | String?       | "Your Gradle Build is Complete:"    |