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

import java.util.function.BiConsumer;

import javax.swing.*;

import com.github.mylibrelab.ui.persistent.PersistenceHelper;
import com.github.mylibrelab.ui.persistent.PersistenceInfo;
import com.github.mylibrelab.ui.persistent.PersistentComponent;
import com.github.weisj.darklaf.components.tabframe.TabFrameContentPane;
import com.github.weisj.darklaf.components.tabframe.ToggleSplitPane;

public class PersistentTabFrameContentPane extends TabFrameContentPane implements PersistentComponent {

    private final PersistenceHelper persistenceHelper = new PersistenceHelper(this);

    @Override
    protected ToggleSplitPane createSplitPane(String name) {
        return new PersistentToggleSplitPane(name, false);
    }

    @Override
    public PersistenceInfo saveState() {
        var info = persistenceHelper.getInfo();
        info.clear();
        BiConsumer<JSplitPane, Void> saver = (s, o) -> {
            var sp = (PersistentToggleSplitPane) s;
            info.merge(sp.saveState(), sp.getIdentifier());
        };
        setupSplitPanes(saver, null);
        setupSplitterPanes(saver, null);
        return info;
    }

    @Override
    public void loadState(PersistenceInfo info) {
        BiConsumer<JSplitPane, Void> loader = (s, o) -> {
            var sp = (PersistentToggleSplitPane) s;
            sp.loadState(info.getSubTree(sp.getIdentifier()));
        };
        setupSplitPanes(loader, null);
        setupSplitterPanes(loader, null);
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
        // Sets the identifier. But don't actually register the be restored.
        // This part is done in this class.
        ((PersistentToggleSplitPane) topSplit).setPersistent(false, "topSplit");
        ((PersistentToggleSplitPane) bottomSplit).setPersistent(false, "bottomSplit");
        ((PersistentToggleSplitPane) leftSplit).setPersistent(false, "leftSplit");
        ((PersistentToggleSplitPane) rightSplit).setPersistent(false, "rightSplit");
        ((PersistentToggleSplitPane) topSplitter).setPersistent(false, "topSplitter");
        ((PersistentToggleSplitPane) bottomSplitter).setPersistent(false, "bottomSplitter");
        ((PersistentToggleSplitPane) leftSplitter).setPersistent(false, "leftSplitter");
        ((PersistentToggleSplitPane) rightSplitter).setPersistent(false, "rightSplitter");
    }
}
