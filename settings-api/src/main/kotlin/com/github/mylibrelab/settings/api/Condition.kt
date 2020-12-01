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

import kotlin.reflect.KProperty0

/**
 * Condition whose value can be observed.
 */
interface Condition : () -> Boolean, Observable<Condition> {
    var value: Boolean

    /**
     * Build the condition (Specifically important for more complex conditions which are
     * bound to property value).
     */
    fun build() {
        this()
    }
}

/**
 * Condition with a constant value.
 */
class ConstantCondition(value: Boolean) : Condition, Observable<Condition> by DefaultObservable() {
    override var value: Boolean = value
        set(_) = throw UnsupportedOperationException()

    override fun invoke(): Boolean {
        return value
    }
}

class DefaultCondition(initial: Boolean, private val cond: () -> Boolean = { initial }) :
    Condition,
    Observable<Condition> by DefaultObservable() {
    override var value: Boolean by observable<Condition, Boolean>(initial)

    override operator fun invoke(): Boolean {
        value = cond()
        return value
    }
}

/**
 * Lazily evaluated condition which is bound to a ValueProperty and is true if the value equals an expected value.
 */
class LazyCondition<T : Any>(private val valueProp: Lazy<ValueProperty<T>>, private val expected: T) :
    Condition,
    Observable<Condition> by DefaultObservable() {
    override var value: Boolean by observable<Condition, Boolean>(true)

    override fun invoke(): Boolean {
        return value
    }

    override fun build() {
        valueProp.value.effective<Any>().let {
            value = it.preview == expected
            it.registerListener(ValueProperty<Any>::preview) { _, _ ->
                value = it.preview == expected
            }
        }
    }
}

class CompoundCondition(
    private val first: Condition,
    private val second: Condition,
    private val combinator: (Boolean, Boolean) -> Boolean
) : Condition, Observable<Condition> by first {
    override var value: Boolean by observable<Condition, Boolean>(combinator(first.value, second.value))

    override fun invoke(): Boolean {
        value = combinator(first(), second())
        return value
    }

    override fun build() {
        first.build()
        second.build()
        first.registerListener(Condition::value) { _, _ ->
            value = combinator(first.value, second())
        }
        second.registerListener(Condition::value) { _, _ ->
            value = combinator(first(), second.value)
        }
    }
}

/**
 * Create compound condition which value is true iff both conditions are met.
 */
infix fun Condition.and(other: Condition): Condition = CompoundCondition(this, other, Boolean::and)

/**
 * Create compound condition which value is true iff both at least one conditions is met.
 */
infix fun Condition.or(other: Condition): Condition = CompoundCondition(this, other, Boolean::or)

/**
 * Inverts the given condition.
 */
fun not(cond: Condition): Condition = CompoundCondition(cond, conditionOf(true)) { a, _ -> !a }

/**
 * Create constant value condition.
 */
fun conditionOf(bool: Boolean): Condition = ConstantCondition(bool)

/**
 * Create condition form a Boolean provider..
 */
fun conditionOf(cond: () -> Boolean): Condition = DefaultCondition(cond(), cond)

/**
 * Create condition from a boolean property.
 */
fun conditionOf(prop: KProperty0<Boolean>): Condition = DefaultCondition(prop.get()) { prop.get() }

/**
 * Create condition from a boolean ValueProperty.
 */
fun conditionOf(valueProp: ValueProperty<Boolean>): Condition = conditionOf(valueProp::value)

/**
 * Create condition from a boolean ValueProperty.
 */
fun conditionOf(valueProp: Lazy<ValueProperty<Boolean>>): Condition = DefaultCondition(true) { valueProp.value.value }

/**
 * Create condition from a boolean ValueProperty. The condition is true of the value of the property mathes
 * the given expected value.
 */
fun <T : Any> isEqual(valueProp: Lazy<ValueProperty<T>>, expected: T) = LazyCondition(valueProp, expected)

/**
 * Create condition from a boolean ValueProperty.
 */
fun isTrue(valueProp: Lazy<ValueProperty<Boolean>>): Condition = isEqual(valueProp, true)

/**
 * Create condition from a boolean ValueProperty.
 */
fun isFalse(valueProp: Lazy<ValueProperty<Boolean>>): Condition = isEqual(valueProp, false)
