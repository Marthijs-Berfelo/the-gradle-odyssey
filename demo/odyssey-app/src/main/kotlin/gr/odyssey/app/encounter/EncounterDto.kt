package gr.odyssey.app.encounter

data class EncounterRequest(
    val heroId: Long,
    val monsterId: Long,
)

data class EncounterResponse(
    val heroId: Long,
    val monsterId: Long,
    val outcome: String,
)

fun EncounterOutcome.toResponse() =
    EncounterResponse(
        heroId = heroId,
        monsterId = monsterId,
        outcome = if (heroWins) "hero wins" else "monster wins",
    )
