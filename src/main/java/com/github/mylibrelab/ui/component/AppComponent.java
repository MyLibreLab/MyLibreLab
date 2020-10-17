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

package com.github.mylibrelab.ui.component;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.github.weisj.darklaf.components.tabframe.PanelPopup;
import com.github.weisj.darklaf.components.tabframe.TabFramePopup;
import com.github.weisj.darklaf.components.tabframe.TabbedPopup;
import com.github.weisj.darklaf.util.Alignment;

/**
 * A pluggable component which corresponds to a tab in the application frame. Classes implementing
 * {@link AppComponent} should be annotated with {@code @Service(AppComponent.class)}
 */
public interface AppComponent {

    /**
     * The display title of the component.
     *
     * @return the title of the component.
     */
    @NotNull
    String getTitle();

    /**
     * The icon of the component. If the component has no icon returns {@link null}.
     *
     * @return the icon.
     */
    @Nullable
    default Icon getIcon() {
        return null;
    }

    /**
     * Returns the preferred docking position in the application frame. This value may be ignored if the
     * user has specified a different position.
     *
     * @return the preferred position.
     */
    @NotNull
    default Alignment getPreferredPosition() {
        return Alignment.SOUTH_WEST;
    }

    /**
     * Returns the UI for this component.
     *
     * @return the visual component.
     */
    @NotNull
    JComponent getComponent();

    /**
     * Returns the popup to add to the docking frame. This function can be overwritten to supply a
     * tabbed style popup using the {@link TabbedPopup}.
     *
     * @return The associated {@link TabFramePopup}.
     */
    @NotNull
    default TabFramePopup getPopupTab() {
        return new PanelPopup(getTitle(), getIcon(), getComponent());
    }

    /**
     * Determines whether the component should be open by default. This value will be ignored after the
     * first application start.
     *
     * @return whether the component should be open by default.
     */
    default boolean openByDefault() {
        return false;
    }

}
