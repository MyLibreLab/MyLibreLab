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

package com.github.mylibrelab.ui.persistent;

import java.util.HashMap;
import java.util.Map;

/**
 * Node for keeping persistence information.
 *
 * @author Jannis Weis
 * @since 2020
 */
final class InfoNode {
    private final String key;
    private final Map<String, InfoNode> children;
    private Object value;
    private boolean keyEnd;

    InfoNode(final String key, final Object value, final boolean keyEnd) {
        this.value = value;
        this.key = key;
        this.keyEnd = keyEnd;
        children = new HashMap<>();
    }

    /**
     * Get the associated key.
     *
     * @return the key.
     */
    public String getKey() {
        return key;
    }

    /**
     * Get the child nodes.
     *
     * @return map of child nodes.
     */
    public Map<String, InfoNode> getChildren() {
        return children;
    }

    /**
     * Get the value.
     *
     * @return the value of the node.
     */

    public Object getValue() {
        return value;
    }

    /**
     * Set the value of the node.
     *
     * @param value the new value.
     */
    public void setValue(final Object value) {
        this.value = value;
    }

    /**
     * If true represents the end of a key and indicates that the value in the node should be saved.
     *
     * @return true if key end.
     */
    public boolean isKeyEnd() {
        return keyEnd;
    }

    /**
     * Set whether a key ends at this node.
     *
     * @param keyEnd true if key ends at this node.
     */
    public void setKeyEnd(final boolean keyEnd) {
        this.keyEnd = keyEnd;
    }
}
