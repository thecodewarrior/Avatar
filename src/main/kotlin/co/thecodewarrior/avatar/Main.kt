package co.thecodewarrior.avatar

import co.thecodewarrior.avatar.ui.AvatarSettingsUI
import kotlin.browser.window

fun main(args: Array<String>) {
    window.onload = {
        AvatarSettingsUI
    }
}
