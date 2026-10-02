package gr.odyssey.app.hero

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service

@Service
class HeroService(
    private val heroRepository: HeroRepository,
) {
    fun listHeroes(): Flow<Hero> =
        heroRepository
            .findAll()
            .map { it.toDomain() }

    @Throws(HeroNotFoundException::class)
    suspend fun getHero(id: Long): Hero =
        heroRepository
            .findById(id)
            ?.toDomain()
            ?: throw HeroNotFoundException(id)

    suspend fun createHero(hero: Hero): Hero =
        hero
            .toEntity()
            .let { heroRepository.save(it) }
            .toDomain()
}
