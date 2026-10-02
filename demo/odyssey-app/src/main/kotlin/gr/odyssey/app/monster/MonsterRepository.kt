package gr.odyssey.app.monster

import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface MonsterRepository : CoroutineCrudRepository<MonsterEntity, Long>
