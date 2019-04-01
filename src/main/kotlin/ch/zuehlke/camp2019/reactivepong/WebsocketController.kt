package ch.zuehlke.camp2019.reactivepong

import org.springframework.stereotype.Controller
import org.springframework.web.util.HtmlUtils
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.handler.annotation.MessageMapping



@Controller
class WebsocketController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    @Throws(Exception::class)
    fun greeting(message: HelloMessage): Greeting {
        return Greeting("Hello, " + HtmlUtils.htmlEscape(message.name) + "!")
    }
}