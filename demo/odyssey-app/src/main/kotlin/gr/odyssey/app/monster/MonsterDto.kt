package gr.odyssey.app.monster

data class CreateMonsterRequest(
    val name: String,
    val domain: String,
    val danger: Int,
)

data class MonsterResponse(
    val id: Long,
    val name: String,
    val domain: String,
    val danger: Int,
)

fun Monster.toResponse() = MonsterResponse(id = requireNotNull(id), name = name, domain = domain, danger = danger)
