package co.thecodewarrior.avatar.generator

class Container(settings: AvatarSettings): AvatarXML(settings) {
    val avatar = Avatar(settings)

    override fun gen() {
        println("")
        println("""
<svg id="main" version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" \
x="0px" y="0px" width="${settings.resolution.x}px" height="${settings.resolution.y}px" \
viewBox="${settings.viewBox.min.x} ${settings.viewBox.min.y} ${settings.viewBox.width} ${settings.viewBox.height}" \
xml:space="preserve">
                """.prepXML())
        println(avatar.toString())
        println("</svg>")
    }
}
