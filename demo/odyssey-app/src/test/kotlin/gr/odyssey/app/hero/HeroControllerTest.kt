package gr.odyssey.app.hero

import gr.odyssey.app.TestcontainersConfiguration
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient
import org.springframework.context.annotation.Import
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import(TestcontainersConfiguration::class)
class HeroControllerTest(
    @Autowired val webTestClient: WebTestClient,
) {
    @Test
    fun `creates and lists a hero`() {
        webTestClient
            .post()
            .uri("/heroes")
            .bodyValue(CreateHeroRequest(name = "Odysseus", epithet = "the Cunning", strength = 8))
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.name")
            .isEqualTo("Odysseus")

        webTestClient
            .get()
            .uri("/heroes")
            .exchange()
            .expectStatus()
            .isOk
            .expectBodyList(HeroResponse::class.java)
            .value<WebTestClient.ListBodySpec<HeroResponse>> { heroes -> assert(heroes?.any { it.name == "Odysseus" } == true) }
    }
}
