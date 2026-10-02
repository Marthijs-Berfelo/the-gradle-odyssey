package gr.odyssey.app.monster

data class MonsterRequestDTO(
    val name: String,
    val domain: String,
    val danger: Int,
)

data class MonsterDTO(
    val id: Long,
    val name: String,
    val domain: String,
    val danger: Int,
)
