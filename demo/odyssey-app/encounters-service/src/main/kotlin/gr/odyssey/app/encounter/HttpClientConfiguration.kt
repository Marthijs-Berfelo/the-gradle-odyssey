package gr.odyssey.app.encounter

import gr.odyssey.api.client.HeroApi
import gr.odyssey.api.client.MonsterApi
import gr.odyssey.encounters.ServicesProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import org.springframework.web.service.invoker.createClient

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ServicesProperties::class)
class HttpClientConfiguration(
    private val services: ServicesProperties,
) {
    @Bean
    fun heroApi(webClientBuilder: WebClient.Builder): HeroApi =
        webClientBuilder
            .clone()
            .baseUrl(services.heroesBaseUrl)
            .build()
            .let { WebClientAdapter.create(it) }
            .let { HttpServiceProxyFactory.builderFor(it) }
            .build()
            .createClient<HeroApi>()

    @Bean
    fun monsterApi(webClientBuilder: WebClient.Builder): MonsterApi =
        webClientBuilder
            .clone()
            .baseUrl(services.monstersBaseUrl)
            .build()
            .let { WebClientAdapter.create(it) }
            .let { HttpServiceProxyFactory.builderFor(it) }
            .build()
            .createClient<MonsterApi>()
}
