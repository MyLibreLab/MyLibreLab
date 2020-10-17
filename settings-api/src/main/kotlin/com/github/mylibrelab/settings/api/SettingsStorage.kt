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

import com.github.mylibrelab.service.ServiceManager
import java.util.prefs.Preferences

object SettingsStorage {

    private const val ROOT_GROUP_NAME = "__root__group__"
    private const val SETTING_VERSION_NAME = "__settings__version__"
    private const val SETTINGS_VERSION = 1

    private val preferencesRoot = Preferences.userRoot().node(javaClass.name)
    private val properties: MutableMap<PropertyIdentifier, PersistentValueProperty<Any>> = HashMap()
    @Volatile
    private var saving: Boolean = false
    val containers: List<SettingsContainer> =
        ServiceManager.getAllServices(SettingsContainerProvider::class.java)
            .asSequence()
            .filter { it.enabled }
            .map { it.create() }
            .onEach { it.init() }
            .toList()

    init {
        containers
            .flatMap { it.allProperties() }
            .mapNotNull { it.asPersistent() }
            .forEach {
                val identifier = it.propertyIdentifier
                properties[identifier]?.let { other ->
                    throw IllegalStateException(
                        "$it clashes with $other. Property with identifier $identifier already defined."
                    )
                }
                properties[it.propertyIdentifier] = it
            }
        properties.entries.groupBy(
            {
                it.key.groupIdentifier
            },
            { it.value }
        ).forEach { (identifier, propertyList) ->
            preferencesRoot.node(identifier).addPreferenceChangeListener {
                if (saving) return@addPreferenceChangeListener
                propertyList.asSequence().map { it.container }.distinct().forEach { container ->
                    loadSettings(container)
                }
            }
        }
    }

    private fun loadSettings(container: SettingsContainer) {
        container.allProperties().forEach {
            val identifier = it.propertyIdentifier
            val node = preferencesRoot.node(identifier.groupIdentifier)
            val savedValue = node[it.name] ?: return@forEach
            it.value = savedValue
        }
        container.onSettingsUpdate()
    }

    private fun Preferences.convertToEntries(): List<Entry> =
        this.childrenNames().flatMap { groupIdentifier ->
            val node = this.node(groupIdentifier!!)
            node.keys().mapNotNull { key ->
                val value = node[key] ?: return@mapNotNull null
                Entry(groupIdentifier, key!!, value)
            }
        }

    /**
     * Load the settings from storage. Automatically populates any active SettingsContainer.
     */
    fun loadState() {
        // Load the values from the preferences nodes.
        val entries = preferencesRoot.convertToEntries()

        // If the current settings version is higher than the stored settings, then we discard any unused
        // values. The entry might have been removed from the application.
        // We can't blindly discard all unused entries as they may be used in a SettingsContainer that currently
        // isn't loaded (e.g. if the container is only enabled on a specific operating system).
        val discardUnusedEntries =
            entries.find { it.groupIdentifier == ROOT_GROUP_NAME && it.name == SETTING_VERSION_NAME }?.let {
                it.value.toInt() < SETTINGS_VERSION
            } ?: false

        entries.forEach { entry ->
            val identifier = PropertyIdentifier(entry.groupIdentifier, entry.name)
            val property = properties[identifier]
            if (property != null) {
                property.value = entry.value
            } else if (discardUnusedEntries) {
                // Remove the entry from the storage as it is no longer used.
                preferencesRoot.node(entry.groupIdentifier).remove(entry.name)
            }
        }
        containers.forEach { it.onSettingsUpdate(loaded = true) }
    }

    /**
     * Save the settings to the storage.
     */
    fun saveState() {
        saving = true
        containers.forEach { it.onSettingsSave() }
        properties.map { (k, v) -> Entry(k.groupIdentifier, k.name, v.value) }.toMutableList().also {
            it.add(Entry(ROOT_GROUP_NAME, SETTING_VERSION_NAME, SETTINGS_VERSION.toString()))
        }.groupBy { it.groupIdentifier }.forEach { (identifier, entries) ->
            val preferencesNode = preferencesRoot.node(identifier)
            preferencesNode ?: return@forEach
            entries.forEach { preferencesNode[it.name] = it.value }
        }
        saving = false
    }

    /**
     * Reset all settings. This will not restore the default values of the current session and has no effect
     * until the application has restarted.
     */
    fun reset() = clearPreferences(preferencesRoot)

    private fun clearPreferences(preferences: Preferences) {
        preferences.clear()
        preferences.childrenNames().forEach { clearPreferences(preferences.node(it)) }
    }

    private val <T> ValueProperty<T>.propertyIdentifier
        get() = PropertyIdentifier(group.getIdentifierPath(), name)

    data class PropertyIdentifier(val groupIdentifier: String, val name: String)

    data class Entry(var groupIdentifier: String = "", var name: String = "", var value: String = "")
}

private operator fun Preferences.set(name: String, value: String) = this.put(name, value)
private operator fun Preferences.get(name: String) = this.get(name, null)
