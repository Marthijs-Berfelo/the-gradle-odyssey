package gr.odyssey.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OdysseyApplication

fun main(args: Array<String>) {
    runApplication<OdysseyApplication>(*args)
}
