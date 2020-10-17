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

package com.github.mylibrelab.action;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.github.mylibrelab.text.Text;

/**
 * This class encapsulates the information for the visual representation for an {@link AnAction}.
 */
public class Presentation {

    private Icon icon;
    private Text displayName;
    private boolean enabled = true;
    private boolean visible = true;
    private boolean compact = false;

    /**
     * Creates a new default {@link Presentation}.
     */
    public Presentation() {
        this(Text.of(""));
    }

    /**
     * Creates a new {@link Presentation} with the given name.
     *
     * @param displayName the display name.
     */
    public Presentation(@NotNull final Text displayName) {
        this.displayName = displayName;
    }

    /**
     * Determines whether the action is enabled.
     *
     * @return true if enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Set whether the action is enabled.
     *
     * @param enabled true if enabled.
     * @return this
     */
    @NotNull
    public Presentation setEnabled(final boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    /**
     * Determines whether the action should be hidden if it disabled.
     *
     * @return true if disabled means hidden.
     */
    public boolean isCompact() {
        return compact;
    }

    /**
     * Set whether the action should be hidden if it disabled.
     *
     * @param compact true if disabled means hidden.
     * @return this.
     */
    @NotNull
    public Presentation setCompact(final boolean compact) {
        this.compact = compact;
        return this;
    }

    /**
     * Determines whether the action is visible.
     *
     * @return true if visible.
     */
    public boolean isVisible() {
        return isCompact() ? isEnabled() && visible : visible;
    }

    /**
     * Set whether the action is visible.
     *
     * @param visible true if visible.
     * @return this
     */
    @NotNull
    public Presentation setVisible(final boolean visible) {
        this.visible = visible;
        return this;
    }

    /**
     * Returns the display name of this action.
     *
     * @return the display name.
     */
    @NotNull
    public Text getDisplayName() {
        return displayName;
    }

    /**
     * Set the display name of this action.
     *
     * @param displayName the display name.
     * @return this
     */
    @NotNull
    public Presentation setDisplayName(@NotNull final Text displayName) {
        this.displayName = displayName;
        return this;
    }

    /**
     * Returns the icon of this action.
     *
     * @return the icon.
     */
    @Nullable
    public Icon getIcon() {
        return icon;
    }

    /**
     * Set the icon of this action.
     *
     * @param icon the icon.
     * @return this.
     */
    @NotNull
    public Presentation setIcon(@Nullable final Icon icon) {
        this.icon = icon;
        return this;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder("Presentation{");
        if (icon != null) sb.append("icon=").append(icon);
        if (displayName != null) sb.append(",displayName='").append(displayName).append('\'');
        sb.append(", enabled=").append(enabled).append(", visible=").append(visible).append(", compact=")
                .append(compact).append('}');
        return sb.toString();
    }
}
