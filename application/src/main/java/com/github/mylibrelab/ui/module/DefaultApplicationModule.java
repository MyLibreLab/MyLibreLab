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

package com.github.mylibrelab.ui.module;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.github.mylibrelab.action.MutableActionContext;
import com.github.mylibrelab.text.Text;
import com.github.mylibrelab.ui.view.ViewFactory;
import com.github.weisj.darklaf.util.Alignment;

public abstract class DefaultApplicationModule implements ApplicationModule {

    private JComponent component;
    private final Text title;
    private final Icon icon;
    private final Alignment preferredAlignment;
    protected final MutableActionContext actionContext = new MutableActionContext();

    /**
     * Creates new application module.
     *
     * @param displayTitle the title of the module.
     */
    protected DefaultApplicationModule(@NotNull final Text displayTitle) {
        this(displayTitle, null);
    }

    /**
     * Creates new application module.
     *
     * @param displayTitle the title of the module.
     * @param icon the icon of the module.
     */
    protected DefaultApplicationModule(@NotNull final Text displayTitle, @Nullable final Icon icon) {
        this(displayTitle, icon, Alignment.SOUTH_WEST);
    }

    /**
     * Creates new application module.
     *
     * @param displayTitle the title of the module.
     * @param icon the icon of the module.
     * @param preferredAlignment the preferred alignment position.
     */
    protected DefaultApplicationModule(@NotNull final Text displayTitle, @Nullable final Icon icon,
            @NotNull final Alignment preferredAlignment) {
        this.title = displayTitle;
        this.icon = icon;
        this.preferredAlignment = preferredAlignment;
    }

    @Override
    public MutableActionContext getActionContext() {
        return actionContext;
    }

    @Override
    public @NotNull Alignment getPreferredPosition() {
        return preferredAlignment;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return icon;
    }

    @Override
    public @NotNull String getIdentifier() {
        return getClass().getName();
    }

    @Override
    public @NotNull Text getTitle() {
        return title;
    }

    @Override
    public @NotNull JComponent getComponent() {
        if (component == null) {
            component = createComponent();
        }
        return component;
    }

    /**
     * Creates the {@link JComponent} for this component.
     *
     * @return the {@link JComponent}.
     */
    @NotNull
    protected JComponent createComponent() {
        return ViewFactory.createView(this);
    }
}
