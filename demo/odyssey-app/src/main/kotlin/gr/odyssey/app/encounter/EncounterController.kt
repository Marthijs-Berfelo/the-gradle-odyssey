package gr.odyssey.app.encounter

import gr.odyssey.app.hero.HeroNotFoundException
import gr.odyssey.app.monster.MonsterNotFoundException
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.tags.Tags
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping(
    value = ["/encounters"],
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
@Tags(Tag(name = "Encounter"))
class EncounterController(
    private val encounterService: EncounterService,
) {
    @PostMapping
    suspend fun resolveEncounter(
        @RequestBody request: EncounterRequestDTO,
    ): EncounterDTO =
        runCatching {
            encounterService
                .resolve(request.heroId, request.monsterId)
                .toDTO()
        }.recover {
            when (it) {
                is MonsterNotFoundException, is HeroNotFoundException -> {
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
}
