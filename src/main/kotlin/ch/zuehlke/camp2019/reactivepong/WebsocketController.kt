package ch.zuehlke.camp2019.reactivepong

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.ReplaySubject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller


@Controller
class WebsocketController constructor(@Autowired val template: SimpMessagingTemplate) {

    private val initialPosition = Point(0.0, 0.0)

    private val joinBuffer = ReplaySubject.create<String>(20);
    private val leftPlayer = BehaviorSubject.createDefault(initialPosition)
    private val rightPlayer = BehaviorSubject.createDefault(initialPosition)
    private val requestStream = BehaviorSubject.create<String>()
    private val ballStream = BehaviorSubject.createDefault<Ball>(Ball(Point(0.5, 0.5), Vector(4.0, 3.0), System.nanoTime()))

    init {
        leftPlayer.zipWith<Point, Point>(rightPlayer, BiFunction { a, b -> b })
                .skip(1)
                .take(1)
                .subscribe {
                    val bothSliderPositions =
                            Observable.combineLatest<Point, Point, Pair<Point, Point>>(leftPlayer, rightPlayer,
                                    BiFunction { left, right -> Pair(left, right) })
                    requestStream
                            .withLatestFrom<Pair<Point, Point>, GameStateRequest>(bothSliderPositions,
                                    BiFunction { id, slidePair -> GameStateRequest(id, slidePair.first, slidePair.second) })
                            .withLatestFrom<Ball, GameState>(ballStream,
                                    BiFunction { gameStateRequest, ball ->
                                        GameState(
                                                updatePosition(ball),
                                                gameStateRequest.leftSlider,
                                                gameStateRequest.rightSlider,
                                                gameStateRequest.id)
                                    })
                            .subscribe {
                                ballStream.onNext(it.ball)
                                template.convertAndSend("/topic/game/" + it.requester, it)
                            }

                    joinBuffer.subscribe {
                        onRequestGameState(it)
                    }

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

    @MessageMapping("/join")
    fun join(id: String) {
        joinBuffer.onNext(id)
    }
}