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

import com.github.mylibrelab.resources.Resources
import com.github.mylibrelab.settings.Settings
import com.github.mylibrelab.settings.api.*
import com.github.mylibrelab.text.textOf
import com.github.mylibrelab.ui.UIStyle
import com.github.mylibrelab.ui.border.Borders
import com.github.weisj.darklaf.LafManager
import com.github.weisj.darklaf.components.DefaultButton
import java.awt.BorderLayout
import javax.swing.Box
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities
import javax.swing.WindowConstants.EXIT_ON_CLOSE

fun main() {
    Settings.loadState()
    Runtime.getRuntime().addShutdownHook(Thread { Settings.saveState() })
    SwingUtilities.invokeLater {
        LafManager.install()
        JFrame().apply {
            contentPane = JPanel(BorderLayout()).apply {
                val settingsPanel = SettingsPanel(SettingsStorage.containers)
                add(settingsPanel, BorderLayout.CENTER)
                val box = Box.createHorizontalBox().apply {
                    border = Borders.withSpacing(Borders.topLineBorder())
                    add(Box.createHorizontalGlue())
                    val applyButton = UIStyle.withText(
                        DefaultButton(""),
                        Resources.getResourceText("dialog.apply")
                    ).apply {
                        addActionListener { settingsPanel.apply() }
                    }
                    add(applyButton)
                }
                add(box, BorderLayout.SOUTH)
            }
            pack()
            setLocationRelativeTo(null)
            title = "Dummy Settings"
            defaultCloseOperation = EXIT_ON_CLOSE
        }.isVisible = true
    }
}

@SettingsProvider
class DummySettingsContainerProvider : SingletonSettingsContainerProvider({ DummySettingsContainer })

object DummySettingsContainer : DefaultSettingsContainer(identifier = "dummy") {

    private enum class EnumValue {
        ENUM_1,
        ENUM_2,
        ENUM_3,
        ENUM_4,
        ENUM_5
    }

    private enum class EnumValueSmall {
        ENUM_SMALL_1,
        ENUM_SMALL_2,
        ENUM_SMALL_3
    }

    private var b1 = true
    private var b2 = true
    private var b3 = false

    private var s1 = "Hello World!"
    private var s2 = "Hello Settings!"
    private var s3 = "Hello Settings!"

    private var i1 = 0
    private var i2 = 2

    private var e1 = EnumValue.ENUM_1
    private var e2 = EnumValueSmall.ENUM_SMALL_1

    init {
        presentation {
            displayName = textOf("Dummy Settings")
        }

        booleanProperty(displayName = textOf("String 2 enabled"), value = ::b1)
        booleanProperty(displayName = textOf("String Choice Group enabled"), value = ::b2)
        booleanProperty(displayName = textOf("Group 3 enabled"), value = ::b3)

        group("Group 1") {
            stringProperty(
                displayName = textOf("String 1"),
                value = ::s1
            )

            stringProperty(
                displayName = textOf("String 2"),
                description = textOf(
                    """
                    Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod
                    tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.
                    At vero eos et accusam et
                    """.trimIndent()
                ),
                value = ::s2
            ) {
                activeIf(::b1.isTrue())
            }

            property(displayName = textOf("Integer"), value = ::i1)
        }
        group(
            name = "Group 2 (Choices)",
            description = textOf(
                """
                    Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod
                    tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.
                    At vero eos et accusam et
                """.trimIndent()
            )
        ) {

            choiceProperty(displayName = textOf("Integer choice"), value = ::i2) {
                choices = (0..100).toList()
            }
            group {
                activeIf(::b2.isTrue())
                group {
                    choiceProperty(displayName = textOf("String choice"), value = ::s3) {
                        choices = (0..10).asSequence().map { "Item $it" }.toList()
                    }
                }
            }
            choiceProperty(displayName = textOf("Enum choice"), value = ::e1) {
                choices = EnumValue.values().asList()
            }
        }
        group("Group 3") {
            activeIf(::b3.isTrue())
            choiceProperty(displayName = textOf("Small Enum choice"), value = ::e2) {
                choices = EnumValueSmall.values().asList()
            }
            stringProperty(displayName = textOf("String 1 again"), value = ::s1)
        }
    }
}
