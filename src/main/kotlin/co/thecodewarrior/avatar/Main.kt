package co.thecodewarrior.avatar

import kotlin.browser.document
import kotlin.browser.window

val settings = AvatarSettings()

fun main(args: Array<String>) {
    window.onload = {
        val container = document.getElementById("preview")!!
        container.innerHTML = Container(settings).toString()
        null
    }
}