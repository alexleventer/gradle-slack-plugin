

# Gradle Slack Plugin

Envía notificaciones a Slack cuando tus compilaciones de Gradle finalicen. Cada mensaje incluye el resultado de la tarea, el autor y el mensaje del último commit, y una insignia de estado, para que tu equipo pueda ver de un vistazo si la compilación tuvo éxito o falló.

![Mensaje de Ejemplo](/assets/example-message.png)

## Requisitos

- Gradle **7.0+** (probado en 8.x)
- JDK **17+**
- Una [URL de webhook entrante](https://api.slack.com/messaging/webhooks) de Slack

## Instalación

Aplica el plugin en tu `build.gradle.kts`:

```kotlin
plugins {
    id("com.alexleventer.slack") version "2.1.0"
}

slack {
    webhookUrl.set("https://hooks.slack.com/services/XXX/YYY/ZZZ")
    shouldMonitor("build", "test")
}
```

O en Groovy (`build.gradle`):

```groovy
plugins {
    id 'com.alexleventer.slack' version '2.1.0'
}

slack {
    webhookUrl = 'https://hooks.slack.com/services/XXX/YYY/ZZZ'
    shouldMonitor 'build', 'test'
}
```

## Configuración

| Propiedad                | Tipo           | Predeterminado                                | Descripción                                                                                       |
| ----------------------- | -------------- | -------------------------------------- | ------------------------------------------------------------------------------------------------- |
| `webhookUrl`            | `String`       | _required_                             | URL de webhook entrante de Slack.                                                                       |
| `shouldMonitor`         | `List<String>` | `[]`                                   | Nombres de tareas cuya finalización activa una notificación (modo por tarea).                              |
| `notifyOnBuildFinished` | `Boolean`      | `false`                                | Cuando es `true`, envía una única notificación de resumen al finalizar toda la compilación en lugar de hacerlo por tarea. |
| `username`              | `String`       | `"Gradle"`                             | Nombre de visualización del bot en Slack.                                                                 |
| `iconUrl`               | `String`       | Gradlephant PNG                        | URL del avatar para el bot.                                                                           |
| `introText`             | `String`       | `"Your Gradle Build is Complete:"`     | Texto introductorio encima del adjunto.                                                                |

### Notificaciones por compilación vs. por tarea

De manera predeterminada, el plugin envía un mensaje cada vez que se completa una tarea listada en `shouldMonitor`. Si prefieres tener un único resumen al final de toda la compilación, con un estado general de Éxito/Fallo y conteo de tareas, configura `notifyOnBuildFinished = true`:

```kotlin
slack {
    webhookUrl.set(providers.environmentVariable("SLACK_WEBHOOK_URL"))
    notifyOnBuildFinished = true
}
```

En este modo se ignora `shouldMonitor`; cada tarea que ejecute la compilación contribuye al resumen.

### Mantener la URL del webhook fuera del control de versiones

Léela desde una propiedad de Gradle o una variable de entorno en lugar de codificarla directamente:

```kotlin
slack {
    webhookUrl.set(providers.environmentVariable("SLACK_WEBHOOK_URL"))
    shouldMonitor("build")
}
```

## Cómo funciona

El plugin registra un [`BuildService`](https://docs.gradle.org/current/userguide/build_services.html) compartido que escucha eventos de finalización de tareas mediante `BuildEventsListenerRegistry`. Cuando una tarea monitoreada termina, publica un mensaje en Slack utilizando el `java.net.http.HttpClient` integrado del JDK; no se agregan dependencias de tiempo de ejecución a tu classpath.

## Compilación desde el código fuente

```bash
./gradlew build
```

Ejecutar las pruebas:

```bash
./gradlew test
```

Publicar una instantánea local para probarla en otro proyecto:

```bash
./gradlew publishToMavenLocal
```

## Licencia

[MIT](LICENSE)
