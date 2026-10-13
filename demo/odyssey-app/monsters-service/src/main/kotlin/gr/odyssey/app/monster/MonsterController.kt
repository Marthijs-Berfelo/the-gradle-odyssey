package gr.odyssey.app.monster

import gr.odyssey.api.server.MonstersApi
import gr.odyssey.api.server.model.MonsterDTO
import gr.odyssey.api.server.model.MonsterRequestDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class MonsterController(
    private val monsterService: MonsterService,
) : MonstersApi {
    override suspend fun listMonsters(): ResponseEntity<Flow<MonsterDTO>> =
        monsterService
            .listMonsters()
            .map(Monster::toDTO)
            .let { ok(it) }

    override suspend fun getMonsterById(id: Long): ResponseEntity<MonsterDTO> =
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
            .let { ok(it) }

    override suspend fun createMonster(monsterRequestDTO: MonsterRequestDTO): ResponseEntity<MonsterDTO> =
        monsterRequestDTO
            .toDomain()
            .let { monsterService.createMonster(it) }
            .toDTO()
            .let { ok(it) }
}
