package gr.odyssey.app.hero

internal fun HeroEntity.toDomain() =
    Hero(
        id = id,
        name = name,
        epithet = epithet,
        strength = strength,
    )

internal fun Hero.toEntity() =
    HeroEntity(
        id = id,
        name = name,
        epithet = epithet,
        strength = strength,
    )
