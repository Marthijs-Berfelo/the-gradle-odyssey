package gr.odyssey.app.hero

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
    value = ["/heroes"],
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
class HeroController(
    private val heroService: HeroService,
) {
    @GetMapping
    fun listHeroes(): Flow<HeroDTO> =
        heroService
            .listHeroes()
            .map(Hero::toDTO)

    @PostMapping
    suspend fun createHero(
        @RequestBody request: HeroRequestDTO,
    ): HeroDTO =
        request
            .toDomain()
            .let { heroService.createHero(it) }
            .toDTO()
}
