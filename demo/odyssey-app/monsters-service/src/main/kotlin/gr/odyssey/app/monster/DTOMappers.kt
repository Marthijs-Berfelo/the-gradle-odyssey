package gr.odyssey.app.monster

import gr.odyssey.api.server.model.MonsterDTO
import gr.odyssey.api.server.model.MonsterRequestDTO

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
