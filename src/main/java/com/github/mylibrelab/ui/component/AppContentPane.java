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

import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

import com.github.mylibrelab.service.ServiceManager;
import com.github.mylibrelab.ui.menu.ApplicationContext;
import com.github.mylibrelab.ui.module.ApplicationModule;
import com.github.mylibrelab.ui.persistent.PersistenceHelper;
import com.github.mylibrelab.ui.persistent.PersistenceNode;
import com.github.mylibrelab.ui.persistent.PersistentComponent;
import com.github.mylibrelab.ui.persistent.component.PersistentTabFrame;
import com.github.weisj.darklaf.components.tabframe.TabFramePopup;
import com.github.weisj.darklaf.util.Alignment;
import com.github.weisj.darklaf.util.PropertyUtil;

import kotlin.Pair;

public class AppContentPane extends JPanel implements PersistentComponent {

    public static final String KEY_APP_MODULE = "ApplicationModule.parent";
    private static final String KEY_ALIGNMENT = "alignment";
    private static final String KEY_INDEX = "index";

    private final Map<ApplicationModule, TabFramePopup> components;
    private final PersistentTabFrame tabFrame;

    public AppContentPane() {
        components = new HashMap<>();
        setLayout(new BorderLayout());
        tabFrame = new PersistentTabFrame();
        ServiceManager.getAllServices(ApplicationModule.class).forEach(c -> {
            Logger.debug("Loaded " + c.getIdentifier());
            components.put(c, null);
        });

        // This will be the center editor.
        var editor = new JPanel();
        editor.setPreferredSize(new Dimension(1000, 600));
        editor.setLayout(new GridBagLayout());
        editor.add(new JLabel("Center Content"));
        tabFrame.setContent(editor);
        add(tabFrame);
        setPersistent(true, "app_content_pane");
    }

    private final PersistenceHelper persistenceHelper = new PersistenceHelper(this);

    @NotNull
    @Override
    public PersistenceNode saveState() {
        var node = persistenceHelper.createInfo();
        node.insert(tabFrame.getIdentifier(), tabFrame.saveState());
        components.forEach((c, popup) -> {
            var compNode = node.getSubNode(c.getIdentifier());
            compNode.insert(KEY_ALIGNMENT, popup.getAlignment().name());
            compNode.insert(KEY_INDEX, popup.getIndex());
        });
        return node;
    }

    @Override
    public void loadState(@NotNull final PersistenceNode info) {
        var tabFrameIdentifier = tabFrame.getIdentifier();
        EnumMap<Alignment, List<Pair<ApplicationModule, Integer>>> indexMap = new EnumMap<>(Alignment.class);
        components.forEach((c, value) -> {
            var node = info.getSubNode(c.getIdentifier());
            var alignStr = node.getString(KEY_ALIGNMENT, "");
            var pos = alignStr.isEmpty() ? c.getPreferredPosition() : Alignment.valueOf(alignStr);
            var index = node.getInt(KEY_INDEX, Integer.MAX_VALUE);
            indexMap.computeIfAbsent(pos, a -> new ArrayList<>()).add(new Pair<>(c, index));
        });
        var tabFrameNode = info.getSubNode(tabFrameIdentifier);
        indexMap.forEach((a, list) -> {
            list.sort(Comparator.comparingInt(Pair<ApplicationModule, Integer>::component2));
            list.forEach(p -> {
                var c = p.component1();
                var tab = c.createPopupTab();
                ApplicationContext.getApplicationContext()
                        .getModuleState().registerModule(c, tab);

                /*
                 * Set the KEY_APP_MODULE property, which is used by ActionUtils to retrieve the ActionContext.
                 */
                PropertyUtil.installProperty((JComponent) tab.getContentPane(), KEY_APP_MODULE, c);
                components.put(c, tab);

                var tabComp =
                        new TextTabFrameTabLabel(c.getTitle(), c.getIcon(), a, tabFrame.getTabCountAt(a), tabFrame);
                tabFrame.addTab(tab, tabComp, a);
                var alignmentNode = tabFrameNode.getSubNode(tab.getAlignment().name());
                if (alignmentNode.get(PersistentTabFrame.KEY_ENABLED) == null) {
                    alignmentNode.insert(PersistentTabFrame.KEY_ENABLED, c.openByDefault());
                    if (c.openByDefault() && alignmentNode.get(PersistentTabFrame.KEY_INDEX) == null) {
                        alignmentNode.insert(PersistentTabFrame.KEY_INDEX, tabFrame.getTabCountAt(a) - 1);
                    }
                }
            });
        });
        tabFrame.loadState(info.getSubNode(tabFrameIdentifier));
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
        tabFrame.setPersistent(false, "tabFrame");
    }
}
