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

import com.github.mylibrelab.settings.ThemeSettings
import com.github.mylibrelab.ui.layout.CCFlags
import com.github.mylibrelab.ui.layout.DialogPanel
import com.github.mylibrelab.ui.layout.createDefaultSpacingConfiguration
import com.github.mylibrelab.ui.layout.panel
import com.github.mylibrelab.ui.view.ViewOfTypeProviderFor
import com.github.mylibrelab.ui.view.ViewProvider
import com.github.weisj.darklaf.components.renderer.SimpleListCellRenderer
import com.github.weisj.darklaf.settings.ThemeSettingsPanel
import java.awt.Insets
import javax.swing.GroupLayout

@ViewProvider
class ThemeSettingsViewProvider :
    SettingsViewProviderBase<ThemeSettings>(),
    ViewOfTypeProviderFor<ThemeSettings, DialogPanel> {

    override fun createView(container: ThemeSettings): DialogPanel = panel {
        row {
            val spacing = createDefaultSpacingConfiguration()
            component(
                ThemeSettingsPanel(
                    GroupLayout.Alignment.LEADING,
                    Insets(spacing.verticalGap + 1, spacing.indentLevel - 4, 0, 0)
                ).apply {
                    border = null
                    container.themeSettings.setThemeSettingsPanel(this)
                    setThemeComboBoxRenderer(SimpleListCellRenderer.create(container.themeRenderer))
                    addChangeListener { container.onLafThemeSettingsUpdated(settingsConfiguration) }
                }
            ).apply {
                onReset { component.loadConfiguration(container.themeSettings.exportConfiguration()) }
                onIsModified { container.isConfigurationEqual(component.settingsConfiguration) }
                constraints(CCFlags.growX)
            }
        }
        container.subgroups.forEach {
            addGroup(it)
        }
    }
}
