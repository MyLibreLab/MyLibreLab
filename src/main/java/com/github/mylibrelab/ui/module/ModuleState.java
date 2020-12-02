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

package com.github.mylibrelab.ui.module;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.github.weisj.darklaf.components.tabframe.TabFramePopup;
import com.github.weisj.darklaf.util.DarkUIUtil;

public class ModuleState {
    private final Map<ApplicationModule, ModuleInfo> moduleInfoMap = new HashMap<>();

    /**
     * Get a view on all current {@link ApplicationModule}s.
     *
     * @return the currently registered {@link ApplicationModule}s.
     */
    public Set<ApplicationModule> getActiveModules() {
        return Collections.unmodifiableSet(moduleInfoMap.keySet());
    }

    /**
     * Get the {@link ModuleInfo} of the given {@link ApplicationModule}s.
     *
     * @param module the {@link ApplicationModule}s.
     * @return the associated {@link ModuleState}.
     */
    @Nullable
    public ModuleInfo getModuleInfo(@Nullable final ApplicationModule module) {
        return moduleInfoMap.get(module);
    }

    /**
     * Register an {@link ApplicationModule}.
     *
     * @param module the module.
     * @param popup the view of the module.
     */
    public void registerModule(@NotNull final ApplicationModule module, @NotNull final TabFramePopup popup) {
        moduleInfoMap.put(module, new ModuleInfo(popup));
    }

    public static class ModuleInfo {

        private final TabFramePopup popup;

        ModuleInfo(@NotNull final TabFramePopup popup) {
            this.popup = popup;
        }

        /**
         * Returns whether the module is currently visible.
         *
         * @return true if it is visible.
         */
        public boolean isVisible() {
            return popup.isOpen();
        }

        /**
         * Returns whether the module currently has focus in the application.
         *
         * @return true if the associated view is the ancestor of the current focus component.
         */
        public boolean hasFocus() {
            return DarkUIUtil.hasFocus(popup.getContentPane());
        }
    }
}
