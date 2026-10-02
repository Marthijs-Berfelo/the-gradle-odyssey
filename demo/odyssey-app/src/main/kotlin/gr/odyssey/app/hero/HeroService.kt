package gr.odyssey.app.hero

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service

@Service
class HeroService(
    private val heroRepository: HeroRepository,
) {
    fun listHeroes(): Flow<Hero> = heroRepository.findAll().map { it.toDomain() }

    suspend fun getHero(id: Long): Hero = heroRepository.findById(id)?.toDomain() ?: throw NoSuchElementException("Hero $id not found")

    suspend fun createHero(
        name: String,
        epithet: String,
        strength: Int,
    ): Hero {
        val saved = heroRepository.save(Hero(id = null, name = name, epithet = epithet, strength = strength).toEntity())
        return saved.toDomain()
    }
}
