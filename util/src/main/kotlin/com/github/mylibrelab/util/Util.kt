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

package com.github.mylibrelab.util

inline fun <reified T> Any.castSafelyTo(): T? = (this as? T)

fun String.toPair(delimiter: Char): Pair<String, String>? = split(delimiter, limit = 2).let {
    if (it.size == 2) it[0] to it[1] else null
}

infix fun <R, T, S> ((R) -> T).andThen(g: (T) -> S): (R) -> S {
    return { r -> g(this(r)) }
}

fun <R, T> ((R) -> T).or(fallback: T): (R?) -> T = { r -> r?.let { this(r) } ?: fallback }

fun <T> Lazy<T?>.assertNonNull(message: String = "Lazy value is null"): Lazy<T> = lazy {
    if (value == null) throw NullPointerException(message)
    value!!
}

fun <T> Lazy<T>.ifPresent(block: (T) -> Unit) {
    if (isInitialized()) block(value)
}

fun <T> Lazy<T>.letValue(block: (T) -> Unit) {
    block(value)
}

fun <T, K> Lazy<T>.map(block: (T) -> K): Lazy<K> = lazy { block(value) }

fun <T, K> Lazy<T>.lazyCall(block: T.() -> Lazy<K>): Lazy<K> = lazy { block(this.value).value }
