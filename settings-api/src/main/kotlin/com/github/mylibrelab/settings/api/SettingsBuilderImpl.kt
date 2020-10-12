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
import com.github.mylibrelab.util.*
import kotlin.reflect.KMutableProperty0

/*
 * To prevent clashes in groups with the same name we need to keep track of how many
 * generated identifiers we have for each group.
 */
private data class UnnamedGroupCounter(var count: Int) {

    companion object {
        private val counterMap = mutableMapOf<SettingsGroup, UnnamedGroupCounter>()

        fun get(group: SettingsGroup): UnnamedGroupCounter = counterMap.getOrPut(group) { UnnamedGroupCounter(0) }
    }

    fun increment(): Int {
        return count++
    }
}

private val SettingsGroup.unnamedGroupCounter
    get() = UnnamedGroupCounter.get(this)

class SettingsGroupBuilder(group: SettingsGroup) : SettingsGroup by group {
    internal var activeCondition: Condition? = null

    fun KMutableProperty0<Boolean>.isTrue() = isTrue(getWithProperty(this))
    fun KMutableProperty0<Boolean>.isFalse() = isFalse(getWithProperty(this))
    fun <T : Any> KMutableProperty0<T>.isEqual(expected: T) = isEqual(getWithProperty(this), expected)
}

/**
 * Specify when the SettingsGroup should be active. This only has an effect on the appearance in the interface
 * and does not prevent writing to the property altogether. The value of the group can be overwritten for each
 * property individually. Groups are enabled by default.
 */
fun SettingsGroupBuilder.activeIf(condition: Condition) {
    activeCondition = condition
}

private fun initGroup(group: SettingsGroup, init: SettingsGroupBuilder.() -> Unit): SettingsGroup {
    val builder = SettingsGroupBuilder(group)
    builder.init()
    builder.activeCondition?.let { cond ->
        group.forEach {
            // Check for constant condition for a fast path. This is the case most of the time.
            if (it.activeCondition is ConstantCondition) {
                if (it.activeCondition.value) {
                    it.activeIf(cond)
                }
            } else {
                it.activeIf(it.activeCondition and cond)
            }
        }
    }
    return group
}

/**
 * Declare a new settings group with the given name and configures it.
 * @param name The name of the group.
 * @param init The initialization block.
 */
fun SettingsGroup.group(name: String = "", init: SettingsGroupBuilder.() -> Unit): SettingsGroup {
    val identifier = if (name.isNotEmpty()) null else "group_${unnamedGroupCounter.increment()}"
    val group = subgroups.find { it.identifier == identifier && it.name == name } ?: {
        val gr = DefaultNamedSettingsGroup(this, name, identifier)
        subgroups.add(gr)
        gr
    }()
    return initGroup(group, init)
}

/**
 * Configure the unnamed group. Where properties in this block are displayed may depend on the implementation
 * of the interface.
 * @param init The initialization block.
 */
fun SettingsContainer.unnamedGroup(init: SettingsGroup.() -> Unit): SettingsGroup = initGroup(this, init)

/**
 * Configure the hidden group. Properties in this group will not contribute to the graphical interface.
 * @param init The initialization block.
 */
fun SettingsContainer.hidden(init: SettingsGroup.() -> Unit): SettingsGroup = initGroup(hiddenGroup, init)

/**
 * Add a new property.
 * @param property the property to add.
 * @param init The initialization block.
 */
fun <T : ValueProperty<T>> SettingsGroup.property(property: T, init: T.() -> Unit = {}): T =
    property.also { it.init(); add(it) }

/**
 * Declare a new raw property.
 * @param name the name of the property.
 * @param displayName the display name of the property
 * @param description the description of the property
 * @param value The property reference.
 * @param init The initialization block.
 */
fun <T : Any> SettingsGroup.property(
    name: String? = null,
    displayName: Text? = null,
    description: Text? = null,
    value: KMutableProperty0<T>,
    init: SimpleValueProperty<T>.() -> Unit = {}
): ValueProperty<T> =
    SimpleValueProperty(name, displayName, description, value, this).also { it.init(); add(it) }

/**
 * Declare a new raw property whose exposed type is different from the internal storage value.
 * @param name the name of the property.
 * @param displayName the display name of the property
 * @param description the description of the property
 * @param value The property reference.
 * @param transformer The transformer to convert between the storage and exposed type.
 * @param init The initialization block.
 */
fun <R : Any, T : Any> SettingsGroup.transformingProperty(
    name: String? = null,
    displayName: Text? = null,
    description: Text? = null,
    value: KMutableProperty0<R>,
    transformer: Transformer<R, T>,
    init: SimpleValueProperty<R>.() -> Unit = {}
): TransformingValueProperty<R, T> =
    SimpleTransformingValueProperty(
        SimpleValueProperty(name, displayName, description, value, this).also(init),
        transformer
    ).also { add(it) }

/**
 * Declare a new String property.
 * @param name the name of the property.
 * @param displayName the display name of the property
 * @param description the description of the property
 * @param value The property reference.
 */
fun SettingsGroup.stringProperty(
    name: String? = null,
    displayName: Text? = null,
    description: Text? = null,
    value: KMutableProperty0<String>
): ValueProperty<String> = property(name, displayName, description, value)

/**
 * Declare a new boolean property.
 * @param name the name of the property.
 * @param displayName the display name of the property
 * @param description the description of the property
 * @param value The property reference.
 */
fun SettingsGroup.booleanProperty(
    name: String? = null,
    displayName: Text? = null,
    description: Text? = null,
    value: KMutableProperty0<Boolean>,
): ValueProperty<Boolean> = property(name, displayName, description, value)

/**
 * Declare a new property which has a limited set of possible values.
 * The possible values can be configured in the initialization block
 * with the 'choices' value.
 * This method allows to declare a transformer to allow for a separate storage type.
 *
 * @param name the name of the property.
 * @param displayName the display name of the property
 * @param description the description of the property
 * @param value The property reference.
 * @param transformer The transformer to convert between the storage and exposed type.
 * @param init The initialization block.
 */
fun <R : Any, T : Any> SettingsGroup.choiceProperty(
    name: String? = null,
    displayName: Text? = null,
    description: Text? = null,
    value: KMutableProperty0<R>,
    transformer: Transformer<R, T>,
    init: ChoiceProperty<R, T>.() -> Unit = {}
): ChoiceProperty<R, T> =
    TransformingChoiceProperty(
        SimpleValueProperty(name, displayName, description, value, this),
        transformer
    ).also {
        it.init(); add(it)
    }

/**
 * Declare a new property which has a limited set of possible values.
 * The possible values can be configured in the initialization block
 * with the 'choices' value.
 *
 * @param name the name of the property.
 * @param displayName the display name of the property
 * @param description the description of the property
 * @param value The property reference.
 * @param init The initialization block.
 */
fun <T : Any> SettingsGroup.choiceProperty(
    name: String?,
    displayName: Text? = null,
    description: Text? = null,
    value: KMutableProperty0<T>,
    init: ChoiceProperty<T, T>.() -> Unit = {}
): ChoiceProperty<T, T> =
    choiceProperty(name, displayName, description, value, identityTransformer(), init)

/**
 * Declare a new property that is persisted between sessions.
 * @param name the name of the property.
 * @param displayName the display name of the property
 * @param description the description of the property
 * @param value The property reference.
 * @param transformer The transformer to read/write the properties to the storage.
 * @param initValueProp The initialization block to configure the underlying value property.
 * @param initPersistentProp The initialization block to configure the underlying persistent property.
 */
fun <R : Any> SettingsGroup.persistentProperty(
    name: String? = null,
    displayName: Text? = null,
    description: Text? = null,
    value: KMutableProperty0<R>,
    transformer: Transformer<R, String>,
    initValueProp: ValueProperty<R>.() -> Unit = {},
    initPersistentProp: PersistentValueProperty<R>.() -> Unit = {}
): PersistentValueProperty<R> =
    SimplePersistentValueProperty(
        SimpleValueProperty(name, displayName, description, value, this).also(initValueProp),
        transformer
    ).also { it.initPersistentProp(); add(it) }

/**
 * Declare a new string property that is persisted between sessions.
 * @param name the name of the property.
 * @param displayName the display name of the property
 * @param description the description of the property
 * @param value The property reference.
 */
fun SettingsGroup.persistentStringProperty(
    name: String? = null,
    displayName: Text? = null,
    description: Text? = null,
    value: KMutableProperty0<String>
): ValueProperty<String> = persistentProperty(name, displayName, description, value, identityTransformer())

/**
 * Declare a new boolean property that is persisted between sessions.
 * @param name the name of the property.
 * @param displayName the display name of the property
 * @param description the description of the property
 * @param value The property reference.
 */
fun SettingsGroup.persistentBooleanProperty(
    name: String? = null,
    displayName: Text? = null,
    description: Text? = null,
    value: KMutableProperty0<Boolean>,
): PersistentValueProperty<Boolean> =
    persistentProperty(name, displayName, description, value, boolParser())

/**
 * Declare a new integer property that is persisted between sessions.
 * @param name the name of the property.
 * @param displayName the display name of the property
 * @param description the description of the property
 * @param value The property reference.
 */
fun SettingsGroup.persistentIntProperty(
    name: String? = null,
    displayName: Text? = null,
    description: Text? = null,
    value: KMutableProperty0<Int>,
): PersistentValueProperty<Int> =
    persistentProperty(name, displayName, description, value, intParser())

/**
 * Declare a new long property that is persisted between sessions.
 * @param name the name of the property.
 * @param displayName the display name of the property
 * @param description the description of the property
 * @param value The property reference.
 */
fun SettingsGroup.persistentLongProperty(
    name: String? = null,
    displayName: Text? = null,
    description: Text? = null,
    value: KMutableProperty0<Long>,
): PersistentValueProperty<Long> =
    persistentProperty(name, displayName, description, value, longParser())

/**
 * Declare a new float property that is persisted between sessions.
 * @param name the name of the property.
 * @param displayName the display name of the property
 * @param description the description of the property
 * @param value The property reference.
 */
fun SettingsGroup.persistentFloatProperty(
    name: String? = null,
    displayName: Text? = null,
    description: Text? = null,
    value: KMutableProperty0<Float>,
): PersistentValueProperty<Float> =
    persistentProperty(name, displayName, description, value, floatParser())

/**
 * Declare a new double property that is persisted between sessions.
 * @param name the name of the property.
 * @param displayName the display name of the property
 * @param description the description of the property
 * @param value The property reference.
 */
fun SettingsGroup.persistentDoubleProperty(
    name: String? = null,
    displayName: Text? = null,
    description: Text? = null,
    value: KMutableProperty0<Double>,
): PersistentValueProperty<Double> =
    persistentProperty(name, displayName, description, value, doubleParser())

/**
 * Declare a new choice property that is persisted between sessions.
 * The choice property has only a finite set of states it can attain.
 * The possible values can be configured in the initialization block
 * with the 'choices' value.
 *
 * @param name the name of the property.
 * @param displayName the display name of the property
 * @param description the description of the property
 * @param value The property reference.
 * @param init The initialization block.
 */
fun <R : Any> SettingsGroup.persistentChoiceProperty(
    name: String? = null,
    displayName: Text? = null,
    description: Text? = null,
    value: KMutableProperty0<R>,
    transformer: Transformer<R, String>,
    init: ChoiceProperty<R, String>.() -> Unit = {}
): ChoiceProperty<R, String> =
    PersistentChoiceProperty(
        SimpleValueProperty(name, displayName, description, value, this),
        transformer
    ).also { it.init(); add(it) }

/**
 * Declare a new choice property that is persisted between sessions.
 * The choice property has only a finite set of states it can attain.
 * The possible values can be configured in the initialization block
 * with the 'choices' value.
 *
 * @param name the name of the property.
 * @param displayName the display name of the property
 * @param description the description of the property
 * @param value The property reference.
 * @param init The initialization block.
 */
fun SettingsGroup.persistentChoiceProperty(
    name: String? = null,
    displayName: Text? = null,
    description: Text? = null,
    value: KMutableProperty0<String>,
    init: ChoiceProperty<String, String>.() -> Unit = {}
): ChoiceProperty<String, String> =
    persistentChoiceProperty(name, displayName, description, value, identityTransformer(), init)
