package com.alexleventer.slack

import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test
import kotlin.test.assertNotNull

class SlackPluginTest {
    @Test
    fun `should apply slack plugin`() {
        val project = ProjectBuilder.builder().build()
        project.plugins.apply(SlackPlugin::class.java)
        assertNotNull(project)
    }
}
