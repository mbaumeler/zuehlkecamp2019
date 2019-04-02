package ch.zuehlke.camp2019.reactivepong

data class Point(val x: Double, val y: Double) {
    fun add(v: Vector) = Point(x + v.x, y + v.y)
}
