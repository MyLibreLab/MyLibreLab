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

package com.github.mylibrelab.ui.dialog;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import com.github.mylibrelab.resources.Resources;
import com.github.mylibrelab.settings.api.SettingsStorage;
import com.github.mylibrelab.text.component.TextButton;
import com.github.mylibrelab.ui.UIStyle;
import com.github.mylibrelab.ui.UIUtil;
import com.github.mylibrelab.ui.border.Borders;
import com.github.mylibrelab.ui.icons.AllIcons;
import com.github.mylibrelab.ui.settings.SettingsPanel;
import com.github.weisj.darklaf.components.DefaultButton;
import com.github.weisj.darklaf.util.Actions;
import com.github.weisj.darklaf.util.DarkUIUtil;
import com.github.weisj.darklaf.util.LazyValue;

public class SettingsDialog extends JPanel {

    private static final LazyValue<SettingsDialog> SHARED_DIALOG = new LazyValue<>(SettingsDialog::new);
    private final SettingsPanel settingsPanel;

    public static void showSettingsDialog(final Frame parent) {
        SettingsDialog content = SHARED_DIALOG.get();
        if (content.isShowing()) return;
        var dialog = new JDialog(parent, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setContentPane(content);
        UIStyle.withTitle(dialog, Resources.getResourceText("actions.settings.displayName"));
        dialog.setIconImage(AllIcons.asWindowIcon(AllIcons.Actions.Settings, dialog));
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                content.settingsPanel.resetAll();
            }
        });
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    private SettingsDialog() {
        super(new BorderLayout());
        settingsPanel = new SettingsPanel(SettingsStorage.INSTANCE.getContainers());

        var okButton = UIStyle.withText(new DefaultButton(""), Resources.getResourceText("dialog.ok"));
        var cancelButton = new TextButton(Resources.getResourceText("dialog.cancel"));
        var applyButton = new TextButton(Resources.getResourceText("dialog.apply"));

        okButton.addActionListener(e -> {
            settingsPanel.apply();
            closeDialog();
        });
        cancelButton.addActionListener(e -> {
            settingsPanel.resetAll();
            closeDialog();
        });
        applyButton.addActionListener(e -> settingsPanel.apply());
        settingsPanel.addPropertyChangeListener(SettingsPanel.KEY_RESET_STATUS,
                e -> applyButton.setEnabled(Boolean.TRUE.equals(e.getNewValue())));
        UIUtil.bindAction(settingsPanel, JComponent.WHEN_IN_FOCUSED_WINDOW, "cancel",
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                Actions.create(e -> cancelButton.doClick()));

        var buttonBox = Box.createHorizontalBox();
        buttonBox.setBorder(Borders.withSpacing(Borders.topLineBorder()));
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.add(okButton);
        buttonBox.add(Box.createHorizontalStrut(2));
        buttonBox.add(cancelButton);
        buttonBox.add(Box.createHorizontalStrut(2));
        buttonBox.add(applyButton);
        add(settingsPanel, BorderLayout.CENTER);
        add(buttonBox, BorderLayout.SOUTH);
    }

    private void closeDialog() {
        Window window = DarkUIUtil.getWindow(this);
        if (window == null) return;
        window.setVisible(false);
        window.dispose();
    }

}
