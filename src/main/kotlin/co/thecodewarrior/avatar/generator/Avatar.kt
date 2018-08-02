package co.thecodewarrior.avatar.generator

class Avatar(settings: AvatarSettings): AvatarXML(settings) {

    val blackHoleRadius = 30
    val haloSize = 10
    val diskRadius = 100

    override fun gen() {
        println(""" <g id="avatar" transform="rotate(-45)"> """)
        if(settings.features.contains(AvatarFeature.HALO)) {
            println(""" <circle id="halo" cx="0" cy="0" r="${blackHoleRadius + haloSize}" fill="#FFF"/> """)
        }

        println("""<circle id="event-horizon-back" cx="0" cy="0" r="$blackHoleRadius" fill="#000"/>""")

        println("""<g transform="scale(1 ${settings.heightRatio})">""")
        println("""<circle id="accretion-disk" cx="0" cy="0" r="$diskRadius" fill="#FFF"/>""")

        if(settings.features.contains(AvatarFeature.INTAKE)) {
            println(AvatarDiskIntake(settings, diskRadius).toString())
        }

        println("""</g>""")

        println("""<path id="event-horizon-front" d="
            M -$blackHoleRadius,0
            A 1,1 0 0,1 $blackHoleRadius,0
            A 1,${settings.heightRatio} 0 0,1 -$blackHoleRadius,0
        " fill="#000"/>""")

        if(settings.features.contains(AvatarFeature.JETS)) {
            println(AvatarJets(settings, blackHoleRadius).toString())
        }

        println("</g>")
    }
}

