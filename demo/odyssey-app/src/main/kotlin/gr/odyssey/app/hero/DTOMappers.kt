package gr.odyssey.app.hero

internal fun HeroRequestDTO.toDomain() =
    Hero(
        name = name,
        epithet = epithet,
        strength = strength,
    )

internal fun Hero.toDTO() =
    HeroDTO(
        id = requireNotNull(id),
        name = name,
        epithet = epithet,
        strength = strength,
    )
