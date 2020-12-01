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

package com.github.mylibrelab.ui.settings

import com.github.mylibrelab.settings.api.*
import com.github.mylibrelab.text.DynamicText
import com.github.mylibrelab.text.Text
import com.github.mylibrelab.text.emptyText
import com.github.mylibrelab.text.isConstantNullOrEmpty
import com.github.mylibrelab.ui.component.CollectionComboBoxModel
import com.github.mylibrelab.ui.component.ContextInfoLabel
import com.github.mylibrelab.ui.layout.DialogPanel
import com.github.mylibrelab.ui.layout.Row
import com.github.mylibrelab.ui.layout.RowBuilder
import com.github.mylibrelab.ui.layout.panel
import com.github.mylibrelab.ui.listeners.DocumentChangeListener
import com.github.mylibrelab.ui.view.ViewOfTypeProviderFor
import com.github.mylibrelab.util.castSafelyTo
import com.github.weisj.darklaf.components.renderer.SimpleListCellRenderer

open class SettingsViewProviderBase<T : SettingsContainer> internal constructor() :
    ViewOfTypeProviderFor<T, DialogPanel> {

    companion object {
        val UNNAMED_GROUP_TITLE: Text = emptyText()
    }

    override fun createView(container: T): DialogPanel {
        return panel {
            container.subgroups.forEach { addGroup(it) }
            addGroup(container.unnamedGroup, UNNAMED_GROUP_TITLE, null)
        }
    }

    protected fun RowBuilder.addGroup(group: NamedSettingsGroup) =
        addGroup(group, group.displayName, group.description)

    protected fun RowBuilder.addGroup(
        properties: SettingsGroup,
        name: Text?,
        description: Text?,
    ) {
        description?.also { row { commentRow(it) } }
        maybeTitledRow(name) {
            properties.forEach { addProperty(it) }
            properties.subgroups.forEach { group ->
                addGroup(group)
            }
        }
    }

    private fun Row.addProperty(valueProp: ValueProperty<Any>) {
        val choiceProperty = valueProp.castSafelyTo<ChoiceProperty<Any, Any>>()
        val effectiveProp = valueProp.effective<Any>()
        val prop = effectiveProp.value
        val rowName = when (prop) {
            is Boolean -> emptyText()
            else -> valueProp.displayName
        }
        maybeNamedRow(rowName) {
            when {
                choiceProperty != null -> addChoiceProperty(choiceProperty)
                prop is Boolean ->
                    checkBox(valueProp.displayName, effectiveProp::value.withType()!!)
                        .applyToComponent {
                            addActionListener { effectiveProp.preview = isSelected }
                        }
                prop is String ->
                    textField(effectiveProp::value.withType()!!)
                        .applyToComponent {
                            document.addDocumentListener(
                                DocumentChangeListener {
                                    effectiveProp.preview = text
                                }
                            )
                        }
                prop is Int ->
                    spinner(effectiveProp::value.withType()!!, Int.MIN_VALUE, Int.MAX_VALUE)
                        .applyToComponent {
                            addChangeListener { effectiveProp.preview = value }
                        }
                else -> throw IllegalArgumentException("Not yet implemented!")
            }
            enableIf(effectiveProp.activeCondition)
            valueProp.description?.also { component(ContextInfoLabel(it)) }
        }
    }

    private fun Row.addChoiceProperty(choiceProperty: ChoiceProperty<Any, Any>) {
        if (choiceProperty.choices.size > 3) {
            comboBox(
                CollectionComboBoxModel(choiceProperty.choices),
                choiceProperty::choiceValue,
                renderer = SimpleListCellRenderer.create { obj -> obj?.let { choiceProperty.renderer(it) } ?: "" }
            )
        } else {
            buttonGroup {
                choiceProperty.choices.forEach { item ->
                    row {
                        radioButton(DynamicText { choiceProperty.renderer(item) }).applyToComponent {
                            isSelected = choiceProperty.value == item
                            addActionListener { if (isSelected) choiceProperty.preview = item }
                        }
                        enableIf(choiceProperty.activeCondition)
                    }
                }
            }
        }
    }

    private fun RowBuilder.maybeTitledRow(name: Text?, init: Row.() -> Unit): Row {
        return if (name === UNNAMED_GROUP_TITLE || !name.isConstantNullOrEmpty()) {
            titledRow(name, init)
        } else {
            row(init = init)
        }
    }

    private fun RowBuilder.maybeNamedRow(name: Text?, init: Row.() -> Unit): Row {
        return if (!name.isConstantNullOrEmpty()) {
            row(name, init = init)
        } else {
            row(init = init)
        }
    }
}
