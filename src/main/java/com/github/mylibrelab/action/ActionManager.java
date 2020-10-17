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

import java.util.*;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import com.github.mylibrelab.event.Dispatchers;
import com.github.mylibrelab.event.EventBus;
import com.github.mylibrelab.event.Topic;
import com.github.mylibrelab.service.ServiceManager;
import com.github.mylibrelab.ui.module.ApplicationModule;

import kotlin.Pair;

/**
 * Manager to handle {@link AnAction}s and {@link AnActionGroup}s.
 */
public enum ActionManager {
    INSTANCE;

    /**
     * Create the {@link Topic} for actions.
     *
     * @return the {@link Topic}.
     */
    @NotNull
    public Topic<Pair<Class<? extends AnAction>, ApplicationModule>, Void> createTopic() {
        return Topic.create("action_topic", Dispatchers.createVoidDispatcher(o -> {
            var actions = actionMap.get(o.component1());
            if (actions != null) {
                var appComp = o.component2();
                var context = ActionUtil.getContext(appComp);
                for (var action : actions.values()) {
                    if (!action.canBePerformed()) continue;
                    action.actionPerformed(context);
                }
            }
        }));
    }

    private final Map<Class<? extends AnActionGroup>, List<Configuration<AnActionGroup>>> pendingGroupsConfigurations =
            new HashMap<>();
    private final Map<Class<? extends AnAction>, List<Configuration<AnAction>>> pendingActionConfigurations =
            Collections.synchronizedMap(new HashMap<>());
    private final Map<Class<? extends AnActionGroup>, Map<String, AnActionGroup>> groupMap =
            Collections.synchronizedMap(new HashMap<>());
    private final Map<Class<? extends AnAction>, Map<String, AnAction>> actionMap =
            Collections.synchronizedMap(new HashMap<>());

    ActionManager() {
        EventBus.get().post(ActionManager::load);
    }

    private static void load() {
        for (var defaultGroup : DefaultActionGroup.values()) {
            INSTANCE.registerGroup(defaultGroup.getActionGroup());
        }
        ServiceManager.getAllServices(AnAction.class).forEach(INSTANCE::registerAction);
    }

    /**
     * Configure an action. If the action isn't yet registered the configuration takes place as soon as
     * it registers itself. If there are multiple actions of the given type then all will be configured.
     *
     * @param actionType the action class.
     * @param configurationAction the configuration action.
     */
    void configureAction(@NotNull final Class<? extends AnAction> actionType,
            @NotNull final Consumer<AnAction> configurationAction) {
        configureAction(actionType, null, configurationAction);
    }

    /**
     * Configure an action. If the action isn't yet registered the configuration takes place as soon as
     * it registers itself.
     *
     * @param actionType the action class.
     * @param identifier the identifier of the action.
     * @param configurationAction the configuration action.
     */
    void configureAction(@NotNull final Class<? extends AnAction> actionType, @Nullable final String identifier,
            @NotNull final Consumer<AnAction> configurationAction) {
        var actions = actionMap.get(actionType);
        if (actions != null) {
            actions.values().forEach(configurationAction);
        } else {
            pendingActionConfigurations
                    .computeIfAbsent(actionType, a -> Collections.synchronizedList(new ArrayList<>()))
                    .add(new Configuration<>(configurationAction, identifier));
        }
    }

    /**
     * Configure a group. If the group isn't yet registered the configuration takes place as soon as it
     * registers itself. If there are multiple groups of the given type then all will be configured.
     *
     * @param actionType the group class.
     * @param configurationAction the configuration action.
     */
    void configureGroup(@NotNull final Class<? extends AnActionGroup> actionType,
            @NotNull final Consumer<AnActionGroup> configurationAction) {
        configureGroup(actionType, null, configurationAction);
    }

    /**
     * Configure a group. If the group isn't yet registered the configuration takes place as soon as it
     * registers itself.
     *
     * @param actionType the group class.
     * @param identifier the group identifier.
     * @param configurationAction the configuration action.
     */
    void configureGroup(@NotNull final Class<? extends AnActionGroup> actionType, @Nullable final String identifier,
            @NotNull final Consumer<AnActionGroup> configurationAction) {
        var groups = groupMap.get(actionType);
        if (groups != null) {
            groups.values().forEach(configurationAction);
        } else {
            pendingGroupsConfigurations
                    .computeIfAbsent(actionType, a -> Collections.synchronizedList(new ArrayList<>()))
                    .add(new Configuration<>(configurationAction, identifier));
        }
    }

    /**
     * Register an action.
     *
     * @param action the action to register.
     */
    void registerAction(@Nullable final AnAction action) {
        if (action == null) return;
        if (registerImpl(action, actionMap, pendingActionConfigurations)) {
            Logger.debug("Registering action '" + action.getIdentifier() + "'");
        }
    }

    /**
     * Register a group.
     *
     * @param group the group to register.
     */
    void registerGroup(@Nullable final AnActionGroup group) {
        if (group == null) return;
        if (registerImpl(group, groupMap, pendingGroupsConfigurations)) {
            Logger.debug("Registering group '" + group.getIdentifier() + "'");
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends AnAction> boolean registerImpl(final T action, final Map<Class<? extends T>, Map<String, T>> map,
            final Map<Class<? extends T>, List<Configuration<T>>> pendingConfigurations) {
        var type = (Class<? extends T>) action.getClass();
        var actions = map.computeIfAbsent(type, t -> Collections.synchronizedMap(new HashMap<>()));
        var identifier = action.getIdentifier();
        if (!actions.containsKey(identifier)) {
            actions.put(identifier, action);
            var pendingConfigs = pendingConfigurations.get(type);
            if (pendingConfigs != null) {
                pendingConfigs.forEach(it -> it.configure(action));
                pendingConfigs.clear();
                pendingConfigurations.remove(type);
            }
            return true;
        }
        return false;
    }

    private static class Configuration<T extends AnAction> {

        private final Consumer<T> action;
        private final String filter;

        private Configuration(final Consumer<T> action, final String filter) {
            this.action = action;
            this.filter = filter;
        }

        public void configure(final T t) {
            if (t == null) return;
            if (filter != null && !filter.equals(t.getIdentifier())) return;
            action.accept(t);
        }

    }
}
