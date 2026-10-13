package gr.odyssey.app.monster

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("monsters")
data class MonsterEntity(
    @Id val id: Long? = null,
    val name: String,
    val domain: String,
    val danger: Int,
)
