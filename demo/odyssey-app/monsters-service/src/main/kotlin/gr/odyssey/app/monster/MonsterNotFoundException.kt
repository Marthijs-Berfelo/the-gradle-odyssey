package gr.odyssey.app.monster

class MonsterNotFoundException(
    val id: Long,
) : Exception(
        "Monster with id $id not found",
    )
