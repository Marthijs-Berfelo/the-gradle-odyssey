package gr.odyssey.app.hero

data class HeroRequestDTO(
    val name: String,
    val epithet: String,
    val strength: Int,
)

data class HeroDTO(
    val id: Long,
    val name: String,
    val epithet: String,
    val strength: Int,
)
