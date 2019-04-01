package ch.zuehlke.camp2019.reactivepong

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ReactivepongApplication

fun main(args: Array<String>) {
	runApplication<ReactivepongApplication>(*args)
}
