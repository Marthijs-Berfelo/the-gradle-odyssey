package gr.odyssey.app.monster

import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.tags.Tags
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping(
    value = ["/monsters"],
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
@Tags(Tag(name = "Monster"))
class MonsterController(
    private val monsterService: MonsterService,
) {
    @GetMapping
    fun listMonsters(): Flow<MonsterDTO> =
        monsterService
            .listMonsters()
            .map(Monster::toDTO)

    @GetMapping("/{id}")
    suspend fun getMonsterById(
        @PathVariable id: Long,
    ): MonsterDTO =
        runCatching {
            monsterService
                .getMonster(id)
                .toDTO()
        }.recover {
            when (it) {
                is MonsterNotFoundException -> {
                    throw ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        it.message,
                        it,
                    )
                }

                else -> {
                    throw it
                }
            }
        }.getOrThrow()

    @PostMapping
    suspend fun createMonster(
        @RequestBody request: MonsterRequestDTO,
    ): MonsterDTO =
        request
            .toDomain()
            .let { monsterService.createMonster(it) }
            .toDTO()
}
