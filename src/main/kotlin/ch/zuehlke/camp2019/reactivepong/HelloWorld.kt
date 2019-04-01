package ch.zuehlke.camp2019.reactivepong

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/")
class HelloWorld {

    @GetMapping("hello")
    fun hello(): String {
        return "Hello, World"
    }
}