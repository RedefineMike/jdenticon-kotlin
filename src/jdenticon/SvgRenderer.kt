package jdenticon

class SvgRenderer(val target: SvgWriter) :Renderer {

    val pathsByColor = HashMap<String, SvgPath>()
    val size = target.size

    lateinit var _path: SvgPath

    override fun setBackground(fillColor: String) {
        val re = Regex("^(#......)(..)?")

        val opacityMatch = re.matchEntire(fillColor)?.groups?.get(2)?.value
        val opacity = opacityMatch?.let {
            opacityMatch.toInt(16) / 255f
        } ?: 1f

        val colorMatch = re.matchEntire(fillColor)?.groups?.get(1)?.value
        val color = colorMatch?.let {
            colorMatch
        } ?: "000000"

        this.target.setBackground(color, opacity)
    }

    override fun beginShape(color: String) {
        if (this.pathsByColor[color] == null) {
            this.pathsByColor[color] = SvgPath()
        }
        this._path = this.pathsByColor[color]!!
    }

    override fun endShape() {

    }

    override fun addPolygon(points: List<Point>) {
        this._path.addPolygon(points)
    }

    override fun addCircle(point: Point, diameter: Float, counterClockwise: Boolean) {
        this._path.addCircle(point, diameter, counterClockwise)
    }

    override fun finish() {
        for (color in this.pathsByColor.keys) {
            this.target.append(color, this.pathsByColor[color]!!.dataString)
        }
    }
}