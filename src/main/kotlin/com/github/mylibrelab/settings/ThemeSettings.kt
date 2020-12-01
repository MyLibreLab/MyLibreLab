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
import com.github.mylibrelab.text.textOf
import com.github.mylibrelab.ui.icons.AllIcons
import com.github.mylibrelab.util.*
import com.github.weisj.darklaf.LafManager
import com.github.weisj.darklaf.settings.SettingsConfiguration
import com.github.weisj.darklaf.theme.*
import com.github.weisj.darklaf.theme.info.*
import java.awt.Color

@SettingsProvider
class ThemeSettingsProvider : SingletonSettingsContainerProvider({ ThemeSettings })

typealias LafThemeSettings = com.github.weisj.darklaf.settings.ThemeSettings

/*
 * Delegate the Settings from 'com.github.weisj.darklaf.settings.ThemeSettings'
 */
object ThemeSettings : DefaultSettingsContainer(identifier = "theme") {

    private val DEFAULT_BASE_THEME = LafManager.getTheme()
    private val ALL_THEMES = LafManager.getRegisteredThemes().toList()
    val themeSettings: LafThemeSettings = LafThemeSettings.getInstance()

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

    private var themeProvider = MutableThemeProvider(
        IntelliJTheme(), DarculaTheme(),
        HighContrastLightTheme(),
        HighContrastDarkTheme()
    ).also {
        LafManager.setThemeProvider(it)
    }

    val themeRenderer = { it: Theme -> Resources.getString("themes.${it.prefix}.displayName") }
    private val updateHandlers = mutableListOf<(SettingsConfiguration) -> Unit>()

    init {
        presentation {
            icon = AllIcons.THEME_SETTINGS
            displayName = Resources.getResourceText("settings.theme.displayName")
        }

        val themeParser = transformerOf(
            write = { name -> ALL_THEMES.find { it.name == name } ?: DEFAULT_BASE_THEME },
            read = Theme::getName
        )

        hidden {
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
                .bindPreview { it.theme }
            persistentProperty(value = ::fontSizeRule, transformer = fontRuleParser)
                .bindPreview { it.fontSizeRule }
            persistentProperty(value = ::colorRule, transformer = colorRuleParser)
                .bindPreview { it.accentColorRule }
            persistentBooleanProperty(value = ::useSystemPreferences)
                .bindPreview { it.isSystemPreferencesEnabled }
            persistentBooleanProperty(value = ::themeFollowsSystem)
                .bindPreview { it.isThemeFollowsSystem }
            persistentBooleanProperty(value = ::fontSizeFollowsSystem)
                .bindPreview { it.isFontSizeFollowsSystem }
            persistentBooleanProperty(value = ::accentColorFollowsSystem)
                .bindPreview { it.isAccentColorFollowsSystem }
            persistentBooleanProperty(value = ::selectionColorFollowsSystem)
                .bindPreview { it.isSelectionColorFollowsSystem }
        }

        group("preferred_themes", displayName = textOf("Theme Preferences")) {
            activeIf(::themeFollowsSystem.isTrue())
            persistentChoiceProperty(
                displayName = Resources.getResourceText("settings.theme.lightThemeLabel"),
                value = themeProvider::lightTheme,
                transformer = themeParser
            ) { choices = ALL_THEMES; renderer = themeRenderer }
            persistentChoiceProperty(
                displayName = Resources.getResourceText("settings.theme.darkThemeLabel"),
                value = themeProvider::darkTheme,
                transformer = themeParser
            ) { choices = ALL_THEMES; renderer = themeRenderer }
            persistentChoiceProperty(
                displayName = Resources.getResourceText("settings.theme.lightHighContrastThemeLabel"),
                value = themeProvider::lightHighContrastTheme,
                transformer = themeParser
            ) { choices = ALL_THEMES; renderer = themeRenderer }
            persistentChoiceProperty(
                displayName = Resources.getResourceText("settings.theme.darkHighContrastThemeLabel"),
                value = themeProvider::darkHighContrastTheme,
                transformer = themeParser
            ) { choices = ALL_THEMES; renderer = themeRenderer }
        }
    }

    private fun <T> PersistentValueProperty<T>.bindPreview(getter: (SettingsConfiguration) -> T) {
        updateHandlers.add {
            backingProperty.preview = getter(it)
        }
    }

    fun onLafThemeSettingsUpdated(config: SettingsConfiguration) {
        updateHandlers.forEach { it(config) }
    }

    fun isConfigurationEqual(config: SettingsConfiguration): Boolean = config.let {
        it.isSystemPreferencesEnabled != useSystemPreferences ||
            it.isThemeFollowsSystem != themeFollowsSystem ||
            it.isAccentColorFollowsSystem != accentColorFollowsSystem ||
            it.isSelectionColorFollowsSystem != selectionColorFollowsSystem ||
            it.isFontSizeFollowsSystem != fontSizeFollowsSystem ||
            it.accentColorRule != colorRule ||
            it.fontSizeRule != fontSizeRule ||
            Theme.baseThemeOf(it.theme) != Theme.baseThemeOf(baseTheme)
    }

    override fun onSettingsUpdate(updatedFromDisk: Boolean) {
        if (!updatedFromDisk) {
            // We allow for different instances of the application to have different themes.
            themeSettings.apply()
            if (!LafManager.isInstalled()) {
                LafManager.install()
            }
        }
    }

    override fun shouldBeDisplayed(): Boolean = true
}

internal class MutableThemeProvider(
    var lightTheme: Theme,
    var darkTheme: Theme,
    var lightHighContrastTheme: Theme,
    var darkHighContrastTheme: Theme
) : ThemeProvider {
    override fun getTheme(themeStyle: PreferredThemeStyle?): Theme {
        val light = themeStyle?.colorToneRule == ColorToneRule.LIGHT
        val highContrast = themeStyle?.contrastRule == ContrastRule.HIGH_CONTRAST
        return when {
            light && !highContrast -> lightTheme
            !light && !highContrast -> darkTheme
            light && highContrast -> lightHighContrastTheme
            !light && highContrast -> darkHighContrastTheme
            else -> lightTheme
        }.derive(themeStyle?.fontSizeRule, themeStyle?.accentColorRule)
    }
}
