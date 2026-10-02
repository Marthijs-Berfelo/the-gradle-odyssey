package gr.odyssey.app.hero

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class HeroController(
    private val heroService: HeroService,
) {
    @GetMapping("/heroes")
    fun listHeroes(): Flow<HeroResponse> = heroService.listHeroes().map { it.toResponse() }

    @PostMapping("/heroes")
    suspend fun createHero(
        @RequestBody request: CreateHeroRequest,
    ): HeroResponse = heroService.createHero(request.name, request.epithet, request.strength).toResponse()
}
