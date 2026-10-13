package gr.odyssey.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EncountersApplication

fun main(args: Array<String>) {
    runApplication<EncountersApplication>(*args)
}
