package ch.zuehlke.camp2019.reactivepong

import io.reactivex.Observable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import java.util.concurrent.TimeUnit


@Controller
class WebsocketController constructor(@Autowired val template: SimpMessagingTemplate, @Autowired val positionController: PositionController) {

    init {
        Observable
                .interval(positionController.tickrateInMillis, TimeUnit.MILLISECONDS)
                .subscribe { template.convertAndSend("/topic/game", currentGameState()) }
    }

    @MessageMapping("/move")
    @SendTo("/topic/moves")
    @Throws(Exception::class)
    fun onMoveEvent(moveEvent: MoveEvent): MoveEvent {
        return moveEvent
    }

    fun currentGameState(): GameState {
        positionController.updatePosition()
        val (xPos, yPos) = positionController.position
        return GameState(Ball(xPos, yPos))
    }
}