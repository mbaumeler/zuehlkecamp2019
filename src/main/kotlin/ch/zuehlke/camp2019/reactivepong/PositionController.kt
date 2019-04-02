package ch.zuehlke.camp2019.reactivepong

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.MonoSink
import reactor.core.publisher.SynchronousSink
import java.time.Duration
import kotlin.random.Random

@RestController
class PositionController {

    private var velocity = Vector(30.0, 20.0)
    val tickrateInMillis: Long = 10
    var position = Point(0.5, 0.5)

    @GetMapping("/position")
    fun position() =
            Flux.generate { sink: SynchronousSink<Point> ->
                run {
                    updatePosition()
                    sink.next(position)
                }
            }.delayElements(Duration.ofMillis(tickrateInMillis))

    fun updatePosition() {
        val newPosition = position.add(velocity.scale(tickrateInMillis / 1000.0))
        updateVelocity(newPosition)
        position = Point(restrictPosition(newPosition.x), restrictPosition(newPosition.y))
    }

    private fun updateVelocity(updatedPosition: Point) {
        val xVelocity = velocity.x * velocityMultiplier(updatedPosition.x)
        val yVelocity = velocity.y * velocityMultiplier(updatedPosition.y)
        velocity = Vector(xVelocity, yVelocity)
    }

    private fun restrictPosition(coord: Double) = when {
        coord < 0 -> -coord
        coord > 100 -> 200 - coord // = 100 - (coord - 100) = 100 - coord + 100
        else -> coord
    }

    private fun velocityMultiplier(coord: Double) = when {
        coord < 0 || coord > 100 -> -1.0 + (Random.nextDouble() * 0.1 - 0.05)
        else -> 1.0
    }
}