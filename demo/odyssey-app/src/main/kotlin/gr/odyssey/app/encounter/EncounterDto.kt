package gr.odyssey.app.encounter

data class EncounterRequestDTO(
    val heroId: Long,
    val monsterId: Long,
)

data class EncounterDTO(
    val heroId: Long,
    val monsterId: Long,
    val outcome: String,
)

internal fun EncounterOutcome.toDTO() =
    EncounterDTO(
        heroId = heroId,
        monsterId = monsterId,
        outcome = if (heroWins) "hero wins" else "monster wins",
    )
