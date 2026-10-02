package gr.odyssey.app.monster

data class Monster(
    val id: Long? = null,
    val name: String,
    val domain: String,
    val danger: Int,
)
