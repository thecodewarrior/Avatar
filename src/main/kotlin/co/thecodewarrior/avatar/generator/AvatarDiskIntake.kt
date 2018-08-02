package co.thecodewarrior.avatar.generator

import co.thecodewarrior.avatar.util.PathBuilder
import co.thecodewarrior.avatar.util.vec
import kotlin.math.pow
import kotlin.math.sqrt

class AvatarDiskIntake(settings: AvatarSettings, val diskMax: Int): AvatarXML(settings) {
    override fun gen() {
        val path = PathBuilder()

        val diskMin = diskMax-diskMax/3

        val outerSemiMajorAxis = diskMax * 6.0
        val outerSemiMinorAxis = sqrt(outerSemiMajorAxis.pow(2) - (outerSemiMajorAxis-diskMax).pow(2))
        val innerSemiMajorAxis = diskMax * 3.0
        val innerSemiMinorAxis = sqrt(innerSemiMajorAxis.pow(2) - (innerSemiMajorAxis-diskMin).pow(2))

        path.add("M", vec(-diskMax, 0))
        path.add("A", outerSemiMajorAxis, outerSemiMinorAxis, 0, 0, 1, vec(outerSemiMajorAxis*2-diskMax, 0))
        path.add("L", vec(innerSemiMajorAxis*2-diskMin, 0))
        path.add("A", innerSemiMajorAxis, innerSemiMinorAxis, 0, 0, 0, vec(-diskMin, 0))
        path.add("L", vec(-diskMax, 0))
        path.add("z")

        println("<path d=\"$path\" fill=\"#fff\"/>")
    }
}
