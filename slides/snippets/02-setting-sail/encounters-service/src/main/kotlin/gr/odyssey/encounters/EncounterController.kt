package gr.odyssey.encounters

import gr.odyssey.api.server.api.EncounterApi
import gr.odyssey.api.server.model.EncounterDTO
import gr.odyssey.api.server.model.EncounterRequestDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.server.ResponseStatusException

@RestController
class EncounterController(
    private val encounterService: EncounterService,
) : EncounterApi {
    override suspend fun resolveEncounter(encounterRequestDTO: EncounterRequestDTO): ResponseEntity<EncounterDTO> =
        runCatching {
            encounterService
                .resolve(encounterRequestDTO.heroId, encounterRequestDTO.monsterId)
                .toDTO()
        }.recover {
            when (it) {
                is WebClientResponseException.NotFound -> {
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
            .let { ResponseEntity.ok(it) }
}
