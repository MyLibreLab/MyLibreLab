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
import com.github.weisj.darklaf.components.tabframe.JTabFrame;
import com.github.weisj.darklaf.components.tabframe.TabFrameContent;
import com.github.weisj.darklaf.util.Alignment;

public class PersistentTabFrame extends JTabFrame implements PersistentComponent {

    public static final String KEY_ENABLED = "enabled";
    public static final String KEY_INDEX = "selectedIndex";
    private final PersistenceHelper persistenceHelper = new PersistenceHelper(this);

    @Override
    protected TabFrameContent createContentPane() {
        return new PersistentTabFrameContentPane();
    }

    @NotNull
    @Override
    public PersistenceNode saveState() {
        var node = persistenceHelper.createInfo();
        var cont = (PersistentTabFrameContentPane) getContentPane().getComponent();
        for (var a : Alignment.values()) {
            var alignmentNode = node.getSubNode(a.toString());
            alignmentNode.insert(KEY_ENABLED, cont.isEnabled(a));
            alignmentNode.insert(KEY_INDEX, getSelectedIndex(a));
        }
        node.insert(cont.getIdentifier(), cont.saveState());
        return node;
    }

    @Override
    public void loadState(@NotNull final PersistenceNode node) {
        var cont = (PersistentTabFrameContentPane) getContentPane().getComponent();
        for (var a : new Alignment[] {Alignment.NORTH, Alignment.NORTH_EAST, Alignment.SOUTH, Alignment.SOUTH_WEST,
                Alignment.WEST, Alignment.NORTH_WEST, Alignment.EAST, Alignment.SOUTH_EAST}) {
            if (a == Alignment.CENTER) continue;
            var alignmentNode = node.getSubNode(a.toString());
            var enabled = alignmentNode.getBoolean(KEY_ENABLED, cont.isEnabled(a));
            int index = enabled ? alignmentNode.getInt(KEY_INDEX, 0) : getSelectedIndex(a);
            if (index >= 0 && index < getTabCountAt(a)) {
                toggleTab(a, getSelectedIndex(a), enabled);
            }
        }
        cont.loadState(node.getSubNode(cont.getIdentifier()));
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
    public void setPersistent(boolean persistent, @NotNull String identifier) {
        persistenceHelper.setPersistent(persistent, identifier);
        ((PersistentTabFrameContentPane) getContentPane()).setPersistent(false, "tab_frame_content_pane");
    }
}
