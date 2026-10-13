package gr.odyssey.app.encounter

import gr.odyssey.api.server.EncountersApi
import gr.odyssey.api.server.model.EncounterDTO
import gr.odyssey.api.server.model.EncounterRequestDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.server.ResponseStatusException

@RestController
class EncounterController(
    private val encounterService: EncounterService,
) : EncountersApi {
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

                is WebClientResponseException.BadGateway,
                is WebClientResponseException.ServiceUnavailable,
                is WebClientResponseException.InternalServerError,
                -> {
                    throw ResponseStatusException(
                        HttpStatus.BAD_GATEWAY,
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
}
