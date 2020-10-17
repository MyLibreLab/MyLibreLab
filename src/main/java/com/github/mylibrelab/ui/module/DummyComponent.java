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

package com.github.mylibrelab.ui.module;

import java.awt.*;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;

import com.github.mylibrelab.text.Text;
import com.github.mylibrelab.text.component.TextLabel;
import com.github.weisj.darklaf.util.Alignment;

public class DummyComponent extends DefaultApplicationModule {

    public DummyComponent(final String title, final Alignment alignment) {
        super(Text.of(title), null, alignment);
    }

    @Override
    protected @NotNull JComponent createComponent() {
        var panel = new JPanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(100, 100));
        panel.add(new TextLabel(getTitle()));
        return panel;
    }

    @Override
    public boolean openByDefault() {
        return true;
    }

    @AppModule
    public static class DummyA extends DummyComponent {

        public DummyA() {
            super("Dummy A", Alignment.NORTH);
        }
    }

    @AppModule
    public static class DummyB extends DummyComponent {

        public DummyB() {
            super("Dummy B", Alignment.NORTH_EAST);
        }
    }

    @AppModule
    public static class DummyC extends DummyComponent {

        public DummyC() {
            super("Dummy C", Alignment.EAST);
        }
    }

    @AppModule
    public static class DummyD extends DummyComponent {

        public DummyD() {
            super("Dummy D", Alignment.SOUTH_EAST);
        }
    }

    @AppModule
    public static class DummyE extends DummyComponent {

        public DummyE() {
            super("Dummy E", Alignment.SOUTH);
        }
    }

    @AppModule
    public static class DummyF extends DummyComponent {

        public DummyF() {
            super("Dummy F", Alignment.SOUTH_WEST);
        }
    }

    @AppModule
    public static class DummyG extends DummyComponent {

        public DummyG() {
            super("Dummy G", Alignment.WEST);
        }
    }

    @AppModule
    public static class DummyH extends DummyComponent {

        public DummyH() {
            super("Dummy H", Alignment.NORTH_WEST);
        }
    }
}
