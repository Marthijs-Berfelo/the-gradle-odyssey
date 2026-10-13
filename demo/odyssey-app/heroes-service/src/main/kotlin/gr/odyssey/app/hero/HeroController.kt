package gr.odyssey.app.hero

import gr.odyssey.api.server.HeroesApi
import gr.odyssey.api.server.model.HeroDTO
import gr.odyssey.api.server.model.HeroRequestDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class HeroController(
    private val heroService: HeroService,
) : HeroesApi {
    override suspend fun listHeroes(): ResponseEntity<Flow<HeroDTO>> =
        heroService
            .listHeroes()
            .map(Hero::toDTO)
            .let { ok(it) }

    override suspend fun getHeroById(id: Long): ResponseEntity<HeroDTO> =
        runCatching {
            heroService
                .getHero(id)
                .toDTO()
        }.recover {
            when (it) {
                is HeroNotFoundException -> {
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

    override suspend fun createHero(heroRequestDTO: HeroRequestDTO): ResponseEntity<HeroDTO> =
        heroRequestDTO
            .toDomain()
            .let { heroService.createHero(it) }
            .toDTO()
            .let { ok(it) }
}
