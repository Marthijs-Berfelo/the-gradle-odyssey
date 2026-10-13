package gr.odyssey.app

import gr.odyssey.app.TestcontainersLifecycleManager.Companion.postgresContainer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.postgresql.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import kotlin.getValue

interface TestcontainersLifecycleManager {
    companion object {
        private const val POSTGRES_IMAGE = "postgres:17"
        val postgresContainer: PostgreSQLContainer by lazy {
            DockerImageName
                .parse(POSTGRES_IMAGE)
                .let(::PostgreSQLContainer)
                .apply { start() }
        }
    }
}

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration : TestcontainersLifecycleManager {
    @Bean
    @ServiceConnection
    fun postgresContainer(): PostgreSQLContainer = postgresContainer
}
