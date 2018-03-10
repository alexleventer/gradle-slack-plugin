# Gradle Slack Plugin

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
```

## Example Message:
![Example Message](/assets/example-message.png)

## Extension Properties:
| Property Name | Type          | Default Value   |
| ------------- |:-------------:| ---------------:|
| `webhookurl`  | String        | ""              |
| `username`    | String        | Gradle          |
| `iconUrl`     | String        | Gradlephant.png |