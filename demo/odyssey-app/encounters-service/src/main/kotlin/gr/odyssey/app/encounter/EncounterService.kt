package gr.odyssey.app.encounter

import gr.odyssey.api.client.HeroApi
import gr.odyssey.api.client.MonsterApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service

@Service
class EncounterService(
    private val heroService: HeroApi,
    private val monsterService: MonsterApi,
) {
    suspend fun resolve(
        heroId: Long,
        monsterId: Long,
    ): EncounterOutcome =
        coroutineScope {
            val heroDeferred = async { heroService.getHeroById(heroId) }
            val monsterDeferred = async { monsterService.getMonsterById(monsterId) }

            val hero = heroDeferred.await().body
            val monster = monsterDeferred.await().body

            EncounterOutcome(
                heroId = requireNotNull(hero?.id),
                monsterId = requireNotNull(monster?.id),
                heroWins = hero.strength >= monster.danger,
            )
        }
}
