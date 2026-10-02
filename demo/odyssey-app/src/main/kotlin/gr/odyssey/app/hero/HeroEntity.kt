package gr.odyssey.app.hero

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("heroes")
data class HeroEntity(
    @Id val id: Long? = null,
    val name: String,
    val epithet: String,
    val strength: Int,
)
