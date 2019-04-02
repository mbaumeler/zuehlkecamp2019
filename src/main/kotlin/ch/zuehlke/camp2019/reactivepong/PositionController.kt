package ch.zuehlke.camp2019.reactivepong

import kotlin.random.Random

class PositionController {

    private val nanosPerSecond = 1_000_000_000.0
    private var lastUpdate = System.nanoTime()
    private var velocity = Vector(30.0, 20.0)
    var position = Point(0.5, 0.5)

    fun updatePosition() {
        val currentTime = System.nanoTime()
        val timeDelta = currentTime - lastUpdate
        lastUpdate = currentTime
        val newPosition = position.add(velocity.scale(timeDelta / nanosPerSecond))
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