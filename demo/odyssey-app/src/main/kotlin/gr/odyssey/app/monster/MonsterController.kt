package gr.odyssey.app.monster

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    value = ["/monsters"],
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
class MonsterController(
    private val monsterService: MonsterService,
) {
    @GetMapping
    fun listMonsters(): Flow<MonsterDTO> =
        monsterService
            .listMonsters()
            .map(Monster::toDTO)

    @PostMapping
    suspend fun createMonster(
        @RequestBody request: MonsterRequestDTO,
    ): MonsterDTO =
        request
            .toDomain()
            .let { monsterService.createMonster(it) }
            .toDTO()
}
