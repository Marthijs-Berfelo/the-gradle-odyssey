package gr.odyssey.app.monster

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MonsterController(
    private val monsterService: MonsterService,
) {
    @GetMapping("/monsters")
    fun listMonsters(): Flow<MonsterResponse> = monsterService.listMonsters().map { it.toResponse() }

    @PostMapping("/monsters")
    suspend fun createMonster(
        @RequestBody request: CreateMonsterRequest,
    ): MonsterResponse = monsterService.createMonster(request.name, request.domain, request.danger).toResponse()
}
