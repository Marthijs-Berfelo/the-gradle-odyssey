package gr.odyssey.heroes

import gr.odyssey.api.server.model.HeroDTO
import gr.odyssey.api.server.model.HeroRequestDTO

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
