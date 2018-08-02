package co.thecodewarrior.avatar.ui

import JQuery
import co.thecodewarrior.avatar.generator.AvatarFeature
import co.thecodewarrior.avatar.generator.AvatarSettings
import co.thecodewarrior.avatar.generator.Container
import co.thecodewarrior.avatar.util.Rect2d
import co.thecodewarrior.avatar.util.jq
import co.thecodewarrior.avatar.util.value
import co.thecodewarrior.avatar.util.vec
import org.w3c.dom.HTMLElement

object AvatarSettingsUI {
    val settings = AvatarSettings()

    init {
        Bounds
        Features
        jq("#load-preview").click {
            loadPreview()
        }
    }

    private fun loadPreview() {
        val container = jq("#preview")[0]!!
        container.innerHTML = Container(settings).toString()
    }

    /* =================================================== Bounds =================================================== */
    private object Bounds {
        private val presetField = jq("#bounds-preset")
        private val xField = jq("#bounds-x")
        private val yField = jq("#bounds-y")
        private val resolutionField = jq("#resolution")

        init {
            selectBounds()
            readBounds()
            presetField.change {
                selectBounds()
                readBounds()
                Any()
            }
            xField.change {
                readBounds()
                presetField.value("")
            }
            yField.change {
                readBounds()
                presetField.value("")
            }
            resolutionField.change {
                readBounds()
                presetField.value("")
            }
        }

        private fun selectBounds() {
            val selected = (presetField.value as String).split(' ').mapNotNull { it.toIntOrNull() }

            if (selected.size == 3) {
                xField.value(selected[0])
                yField.value(selected[1])
                resolutionField.value(selected[2])
            }
        }

        private fun readBounds() {
            val x = (xField.value as String).toIntOrNull() ?: 100
            val y = (yField.value as String).toIntOrNull() ?: 100
            val resolution = (resolutionField.value as String).toIntOrNull() ?: 500
            settings.viewBox = Rect2d(vec(-x, -y), vec(x, y))
            when {
                x == y -> settings.resolution = vec(resolution, resolution)
                x > y -> settings.resolution = vec(resolution, (y*resolution)/x)
                x < y -> settings.resolution = vec((x*resolution)/y, resolution)
            }
            println("Updated bounds: x: ±$x y: ±$y")
            println("Updated resolution: x: ${settings.resolution.xi} y: $${settings.resolution.yi}")
        }
    }

    /* ================================================== Features ================================================== */
    private object Features {
        private val featuresSet = jq("#features-set")

        init {
            AvatarFeature.values().forEach { addFeature(it) }
        }

        private fun addFeature(feature: AvatarFeature) {
            @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
            featuresSet.append(jq.parseHTML("""
                <div>
                    <input type="checkbox" id="feature-${feature.name}" ${if(feature.default) "checked" else ""}>
                    <label for="feature-${feature.name}">${feature.description}</label>
                </div>
            """.trimIndent())[0] as JQuery)
            if(feature.default)
                settings.features.add(feature)
            val checkbox = jq("#feature-${feature.name}")
            checkbox.change {
                if(checkbox.`is`(":checked"))
                    settings.features.add(feature)
                else
                    settings.features.remove(feature)
            }
        }
    }

}
