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

import com.github.mylibrelab.text.Text
import com.github.mylibrelab.text.textOf
import com.github.mylibrelab.util.*
import com.github.mylibrelab.view.Presentation
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty0

@DslMarker
annotation class PropertyMarker

/**
 * Provider for SettingsContainer.
 * You need to implement this interface to register the provided SettingsContainer with the backing storage
 * manager.
 */
interface SettingsContainerProvider : Provider<SettingsContainer> {
    val enabled: Boolean

    fun createAndInit(): SettingsContainer = create().apply { init() }
}

/**
 * Convenience class for SettingsContainerProvider which simply serves a singleton value.
 */
open class SingletonSettingsContainerProvider(
    provider: () -> SettingsContainer,
    override val enabled: Boolean = true
) : SettingsContainerProvider {
    private val singletonProvider = singletonProvider(provider)
    override fun create(): SettingsContainer = singletonProvider.create()
}

/**
 * A container which holds several SettingsGroups.
 * Containers correspond to separate views in the settings interface.
 */
interface SettingsContainer : SettingsGroup {
    val unnamedGroup: SettingsGroup
    val hiddenGroup: SettingsGroup
    val presentation: Presentation

    /**
     * Called after the settings have been loaded/updated from storage.
     */
    fun onSettingsUpdate(updatedFromDisk: Boolean = false)

    /**
     * Called before the settings are being saved.
     */
    fun onSettingsSave()

    /**
     * Determines whether the container contributes to a visual representation
     * of the settings structure.
     */
    fun shouldBeDisplayed(): Boolean = isNonEmpty()

    override fun allProperties(): List<ValueProperty<Any>> =
        subgroups.map { it.allProperties() }.flatten() + unnamedGroup + hiddenGroup
}

fun SettingsContainer.presentation(init: Presentation.() -> Unit) = presentation.init()

/**
 * Initialize the SettingsContainer.
 * This ensures all groups are enabled accordingly to their initial active condition.
 */
fun SettingsContainer.init() {
    allProperties().forEach { it.activeCondition.build() }
}

/**
 * Retrieve the ValueProperty with the given name. This method is lazy to allow forwards references
 * while defining the SettingsGroup.
 */
fun SettingsGroup.getWithName(name: String): Lazy<ValueProperty<Any>> =
    lazy {
        allProperties().findWithName(name).value ?: parent?.getWithName(name)?.value
    }.assertNonNull("Property with name '$name' not found.")

/**
 * Retrieve the ValueProperty with the given underlying property. This method is lazy to allow forwards references
 * while defining the SettingsGroup.
 */
fun <T> SettingsGroup.getWithProperty(prop: KMutableProperty0<T>): Lazy<ValueProperty<T>> =
    getWithName(prop.name).map { it.castSafelyTo<ValueProperty<T>>()!! }

private fun Collection<ValueProperty<Any>>.findWithName(name: String): Lazy<ValueProperty<Any>?> =
    lazy { find { it.name == name } }

/**
 * Container for {@link ValueProperty}s. Properties can be group into
 * logical units using a {@SettingsGroup}.
 *
 * All properties not contained inside a {@SettingsGroup} will automatically belong
 * to the unnamed group of the container.
 */
@Suppress("LeakingThis")
abstract class DefaultSettingsContainer private constructor(
    final override val unnamedGroup: SettingsGroup,
    final override val hiddenGroup: SettingsGroup
) : SettingsContainer, SettingsGroup by unnamedGroup {
    constructor(identifier: String = "") : this(
        DefaultSettingsGroup(identifier = identifier),
        DefaultSettingsGroup(identifier = "${identifier}_hidden")
    )

    init {
        (hiddenGroup as DefaultSettingsGroup).parent = this
    }

    override val subgroups: MutableList<NamedSettingsGroup> = mutableListOf()

    override val presentation: Presentation = Presentation(textOf(identifier))

    override fun onSettingsUpdate(updatedFromDisk: Boolean) { /* Default implementation */
    }

    override fun onSettingsSave() { /* Default implementation */
    }

    override fun allProperties(): List<ValueProperty<Any>> {
        return super<SettingsContainer>.allProperties()
    }
}

fun <T> SettingsGroup.add(property: ValueProperty<T>) {
    this.add(property.castSafelyTo()!!)
}

/**
 * Provides grouping for {@link ValueProperty}s
 */
interface SettingsGroup : MutableList<ValueProperty<Any>> {
    val identifier: String
    val parent: SettingsGroup?
    val subgroups: MutableList<NamedSettingsGroup>

    fun isAllEmpty(): Boolean = isEmpty() && subgroups.all { it.isAllEmpty() }
    fun isNonEmpty(): Boolean = !isEmpty() || subgroups.any { it.isNonEmpty() }

    fun allProperties(): List<ValueProperty<Any>> = this + subgroups.map { it.allProperties() }.flatten()

    fun getIdentifierPath(): String = "${parent?.let { it.getIdentifierPath() + ":" } ?: ""}$identifier"
}

internal val SettingsGroup.container: SettingsContainer
    get() {
        var grp = this
        while (grp !is SettingsContainer) {
            grp = grp.parent!!
        }
        return grp
    }

internal val <T> ValueProperty<T>.container: SettingsContainer
    get() = group.container

interface NamedSettingsGroup : SettingsGroup {
    val name: String
    val displayName: Text
    val description: Text?
}

internal open class DefaultSettingsGroup internal constructor(
    override val identifier: String,
    override var parent: SettingsGroup?,
    private val properties: MutableList<ValueProperty<Any>>
) : SettingsGroup, MutableList<ValueProperty<Any>> by properties {
    override val subgroups: MutableList<NamedSettingsGroup> = mutableListOf()

    constructor(parent: SettingsGroup? = null, identifier: String) : this(
        identifier,
        parent,
        mutableListOf()
    )
}

internal class DefaultNamedSettingsGroup internal constructor(
    override var parent: SettingsGroup?,
    override val name: String,
    override val displayName: Text,
    override val description: Text?,
    identifier: String? = null
) : DefaultSettingsGroup(parent, identifier ?: name), NamedSettingsGroup

/**
 * Wrapper for properties that provides a description and parser/writer used
 * for persistent storage.
 */
@PropertyMarker
interface ValueProperty<T> : Observable<ValueProperty<T>> {
    val description: Text?
    val displayName: Text
    val name: String
    var value: T
    var preview: T
    val group: SettingsGroup
    var activeCondition: Condition
}

/**
 * Specify when the ValueProperty should be active. This only has an effect on the appearance in the interface
 * and does not prevent writing to the property altogether.
 * PropertyValues are enabled by default.
 */
fun <T> ValueProperty<T>.activeIf(condition: Condition) {
    activeCondition = condition
}

/**
 * Property with a backing value that has a different type than the exposed value.
 */
interface TransformingValueProperty<R, T> : ValueProperty<T> {
    val backingProperty: ValueProperty<R>
}

/**
 * Property that can be stored in String format.
 */
typealias PersistentValueProperty<T> = TransformingValueProperty<T, String>

fun ValueProperty<*>.toTransformer(): TransformingValueProperty<Any, Any>? =
    castSafelyTo<TransformingValueProperty<Any, Any>>()

inline fun <reified T : Any> ValueProperty<T>.asPersistent(): PersistentValueProperty<T>? =
    castSafelyTo<TransformingValueProperty<T, Any>>()?.let {
        if (it.value is String) it.castSafelyTo<PersistentValueProperty<T>>() else null
    }

inline fun <reified K : Any> ValueProperty<*>.effective(): ValueProperty<K> = effective<K>(type = K::class)

fun <K : Any> ValueProperty<*>.effective(type: KClass<K>): ValueProperty<K> {
    var prop: ValueProperty<*> = this
    var trans: TransformingValueProperty<Any, Any>? = prop.toTransformer()
    while (trans != null) {
        prop = trans.backingProperty
        trans = prop.toTransformer()
    }
    if (!type.isInstance(prop.value)) throw IllegalArgumentException("Value ${prop.value} isn't of type $type.")
    if (!type.isInstance(prop.preview)) throw IllegalArgumentException("Preview ${prop.value} isn't of type $type.")
    return prop.castSafelyTo()!!
}

class SimpleValueProperty<T : Any> internal constructor(
    name: String?,
    displayName: Text?,
    override val description: Text?,
    property: KMutableProperty0<T>,
    override val group: SettingsGroup
) : ValueProperty<T>, Observable<ValueProperty<T>> by DefaultObservable() {
    override val displayName: Text = displayName ?: textOf(property.name)
    override val name: String = name ?: property.name
    override var value: T by observable<ValueProperty<T>, T>(property)
    override var preview: T by observable<ValueProperty<T>, T>(value)
    override var activeCondition: Condition = conditionOf(true)

    init {
        registerListener(ValueProperty<T>::value) { _, new -> preview = new }
    }
}

open class SimpleTransformingValueProperty<R : Any, T : Any> internal constructor(
    final override val backingProperty: ValueProperty<R>,
    transformer: Transformer<R, T>
) : TransformingValueProperty<R, T>,
    Observable<ValueProperty<T>> by DefaultObservable() {
    override val description by backingProperty::description
    override val displayName by backingProperty::displayName
    override val name by backingProperty::name
    override var activeCondition by backingProperty::activeCondition
    override val group by backingProperty::group

    final override var value: T by transformer.delegate(backingProp = backingProperty::value)
    override var preview: T by observable<ValueProperty<T>, T>(value)
}

class SimplePersistentValueProperty<R : Any>(
    delegate: ValueProperty<R>,
    transformer: Transformer<R, String>
) : SimpleTransformingValueProperty<R, String>(delegate, transformer),
    PersistentValueProperty<R>

/**
 * Property that has a limited set of values the property can take on.
 */
abstract class ChoiceProperty<R, T> internal constructor(
    delegateProperty: TransformingValueProperty<R, T>
) : TransformingValueProperty<R, T> by delegateProperty {
    var choiceValue: R by delegateProperty.backingProperty::value
    var choices: List<R> = ArrayList()
    var renderer: (R) -> String = { it.toString() }
}

class TransformingChoiceProperty<R : Any, T : Any> internal constructor(
    property: TransformingValueProperty<R, T>
) : ChoiceProperty<R, T>(property) {
    constructor(property: ValueProperty<R>, transformer: Transformer<R, T>) :
        this(SimpleTransformingValueProperty(property, transformer))
}

class PersistentChoiceProperty<R : Any>(
    property: PersistentValueProperty<R>
) : ChoiceProperty<R, String>(property),
    PersistentValueProperty<R> {
    constructor(property: ValueProperty<R>, transformer: Transformer<R, String>) :
        this(SimplePersistentValueProperty(property, transformer))
}
