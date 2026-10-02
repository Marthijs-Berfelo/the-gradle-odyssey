package gr.odyssey.app.hero

data class Hero(
    val id: Long?,
    val name: String,
    val epithet: String,
    val strength: Int,
)

fun HeroEntity.toDomain() = Hero(id = id, name = name, epithet = epithet, strength = strength)

fun Hero.toEntity() = HeroEntity(id = id, name = name, epithet = epithet, strength = strength)
