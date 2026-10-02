package gr.odyssey.app.monster

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service

@Service
class MonsterService(
    private val monsterRepository: MonsterRepository,
) {
    fun listMonsters(): Flow<Monster> =
        monsterRepository
            .findAll()
            .map { it.toDomain() }

    @Throws(MonsterNotFoundException::class)
    suspend fun getMonster(id: Long): Monster =
        monsterRepository
            .findById(id)
            ?.toDomain()
            ?: throw MonsterNotFoundException(id)

    suspend fun createMonster(monster: Monster): Monster =
        monster
            .toEntity()
            .let { monsterRepository.save(it) }
            .toDomain()
}
