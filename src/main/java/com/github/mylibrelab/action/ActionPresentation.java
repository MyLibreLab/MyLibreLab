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

import org.jetbrains.annotations.NotNull;

import com.github.mylibrelab.text.Text;
import com.github.mylibrelab.view.Presentation;

/**
 * This class encapsulates the information for the visual representation for an {@link AnAction}.
 */
public class ActionPresentation extends Presentation {

    private boolean enabled = true;
    private boolean visible = true;
    private boolean compact = false;

    /**
     * Creates a new default {@link ActionPresentation}.
     */
    public ActionPresentation() {
        this(Text.of(""));
    }

    /**
     * Creates a new {@link ActionPresentation} with the given name.
     *
     * @param displayName the display name.
     */
    public ActionPresentation(@NotNull final Text displayName) {
        super(displayName);
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
     */
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
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
     */
    public void setCompact(final boolean compact) {
        this.compact = compact;
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
     */
    public void setVisible(final boolean visible) {
        this.visible = visible;
    }


    @Override
    public String toString() {
        var sb = new StringBuilder("ActionPresentation{");
        if (icon != null) sb.append("icon=").append(icon);
        if (displayName != null) sb.append(",displayName='").append(displayName).append('\'');
        sb.append(", enabled=").append(enabled).append(", visible=").append(visible).append(", compact=")
                .append(compact).append('}');
        return sb.toString();
    }
}
