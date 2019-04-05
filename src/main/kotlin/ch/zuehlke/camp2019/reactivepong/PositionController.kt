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

    if (isPaddleTouched(newPosition, leftSlider, rightSlider)) {
        return Ball(
                Point(restrictPositionX(newPosition.x), restrictPositionY(newPosition.y)),
                updateVelocity(newPosition, ball.velocity),
                currentTime)
    }

    if (newPosition.x < 0 - ballWidth || newPosition.x > 400 || newPosition.y < 0 - ballWidth || newPosition.y > 100) {
        throw IllegalStateException("You lose")
    }

    return Ball(
            Point(newPosition.x, newPosition.y), ball.velocity, currentTime)
}

private fun isPaddleTouched(newPosition: Point, leftSlider: Point, rightSlider: Point): Boolean {

    val ellipse2d = Ellipse2D.Double(newPosition.x, newPosition.y, ballWidth, ballWidth)
    if (newPosition.x <= 200) {
        if (intersects(ellipse2d, leftSlider, true)) {
            return true
        }

    } else {
        if (intersects(ellipse2d, rightSlider, false)) {
            return true
        }
    }
    return false
}

private fun intersects(ellipse2d: Ellipse2D.Double, slider: Point, isLeft: Boolean): Boolean {
    val sidePosition = if (isLeft) 0.0 else 400 - paddleHeight
    val touchesPaddle =
            ellipse2d.intersects(slider.x, 0.0, paddleWidth, paddleHeight)
                    || ellipse2d.intersects(slider.x, 100 - paddleHeight, paddleWidth, paddleHeight)
                    || ellipse2d.intersects(sidePosition, slider.y, paddleHeight, paddleWidth)
    return touchesPaddle
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
    coord < paddleHeight -> 2 * paddleHeight - coord
    coord > 400 - paddleHeight - ballWidth -> 800 - 2 * paddleHeight - coord - 2 * ballWidth
    else -> coord
}

private fun restrictPositionY(coord: Double) = when {
    coord < paddleHeight -> 2 * paddleHeight - coord
    coord > 100 - paddleHeight - ballWidth -> 200 - 2 * paddleHeight - coord - 2 * ballWidth
    else -> coord
}

private fun velocityMultiplierX(coord: Double) = when {
    coord < paddleHeight || coord > 400 - paddleHeight - ballWidth -> -1.0 + (Random.nextDouble() * 0.1 - 0.05)
    else -> 1.0
}

private fun velocityMultiplierY(coord: Double) = when {
    coord < paddleHeight || coord > 100 - paddleHeight - ballWidth -> -1.0 + (Random.nextDouble() * 0.1 - 0.05)
    else -> 1.0
}