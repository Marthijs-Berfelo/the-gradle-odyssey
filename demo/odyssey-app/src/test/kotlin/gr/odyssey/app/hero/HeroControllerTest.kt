package gr.odyssey.app.hero

import gr.odyssey.app.TestcontainersConfiguration
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient
import org.springframework.context.annotation.Import
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.test.web.reactive.server.expectBodyList

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import(TestcontainersConfiguration::class)
class HeroControllerTest(
    @Autowired val webTestClient: WebTestClient,
    @Autowired val heroRepository: HeroRepository,
) {
    private val createdHeroIds = mutableListOf<Long>()

    @AfterEach
    fun cleanUp() =
        runBlocking {
            createdHeroIds.forEach { heroRepository.deleteById(it) }
            createdHeroIds.clear()
        }

    @Test
    fun `creates and lists a hero`() {
        val created =
            webTestClient
                .post()
                .uri("/heroes")
                .bodyValue(HeroRequestDTO(name = "Penelope", epithet = "the Faithful", strength = 6))
                .exchange()
                .expectStatus()
                .isOk
                .expectBody<HeroDTO>()
                .returnResult()
                .responseBody!!
        createdHeroIds += created.id

        assertThat(created.name).isEqualTo("Penelope")

        val heroes =
            webTestClient
                .get()
                .uri("/heroes")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<HeroDTO>()
                .returnResult()
                .responseBody

        assertThat(heroes).anySatisfy { assertThat(it.name).isEqualTo("Penelope") }
    }
}
