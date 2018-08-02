package co.thecodewarrior.avatar.util

import jQuery
import JQuery
import org.w3c.dom.events.EventTarget

val jq = jQuery

val JQuery.value: Any
    get() = this.`val`()
fun JQuery.value(value: String): JQuery = this.`val`(value)
fun JQuery.value(value: Number): JQuery = this.`val`(value)
fun JQuery.value(value: Array<String>): JQuery = this.`val`(value)
fun JQuery.value(func: (index: Number, value: String) -> String): JQuery = this.`val`(func)

//fun <TCurrentTarget: EventTarget, TData> eventHandlerOf (
//        lambda: TCurrentTarget.(event: Event<TCurrentTarget, TData>, args: Array<out Any>) -> dynamic
//): EventHandler<TCurrentTarget, TData> {
//    return LambdaEventHandler(lambda)
//}
//
//private class LambdaEventHandler<TCurrentTarget: EventTarget, TData>(
//        val lambda: TCurrentTarget.(event: Event<TCurrentTarget, TData>, args: Array<out Any>) -> dynamic
//): EventHandler<TCurrentTarget, TData> {
//    override fun invoke(`this`: TCurrentTarget, t: Event<TCurrentTarget, TData>, vararg args: Any): dynamic {
//        return `this`.lambda(t, args)
//    }
//}

