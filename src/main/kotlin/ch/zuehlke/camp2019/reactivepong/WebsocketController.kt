package ch.zuehlke.camp2019.reactivepong

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller


@Controller
class WebsocketController constructor(@Autowired val template: SimpMessagingTemplate) {

    private val positionController = PositionController()
    private var leftPosition = Point(0.0, 0.0)
    private var rightPosition = Point(0.0, 0.0)

    @MessageMapping("/move/LEFT")
    fun onMoveEventLeft(point: Point) {
        leftPosition = point
    }

    @MessageMapping("/move/RIGHT")
    fun onMoveEventRight(point: Point) {
        rightPosition = point
    }

    @MessageMapping("/requestGameState")
    fun onRequestGameState(id: String) {
        template.convertAndSend("/topic/game/" + id, currentGameState())
    }

    fun currentGameState(): GameState {
        positionController.updatePosition()
        val (xPos, yPos) = positionController.position
        return GameState(Ball(xPos, yPos), leftPosition, rightPosition)
    }
}