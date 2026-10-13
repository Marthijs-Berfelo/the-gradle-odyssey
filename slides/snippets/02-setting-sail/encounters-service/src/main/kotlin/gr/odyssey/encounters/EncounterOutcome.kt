package gr.odyssey.encounters

data class EncounterOutcome(
    val heroId: Long,
    val monsterId: Long,
    val heroWins: Boolean,
)
