package ch.zuehlke.camp2019.reactivepong

import kotlin.math.sqrt

data class Vector(val x: Double, val y: Double)

fun scale(v: Vector, factor: Double) = Vector(sqrt(factor) * v.x, sqrt(factor) * v.y)