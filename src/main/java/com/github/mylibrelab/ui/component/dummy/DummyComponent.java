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

package com.github.mylibrelab.ui.component.dummy;

import java.awt.*;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;

import com.github.mylibrelab.ui.component.AppComponent;
import com.github.weisj.darklaf.util.Alignment;

public class DummyComponent extends JPanel implements AppComponent {

    private final String title;
    private final Icon icon;
    private final Alignment alignment;

    public DummyComponent(final String title, final Icon icon, final Alignment alignment) {
        this.title = title;
        this.icon = icon;
        this.alignment = alignment;
        setPreferredSize(new Dimension(100, 100));
        setLayout(new GridBagLayout());
        add(new JLabel(title));
    }

    @NotNull
    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Icon getIcon() {
        return icon;
    }

    @NotNull
    @Override
    public Alignment getPreferredPosition() {
        return alignment;
    }

    @NotNull
    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public boolean openByDefault() {
        return true;
    }
}
