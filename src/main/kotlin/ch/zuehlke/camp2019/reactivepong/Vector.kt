package ch.zuehlke.camp2019.reactivepong

import kotlin.math.sqrt

data class Vector(val x: Double, val y: Double) {

    fun scale(factor: Double) = Vector(sqrt(factor) * x, sqrt(factor) * y)

    fun length() = sqrt(x * x + y * y)

}
