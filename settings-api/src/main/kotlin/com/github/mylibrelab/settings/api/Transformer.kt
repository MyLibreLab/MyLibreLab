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

import com.github.mylibrelab.util.andThen
import com.github.mylibrelab.util.castSafelyTo
import com.github.mylibrelab.util.toPair
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty

private const val defaultDelimiter: Char = Char.MAX_VALUE
private const val defaultDelimiterAlt: Char = Char.MAX_VALUE - 1

/**
 * Transformer for changing the outwards type of a property.
 * R is the real type and T the transformed type.
 */
interface Transformer<R, T> {
    val write: (T) -> R
    val read: (R) -> T
}

internal class DefaultTransformer<R, T>(
    override val write: (T) -> R,
    override val read: (R) -> T
) : Transformer<R, T>

/**
 * Combines two transformers. The intermediate type needs to match for both transformers.
 */
infix fun <R, T, S> Transformer<R, T>.andThen(other: Transformer<T, S>): Transformer<R, S> {
    return transformerOf(other.write andThen write, read andThen other.read)
}

/**
 * Lifts a transformer which as a nullable transformed type to a non-nullable version using the given fallback value.
 */
fun <R, T> Transformer<R, T?>.readFallback(fallback: T): Transformer<R, T> {
    return this andThen transformerOf({ t -> t }, { t -> t ?: fallback })
}

/**
 * Lifts a transformer which as a nullable original type to a non-nullable version using the given fallback value.
 */
fun <R, T> Transformer<R?, T>.writeFallback(fallback: R): Transformer<R, T> {
    return transformerOf<R, R?>({ t -> t ?: fallback }, { t -> t }) andThen this
}

fun <R, T> Transformer<R, T>.reverse(): Transformer<T, R> = transformerOf(read, write)

object IdentityTransformer : Transformer<Any, Any> by DefaultTransformer({ t -> t }, { t -> t })

/**
 * Transformer that does nothing.
 */
fun <T : Any> identityTransformer(): Transformer<T, T> =
    IdentityTransformer.castSafelyTo<Transformer<T, T>>()!!

/**
 * Creates a transformer with the given write and read operation.
 */
fun <R, T> transformerOf(write: (T) -> R, read: (R) -> T): Transformer<R, T> = DefaultTransformer(write, read)

fun intParser() = transformerOf(String::toInt, Int::toString)
fun longParser() = transformerOf(String::toLong, Long::toString)
fun floatParser() = transformerOf(String::toFloat, Float::toString)
fun doubleParser() = transformerOf(String::toDouble, Double::toString)
fun boolParser() = transformerOf(String::toBoolean, Boolean::toString)

/**
 * Parser for lists.
 * It is important the the string representation of the individual parts don't contain the delimiter value.
 */
fun <T, ListType> listParser(
    elementParser: Transformer<T, String>,
    collectionConstructor: (Iterable<T>) -> ListType,
    delimiter: Char = defaultDelimiter
): Transformer<ListType, String> where ListType : List<T> = transformerOf(
    write = { value ->
        collectionConstructor(value.split(delimiter).map { elementParser.write(it) })
    },
    read = { list ->
        val mappedList = list.map { elementParser.read(it) }
        if (mappedList.firstOrNull { it.contains(delimiter) } != null) {
            throw IllegalStateException("String representation of element can't contain delimiter char '$delimiter'")
        }
        mappedList.joinToString(separator = delimiter.toString())
    }
)

/**
 * Parser for lists.
 * It is important the the string representation of the individual parts don't contain the delimiter value.
 */
fun <T> listParser(
    elementParser: Transformer<T, String>,
    delimiter: Char = defaultDelimiter
): Transformer<List<T>, String> = listParser(
    elementParser,
    Iterable<T>::toList,
    delimiter
)

/**
 * Parser for mutable lists.
 * It is important the the string representation of the individual parts don't contain the delimiter value.
 */
fun <T> mutableListParser(
    elementParser: Transformer<T, String>,
    delimiter: Char = defaultDelimiter
): Transformer<List<T>, String> = listParser(
    elementParser,
    Iterable<T>::toMutableList,
    delimiter
)

fun <K, V, MapType> mapParser(
    keyParser: Transformer<K, String>,
    valueParser: Transformer<V, String>,
    collectionConstructor: (Iterable<Pair<K, V>>) -> MapType,
    entryDelimiter: Char = defaultDelimiterAlt,
    entryListDelimiter: Char = defaultDelimiter
): Transformer<MapType, String> where MapType : Map<K, V> =
    transformerOf(
        write = { entries -> collectionConstructor(entries) },
        read = { map: MapType -> map.entries.map { it.key to it.value } }
    ) andThen listParser(
        elementParser = transformerOf(
            write = {
                val pair = it.toPair(entryDelimiter)!!
                keyParser.write(pair.first) to valueParser.write(pair.second)
            },
            read = { entry -> "${keyParser.read(entry.first)}$entryDelimiter${valueParser.read(entry.second)}" }
        ),
        delimiter = entryListDelimiter
    )

fun <K, V> mapParser(
    keyParser: Transformer<K, String>,
    valueParser: Transformer<V, String>,
    entryDelimiter: Char = defaultDelimiterAlt,
    entryListDelimiter: Char = defaultDelimiter
): Transformer<Map<K, V>, String> =
    mapParser(
        keyParser = keyParser,
        valueParser = valueParser,
        collectionConstructor = { it.toMap() },
        entryDelimiter = entryDelimiter,
        entryListDelimiter = entryListDelimiter
    )

fun <K, V> mutableMapParser(
    keyParser: Transformer<K, String>,
    valueParser: Transformer<V, String>,
    entryDelimiter: Char = '#',
    entryListDelimiter: Char = ','
): Transformer<MutableMap<K, V>, String> =
    mapParser(
        keyParser = keyParser,
        valueParser = valueParser,
        collectionConstructor = { it.toMap().toMutableMap() },
        entryDelimiter = entryDelimiter,
        entryListDelimiter = entryListDelimiter
    )

fun propertyParser(
    entryDelimiter: Char = defaultDelimiterAlt,
    entryListDelimiter: Char = defaultDelimiter
): Transformer<MutableMap<String, String>, String> =
    mutableMapParser(identityTransformer(), identityTransformer(), entryDelimiter, entryListDelimiter)

/**
 * Parser that converts a class which is representable as a list of homogenous components.
 * It is important the the string representation of the individual parts don't contain the delimiter value.
 */
fun <T, ComponentType> componentParser(
    componentParser: Transformer<ComponentType, String>,
    listTransformer: Transformer<T, List<ComponentType>>,
    delimiter: Char = defaultDelimiter
): Transformer<T, String> =
    listTransformer andThen listParser(componentParser, delimiter)

/**
 * Parser that converts a class which can be constructed using two values to a String representation
 * It is important the the string representation of the individual parts don't contain the delimiter value.
 */
fun <T, ComponentType> pairParser(
    toType: (Pair<ComponentType, ComponentType>) -> T,
    toPair: (T) -> Pair<ComponentType, ComponentType>,
    componentParser: Transformer<ComponentType, String>,
    delimiter: Char = defaultDelimiter
): Transformer<T, String> {
    val listTransformer: Transformer<T, List<ComponentType>> =
        transformerOf(toType, toPair) andThen listToPair<ComponentType>().reverse()
    return componentParser(componentParser, listTransformer, delimiter)
}

/**
 * Converts between a list and a pair. (The list is assumed to always contain two elements).
 */
fun <T> listToPair(): Transformer<List<T>, Pair<T, T>> =
    transformerOf(
        write = Pair<T, T>::toList,
        read = { list -> Pair(list[0], list[1]) }
    )

/**
 * Converts between a pair and a list (which is assumed to always contain two elements).
 */
fun <T> pairToList(): Transformer<Pair<T, T>, List<T>> = listToPair<T>().reverse()

/**
 * Parser that converts a class which can be constructed using two values to a String representation
 * It is important the the string representation of the individual parts don't contain the delimiter value.
 */
fun <T, ComponentType> pairParser(
    toType: (ComponentType, ComponentType) -> T,
    firstAccessor: (T) -> ComponentType,
    secondAccessor: (T) -> ComponentType,
    parser: Transformer<ComponentType, String>,
    delimiter: Char = defaultDelimiter
): Transformer<T, String> = pairParser(
    toType = { toType(it.first, it.second) },
    toPair = { Pair(firstAccessor(it), secondAccessor(it)) },
    componentParser = parser,
    delimiter = delimiter
)

internal class TransformerDelegate<R, T>(
    private val prop: KMutableProperty0<R>,
    private val transformer: Transformer<R, T>
) : ReadWriteProperty<Any, T> {
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return transformer.read(prop.get())
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        prop.set(transformer.write(value))
    }
}

fun <R, T> Transformer<R, T>.delegate(backingProp: KMutableProperty0<R>): ReadWriteProperty<Any, T> =
    TransformerDelegate(backingProp, this)
