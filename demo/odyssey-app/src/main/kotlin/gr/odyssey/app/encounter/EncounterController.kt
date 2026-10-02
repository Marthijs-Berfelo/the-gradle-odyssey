package gr.odyssey.app.encounter

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class EncounterController(
    private val encounterService: EncounterService,
) {
    @PostMapping("/encounters")
    suspend fun resolveEncounter(
        @RequestBody request: EncounterRequest,
    ): EncounterResponse = encounterService.resolve(request.heroId, request.monsterId).toResponse()
}
