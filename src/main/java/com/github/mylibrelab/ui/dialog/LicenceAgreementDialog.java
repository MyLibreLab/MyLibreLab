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

import javax.swing.*;

import org.jetbrains.annotations.Nullable;

import com.github.mylibrelab.resources.Resources;
import com.github.mylibrelab.text.component.TextButton;
import com.github.mylibrelab.ui.UIStyle;
import com.github.mylibrelab.ui.UIUtil;
import com.github.mylibrelab.ui.border.Borders;
import com.github.mylibrelab.ui.component.LicencePanel;
import com.github.weisj.darklaf.components.DefaultButton;
import com.github.weisj.darklaf.util.AlignmentExt;

public class LicenceAgreementDialog extends JDialog {

    public enum Result {
        ACCEPT, DECLINE
    }

    private Result result = Result.DECLINE;

    private LicenceAgreementDialog(@Nullable final Frame parent, final boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * Show a dialog with the Licence.
     *
     * @param parent the parent frame.
     * @return whether the user has accepted or declined the licence.
     */
    public static Result showLicenceDialog(@Nullable final Frame parent) {
        return UIUtil.runAndWait(() -> showDialog(parent));
    }

    private static Result showDialog(@Nullable final Frame parent) {
        LicenceAgreementDialog dialog = new LicenceAgreementDialog(parent, true);
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        Dimension size = dialog.getPreferredSize();
        size.height = Math.min(size.height, screenSize.height / 2);
        size.width = Math.min(size.width, screenSize.width / 2);
        dialog.setPreferredSize(size);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
        return dialog.result;
    }

    private void initComponents() {
        UIStyle.withTitle(this, Resources.getResourceText("licence.title"));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(new LicencePanel(), BorderLayout.CENTER);
        contentPane.add(createButtonArea(), BorderLayout.SOUTH);
        contentPane.setBorder(Borders.topLineBorder());

        KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        UIUtil.bindAction(contentPane, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, "decline", escape,
                e -> decline());
        setContentPane(contentPane);
    }

    private JComponent createButtonArea() {
        Box buttonArea = Box.createHorizontalBox();
        buttonArea.add(Box.createHorizontalGlue());
        buttonArea.add(createAcceptButton());
        buttonArea.add(createDeclineButton());
        buttonArea.setBorder(Borders.compoundBorder(Borders.topLineBorder(),
                Borders.mask(Borders.dialogBorder(), AlignmentExt.BOTTOM, Borders.DEFAULT_SPACING)));
        return buttonArea;
    }

    private JButton createAcceptButton() {
        JButton button = UIStyle.withText(new DefaultButton(""), Resources.getResourceText("licence.accept"));
        button.addActionListener(e -> accept());
        return button;
    }

    private JButton createDeclineButton() {
        JButton button = new TextButton(Resources.getResourceText("licence.decline"));
        button.addActionListener(e -> decline());
        return button;
    }

    private void accept() {
        result = Result.ACCEPT;
        dispose();
    }

    private void decline() {
        result = Result.DECLINE;
        dispose();
    }
}
