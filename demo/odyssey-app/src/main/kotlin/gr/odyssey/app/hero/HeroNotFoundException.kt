package gr.odyssey.app.hero

class HeroNotFoundException(
    val id: Long,
) : Exception(
        "Hero with id $id not found",
    )
