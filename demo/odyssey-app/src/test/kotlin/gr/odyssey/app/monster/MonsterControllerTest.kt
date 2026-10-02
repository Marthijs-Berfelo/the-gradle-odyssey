package gr.odyssey.app.monster

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
class MonsterControllerTest(
    @Autowired val webTestClient: WebTestClient,
) {
    @Test
    fun `creates and lists a monster`() {
        webTestClient
            .post()
            .uri("/monsters")
            .bodyValue(CreateMonsterRequest(name = "Polyphemus", domain = "cave", danger = 7))
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.name")
            .isEqualTo("Polyphemus")

        webTestClient
            .get()
            .uri("/monsters")
            .exchange()
            .expectStatus()
            .isOk
            .expectBodyList(MonsterResponse::class.java)
            .value<WebTestClient.ListBodySpec<MonsterResponse>> { monsters -> assert(monsters?.any { it.name == "Polyphemus" } == true) }
    }
}
