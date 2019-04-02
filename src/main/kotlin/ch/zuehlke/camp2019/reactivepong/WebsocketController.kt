package ch.zuehlke.camp2019.reactivepong

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller


@Controller
class WebsocketController constructor(@Autowired val template: SimpMessagingTemplate) {

    private val positionController = PositionController()
    private var leftPosition = 0.0
    private var rightPosition = 0.0

    @MessageMapping("/move")
    fun onMoveEvent(moveEvent: MoveEvent) {
        when (moveEvent.side) {
            Side.LEFT -> leftPosition = moveEvent.x
            Side.RIGHT -> rightPosition = moveEvent.x
        }
    }

    @MessageMapping("/requestGameState")
    @SendTo("/topic/game")
    fun onRequestGameState(): GameState {
        return currentGameState()
    }

    fun currentGameState(): GameState {
        positionController.updatePosition()
        val (xPos, yPos) = positionController.position
        return GameState(Ball(xPos, yPos), leftPosition, rightPosition)
    }
}