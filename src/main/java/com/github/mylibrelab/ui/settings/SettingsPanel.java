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

package com.github.mylibrelab.ui.settings;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.*;
import javax.swing.tree.*;

import org.jetbrains.annotations.NotNull;

import com.github.mylibrelab.resources.Resources;
import com.github.mylibrelab.settings.api.SettingsContainer;
import com.github.mylibrelab.ui.UIStyle;
import com.github.mylibrelab.ui.border.Borders;
import com.github.mylibrelab.ui.icons.AllIcons;
import com.github.mylibrelab.ui.layout.DialogPanel;
import com.github.mylibrelab.ui.view.ViewFactory;
import com.github.weisj.darklaf.components.ComponentHelper;
import com.github.weisj.darklaf.components.OverlayScrollPane;
import com.github.weisj.darklaf.focus.FocusParentHelper;
import com.github.weisj.darklaf.icons.EmptyIcon;
import com.github.weisj.darklaf.layout.LayoutHelper;
import com.github.weisj.darklaf.ui.splitpane.SplitPaneConstants;
import com.github.weisj.darklaf.util.Alignment;
import com.github.weisj.darklaf.util.DarkUIUtil;
import com.github.weisj.darklaf.util.PropertyKey;

/**
 * Panel which displays the settings for a given set of {@link SettingsContainer}s.
 */
public class SettingsPanel extends JPanel implements AWTEventListener {

    public static final String KEY_RESET_STATUS = "resetStatus";
    private final Map<SettingsContainer, DialogPanel> containers;
    private final JPanel contentPanel;
    private final CardLayout cardLayout;
    private final JButton resetButton;
    private SettingsContainer currentContainer;

    public SettingsPanel(@NotNull final List<SettingsContainer> settingsContainers) {
        setLayout(new BorderLayout());

        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);

        var containerList = settingsContainers.stream()
                .filter(SettingsContainer::shouldBeDisplayed)
                .sorted(Comparator.comparing(SettingsContainer::getIdentifier))
                .collect(Collectors.toList());

        containers = containerList.stream().collect(Collectors.toMap(cont -> cont, cont -> {
            String name = cont.getIdentifier();
            DialogPanel view = ViewFactory.createView(cont, DialogPanel.class);
            contentPanel.add(view, name);
            return view;
        }));

        resetButton = ComponentHelper.createIconOnlyButton(AllIcons.Actions.Revert, AllIcons.Actions.RevertDisabled);
        resetButton.addActionListener(e -> resetCurrent());
        UIStyle.withTooltipText(resetButton, Resources.getResourceText("dialog.reset"));

        var navigationTree = createNavigationTree(containerList);

        OverlayScrollPane sp = new OverlayScrollPane(navigationTree) {
            @Override
            public Dimension getMinimumSize() {
                return getPreferredSize();
            }
        };
        OverlayScrollPane contentSp = LayoutHelper.createScrollPaneWithHoverOverlay(contentPanel, resetButton,
                Alignment.NORTH_EAST, Borders::createSpacingInsets);
        contentSp.getScrollPane().setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        var splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sp, contentSp) {

            private void updateDividerLocation() {
                setDividerLocation(Math.max(getMinimumDividerLocation(), getDividerLocation()));
            }

            @Override
            public void addNotify() {
                super.addNotify();
                updateDividerLocation();
            }

            @Override
            public void updateUI() {
                super.updateUI();
                updateDividerLocation();
            }
        };
        addPropertyChangeListener(PropertyKey.ANCESTOR,
                e -> FocusParentHelper.setFocusParent(navigationTree, (JComponent) getParent()));


        ((JComponent) resetButton.getParent()).setBorder(Borders.emptyBorder());
        splitPane.putClientProperty(SplitPaneConstants.KEY_STYLE, SplitPaneConstants.STYLE_INVISIBLE);
        add(splitPane, BorderLayout.CENTER);

        Toolkit.getDefaultToolkit().addAWTEventListener(this,
                AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK);
        updateResetAction();
    }

    private JTree createNavigationTree(final List<SettingsContainer> containerList) {
        var root = new DefaultMutableTreeNode();
        for (var cont : containerList) {
            root.add(new DefaultMutableTreeNode(cont));
        }
        var navigationTree = new JTree(new DefaultTreeModel(root));
        navigationTree.setCellRenderer(new SettingsContainerTreeCellRenderer());
        navigationTree.setBorder(LayoutHelper.createEmptyContainerBorder());
        navigationTree.setRootVisible(false);
        navigationTree.setShowsRootHandles(false);
        navigationTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        navigationTree.addTreeSelectionListener(e -> {
            TreePath path = navigationTree.getSelectionPath();
            if (path == null) return;
            var treeNode = ((DefaultMutableTreeNode) path.getLastPathComponent());
            var cont = (SettingsContainer) treeNode.getUserObject();
            if (cont == null) return;
            currentContainer = cont;
            cardLayout.show(contentPanel, cont.getIdentifier());
            updateResetAction();
        });
        navigationTree.setSelectionRow(0);
        return navigationTree;
    }

    public void applyCurrent() {
        if (currentContainer != null) {
            var panel = containers.get(currentContainer);
            if (panel.isModified()) {
                panel.apply();
                currentContainer.onSettingsUpdate(false);
            }
        }
    }

    public void apply() {
        containers.forEach((cont, panel) -> {
            if (panel.isModified()) {
                panel.apply();
                cont.onSettingsUpdate(false);
            }
        });
    }

    public void resetCurrent() {
        if (currentContainer != null) containers.get(currentContainer).reset();
    }

    public void resetAll() {
        containers.forEach((cont, panel) -> {
            if (panel.isModified()) {
                panel.reset();
            }
        });
    }

    @Override
    public final void eventDispatched(final AWTEvent event) {
        if (!isVisible()) return;
        switch (event.getID()) {
            case MouseEvent.MOUSE_PRESSED:
            case MouseEvent.MOUSE_RELEASED:
            case MouseEvent.MOUSE_DRAGGED:
                MouseEvent me = (MouseEvent) event;
                if (SwingUtilities.isDescendingFrom(me.getComponent(), containers.get(currentContainer))
                        || isPopupOverEditor(me.getComponent())) {
                    requestUpdate();
                }
                break;
            case KeyEvent.KEY_PRESSED:
            case KeyEvent.KEY_RELEASED:
                KeyEvent ke = (KeyEvent) event;
                if (SwingUtilities.isDescendingFrom(ke.getComponent(),
                        containers.get(currentContainer))) {
                    requestUpdate();
                }
                break;
            default:
                break;
        }
    }

    private void requestUpdate() {
        final SettingsContainer container = currentContainer;
        if (container != null) {
            SwingUtilities.invokeLater(() -> {
                if (currentContainer == container) {
                    updateResetAction();
                }
            });
        }
    }

    private void updateResetAction() {
        if (currentContainer == null) return;
        boolean old = resetButton.isEnabled();
        boolean modified = containers.get(currentContainer).isModified();
        resetButton.setEnabled(modified);
        resetButton.setVisible(modified);
        firePropertyChange(KEY_RESET_STATUS, old, modified);
    }

    private boolean isPopupOverEditor(Component component) {
        Window editor = DarkUIUtil.getWindow(this);
        if (editor != null) {
            Window popup = DarkUIUtil.getWindow(component);
            // light-weight popup is located on the layered pane of the same window
            if (popup == editor) {
                return true;
            }
            // heavy-weight popup opens new window with the corresponding parent
            if (popup != null && editor == popup.getParent()) {
                if (popup instanceof JDialog) {
                    JDialog dialog = (JDialog) popup;
                    return Dialog.ModalityType.MODELESS == dialog.getModalityType();
                }
                return popup instanceof JWindow;
            }
        }
        return false;
    }

    private static class SettingsContainerTreeCellRenderer extends DefaultTreeCellRenderer {
        @Override
        public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean selected,
                final boolean expanded, final boolean leaf, final int row, final boolean hasFocus) {
            var cont = ((SettingsContainer) ((DefaultMutableTreeNode) value).getUserObject());
            var presentation = cont != null ? cont.getPresentation() : null;
            super.getTreeCellRendererComponent(tree,
                    presentation != null ? presentation.getDisplayName().getText() : null,
                    selected, expanded, leaf, row, hasFocus);
            if (presentation != null) {
                Icon icon = presentation.getIcon();
                if (icon == null) icon = EmptyIcon.create(16);
                setIcon(icon);
            }
            return this;
        }
    }
}
