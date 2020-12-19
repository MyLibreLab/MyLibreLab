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

package com.github.mylibrelab.action;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Multiple {@link AnAction}s can be grouped inside an {@link AnActionGroup}.
 */
public class AnActionGroup extends AnAction implements Iterable<AnAction> {

    protected static final Comparator<AnAction> PRIORITY_THEN_IDENTIFIER = (o1, o2) -> {
        if (o1 instanceof AnActionGroup && !(o2 instanceof AnActionGroup)) return -1;
        if (o2 instanceof AnActionGroup && !(o1 instanceof AnActionGroup)) return 1;
        int comp = o2.getPriority().compareTo(o1.getPriority());
        if (comp != 0) return comp;
        return o1.getIdentifier().compareTo(o2.getIdentifier());
    };

    protected final List<AnAction> children = new ArrayList<>();
    private AnAction defaultAction;

    /**
     * Creates a new action group with the given identifier.The identifier will be the class name of the
     * action.
     *
     */
    public AnActionGroup() {
        this(null);
    }

    /**
     * Creates a new action group with the given identifier.If the identifier is {@code null} then the
     * class name of the action will be used.
     *
     *
     * @param identifier the identifier of the group.
     */
    public AnActionGroup(@Nullable final String identifier) {
        super(identifier);
        ActionManager.INSTANCE.registerGroup(this);
    }

    /**
     * {@inheritDoc}If the group specifies a default action it is performed. {@link #getDefaultAction()}
     */
    @Override
    public final void actionPerformed(@NotNull final ActionContext context) {
        var defAction = getDefaultAction();
        if (defAction != null) {
            defAction.actionPerformed(context);
        }
    }

    /**
     * {@inheritDoc} Returns true if the group has an enabled default action.
     * {@link #getDefaultAction()}
     */
    @Override
    public boolean canBePerformed() {
        var defAction = getDefaultAction();
        return defAction != null && defAction.canBePerformed();
    }

    /**
     * Add an action to this group.
     *
     * @param action the action to add.
     */
    public void add(@NotNull final AnAction action) {
        children.add(action);
    }

    /**
     * Remove an action from this group. Instead of removing the action consider disabling it instead.
     *
     * @param action the action to remove.
     */
    public void remove(@Nullable final AnAction action) {
        if (action == null) return;
        children.remove(action);
    }

    /**
     * Returns the groups default action.
     *
     * @return the default action of the group.
     */
    @Nullable
    public AnAction getDefaultAction() {
        return defaultAction;
    }

    /**
     * Sets the default {@link AnAction} of this group.
     *
     * @param defaultAction the default action.
     */
    public void setDefaultAction(final AnAction defaultAction) {
        this.defaultAction = defaultAction;
    }

    /**
     * Returns a list of all children of the group.
     *
     * @return list of all children.
     */
    @NotNull
    public List<AnAction> getChildren() {
        return children;
    }

    /**
     * Returns a list of all children of the group. The list may depend on the actions context.
     *
     * @param context the action context.
     * @return list of all children.
     */
    @NotNull
    public List<AnAction> getChildren(@NotNull final ActionContext context) {
        return getChildren();
    }

    /**
     * Get the number of children this group has.
     *
     * @return the number of children.
     */
    public int getChildCount() {
        return children.size();
    }

    /**
     * Returns whether the actions displays its text when it the toolbar. Default is {@code false}.
     *
     * @return true if the text should be displayed.
     */
    public boolean displayTextInToolbar() {
        return false;
    }

    /**
     * Determines whether the group should display its children in a popup. Based on the location
     * context this value may be ignored.
     *
     * @return true if the children should be shown in a popup.
     */
    public boolean isPopup() {
        return false;
    }

    /**
     * Determines whether the group should display its children in a popup. Based on the location
     * context this value may be ignored.
     *
     * @param place the location of the actions visible representation.
     * @return true if the children should be shown in a popup.
     */
    public boolean isPopup(@NotNull Place place) {
        return isPopup();
    }

    /**
     * Returns the sorting of the groups children.
     *
     * @return the {@link Comparator} which determines the sorting.
     */
    public Comparator<AnAction> getSorting() {
        return PRIORITY_THEN_IDENTIFIER;
    }

    /**
     * Returns whether this group uses an absolute sorting for their children. If true the group
     * guarantees that {@link #getChildren()} and {@link #getChildren(ActionContext)} always return the
     * correctly sorted list of children.
     *
     * @return true if the group uses absolute sorting.
     */
    public boolean useAbsoluteSorting() {
        return false;
    }

    /**
     * Indicates whether the group should be hidden if it has no visible children.
     *
     * @return true if the group should be hidden with no visible children.
     */
    public boolean hideIfNoVisibleChildren() {
        return !isPopup();
    }

    /**
     * Indicates whether the group should be disabled if it has no visible children.
     *
     * @return true if the group should be disabled with no visible children.
     */
    public boolean disableIfNoVisibleChildren() {
        return true;
    }

    @NotNull
    @Override
    public Iterator<AnAction> iterator() {
        return getChildren().iterator();
    }

    @Override
    public String toString() {
        return "ActionGroup{" + "identifier='" + getIdentifier() + '\'' + ", presentation=" + getPresentation()
                + ", children=" + children + ", defaultAction=" + defaultAction + '}';
    }
}
