package gr.odyssey.app.encounter

import gr.odyssey.app.TestcontainersConfiguration
import gr.odyssey.app.hero.HeroDTO
import gr.odyssey.app.hero.HeroRepository
import gr.odyssey.app.hero.HeroRequestDTO
import gr.odyssey.app.monster.MonsterDTO
import gr.odyssey.app.monster.MonsterRepository
import gr.odyssey.app.monster.MonsterRequestDTO
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import(TestcontainersConfiguration::class)
class EncounterControllerTest(
    @Autowired val webTestClient: WebTestClient,
    @Autowired val heroRepository: HeroRepository,
    @Autowired val monsterRepository: MonsterRepository,
) {
    private val createdHeroIds = mutableListOf<Long>()
    private val createdMonsterIds = mutableListOf<Long>()

    @AfterEach
    fun cleanUp() =
        runBlocking {
            createdHeroIds.forEach { heroRepository.deleteById(it) }
            createdMonsterIds.forEach { monsterRepository.deleteById(it) }
            createdHeroIds.clear()
            createdMonsterIds.clear()
        }

    @Test
    fun `hero with higher strength wins`() {
        val hero =
            webTestClient
                .post()
                .uri("/heroes")
                .bodyValue(HeroRequestDTO(name = "Menelaus", epithet = "the Horse-Tamer", strength = 9))
                .exchange()
                .expectStatus()
                .isOk
                .expectBody<HeroDTO>()
                .returnResult()
                .responseBody!!
        createdHeroIds += hero.id

        val monster =
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
        createdMonsterIds += monster.id

        val outcome =
            webTestClient
                .post()
                .uri("/encounters")
                .bodyValue(EncounterRequestDTO(heroId = hero.id, monsterId = monster.id))
                .exchange()
                .expectStatus()
                .isOk
                .expectBody<EncounterDTO>()
                .returnResult()
                .responseBody!!

        assertThat(outcome.outcome).isEqualTo("hero wins")
    }
}
