# Gradle Slack Plugin


## Example Usage
```
slack {
    webhookUrl = "https://webhook.slack.com"
    username "test"
    shouldMonitor "compileJava", "build"
}

```