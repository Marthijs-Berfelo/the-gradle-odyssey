package gr.odyssey.app.monster

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service

@Service
class MonsterService(
    private val monsterRepository: MonsterRepository,
) {
    fun listMonsters(): Flow<Monster> = monsterRepository.findAll().map { it.toDomain() }

    suspend fun getMonster(id: Long): Monster =
        monsterRepository.findById(id)?.toDomain() ?: throw NoSuchElementException("Monster $id not found")

    suspend fun createMonster(
        name: String,
        domain: String,
        danger: Int,
    ): Monster {
        val saved = monsterRepository.save(Monster(id = null, name = name, domain = domain, danger = danger).toEntity())
        return saved.toDomain()
    }
}
