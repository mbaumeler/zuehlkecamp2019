package ch.zuehlke.camp2019.reactivepong

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller


@Controller
class WebsocketController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    @Throws(Exception::class)
    fun greeting(message: HelloMessage): Greeting {
        return Greeting(message.name)
    }
}