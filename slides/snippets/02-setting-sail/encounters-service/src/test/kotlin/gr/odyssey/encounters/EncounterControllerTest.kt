package gr.odyssey.encounters

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import gr.odyssey.api.server.model.EncounterDTO
import gr.odyssey.api.server.model.EncounterRequestDTO
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.wiremock.spring.ConfigureWireMock
import org.wiremock.spring.EnableWireMock
import org.wiremock.spring.InjectWireMock

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@EnableWireMock(
    ConfigureWireMock(name = "heroes-service", baseUrlProperties = ["services.heroes-base-url"]),
    ConfigureWireMock(name = "monsters-service", baseUrlProperties = ["services.monsters-base-url"]),
)
class EncounterControllerTest(
    @Autowired val webTestClient: WebTestClient,
) {
    @InjectWireMock("heroes-service")
    lateinit var heroesService: WireMockServer

    @InjectWireMock("monsters-service")
    lateinit var monstersService: WireMockServer

    @Test
    fun `hero with higher strength wins`(): Unit =
        runBlocking {
            heroesService.stubFor(
                get(urlEqualTo("/heroes/1")).willReturn(
                    aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                            """{"id":1,"name":"Menelaus","epithet":"the Horse-Tamer","strength":9}""",
                        ),
                ),
            )
            monstersService.stubFor(
                get(urlEqualTo("/monsters/1")).willReturn(
                    aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                            """{"id":1,"name":"Lamia","domain":"the Grove","danger":7}""",
                        ),
                ),
            )

            val outcome =
                webTestClient
                    .post()
                    .uri("/encounters")
                    .bodyValue(EncounterRequestDTO(heroId = 1, monsterId = 1))
                    .exchange()
                    .expectStatus()
                    .isOk
                    .expectBody<EncounterDTO>()
                    .returnResult()
                    .responseBody!!

            assertThat(outcome.outcome).isEqualTo("hero wins")
        }
}
