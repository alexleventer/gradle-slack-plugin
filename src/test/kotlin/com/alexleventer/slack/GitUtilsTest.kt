package com.alexleventer.slack

import com.alexleventer.slack.utils.GitUtils
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test
import kotlin.test.assertNotNull

class GitUtilsTest {
    @Test
    fun `should get git remote`() {
        println(GitUtils.getGitRemote())
    }
}