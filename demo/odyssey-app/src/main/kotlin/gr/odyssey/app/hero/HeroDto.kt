package gr.odyssey.app.hero

data class CreateHeroRequest(
    val name: String,
    val epithet: String,
    val strength: Int,
)

data class HeroResponse(
    val id: Long,
    val name: String,
    val epithet: String,
    val strength: Int,
)

fun Hero.toResponse() = HeroResponse(id = requireNotNull(id), name = name, epithet = epithet, strength = strength)
