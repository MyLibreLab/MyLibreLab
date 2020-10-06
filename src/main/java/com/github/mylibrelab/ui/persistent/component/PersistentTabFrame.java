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
import com.github.weisj.darklaf.components.tabframe.JTabFrame;
import com.github.weisj.darklaf.components.tabframe.TabFrameContent;
import com.github.weisj.darklaf.util.Alignment;

public class PersistentTabFrame extends JTabFrame implements PersistentComponent {

    private static final String INDEX_SUFFIX = ".index";
    private final PersistenceHelper persistenceHelper = new PersistenceHelper(this);

    @Override
    protected TabFrameContent createContentPane() {
        return new PersistentTabFrameContentPane();
    }

    @Override
    public PersistenceInfo saveState() {
        var persistenceInfo = persistenceHelper.getInfo();
        persistenceInfo.clear();
        var cont = (PersistentTabFrameContentPane) getContentPane().getComponent();
        for (var a : Alignment.values()) {
            var enabled = cont.isEnabled(a);
            var index = getSelectedIndex(a);
            persistenceInfo.putValue(a.toString(), enabled);
            persistenceInfo.putValue(a.toString() + INDEX_SUFFIX, index);
        }
        var info = cont.saveState();
        info.merge(persistenceInfo);
        return info;
    }

    @Override
    public void loadState(final PersistenceInfo info) {
        var cont = (PersistentTabFrameContentPane) getContentPane().getComponent();
        for (var a : Alignment.values()) {
            var index = info.getInt(a.toString() + INDEX_SUFFIX, 0);
            if (index >= 0) {
                var enabled = info.getBoolean(a.toString(), isTabSelected(a, index));
                toggleTab(a, index, enabled);
            }
        }
        cont.loadState(info.getSubTree(cont.getIdentifier()));
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
        ((PersistentTabFrameContentPane) getContentPane()).setPersistent(false, "tab_frame_content_pane");
    }
}
