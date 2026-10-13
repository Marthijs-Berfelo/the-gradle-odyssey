package gr.odyssey.encounters

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "services")
data class ServicesProperties(
    val heroesBaseUrl: String,
    val monstersBaseUrl: String,
)
