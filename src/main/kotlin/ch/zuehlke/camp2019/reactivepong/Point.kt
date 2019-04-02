package ch.zuehlke.camp2019.reactivepong

data class Point(val x: Double, val y: Double)

fun add(p: Point, v: Vector) = Point(p.x + v.x, p.y + v.y)