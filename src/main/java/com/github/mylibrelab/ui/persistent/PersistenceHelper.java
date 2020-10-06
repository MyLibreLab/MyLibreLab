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

package com.github.mylibrelab.ui.persistent;

public class PersistenceHelper implements PersistentComponent {

    private final PersistentComponent component;
    private String identifier = "";
    private boolean persistent = false;
    private final PersistenceInfo info = new PersistenceInfo();

    public PersistenceHelper(final PersistentComponent component) {
        this.component = component;
    }

    @Override
    public PersistenceInfo saveState() {
        return new PersistenceInfo();
    }

    public PersistenceInfo getInfo() {
        return info;
    }

    @Override
    public void loadState(PersistenceInfo info) {}

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public boolean isPersistent() {
        return persistent;
    }

    public void setPersistent(final boolean persistent, final String identifier, final boolean register) {
        this.identifier = identifier;
        this.persistent = persistent;
        if (register && persistent) {
            PersistenceManager.INSTANCE.register(component);
        }
    }

    @Override
    public void setPersistent(final boolean persistent, final String identifier) {
        setPersistent(persistent, identifier, true);
    }
}
