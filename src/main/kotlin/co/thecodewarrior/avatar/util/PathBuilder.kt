package co.thecodewarrior.avatar.util

class PathBuilder {
    private val path = ArrayList<Any>()

    fun add(command: String, vararg args: Any) {
        path.add(command)
        path.addAll(args)
    }

    fun build(): String {
        return path.joinToString(" ") { (it as? Vec2d)?.let { "${it.x},${it.y}" } ?: it.toString() }
    }

    override fun toString(): String {
        return build()
    }
}
