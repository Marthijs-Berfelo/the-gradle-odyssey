package gr.odyssey.app.monster

internal fun MonsterEntity.toDomain() =
    Monster(
        id = id,
        name = name,
        domain = domain,
        danger = danger,
    )

internal fun Monster.toEntity() =
    MonsterEntity(
        id = id,
        name = name,
        domain = domain,
        danger = danger,
    )
