package dev.thecodewarrior.avatar.generator

class SvgGenerator {
    fun generate(): String {

        return """
<svg id="main-svg" version="1.1"
    xmlns="http://www.w3.org/2000/svg"
    xmlns:xlink="http://www.w3.org/1999/xlink" x="0px" y="0px" width="1000px" height="1000px" viewBox="-500 -500 1000 1000" xml:space="preserve">
    <rect id="background" x="-10000" y="-10000" width="20000" height="20000" fill="#FF0000"></rect>
    <g id="main-avatar" transform="rotate(-45)">
        <circle id="event-horizon-back" cx="0" cy="0" r="30" fill="#000"></circle>
        <ellipse id="accretion-disk" cx="0" cy="0" rx="90" ry="20" fill="#FFF" data-animate-wipe-duration="4" data-animate-wipe-phase="-90"></ellipse>
        <g id="event-horizon-front">
            <path d="
				M -30,0
				A 1,1 0 0,1 30,0
				A 9,2 0 0,1 -30,0
				" fill="#000"></path>
        </g>
        <g id="astrophysical-jets">
            <path d="M -14.999999999999998 25.98076211353316 A 9 2 0 0 0 14.999999999999998 25.98076211353316 Q 4.986682044916573 43.32433756407934 5.013333333333334 83.30127018922192 L 6.013333333333334 1583.301270189222 L -6.013333333333334 1583.301270189222 L -5.013333333333334 83.30127018922192 Q -4.986682044916573 43.32433756407934 -14.999999999999998 25.98076211353316" fill="#fff"></path>
            <path d="M -14.999999999999998 -25.98076211353316 A 9 2 0 0 0 14.999999999999998 -25.98076211353316 Q 4.986682044916573 -43.32433756407934 5.013333333333334 -83.30127018922192 L 6.013333333333334 -1583.301270189222 L -6.013333333333334 -1583.301270189222 L -5.013333333333334 -83.30127018922192 Q -4.986682044916573 -43.32433756407934 -14.999999999999998 -25.98076211353316" fill="#fff"></path>
        </g>
    </g>
    <g id="orbits" transform="rotate(-45) scale(1 0.22222222)">
        <g id="orbit-1">
            <circle id="orbit-1-circle" cx="0" cy="0" r="250" fill="none" stroke-dasharray="8.726646259971647" stroke-dashoffset="4.363323129985823" stroke="#FFF" stroke-width="2"></circle>
            <g transform="rotate(-67.5)">
                <g transform="translate(250 0)">
                    <g transform="rotate(67.5)">
                        <g transform="scale(1 4.5)">
                            <circle id="orbit-1-body" cx="0" cy="0" r="6" fill="#FFF"></circle>
                        </g>
                    </g>
                </g>
            </g>
        </g>
        <g id="orbit-2">
            <circle id="orbit-2-circle" cx="0" cy="0" r="396.85026299204986" fill="none" stroke-dasharray="13.852687453233347" stroke-dashoffset="6.926343726616674" stroke="#FFF" stroke-width="2"></circle>
            <g transform="rotate(56.25)">
                <g transform="translate(396.85026299204986 0)">
                    <g transform="rotate(-56.25)">
                        <g transform="scale(1 4.5)">
                            <circle id="orbit-2-body" cx="0" cy="0" r="6" fill="#FFF"></circle>
                        </g>
                    </g>
                </g>
            </g>
        </g>
        <g id="orbit-3">
            <circle id="orbit-3-circle" cx="0" cy="0" r="629.9605249474366" fill="none" stroke-dasharray="21.989770635849293" stroke-dashoffset="10.994885317924647" stroke="#FFF" stroke-width="2"></circle>
            <g transform="rotate(247.5)">
                <g transform="translate(629.9605249474366 0)">
                    <g transform="rotate(-247.5)">
                        <g transform="scale(1 4.5)">
                            <circle id="orbit-3-body" cx="0" cy="0" r="8" fill="#FFF"></circle>
                        </g>
                    </g>
                </g>
            </g>
        </g>
    </g>
</svg>
""".trimIndent()
    }
}