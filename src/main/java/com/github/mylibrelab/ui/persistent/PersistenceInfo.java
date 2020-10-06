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

import com.github.weisj.darklaf.util.Pair;

/**
 * Persistence Info Object. Uses a tree like structure for managing keys and values. keys are
 * separated by '.'. Uses a Map for faster key lookups as the tree structure is purely for having
 * faster merging speed and the ability to create subtree information.
 *
 * @author Jannis Weis
 * @since 2019
 */
public class PersistenceInfo {

    private final Map<String, String> valueMap;
    private InfoNode root;



    public PersistenceInfo() {
        root = new InfoNode("", "", false);
        valueMap = new HashMap<>();
    }


    private PersistenceInfo(final InfoNode node, final Map<String, String> valueMap) {
        this.root = node;
        this.valueMap = valueMap;
    }

    /**
     * Get the subtree starting with the given prefix.
     *
     * @param prefix the prefix.
     * @return the subtree.
     */
    public PersistenceInfo getSubTree(final String prefix) {
        String[] keys = prefix.isEmpty() ? new String[0] : prefix.split("\\.");
        InfoNode node = traverseToSub(keys, root, keys.length).getFirst();
        return new PersistenceInfo(node, valueMap);
    }

    private Pair<InfoNode, Integer> traverseToSub(final String[] keys, final InfoNode start, final int maxIndex) {
        InfoNode curr = start;
        InfoNode next = start;
        int i = 0;
        while (i < maxIndex && next != null) {
            curr = next;
            next = next.getChildren().get(keys[i]);
            i++;
        }
        if (next == null) {
            return new Pair<>(curr, i - 1);
        } else {
            return new Pair<>(next, i);
        }
    }

    /**
     * Store a value.
     *
     * @param key the key.
     * @param value the value.
     */
    public void putValue(final String key, final Object value) {
        InfoNode node = createNodes(key);
        node.setKeyEnd(true);
        node.setValue(value);
        valueMap.put(key, value.toString());
    }

    /*
     * Create all missing nodes necessary to traverse to full length of the key.
     */
    private InfoNode createNodes(final String key) {
        String[] keys = key.isEmpty() ? new String[0] : key.split("\\.");
        var traverse = traverseToSub(keys, root, keys.length);
        StringBuilder subKey = new StringBuilder(traverse.getFirst().getKey());
        InfoNode curr = traverse.getFirst();
        for (int i = traverse.getSecond(); i < keys.length; i++) {
            if (subKey.length() > 0) {
                subKey.append('.');
            }
            subKey.append(keys[i]);
            var node = new InfoNode(subKey.toString(), null, false);
            curr.getChildren().put(keys[i], node);
            curr = node;
        }
        return curr;
    }


    private Object get(final String key) {
        String k = root.getKey();
        k = k.isEmpty() ? key : k + "." + key;
        return valueMap.get(k);
    }

    /**
     * Remove the intersection of the given PersistenceInfo.
     *
     * @param info the info to remove.
     * @param prefix the prefix to start the removal process from. All keys in 'info' will be treated as
     *        if they start with the given prefix.
     */
    public void remove(final PersistenceInfo info, final String prefix) {
        String[] keys = prefix.isEmpty() ? new String[0] : prefix.split("\\.");
        InfoNode subTree = traverseToSub(keys, root, keys.length).getFirst();
        remove(subTree, info.root);
    }

    private void remove(final InfoNode target, final InfoNode toRemove) {
        if (target == null) return;
        for (var entry : toRemove.getChildren().entrySet()) {
            InfoNode n = entry.getValue();
            String key = entry.getKey();
            InfoNode tarNode = target.getChildren().get(key);
            remove(tarNode, n);
            if (n.isKeyEnd()) {
                valueMap.remove(n.getKey());
                if (tarNode != null) {
                    if (tarNode.getChildren().isEmpty()) {
                        target.getChildren().remove(key);
                    } else {
                        tarNode.setKeyEnd(false);
                    }
                }
            }
        }
    }

    /**
     * Get the value associated with the key.
     *
     * @param key the kay.
     * @param defaultValue default value to use when value could not be loaded.
     * @return the value.
     */
    public String getString(final String key, final String defaultValue) {
        String value = (String) get(key);
        return value != null ? value : defaultValue;
    }

    /**
     * Get the value associated with the key.
     *
     * @param key the kay.
     * @param defaultValue default value to use when value could not be loaded.
     * @return the value.
     */
    public double getDouble(final String key, final double defaultValue) {
        String value = (String) get(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Get the value associated with the key.
     *
     * @param key the kay.
     * @param defaultValue default value to use when value could not be loaded.
     * @return the value.
     */
    public int getInt(final String key, final int defaultValue) {
        String value = (String) get(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Get the value associated with the key.
     *
     * @param key the kay.
     * @param defaultValue default value to use when value could not be loaded.
     * @return the value.
     */
    public boolean getBoolean(final String key, final boolean defaultValue) {
        String value = (String) get(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }

    /**
     * Merge the given information into the tree.
     *
     * @param saveState info to merge.
     */
    public void merge(final PersistenceInfo saveState) {
        merge(saveState, saveState.root.getKey());
    }

    /**
     * Merge the given information into the tree.
     *
     * @param saveState info to merge.
     * @param prefix the prefix to start the merge process from. All keys in 'saveState' will be treated
     *        as if they start with the given prefix.
     */
    public void merge(final PersistenceInfo saveState, final String prefix) {
        InfoNode node = createNodes(prefix);
        merge(saveState.root, node);
    }

    private void merge(final InfoNode source, final InfoNode dest) {
        dest.setKeyEnd(source.isKeyEnd());
        String pref = dest.getKey().isEmpty() ? "" : dest.getKey() + '.';
        for (var entry : source.getChildren().entrySet()) {
            InfoNode n = entry.getValue();
            String key = entry.getKey();
            if (n.isKeyEnd()) {
                valueMap.put(pref + key, n.getValue() == null ? "" : n.getValue().toString());
            }
            if (dest.getChildren().containsKey(key)) {
                merge(n, dest.getChildren().get(key));
            } else {
                InfoNode newNode = new InfoNode(pref + n.getKey(), n.getValue(), n.isKeyEnd());
                dest.getChildren().put(entry.getKey(), newNode);
                merge(n, newNode);
            }
        }
    }

    public void clear() {
        valueMap.clear();
        root = new InfoNode("", "", false);
    }

    /**
     * Get te´he direct mapping for all contained keys.
     *
     * @return the map form keys to values.
     */
    public Map<String, String> directMap() {
        return new MaskedMap<>(valueMap, root.getKey());
    }

}
