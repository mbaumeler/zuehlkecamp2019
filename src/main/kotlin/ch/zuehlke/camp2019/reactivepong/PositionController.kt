package ch.zuehlke.camp2019.reactivepong

import java.awt.geom.Ellipse2D
import kotlin.random.Random

private val ballWidth = 8.0
private val paddleWidth = 14.0
private val paddleHeight = 3.0

private val nanosPerSecond = 1_000_000_000.0
private val nominalVelocity = 4.0

private val maxVelocityDifference = 0.25
private val minVelocity = nominalVelocity - (maxVelocityDifference * nominalVelocity)
private val maxVelocity = nominalVelocity + (maxVelocityDifference * nominalVelocity)

fun updatePosition(ball: Ball, leftSlider: Point, rightSlider: Point): Ball {
    val currentTime = System.nanoTime()
    val timeDelta = currentTime - ball.lastUpdate
    val newPosition = ball.position.add(ball.velocity.scale(timeDelta / nanosPerSecond))

    val ellipse2d = Ellipse2D.Double(ball.position.x, ball.position.y, ballWidth, ballWidth)

    if (ball.position.x <= 200) {
        val intersects =
                ellipse2d.intersects(leftSlider.x, paddleHeight, leftSlider.x + paddleWidth, paddleHeight)
                        || ellipse2d.intersects(leftSlider.x, 100 - paddleHeight, leftSlider.x + paddleWidth, 100 - paddleHeight)
                        || ellipse2d.intersects(paddleHeight, leftSlider.y, paddleHeight, leftSlider.y + paddleWidth)

        if (intersects) {
            println("Yes Left")
        }
    } else {

    }

    return Ball(
            Point(restrictPositionX(newPosition.x), restrictPositionY(newPosition.y)),
            updateVelocity(newPosition, ball.velocity),
            currentTime
    )
}

private fun updateVelocity(updatedPosition: Point, velocity: Vector): Vector {
    val xVelocity = velocity.x * velocityMultiplierX(updatedPosition.x)
    val yVelocity = velocity.y * velocityMultiplierY(updatedPosition.y)
    val updatedVelocity = Vector(xVelocity, yVelocity)
    if (updatedVelocity.length() < minVelocity || updatedVelocity.length() > maxVelocity) {
        return updatedVelocity.scale(nominalVelocity / updatedVelocity.length())
    }
    return updatedVelocity
}

private fun restrictPositionX(coord: Double) = when {
    coord < 0 -> -coord
    coord > 400 -> 800 - coord
    else -> coord
}

private fun restrictPositionY(coord: Double) = when {
    coord < 0 -> -coord
    coord > 100 -> 200 - coord
    else -> coord
}

private fun velocityMultiplierX(coord: Double) = when {
    coord < 0 || coord > 400 -> -1.0 + (Random.nextDouble() * 0.1 - 0.05)
    else -> 1.0
}

private fun velocityMultiplierY(coord: Double) = when {
    coord < 0 || coord > 100 -> -1.0 + (Random.nextDouble() * 0.1 - 0.05)
    else -> 1.0
}