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

import com.github.mylibrelab.ui.persistent.PersistenceHelper;
import com.github.mylibrelab.ui.persistent.PersistenceInfo;
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

    @Override
    public PersistenceInfo saveState() {
        var persistenceInfo = persistenceHelper.getInfo();
        persistenceInfo.putValue(KEY_POSITION, getRelativeDividerLocation());
        persistenceInfo.putValue(KEY_SAVED_POSITION, getRestorePosition());
        if (persistResizableState) {
            persistenceInfo.putValue(KEY_LOCKED_POSITION, getLockedPosition());
            persistenceInfo.putValue(KEY_RESIZABLE, isResizable());
        }
        return persistenceInfo;
    }

    @Override
    public void loadState(final PersistenceInfo info) {
        forceSetDividerLocation(getPositionValue(info, KEY_POSITION, getRelativeDividerLocation()));
        savePosition(getPositionValue(info, KEY_SAVED_POSITION, getRestorePosition()));
        if (persistResizableState) {
            setResizable(false, getPositionValue(info, KEY_LOCKED_POSITION, getLockedPosition()));
            setResizable(info.getBoolean(KEY_RESIZABLE, true));
        }
    }

    private double getPositionValue(final PersistenceInfo source, final String key, final double fallback) {
        return Math.max(0.0, Math.min(1.0, source.getDouble(key, fallback)));
    }

    @Override
    public String getIdentifier() {
        return persistenceHelper.getIdentifier();
    }

    @Override
    public boolean isPersistent() {
        return persistenceHelper.isPersistent();
    }

    @Override
    public void setPersistent(final boolean persistent, final String identifier) {
        persistenceHelper.setPersistent(persistent, identifier);
    }
}
