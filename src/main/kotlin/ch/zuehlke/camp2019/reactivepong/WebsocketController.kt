package ch.zuehlke.camp2019.reactivepong

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller


@Controller
class WebsocketController constructor(@Autowired val template: SimpMessagingTemplate) {

    private val leftPlayer = BehaviorSubject.createDefault(Point(0.0, 0.0))
    private val rightPlayer = BehaviorSubject.createDefault(Point(0.0, 0.0))

    private val bothSliderPositions = Observable.combineLatest<Point, Point, Pair<Point, Point>>(leftPlayer, rightPlayer, BiFunction { left, right -> Pair(left, right) })

    private val requestGameStateStream = BehaviorSubject.create<String>()

    private val positionController = PositionController()

    init {
        requestGameStateStream
                .withLatestFrom<Pair<Point, Point>, GameStateRequest>(bothSliderPositions, BiFunction<String, Pair<Point, Point>, GameStateRequest> { id, slidePair -> GameStateRequest(id, slidePair.first, slidePair.second) })
                .subscribe {
                    template.convertAndSend("/topic/game/" + it.id, currentGameState(it.leftSlider, it.rightSlider))
                }
    }

    @MessageMapping("/move/LEFT")
    fun onMoveEventLeft(point: Point) {
        leftPlayer.onNext(point)
    }

    @MessageMapping("/move/RIGHT")
    fun onMoveEventRight(point: Point) {
        rightPlayer.onNext(point)
    }

    @MessageMapping("/requestGameState")
    fun onRequestGameState(id: String) {
        requestGameStateStream.onNext(id)
    }

    fun currentGameState(left: Point, right: Point): GameState {
        positionController.updatePosition()
        val (xPos, yPos) = positionController.position
        return GameState(Ball(xPos, yPos), left, right)
    }
}