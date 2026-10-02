package gr.odyssey.app.hero

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
    value = ["/heroes"],
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
@Tags(Tag(name = "Hero"))
class HeroController(
    private val heroService: HeroService,
) {
    @GetMapping
    fun listHeroes(): Flow<HeroDTO> =
        heroService
            .listHeroes()
            .map(Hero::toDTO)

    @GetMapping("/{id}")
    suspend fun getHeroById(
        @PathVariable id: Long,
    ): HeroDTO =
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

    @PostMapping
    suspend fun createHero(
        @RequestBody request: HeroRequestDTO,
    ): HeroDTO =
        request
            .toDomain()
            .let { heroService.createHero(it) }
            .toDTO()
}
