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

package com.github.mylibrelab.elements.front.twomatrix.vectors.listoned;/*
                                                                         * Copyright (C) 2020 MyLibreLab
                                                                         * Based on MyOpenLab by Carmelo Salafia
                                                                         * www.myopenlab.de
                                                                         * Copyright (C) 2004 Carmelo Salafia
                                                                         * cswi@gmx.de
                                                                         *
                                                                         * This program is free software: you can
                                                                         * redistribute it and/or modify
                                                                         * it under the terms of the GNU General Public
                                                                         * License as published by
                                                                         * the Free Software Foundation, either version
                                                                         * 3 of the License, or
                                                                         * (at your option) any later version.
                                                                         *
                                                                         * This program is distributed in the hope that
                                                                         * it will be useful,
                                                                         * but WITHOUT ANY WARRANTY; without even the
                                                                         * implied warranty of
                                                                         * MERCHANTABILITY or FITNESS FOR A PARTICULAR
                                                                         * PURPOSE. See the
                                                                         * GNU General Public License for more details.
                                                                         *
                                                                         * You should have received a copy of the GNU
                                                                         * General Public License
                                                                         * along with this program. If not, see
                                                                         * <http://www.gnu.org/licenses/>.
                                                                         *
                                                                         */

import java.awt.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.github.mylibrelab.elements.tools.JVSMain;

import VisualLogic.ExternalIF;
import VisualLogic.PanelIF;
import VisualLogic.variables.VS1DString;
import VisualLogic.variables.VSBoolean;

public class Panel extends JVSMain implements PanelIF {
    private JPanel panel;
    private JList liste = new JList();
    private VS1DString in = null;
    private VSBoolean multiSelection = new VSBoolean(false);

    ExternalIF circuitElement;
    private DefaultListModel model = new DefaultListModel();


    public void onDispose() {
        JPanel panel = element.getFrontPanel();
        panel.removeAll();
    }

    public void proc() {

        String str = "";
        if (in != null) {
            model.clear();
            for (int i = 0; i < in.getLength(); i++) {
                str = in.getValue(i);
                model.addElement(str);
            }

        }
    }

    // aus PanelIF
    public void processPanel(int pinIndex, double value, Object obj) {
        in = (VS1DString) obj;


        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                proc();
                // new DialogWait().setVisible(true);
            }
        });
    }

    public void ListValueChanged(ListSelectionEvent e) {
        Object[] values = liste.getSelectedValues();


        if (circuitElement != null) circuitElement.Change(0, values);


    }


    public void init() {
        initPins(0, 0, 0, 0);
        setSize(100, 150);

        element.jSetResizable(true);
        initPinVisibility(false, false, false, false);
        liste.setModel(model);
        liste.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        liste.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                ListValueChanged(e);
            }
        });

    }

    public void xOnInit() {
        panel = element.getFrontPanel();

        panel.setLayout(new BorderLayout());

        JScrollPane bar = new JScrollPane(liste);
        panel.add(bar, BorderLayout.CENTER);
        circuitElement = element.getCircuitElement();
        element.setAlwaysOnTop(true);
    }

    public void setPropertyEditor() {
        element.jAddPEItem("Multiselection", multiSelection, 0, 0);
        localize();
    }


    private void localize() {
        int d = 6;
        String language;

        language = "en_US";

        element.jSetPEItemLocale(d + 0, language, "Multiselection");

        language = "es_ES";

        element.jSetPEItemLocale(d + 0, language, "Multiselectionón");
    }

    public void propertyChanged(Object o) {
        if (multiSelection.getValue()) {
            liste.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            // liste.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        } else {
            liste.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }

    }

    public void start() {
        model.clear();
    }

    public void loadFromStream(java.io.FileInputStream fis) {
        multiSelection.loadFromStream(fis);
        propertyChanged(null);
    }

    public void saveToStream(java.io.FileOutputStream fos) {
        multiSelection.saveToStream(fos);
    }

}
