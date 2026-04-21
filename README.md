# Gradle Slack Plugin

Post notifications to Slack when your Gradle builds finish. Each message includes
the task outcome, the last commit's author and message, and a status badge — so
your team can see at a glance whether the build succeeded or failed.

![Example Message](/assets/example-message.png)

## Requirements

- Gradle **7.0+** (tested on 8.x)
- JDK **17+**
- A Slack [incoming webhook URL](https://api.slack.com/messaging/webhooks)

## Installation

Apply the plugin in your `build.gradle.kts`:

```kotlin
plugins {
    id("com.alexleventer.slack") version "2.1.0"
}

slack {
    webhookUrl.set("https://hooks.slack.com/services/XXX/YYY/ZZZ")
    shouldMonitor("build", "test")
}
```

Or in Groovy (`build.gradle`):

```groovy
plugins {
    id 'com.alexleventer.slack' version '2.1.0'
}

slack {
    webhookUrl = 'https://hooks.slack.com/services/XXX/YYY/ZZZ'
    shouldMonitor 'build', 'test'
}
```

## Configuration

| Property                | Type           | Default                                | Description                                                                                       |
| ----------------------- | -------------- | -------------------------------------- | ------------------------------------------------------------------------------------------------- |
| `webhookUrl`            | `String`       | _required_                             | Slack incoming-webhook URL.                                                                       |
| `shouldMonitor`         | `List<String>` | `[]`                                   | Task names whose completion triggers a notification (per-task mode).                              |
| `notifyOnBuildFinished` | `Boolean`      | `false`                                | When `true`, send a single summary notification after the whole build finishes instead of per task. |
| `username`              | `String`       | `"Gradle"`                             | Display name of the bot in Slack.                                                                 |
| `iconUrl`               | `String`       | Gradlephant PNG                        | Avatar URL for the bot.                                                                           |
| `introText`             | `String`       | `"Your Gradle Build is Complete:"`     | Leading text above the attachment.                                                                |

### Per-build vs. per-task notifications

By default the plugin sends a message each time a task listed in `shouldMonitor`
completes. If you'd rather have a single summary at the end of the whole build —
with an overall Success/Failure status and task counts — set
`notifyOnBuildFinished = true`:

```kotlin
slack {
    webhookUrl.set(providers.environmentVariable("SLACK_WEBHOOK_URL"))
    notifyOnBuildFinished = true
}
```

In this mode `shouldMonitor` is ignored — every task the build runs contributes
to the summary.

### Keeping the webhook URL out of source control

Read it from a Gradle property or environment variable instead of hard-coding it:

```kotlin
slack {
    webhookUrl.set(providers.environmentVariable("SLACK_WEBHOOK_URL"))
    shouldMonitor("build")
}
```

## How it works

The plugin registers a shared [`BuildService`](https://docs.gradle.org/current/userguide/build_services.html)
that listens for task-completion events via `BuildEventsListenerRegistry`. When a
monitored task finishes, it posts a Slack message using the JDK's built-in
`java.net.http.HttpClient` — no runtime dependencies pulled onto your classpath.

## Building from source

```bash
./gradlew build
```

Run the tests:

```bash
./gradlew test
```

Publish a local snapshot for testing in another project:

```bash
./gradlew publishToMavenLocal
```

## License

[MIT](LICENSE)
