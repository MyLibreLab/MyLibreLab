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

// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.github.mylibrelab.ui.layout

import com.github.mylibrelab.text.Text
import com.github.mylibrelab.text.component.TextButton
import com.github.mylibrelab.text.component.TextCheckBox
import com.github.mylibrelab.text.component.TextLabel
import com.github.mylibrelab.text.component.TextRadioButton
import com.github.mylibrelab.ui.UIStyle
import com.github.weisj.darklaf.components.OverlayScrollPane
import com.github.weisj.darklaf.components.renderer.SimpleListCellRenderer
import java.awt.Dimension
import java.awt.event.ActionEvent
import javax.swing.*
import kotlin.reflect.KMutableProperty0

@CellMarker
abstract class Cell : BaseBuilder {

    internal companion object {
        const val UNBOUND_RADIO_BUTTON = "unbound.radio.button"
    }

    /**
     * Sets how keen the component should be to grow in relation to other component **in the same cell**. Use `push` in addition if need.
     * If this constraint is not set the grow weight is set to 0 and the component will not grow (unless some automatic rule is not applied.
     * Grow weight will only be compared against the weights for the same cell.
     */
    val growX = CCFlags.growX

    @Suppress("unused")
    val growY = CCFlags.growY
    val grow = CCFlags.grow

    /**
     * Makes the column that the component is residing in grow with `weight`.
     */
    val pushX = CCFlags.pushX

    /**
     * Makes the row that the component is residing in grow with `weight`.
     */
    @Suppress("unused")
    val pushY = CCFlags.pushY
    val push = CCFlags.push

    @JvmOverloads
    fun label(text: Text, bold: Boolean = false): CellBuilder<JLabel> {
        val label = if (bold) UIStyle.withBoldFont(TextLabel(text)) else TextLabel(text)
        return component(label)
    }

    fun button(text: Text, actionListener: (event: ActionEvent) -> Unit): CellBuilder<JButton> {
        val button = TextButton(text)
        button.addActionListener(actionListener)
        return component(button)
    }

    @JvmOverloads
    fun checkBox(
        text: Text,
        isSelected: Boolean = false,
        comment: Text? = null,
        actionListener: (event: ActionEvent, component: JCheckBox) -> Unit
    ): CellBuilder<JCheckBox> {
        return checkBox(text, isSelected, comment)
            .applyToComponent {
                addActionListener { actionListener(it, this) }
            }
    }

    @JvmOverloads
    fun checkBox(
        text: Text,
        isSelected: Boolean = false,
        comment: Text? = null
    ): CellBuilder<JCheckBox> {
        val result = TextCheckBox(text)
        result.isSelected = isSelected
        return result(comment = comment)
    }

    fun checkBox(
        text: Text,
        prop: KMutableProperty0<Boolean>,
        comment: Text? = null
    ): CellBuilder<JCheckBox> {
        return checkBox(text, prop.toBinding(), comment)
    }

    @JvmOverloads
    fun checkBox(
        text: Text,
        getter: () -> Boolean,
        setter: (Boolean) -> Unit,
        comment: Text? = null
    ): CellBuilder<JCheckBox> {
        return checkBox(text, PropertyBinding(getter, setter), comment)
    }

    private fun checkBox(
        text: Text,
        modelBinding: PropertyBinding<Boolean>,
        comment: Text?
    ): CellBuilder<JCheckBox> {
        val component = TextCheckBox(text)
        component.isSelected = modelBinding.get()
        return component(comment = comment).withSelectedBinding(modelBinding)
    }

    open fun radioButton(text: Text, comment: Text? = null): CellBuilder<JRadioButton> {
        val component = TextRadioButton(text)
        component.putClientProperty(UNBOUND_RADIO_BUTTON, true)
        return component(comment = comment)
    }

    open fun radioButton(
        text: Text,
        getter: () -> Boolean,
        setter: (Boolean) -> Unit,
        comment: Text? = null
    ): CellBuilder<JRadioButton> {
        val component = TextRadioButton(text)
        component.isSelected = getter()
        return component(comment = comment)
            .withSelectedBinding(PropertyBinding(getter, setter))
    }

    open fun radioButton(
        text: Text,
        prop: KMutableProperty0<Boolean>,
        comment: Text? = null
    ): CellBuilder<JRadioButton> {
        val component = TextRadioButton(text)
        component.isSelected = prop.get()
        return component(comment = comment).withSelectedBinding(prop.toBinding())
    }

    fun <T> comboBox(
        model: ComboBoxModel<T>,
        getter: () -> T?,
        setter: (T?) -> Unit,
        renderer: ListCellRenderer<T?>? = null
    ): CellBuilder<JComboBox<T>> {
        return comboBox(model, PropertyBinding(getter, setter), renderer)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> comboBox(
        model: ComboBoxModel<T>,
        modelBinding: PropertyBinding<T?>,
        renderer: ListCellRenderer<T?>? = null
    ): CellBuilder<JComboBox<T>> {
        return component(JComboBox(model))
            .applyToComponent {
                this.renderer = renderer ?: SimpleListCellRenderer.create { it -> it.toString() }
                selectedItem = modelBinding.get()
            }
            .withBinding(
                { component -> component.selectedItem as T? },
                { component, value -> component.setSelectedItem(value) },
                modelBinding
            )
    }

    inline fun <reified T : Any> comboBox(
        model: ComboBoxModel<T>,
        prop: KMutableProperty0<T>,
        renderer: ListCellRenderer<T?>? = null
    ): CellBuilder<JComboBox<T>> {
        return comboBox(model, prop.toBinding().toNullable(), renderer)
    }

    fun textField(prop: KMutableProperty0<String>, columns: Int? = null): CellBuilder<JTextField> =
        textField(prop.toBinding(), columns)

    @JvmOverloads
    fun textField(getter: () -> String, setter: (String) -> Unit, columns: Int? = null) =
        textField(PropertyBinding(getter, setter), columns)

    @JvmOverloads
    fun textField(binding: PropertyBinding<String>, columns: Int? = null): CellBuilder<JTextField> {
        return component(JTextField(binding.get(), columns ?: 0))
            .withTextBinding(binding)
    }

    fun spinner(prop: KMutableProperty0<Int>, minValue: Int, maxValue: Int, step: Int = 1): CellBuilder<JSpinner> {
        val spinnerModel = SpinnerNumberModel(prop.get(), minValue, maxValue, step)
        val spinner = JSpinner(spinnerModel)
        return component(spinner).withBinding({ it.value as Int }, JSpinner::setValue, prop.toBinding())
    }

    @JvmOverloads
    fun spinner(
        getter: () -> Int,
        setter: (Int) -> Unit,
        minValue: Int,
        maxValue: Int,
        step: Int = 1
    ): CellBuilder<JSpinner> {
        val spinnerModel = SpinnerNumberModel(getter(), minValue, maxValue, step)
        val spinner = JSpinner(spinnerModel)
        return component(spinner).withBinding(
            { it.value as Int },
            JSpinner::setValue,
            PropertyBinding(getter, setter)
        )
    }

    fun scrollPane(component: JComponent): CellBuilder<OverlayScrollPane> {
        return component(OverlayScrollPane(component))
    }

    fun placeholder(): CellBuilder<JComponent> {
        return component(
            JPanel().apply {
                minimumSize = Dimension(0, 0)
                preferredSize = Dimension(0, 0)
                maximumSize = Dimension(0, 0)
            }
        )
    }

    abstract fun <T : JComponent> component(component: T): CellBuilder<T>

    operator fun <T : JComponent> T.invoke(
        growPolicy: GrowPolicy? = null,
        comment: Text? = null,
        vararg constraints: CCFlags
    ): CellBuilder<T> = component(this).apply {
        constraints(*constraints)
        if (comment != null) comment(comment)
        if (growPolicy != null) growPolicy(growPolicy)
    }
}
