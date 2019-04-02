package ch.zuehlke.camp2019.reactivepong

import io.reactivex.Observable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import java.util.concurrent.TimeUnit


@Controller
class WebsocketController constructor(@Autowired val template: SimpMessagingTemplate) {

    init {
        Observable
                .interval(1000 / 60, TimeUnit.MILLISECONDS)
                .subscribe { template.convertAndSend("/topic/game", GameState(Ball((it % 100).toDouble(), 50.0))) }
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    @Throws(Exception::class)
    fun greeting(message: HelloMessage): Greeting {
        return Greeting(message.name)
    }
}