package ch.zuehlke.camp2019.reactivepong

import kotlin.random.Random


private val nanosPerSecond = 1_000_000_000.0
private val nominalVelocity = 1.0

private val maxVelocityDifference = 0.25
private val minVelocity = nominalVelocity - (maxVelocityDifference * nominalVelocity)
private val maxVelocity = nominalVelocity + (maxVelocityDifference * nominalVelocity)

fun updatePosition(ball: Ball): Ball {
    val currentTime = System.nanoTime()
    val timeDelta = currentTime - ball.lastUpdate
    val newPosition = ball.position.add(ball.velocity.scale(timeDelta / nanosPerSecond))
    return Ball(
            Point(restrictPosition(newPosition.x), restrictPosition(newPosition.y)),
            updateVelocity(newPosition, ball.velocity),
            currentTime
    )
}

private fun updateVelocity(updatedPosition: Point, velocity: Vector): Vector {
    val xVelocity = velocity.x * velocityMultiplier(updatedPosition.x)
    val yVelocity = velocity.y * velocityMultiplier(updatedPosition.y)
    val updatedVelocity = Vector(xVelocity, yVelocity)
    if (updatedVelocity.length() < minVelocity || updatedVelocity.length() > maxVelocity) {
        return updatedVelocity.scale(nominalVelocity / updatedVelocity.length())
    }
    return updatedVelocity
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