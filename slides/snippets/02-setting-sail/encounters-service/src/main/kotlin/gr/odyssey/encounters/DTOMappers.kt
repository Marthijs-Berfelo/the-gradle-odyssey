package gr.odyssey.encounters

import gr.odyssey.api.server.model.EncounterDTO

internal fun EncounterOutcome.toDTO() =
    EncounterDTO(
        heroId = heroId,
        monsterId = monsterId,
        outcome = if (heroWins) "hero wins" else "monster wins",
    )
