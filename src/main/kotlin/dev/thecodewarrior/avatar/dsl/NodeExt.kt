package dev.thecodewarrior.avatar.dsl

import org.redundent.kotlin.xml.Element
import org.redundent.kotlin.xml.Node

fun Node.addElement(element: Element) {
    (this.children as MutableList<Element>).add(element)
}