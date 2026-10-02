package gr.odyssey.app.monster

data class Monster(
    val id: Long?,
    val name: String,
    val domain: String,
    val danger: Int,
)

fun MonsterEntity.toDomain() = Monster(id = id, name = name, domain = domain, danger = danger)

fun Monster.toEntity() = MonsterEntity(id = id, name = name, domain = domain, danger = danger)
