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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.github.mylibrelab.text.Text;

public abstract class AnAction {

    private String identifier;
    private final ActionPresentation presentation;

    /**
     * Creates a new {@link AnAction} with the given identifier. The identifier will be the class name
     * of the action.
     */
    public AnAction() {
        this(null);
    }

    /**
     * Creates a new {@link AnAction} with the given identifier. If the identifier is {@code null} then
     * the class name of the action will be used.
     *
     * @param identifier the identifier.
     */
    public AnAction(@Nullable final String identifier) {
        this.identifier = identifier;
        if (this.identifier == null) {
            var type = getClass();
            if (type.isAnonymousClass()) {
                throw new IllegalStateException("Anonymous actions need to provide an explicit identifier");
            }
            this.identifier = type.getName();
        }
        ParentGroup parentGroup = getClass().getAnnotation(ParentGroup.class);
        if (parentGroup != null) {
            registerWithParents(parentGroup);
        }
        presentation = createDefaultPresentation();
        ActionManager.INSTANCE.registerAction(this);
    }

    private void registerWithParents(@NotNull final ParentGroup parentGroup) {
        var defaultGroups = parentGroup.defaultGroups();
        for (var defaultGroup : defaultGroups) {
            defaultGroup.getActionGroup().add(this);
        }
        var otherGroups = parentGroup.groups();
        for (var otherGroup : otherGroups) {
            ActionManager.INSTANCE.configureGroup(otherGroup, g -> g.add(this));
        }
    }

    protected ActionPresentation createDefaultPresentation() {
        return new ActionPresentation(Text.of(getIdentifier()));
    }

    /**
     * Returns the identifier of the action.
     *
     * @return the identifier.
     */
    @NotNull
    public String getIdentifier() {
        return identifier;
    }

    /**
     * This method is called if the action is invoked.
     *
     *
     * @param context the {@link ActionContext}.
     */
    public abstract void actionPerformed(@NotNull final ActionContext context);

    /**
     * Returns whether the action can be performed.
     *
     * @return true if the action can be performed.
     */
    public boolean canBePerformed() {
        return getPresentation().isEnabled();
    }

    /**
     * Any updates to the actions presentation should occur in this method. Because this method is
     * called on the UI thread it shouldn't do any heavy computations.
     *
     * @param context the {@link ActionContext}.
     */
    public void update(@NotNull final ActionContext context) {}

    /**
     * Returns the {@link ActionPresentation} of the action. The presentation should only be updated in
     * {@link #update(ActionContext)}
     *
     * @return the {@link ActionPresentation} of the action.
     */
    @NotNull
    public ActionPresentation getPresentation() {
        return presentation;
    }

    /**
     * Returns the {@link Priority} of the action. Default is {@link Priority#DEFAULT}.
     *
     * @return the {@link Priority}
     */
    @NotNull
    public Priority getPriority() {
        return Priority.DEFAULT;
    }

    @Override
    public String toString() {
        return "AnAction{" + "identifier='" + identifier + '\'' + ", presentation=" + presentation + '}';
    }
}
