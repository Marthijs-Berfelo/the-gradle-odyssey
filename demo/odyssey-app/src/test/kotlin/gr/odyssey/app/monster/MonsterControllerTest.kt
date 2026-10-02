package gr.odyssey.app.monster

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
class MonsterControllerTest(
    @Autowired val webTestClient: WebTestClient,
    @Autowired val monsterRepository: MonsterRepository,
) {
    private val createdMonsterIds = mutableListOf<Long>()

    @AfterEach
    fun cleanUp() =
        runBlocking {
            createdMonsterIds.forEach { monsterRepository.deleteById(it) }
            createdMonsterIds.clear()
        }

    @Test
    fun `creates and lists a monster`() {
        val created =
            webTestClient
                .post()
                .uri("/monsters")
                .bodyValue(MonsterRequestDTO(name = "Lamia", domain = "the Grove", danger = 7))
                .exchange()
                .expectStatus()
                .isOk
                .expectBody<MonsterDTO>()
                .returnResult()
                .responseBody!!
        createdMonsterIds += created.id

        assertThat(created.name).isEqualTo("Lamia")

        val monsters =
            webTestClient
                .get()
                .uri("/monsters")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<MonsterDTO>()
                .returnResult()
                .responseBody

        assertThat(monsters).anySatisfy { assertThat(it.name).isEqualTo("Lamia") }
    }
}
