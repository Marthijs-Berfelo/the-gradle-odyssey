package gr.odyssey.app.encounter

import gr.odyssey.app.TestcontainersConfiguration
import gr.odyssey.app.hero.CreateHeroRequest
import gr.odyssey.app.hero.HeroResponse
import gr.odyssey.app.monster.CreateMonsterRequest
import gr.odyssey.app.monster.MonsterResponse
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient
import org.springframework.context.annotation.Import
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import(TestcontainersConfiguration::class)
class EncounterControllerTest(
    @Autowired val webTestClient: WebTestClient,
) {
    @Test
    fun `hero with higher strength wins`() {
        val hero =
            webTestClient
                .post()
                .uri("/heroes")
                .bodyValue(CreateHeroRequest(name = "Odysseus", epithet = "the Cunning", strength = 9))
                .exchange()
                .expectStatus()
                .isOk
                .expectBody(HeroResponse::class.java)
                .returnResult()
                .responseBody!!

        val monster =
            webTestClient
                .post()
                .uri("/monsters")
                .bodyValue(CreateMonsterRequest(name = "Polyphemus", domain = "cave", danger = 7))
                .exchange()
                .expectStatus()
                .isOk
                .expectBody(MonsterResponse::class.java)
                .returnResult()
                .responseBody!!

        webTestClient
            .post()
            .uri("/encounters")
            .bodyValue(EncounterRequest(heroId = hero.id, monsterId = monster.id))
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.outcome")
            .isEqualTo("hero wins")
    }
}
