package gr.odyssey.app.encounter

data class EncounterOutcome(
    val heroId: Long,
    val monsterId: Long,
    val heroWins: Boolean,
)
