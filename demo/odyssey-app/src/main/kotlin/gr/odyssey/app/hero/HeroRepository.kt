package gr.odyssey.app.hero

import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface HeroRepository : CoroutineCrudRepository<HeroEntity, Long>
