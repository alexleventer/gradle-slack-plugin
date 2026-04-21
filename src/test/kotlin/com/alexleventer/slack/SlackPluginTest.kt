package com.alexleventer.slack

import org.assertj.core.api.Assertions.assertThat
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test

class SlackPluginTest {

    @Test
    fun `applies plugin and registers slack extension`() {
        val project = ProjectBuilder.builder().build()
        project.plugins.apply(SlackPlugin::class.java)

        val extension = project.extensions.findByName("slack")
        assertThat(extension).isInstanceOf(SlackExtension::class.java)
    }

    @Test
    fun `extension exposes sensible defaults`() {
        val project = ProjectBuilder.builder().build()
        project.plugins.apply(SlackPlugin::class.java)

        val ext = project.extensions.getByType(SlackExtension::class.java)
        assertThat(ext.username.get()).isEqualTo("Gradle")
        assertThat(ext.introText.get()).isEqualTo("Your Gradle Build is Complete:")
        assertThat(ext.shouldMonitor.get()).isEmpty()
        assertThat(ext.iconUrl.get()).contains("gradlephant.png")
    }

    @Test
    fun `shouldMonitor varargs sets task list`() {
        val project = ProjectBuilder.builder().build()
        project.plugins.apply(SlackPlugin::class.java)

        val ext = project.extensions.getByType(SlackExtension::class.java)
        ext.shouldMonitor("build", "test")
        assertThat(ext.shouldMonitor.get()).containsExactly("build", "test")
    }
}
