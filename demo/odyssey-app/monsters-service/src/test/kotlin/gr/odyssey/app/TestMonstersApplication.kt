package gr.odyssey.app

import org.springframework.boot.fromApplication
import org.springframework.boot.with

fun main(args: Array<String>) {
    fromApplication<MonstersApplication>().with(TestcontainersConfiguration::class).run(*args)
}
