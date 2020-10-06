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
import java.util.ServiceLoader;

import javax.swing.*;

import com.github.mylibrelab.ui.persistent.component.PersistentTabFrame;
import com.github.weisj.darklaf.util.Alignment;

public class AppContentPane extends JPanel {

    private final PersistentTabFrame tabFrame;

    public AppContentPane() {
        setLayout(new BorderLayout());
        tabFrame = new PersistentTabFrame();
        tabFrame.setPersistent(true, "app_content_pane");
        ServiceLoader.load(AppComponent.class).forEach(c -> {
            var popup = c.getPopupTab();
            var position = c.getPreferredPosition();
            if (position == null) position = Alignment.SOUTH_WEST;
            tabFrame.addTab(popup, c.getTitle(), c.getIcon(), position);
            if (c.openByDefault()) {
                tabFrame.toggleTab(popup.getAlignment(), popup.getIndex(), true);
            }
        });

        // Todo this will be the center editor.
        var editor = new JPanel();
        editor.setPreferredSize(new Dimension(1000, 600));
        editor.setLayout(new GridBagLayout());
        editor.add(new JLabel("Center Content"));
        tabFrame.setContent(editor);

        add(tabFrame);
    }

    // @Override
    // public void addNotify() {
    // super.addNotify();
    // Timer t = new Timer(2000, e -> {
    // tabFrame.toggleTab(Alignment.NORTH, 0, true);
    // tabFrame.toggleTab(Alignment.NORTH_EAST, 0, false);
    // });
    // t.setRepeats(false);
    // t.start();
    // }
}
