package gr.odyssey.app.monster

internal fun MonsterRequestDTO.toDomain() =
    Monster(
        name = name,
        domain = domain,
        danger = danger,
    )

internal fun Monster.toDTO() =
    MonsterDTO(
        id = requireNotNull(id),
        name = name,
        domain = domain,
        danger = danger,
    )
