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

//
// package com.github.mylibrelab.ui.menu;
//
// import java.awt.*;
//
// import javax.swing.*;
//
// import org.jetbrains.annotations.NotNull;
//
// import com.github.mylibrelab.action.*;
// import com.github.mylibrelab.text.component.TextMenu;
// import com.github.mylibrelab.text.component.TextMenuItem;
//
// public class ActionMenuBar extends JMenuBar {
//
// public ActionMenuBar() {
// for (var defaultGroup : DefaultActionGroup.values()) {
// if (defaultGroup.isInMenuBar()) {
// var group = defaultGroup.getActionGroup();
// add(new ActionMenu(group));
// }
// }
// }
//
// private static class ActionMenu extends TextMenu {
//
// private final AnActionGroup group;
//
// private ActionMenu(final AnActionGroup group) {
// super(group.getPresentation().getDisplayName());
// this.group = group;
// addGroup(this, group);
// }
//
// private void addGroup(final JMenu menu, final AnActionGroup group) {
// var children = group.getChildren(getContext());
// if (!group.useAbsoluteSorting()) {
// children.sort(group.getSorting());
// }
// for (int i = 0; i < children.size(); i++) {
// var action = children.get(i);
// if (action instanceof AnActionGroup) {
// var actionGroup = (AnActionGroup) action;
// if (actionGroup.isPopup(Place.MENUBAR)) {
// menu.add(new ActionMenu(actionGroup));
// } else {
// addGroup(menu, actionGroup);
// if (i < children.size() - 1) {
// // Only add the separator if there are elements after this one.
// menu.addSeparator();
// }
// }
// } else {
// menu.add(new ActionMenuItem(action));
// }
// }
// }
//
// private ActionContext getContext() {
// return new ActionContext();
// }
//
// private void updateActions() {
// // Note: configure context semantics.
// ActionContext context = getContext();
// for (var action : group.getChildren(context)) {
// action.update(context);
// }
// }
//
// @Override
// public void doLayout() {
// updateActions();
// super.doLayout();
// }
//
// @Override
// public void paint(final Graphics g) {
// updateActions();
// super.paint(g);
// }
// }
//
// private static class ActionMenuItem extends TextMenuItem {
//
// private final AnAction action;
//
// private ActionMenuItem(@NotNull final AnAction action) {
// super(action.getPresentation().getDisplayName());
// this.action = action;
// }
//
// @Override
// public Icon getIcon() {
// return action.getPresentation().getIcon();
// }
//
// @Override
// public Icon getDisabledIcon() {
// return getIcon();
// }
//
// @Override
// public boolean isEnabled() {
// return action.getPresentation().isEnabled();
// }
//
// @Override
// public boolean isVisible() {
// return action.getPresentation().isVisible();
// }
// }
// }
