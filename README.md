# Gradle Slack Plugin

## Example Usage
```
slack {
    webhookUrl "https://hooks.slack.com/services/"
    username "Gradle"
    shouldMonitor "compileJava", "build"
}
```

## Example Message
![Example Message](/example-message.png)