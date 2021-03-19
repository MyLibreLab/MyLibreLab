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

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.github.mylibrelab.resources.Resources;
import com.github.mylibrelab.ui.UIStyle;
import com.github.mylibrelab.ui.border.Borders;
import com.github.weisj.darklaf.components.OverlayScrollPane;

public class LicencePanel extends JPanel {

    public LicencePanel() {
        setLayout(new BorderLayout());
        add(createLicenceArea(Resources.getFileContent("licence.txt")));
    }

    private JComponent createLicenceArea(final String licenseText) {
        JTextComponent textArea = UIStyle.withMonospacedFont(new NonEditableTexPane());
        textArea.setMargin(Borders.insets(Borders.DIALOG_SPACING));
        textArea.setText(licenseText);
        textArea.setEditable(false);
        textArea.setCaretPosition(0);
        return new OverlayScrollPane(textArea);
    }
}
