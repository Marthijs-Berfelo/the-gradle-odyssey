package gr.odyssey.app.encounter

import gr.odyssey.app.hero.HeroService
import gr.odyssey.app.monster.MonsterService
import org.springframework.stereotype.Service

@Service
class EncounterService(
    private val heroService: HeroService,
    private val monsterService: MonsterService,
) {
    suspend fun resolve(
        heroId: Long,
        monsterId: Long,
    ): EncounterOutcome {
        val hero = heroService.getHero(heroId)
        val monster = monsterService.getMonster(monsterId)

        return EncounterOutcome(
            heroId = requireNotNull(hero.id),
            monsterId = requireNotNull(monster.id),
            heroWins = hero.strength >= monster.danger,
        )
    }
}
