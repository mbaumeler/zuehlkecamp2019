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
    private val requestStream = BehaviorSubject.create<String>()
    private val ballStream = BehaviorSubject.createDefault<Ball>(Ball(Point(0.5, 0.5), Vector(4.0, 3.0), System.nanoTime()))

    init {
        val bothSliderPositions =
                Observable.combineLatest<Point, Point, Pair<Point, Point>>(leftPlayer, rightPlayer,
                        BiFunction { left, right -> Pair(left, right) })
        requestStream
                .withLatestFrom<Pair<Point, Point>, GameStateRequest>(bothSliderPositions,
                        BiFunction { id, slidePair -> GameStateRequest(id, slidePair.first, slidePair.second) })
                .withLatestFrom<Ball, GameState>(ballStream,
                        BiFunction { gameStateRequest, ball ->
                            currentGameState(
                                    gameStateRequest.leftSlider,
                                    gameStateRequest.rightSlider,
                                    gameStateRequest.id,
                                    ball)
                        })
                .subscribe {
                    ballStream.onNext(it.ball)
                    template.convertAndSend("/topic/game/" + it.requester, it)
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
        requestStream.onNext(id)
    }

    fun currentGameState(left: Point, right: Point, requester: String, ball: Ball): GameState {
        val updatedBall = updatePosition(ball)
        return GameState(updatedBall, left, right, requester)
    }
}