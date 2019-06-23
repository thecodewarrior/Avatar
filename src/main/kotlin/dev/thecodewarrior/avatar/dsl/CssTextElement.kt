package dev.thecodewarrior.avatar.dsl

import org.redundent.kotlin.xml.Element
import org.redundent.kotlin.xml.PrintOptions

open class CssTextElement(val text: String) : Element {
    override fun render(builder: Appendable, indent: String, printOptions: PrintOptions) {
        if(printOptions.pretty) {
            val lineEnding = System.lineSeparator()
            text.lineSequence().forEach { line ->
                line.trim('\r', '\n')
                builder.append("$indent$line$lineEnding")
            }
        } else {
            val flat = text.replace("\\s+".toRegex(RegexOption.MULTILINE), " ")
            builder.append("$indent$flat")
        }

    }
}
