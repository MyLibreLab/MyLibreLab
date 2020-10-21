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

package com.github.mylibrelab.settings

import com.github.mylibrelab.resources.Resources
import com.github.mylibrelab.settings.api.*
import com.github.mylibrelab.util.*
import com.github.weisj.darklaf.LafManager
import com.github.weisj.darklaf.theme.Theme
import com.github.weisj.darklaf.theme.info.AccentColorRule
import com.github.weisj.darklaf.theme.info.FontSizeRule
import java.awt.Color

@SettingsProvider
class ThemeSettingsProvider : SingletonSettingsContainerProvider({ ThemeSettings })

/*
 * Delegate the Settings from 'com.github.weisj.darklaf.settings.ThemeSettings'
 */
object ThemeSettings : DefaultSettingsContainer(identifier = "theme") {

    private val DEFAULT_BASE_THEME = LafManager.getTheme()
    private val ALL_THEMES = LafManager.getRegisteredThemes().let { themes ->
        themes.forEach {
            val displayName = Resources.getString("themes.${it.prefix}.displayName")
            LafManager.replaceTheme(it, it.withDisplayName(displayName))
        }
        LafManager.getRegisteredThemes()
    }
    private val themeSettings = com.github.weisj.darklaf.settings.ThemeSettings.getInstance()

    private var baseTheme: Theme by themeSettings::theme

    private var fontSizeRule: FontSizeRule by themeSettings::fontSizeRule
    private var colorRule: AccentColorRule by themeSettings::accentColorRule

    private var useSystemPreferences: Boolean by delegate(
        themeSettings::isSystemPreferencesEnabled,
        themeSettings::setSystemPreferencesEnabled
    )
    private var themeFollowsSystem: Boolean by delegate(
        themeSettings::isThemeFollowsSystem,
        themeSettings::setThemeFollowsSystem
    )
    private var fontSizeFollowsSystem: Boolean by delegate(
        themeSettings::isFontSizeFollowsSystem,
        themeSettings::setFontSizeFollowsSystem
    )
    private var accentColorFollowsSystem: Boolean by delegate(
        themeSettings::isAccentColorFollowsSystem,
        themeSettings::setAccentColorFollowsSystem
    )
    private var selectionColorFollowsSystem: Boolean by delegate(
        themeSettings::isSelectionColorFollowsSystem,
        themeSettings::setSelectionColorFollowsSystem
    )

    init {
        hidden {
            val themeParser = transformerOf(
                write = { name -> ALL_THEMES.find { it.name == name } ?: DEFAULT_BASE_THEME },
                read = Theme::getName
            )
            val colorParser = transformerOf(
                write = { hex -> if (hex != -1) Color(hex) else null },
                read = Color::getRGB.or(-1)
            ) andThen intParser()
            val fontRuleParser = transformerOf(
                write = FontSizeRule::relativeAdjustment,
                read = FontSizeRule::getPercentage
            ) andThen intParser()
            val colorRuleParser = pairParser(
                toType = AccentColorRule::fromColor,
                firstAccessor = AccentColorRule::getAccentColor,
                secondAccessor = AccentColorRule::getSelectionColor,
                parser = colorParser
            )

            persistentProperty(value = ::baseTheme, transformer = themeParser)
            persistentProperty(value = ::fontSizeRule, transformer = fontRuleParser)
            persistentProperty(value = ::colorRule, transformer = colorRuleParser)
            persistentBooleanProperty(value = ::useSystemPreferences)
            persistentBooleanProperty(value = ::themeFollowsSystem)
            persistentBooleanProperty(value = ::fontSizeFollowsSystem)
            persistentBooleanProperty(value = ::accentColorFollowsSystem)
            persistentBooleanProperty(value = ::selectionColorFollowsSystem)
        }
    }

    override fun onSettingsUpdate(loaded: Boolean) {
        if (loaded) {
            // We allow for different instances of the application to have different themes.
            themeSettings.apply()
            if (!LafManager.isInstalled()) {
                LafManager.install()
            }
        }
    }
}
