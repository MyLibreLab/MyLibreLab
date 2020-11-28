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

package com.github.mylibrelab.ui.menu;

import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import com.github.mylibrelab.action.ActionContext;
import com.github.mylibrelab.action.AnAction;
import com.github.mylibrelab.action.AnActionGroup;
import com.github.mylibrelab.action.Place;
import com.github.mylibrelab.text.component.TextMenu;

class ActionMenu extends TextMenu implements ActionMenuElement {

    private final AnActionGroup group;
    private final Map<AnAction, ActionMenuElement> menuMap = new HashMap<>();
    private final Map<AnAction, ActionMenuSeparator> separatorMap = new HashMap<>();

    ActionMenu(final AnActionGroup group) {
        super(group.getPresentation().getDisplayName());
        this.group = group;
        addGroup(this, group);
        updateActions(getContext(), true);
    }

    private void addGroup(final JMenu menu, final AnActionGroup group) {
        var children = group.getChildren(getContext());
        if (!group.useAbsoluteSorting()) {
            children.sort(group.getSorting());
        }
        for (int i = 0; i < children.size(); i++) {
            var action = children.get(i);
            if (action instanceof AnActionGroup) {
                var actionGroup = (AnActionGroup) action;
                if (actionGroup.isPopup(Place.MENUBAR)) {
                    var subMenu = new ActionMenu(actionGroup);
                    menuMap.put(actionGroup, subMenu);
                    menu.add(subMenu);
                } else {
                    addGroup(menu, actionGroup);
                    // Only add the separator if there are elements after this one.
                    if (i < children.size() - 1) {
                        var sep = new ActionMenuSeparator();
                        separatorMap.put(actionGroup, sep);
                        menu.add(sep);
                    }
                }
            } else {
                var menuItem = new ActionMenuItem(action);
                menuMap.put(action, menuItem);
                menu.add(menuItem);
            }
        }
    }

    private ActionContext getContext() {
        /*
         * Todo: The context for updating the actions should be retrieved as follows:
         * - The application holds a global session context which contains all
         * registered modules together with the following information:
         * - Whether the module is displayed. - Whether the module is
         * active/enabled (this may not be needed).
         * - Whether the module is in focus.
         * Additionally the current focused module should be retrievable directly.
         *
         * For now we return an empty action context.
         */
        return new ActionContext();
    }

    public void updateActions(final ActionContext context, final boolean updateChildren) {
        group.update(context);
        int visibleCount = updateChildActions(context, updateChildren);
        updatePresentation(visibleCount);
    }

    private void updatePresentation(final int visibleCount) {
        var presentation = group.getPresentation();
        setVisible(presentation.isVisible());
        setEnabled(presentation.isEnabled());
        if (visibleCount == 0) {
            if (group.hideIfNoVisibleChildren()) {
                setVisible(false);
            } else if (group.disableIfNoVisibleChildren()) {
                setEnabled(false);
            }
        }
    }

    private int updateChildActions(final ActionContext context, final boolean updateChildren) {
        int visibleCount = 0;
        for (var action : group.getChildren(context)) {
            var menu = menuMap.get(action);
            if (menu != null) {
                if (updateChildren) {
                    menu.updateActions(context, false);
                }
                if (menu.isVisible()) visibleCount++;
            } else if (action instanceof AnActionGroup) {
                updateActionGroup(context, (AnActionGroup) action);
            }
        }
        return visibleCount;
    }

    private void updateActionGroup(ActionContext context, AnActionGroup group) {
        // The action is a group and not a popup thus resulting in an inline group.
        // For those we need to keep track of the number of visible children manually.
        int visibleChildCount = 0;
        for (var action : group) {
            action.update(context);
            if (action.getPresentation().isVisible()) visibleChildCount++;
        }
        var sep = separatorMap.get(group);
        if (sep != null) {
            sep.setVisible(visibleChildCount != 0 || !group.hideIfNoVisibleChildren());
        }
    }

    @Override
    public void setPopupMenuVisible(boolean b) {
        if (!isPopupMenuVisible() && b) {
            updateActions(getContext(), true);
        }
        super.setPopupMenuVisible(b);
    }
}
