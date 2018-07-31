package co.thecodewarrior.avatar


data class AvatarSettings(
        var viewBox: Rect2d = Rect2d(vec(-100, -100), vec(100, 100)),
        var resolution: Vec2d = vec(500, 500),
        var enableJets: Boolean = true,
        var enableHalo: Boolean = false,
        var heightRatio: Double = 2/9.0
)

class AvatarSettingsManager {
init {}

}
