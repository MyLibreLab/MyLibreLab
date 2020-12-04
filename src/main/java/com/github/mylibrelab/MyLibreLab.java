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

package com.github.mylibrelab;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.*;

import org.tinylog.Logger;

import com.github.mylibrelab.event.EventBus;
import com.github.mylibrelab.lifecycle.AppLifecycleManager;
import com.github.mylibrelab.resources.ErrorType;
import com.github.mylibrelab.resources.Resources;
import com.github.mylibrelab.settings.StartupSettings;
import com.github.mylibrelab.ui.component.AppContentPane;
import com.github.mylibrelab.ui.dialog.LicenceAgreementDialog;
import com.github.mylibrelab.ui.menu.ActionMenuBar;

import VisualLogic.FrameMain;
import VisualLogic.ProjectProperties;
import VisualLogic.Tools;

public class MyLibreLab {

    private static JFrame frame;

    public static void main(final String[] args) {
        parseArguments(args);

        AppLifecycleManager.INSTANCE.notifyApplicationStarted();

        if (!checkLicenceAgreement()) {
            Logger.error(Resources.getErrorMessage(ErrorType.LICENCE_DECLINED));
            stopApplication();
            return;
        }

        try {
            FrameMain.userURL = getApplicationDirectory().toUri().toURL();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            // Launch old application for now.
            FrameMain.frm = new FrameMain(args);

            String projectPath = args.length > 1 ? args[1] : null;

            if (projectPath != null) {
                ProjectProperties props = Tools.openProjectFile(new File(projectPath));
                FrameMain.frm.openFileAsFront(projectPath, props.mainVM);
            } else {
                FrameMain.frm.setVisible(true);
            }
        });
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame();
            // Only hide for now. The application lifecycle is still bound to
            // the legacy frames lifetime. Because persistent components are weakly
            // referenced we also don't want to dispose the frame (though they will be saved
            // none the less). Ideally a persistent component should be bound to the lifetime of
            // it's parent frame which currently is hard to estimate whether we need to have such
            // capability as this would only be beneficial for complex dialogs.
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    AppLifecycleManager.INSTANCE.notifyAppFrameClosing();
                }
            });
            frame.setContentPane(new AppContentPane());
            frame.setJMenuBar(new ActionMenuBar());
            frame.setTitle("New GUI Frame");
            frame.pack();
            frame.setLocationRelativeTo(null);

            AppLifecycleManager.INSTANCE.notifyAppFrameCreated();
            frame.setVisible(true);
            AppLifecycleManager.INSTANCE.notifyAppFrameOpened();
        });

        // Necessary because the legacy code uses System#exit(int) in a lot of places which makes
        // notifying the AppLifeCycleManager difficult.
        Runtime.getRuntime().addShutdownHook(new Thread(MyLibreLab::stopApplication));
    }

    public static JFrame getFrame() {
        return frame;
    }

    private static void stopApplication() {
        if (frame != null && frame.isDisplayable()) {
            // Necessary if the legacy frame gets closed first.
            AppLifecycleManager.INSTANCE.notifyAppFrameClosing();
        }
        AppLifecycleManager.INSTANCE.notifyApplicationStopping();
        try {
            EventBus.shutDown();
        } catch (InterruptedException e) {
            Logger.error(e);
            Thread.currentThread().interrupt();
        }
    }

    private static Path getApplicationDirectory() {
        Path applicationPath = Path.of(System.getProperty("user.home"), "VisualLogic");
        if (!Files.exists(applicationPath)) {
            try {
                Files.createDirectory(applicationPath);
            } catch (IOException e) {
                Tools.showMessage(String.format("%s %s", Resources.getErrorMessage(ErrorType.DIRECTORY_NOT_FOUND),
                        applicationPath.toAbsolutePath().toString()));
            }
        }
        return applicationPath;
    }

    private static boolean checkLicenceAgreement() {
        if (!StartupSettings.INSTANCE.getLicenceAccepted()) {
            LicenceAgreementDialog.Result result = LicenceAgreementDialog.showLicenceDialog(null);
            StartupSettings.INSTANCE.setLicenceAccepted(LicenceAgreementDialog.Result.ACCEPT == result);
        }
        return StartupSettings.INSTANCE.getLicenceAccepted();
    }

    private static void parseArguments(final String[] args) {
        for (int i = 0; i < args.length; i++) {
            Logger.debug("Arg[" + i + "]=" + args[i]);
        }
        if (args.length > 0) {
            FrameMain.elementPath = args[0];
            Tools.elementPath = FrameMain.elementPath;
        }
    }
}
