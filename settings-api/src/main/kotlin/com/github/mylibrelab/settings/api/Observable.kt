/*
 * Copyright (C) 2020 MyLibreLab
 * Based on MyOpenLab by Carmelo Salafia www.myopenlab.de
 * Copyright (C) 2004  Carmelo Salafia cswi@gmx.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.github.mylibrelab.settings.api

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

typealias BiConsumer<T> = (T, T) -> Unit

class ObservableManager<T> {
    private val properties = mutableMapOf<String, Any>()
    private val listeners = mutableMapOf<String, MutableList<BiConsumer<Any>>>()

    private fun updateValue(name: String, old: Any, new: Any) {
        if (old != new) {
            listeners[name]?.forEach { it(old, new) }
        }
    }

    private fun updateValue(name: String, value: Any) {
        updateValue(name, properties.put(name, value)!!, value)
    }

    fun registerListener(name: String, biConsumer: BiConsumer<Any>) {
        listeners.getOrPut(name) { mutableListOf() }.add(biConsumer)
        properties[name]?.let { biConsumer(it, it) }
    }

    fun removeListeners(name: String) {
        properties.remove(name)
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <V : Any> registerListener(property: KProperty1<T, V>, crossinline consumer: BiConsumer<V>) =
        registerListener(property.name) { old, new -> consumer(old as V, new as V) }

    inline fun <reified V : Any> removeListeners(property: KProperty1<T, V>) =
        removeListeners(property.name)

    inner class WrappingRWProperty<V : Any>(
        prop: KProperty<*>,
        value: V
    ) : ReadWriteProperty<T, V> {

        init {
            properties[prop.name] = value
        }

        @Suppress("UNCHECKED_CAST")
        override operator fun getValue(thisRef: T, property: KProperty<*>) = properties[property.name] as V

        override operator fun setValue(thisRef: T, property: KProperty<*>, value: V) =
            updateValue(property.name, value)
    }

    inner class WrappingDelegateRWProperty<V : Any>(
        private val delegate: KMutableProperty0<V>
    ) : ReadWriteProperty<T, V> {

        override operator fun getValue(thisRef: T, property: KProperty<*>) = delegate.get()

        override operator fun setValue(thisRef: T, property: KProperty<*>, value: V) {
            val old = delegate.get()
            delegate.set(value)
            updateValue(property.name, old, value)
        }
    }
}

interface DelegateProvider<T, V> {
    operator fun provideDelegate(
        thisRef: T,
        prop: KProperty<*>
    ): ReadWriteProperty<T, V>
}

class ObservableValue<T, V : Any>(
    private val delegate: ObservableManager<T>,
    private val value: V
) : DelegateProvider<T, V> {
    override operator fun provideDelegate(
        thisRef: T,
        prop: KProperty<*>
    ): ReadWriteProperty<T, V> = delegate.WrappingRWProperty(prop, value)
}

class ObservablePropertyValue<T, V : Any>(
    private val delegate: ObservableManager<T>,
    private val property: KMutableProperty0<V>
) : DelegateProvider<T, V> {
    override operator fun provideDelegate(
        thisRef: T,
        prop: KProperty<*>
    ): ReadWriteProperty<T, V> = delegate.WrappingDelegateRWProperty(property)
}

/**
 * Classes which implement this interface can provide values that can be observed through listeners.
 */
interface Observable<T> {
    val manager: ObservableManager<T>
}

open class DefaultObservable<T> : Observable<T> {
    override val manager = ObservableManager<T>()
}

/**
 * Create an observable property with the given initial value.
 */
fun <T, V : Any> Observable<T>.observable(value: V): DelegateProvider<T, V> =
    ObservableValue(manager, value)

/**
 * Create an observable property which is delegated to the given property.
 */
fun <T, V : Any> Observable<T>.observable(prop: KMutableProperty0<V>): DelegateProvider<T, V> =
    ObservablePropertyValue(manager, prop)

/**
 * Add a listener to receive events when a property has changed.
 */
inline fun <T, V : Any> Observable<T>.registerListener(
    property: KProperty1<T, V>,
    crossinline consumer: BiConsumer<V>
) = manager.registerListener(property, consumer)

/**
 * Remove all listeners registered to a given property.
 */
inline fun <T, reified V : Any> Observable<T>.removeListeners(
    property: KProperty1<T, V>
) = manager.removeListeners(property)
