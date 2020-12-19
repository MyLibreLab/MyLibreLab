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

import com.github.mylibrelab.util.castSafelyTo
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0

/**
 * Cast the property to the specified type if applicable for read and write operations.
 */
inline fun <reified T, R : Any> KMutableProperty0<R>.withType(): KMutableProperty0<T>? {
    return if (get().javaClass.kotlin == T::class) {
        this.castSafelyTo<KMutableProperty0<T>>()
    } else {
        null
    }
}

/**
 * Cast the property to the specified type if applicable for read operations.
 */
inline fun <reified T, R : Any> KMutableProperty0<R>.withOutType(): KProperty0<T>? {
    return if (T::class.java.isAssignableFrom(get().javaClass)) {
        this.castSafelyTo<KProperty0<T>>()
    } else {
        null
    }
}

/**
 * Cast the property to the specified type if applicable for write operations.
 */
inline fun <reified T, R : Any> KMutableProperty0<R>.withInType(): KMutableProperty0<T>? {
    return if (get().javaClass.isAssignableFrom(T::class.java)) {
        this.castSafelyTo<KMutableProperty0<T>>()
    } else {
        null
    }
}

operator fun <T> KMutableProperty0<T>.setValue(target: Any?, property: KProperty<*>, value: T) = set(value)

operator fun <T> KProperty0<T>.getValue(target: Any?, property: KProperty<*>): T = get()

fun <T> delegate(getter: () -> T, setter: (T) -> Unit): ReadWriteProperty<Any, T> = object : ReadWriteProperty<Any, T> {
    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        setter(value)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return getter()
    }
}
