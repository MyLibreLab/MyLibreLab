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

import javax.swing.*;

import com.github.mylibrelab.ui.persistent.PersistenceHelper;
import com.github.mylibrelab.ui.persistent.PersistenceInfo;
import com.github.mylibrelab.ui.persistent.PersistentComponent;

public class PersistentTabbedPane extends JTabbedPane implements PersistentComponent {

    private static final String SELECTED_TAB_INDEX = "selectedTabIndex";
    private final PersistenceHelper persistenceHelper = new PersistenceHelper(this);

    @Override
    public PersistenceInfo saveState() {
        var info = persistenceHelper.getInfo();
        info.clear();
        info.putValue(SELECTED_TAB_INDEX, getSelectedIndex());
        return info;
    }

    @Override
    public void loadState(final PersistenceInfo info) {
        int index = info.getInt(SELECTED_TAB_INDEX, 0);
        if (index < getTabCount()) {
            setSelectedIndex(index);
        }
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
    public void setPersistent(boolean persistent, String identifier) {
        persistenceHelper.setPersistent(persistent, identifier);
    }
}
