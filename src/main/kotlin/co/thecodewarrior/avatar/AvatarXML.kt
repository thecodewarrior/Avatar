package co.thecodewarrior.avatar

abstract class AvatarXML(val settings: AvatarSettings) {
    private val lines: MutableList<String> = ArrayList()

    fun println(line: String) {
        lines.add(line.prepXML())
    }
    fun print(str: Any) {
        lines[lines.indices.last] = lines[lines.indices.last] + str.toString()
    }

    abstract fun gen()

    override fun toString(): String {
        lines.clear()
        gen()
        return lines.joinToString("\n")
    }

    protected fun String.prepXML(): String {
        return this.trimIndent().replace("\\\\\n\\s*".toRegex(), "").replace('\n', ' ')
    }
}