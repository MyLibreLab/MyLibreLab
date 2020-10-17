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

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import com.github.mylibrelab.event.EventBus;
import com.github.mylibrelab.event.Topics;
import com.github.mylibrelab.ui.component.AppContentPane;
import com.github.mylibrelab.ui.module.ApplicationModule;
import com.github.weisj.darklaf.util.PropertyUtil;

import kotlin.Pair;

/**
 * Utility class for working with action.
 */
public class ActionUtil {

    private ActionUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static final String ALL_ACTIONS = null;

    /**
     * Bind an action to the given button. If there are multiple actions with of given class all will be
     * be bound.
     *
     * @param button the button to bind to.
     * @param actionClass the action class to bind.
     */
    public static void bindAction(@NotNull final AbstractButton button,
            @NotNull final Class<? extends AnAction> actionClass) {
        bindAction(button, actionClass, ALL_ACTIONS);
    }

    /**
     * Bind an action to the given button.
     *
     * @param button the button to bind to.
     * @param actionClass the action class to bind.
     * @param identifier the identifier of the action.
     */
    public static void bindAction(@NotNull final AbstractButton button,
            @NotNull final Class<? extends AnAction> actionClass, @Nullable final String identifier) {
        ActionManager.INSTANCE.configureAction(actionClass, identifier, a -> button.addActionListener(e -> {
            var appComponent = findApplicationModule(button);
            EventBus.get(Topics.ACTIONS).post(new Pair<>(actionClass, appComponent));
        }));
    }

    /**
     * Create a button which is bound to the given action. The button will take it's visual
     * representation from {@link AnAction#getPresentation()}.
     *
     * @param actionClass the action class.
     * @return the button.
     */
    @NotNull
    public static AbstractButton createButton(@NotNull final Class<? extends AnAction> actionClass) {
        return createButton(actionClass, ALL_ACTIONS);
    }

    /**
     * Create a button which is bound to the given action. The button will take it's visual
     * representation from {@link AnAction#getPresentation()}.
     *
     * @param actionClass the action class.
     * @param identifier the identifier of the action.
     * @return the button.
     */
    @NotNull
    public static AbstractButton createButton(@NotNull final Class<? extends AnAction> actionClass,
            @Nullable final String identifier) {
        List<AnAction> actions = new ArrayList<>();
        ActionManager.INSTANCE.configureAction(actionClass, identifier, a -> {
            actions.add(a);
            if (actions.size() == 2) {
                Logger.warn("Multiple actions bound to created button. "
                        + "The visual representation will be determined by the first added action.");
            }
        });
        var button = new JButton() {

            private AnAction getAnAction() {
                return !actions.isEmpty() ? actions.get(0) : null;
            }

            private Presentation getPresentation() {
                var action = getAnAction();
                if (action != null) {
                    return action.getPresentation();
                }
                return new Presentation();
            }

            @Override
            public void paint(Graphics g) {
                var action = getAnAction();
                if (action != null) {
                    action.update(getContext(findApplicationModule(this)));
                    setVisible(getPresentation().isVisible());
                }
                super.paint(g);
            }

            @Override
            public Icon getIcon() {
                return getPresentation().getIcon();
            }

            @Override
            public String getText() {
                return getPresentation().getDisplayName().getText();
            }

            @Override
            public boolean isEnabled() {
                return getPresentation().isEnabled();
            }
        };
        bindAction(button, actionClass);
        return button;
    }

    @NotNull
    protected static ActionContext getContext(@Nullable final ApplicationModule module) {
        var context = module != null ? module.getActionContext() : null;
        return context != null ? context : new ActionContext();
    }

    @Nullable
    protected static ApplicationModule findApplicationModule(@Nullable final JComponent component) {
        Component curr = component;
        while (curr != null) {
            var appComp = PropertyUtil.getObject(curr, AppContentPane.KEY_APP_MODULE, ApplicationModule.class);
            if (appComp != null) {
                // Cache the value for faster access.
                PropertyUtil.installProperty(component, AppContentPane.KEY_APP_MODULE, appComp);
                return appComp;
            }
            curr = curr.getParent();
        }
        return null;
    }
}
