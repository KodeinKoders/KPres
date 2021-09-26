package org.kodein.kpres.utils

import react.FC
import react.Props
import react.StateSetter
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

// https://github.com/JetBrains/kotlin-wrappers/issues/125
internal operator fun <P : Props> FC<P>.getValue(thisRef: Any?, property: KProperty<*>): FC<P> {
    this.asDynamic().displayName = property.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    return this
}

internal class StateDelegate<T>(state: Pair<T, StateSetter<T>>) : ReadWriteProperty<Any?, T> {

    private var value = state.first
    private val set = state.second

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = value

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        if (value == this.value) return
        this.value = value
        set(value)
    }

}

internal operator fun <T> Pair<T, StateSetter<T>>.provideDelegate(thisRef: Any?, property: KProperty<*>) = StateDelegate(this)
