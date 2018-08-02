package co.thecodewarrior.avatar.generator

import co.thecodewarrior.avatar.util.Rect2d
import co.thecodewarrior.avatar.util.Vec2d
import co.thecodewarrior.avatar.util.vec

data class AvatarSettings(
        var viewBox: Rect2d = Rect2d(vec(-100, -100), vec(100, 100)),
        var resolution: Vec2d = vec(500, 500),
        var features: MutableSet<AvatarFeature> = HashSet(),
        var heightRatio: Double = 2/9.0
)

enum class AvatarFeature(val description: String, val default: Boolean = false) {
    JETS("Relativistic jets"),
    HALO("Distorted accretion disk", true),
    INTAKE("Accretion disk intake")
}
