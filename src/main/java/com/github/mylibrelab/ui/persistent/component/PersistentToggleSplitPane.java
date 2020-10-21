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

package com.github.mylibrelab.ui.persistent.component;

import org.jetbrains.annotations.NotNull;

import com.github.mylibrelab.ui.persistent.PersistenceHelper;
import com.github.mylibrelab.ui.persistent.PersistenceNode;
import com.github.mylibrelab.ui.persistent.PersistentComponent;
import com.github.weisj.darklaf.components.tabframe.ToggleSplitPane;

public class PersistentToggleSplitPane extends ToggleSplitPane implements PersistentComponent {

    private static final String KEY_POSITION = "position";
    private static final String KEY_SAVED_POSITION = "savedPos";
    private static final String KEY_RESIZABLE = "resizable";
    private static final String KEY_LOCKED_POSITION = "locked";

    protected boolean persistResizableState;

    private final PersistenceHelper persistenceHelper = new PersistenceHelper(this);

    public PersistentToggleSplitPane() {
        this("", true);
    }

    public PersistentToggleSplitPane(final boolean persistResizableState) {
        this("", persistResizableState);
    }

    public PersistentToggleSplitPane(final String name) {
        this(name, true);
    }

    public PersistentToggleSplitPane(final String name, final boolean persistResizableState) {
        super(name);
        this.persistResizableState = persistResizableState;
    }

    public void setPersistResizableState(boolean persistResizableState) {
        this.persistResizableState = persistResizableState;
    }

    public boolean isPersistResizableState() {
        return persistResizableState;
    }

    @NotNull
    @Override
    public PersistenceNode saveState() {
        var node = persistenceHelper.createInfo();
        node.insert(KEY_POSITION, getDividerLocation());
        node.insert(KEY_SAVED_POSITION, getRestorePosition());
        if (persistResizableState) {
            node.insert(KEY_LOCKED_POSITION, getLockedPosition());
            node.insert(KEY_RESIZABLE, isResizable());
        }
        return node;
    }

    @Override
    public void loadState(@NotNull final PersistenceNode node) {
        double restorePos = clamp(node.getDouble(KEY_SAVED_POSITION, -1), -1, 1);
        if (restorePos >= 0) {
            savePosition(restorePos);
        }
        int pos = node.getInt(KEY_POSITION, -1);
        if (pos >= 0) {
            forceSetDividerLocation(pos);
        } else {
            forceSetDividerLocation(clamp(getRestorePosition(), 0, 1));
        }
        if (persistResizableState) {
            setResizable(node.getBoolean(KEY_RESIZABLE, true),
                    clamp(node.getDouble(KEY_LOCKED_POSITION, getLockedPosition()), 0, 1));
        }
    }

    private double clamp(final double value, final double min, final double max) {
        return Math.max(min, Math.min(max, value));
    }

    @NotNull
    @Override
    public String getIdentifier() {
        return persistenceHelper.getIdentifier();
    }

    @Override
    public boolean isPersistent() {
        return persistenceHelper.isPersistent();
    }

    @Override
    public void setPersistent(final boolean persistent, @NotNull final String identifier) {
        persistenceHelper.setPersistent(persistent, identifier);
    }
}
