package gr.odyssey.app.encounter

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    value = ["/encounters"],
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
class EncounterController(
    private val encounterService: EncounterService,
) {
    @PostMapping
    suspend fun resolveEncounter(
        @RequestBody request: EncounterRequestDTO,
    ): EncounterDTO =
        encounterService
            .resolve(request.heroId, request.monsterId)
            .toDTO()
}
