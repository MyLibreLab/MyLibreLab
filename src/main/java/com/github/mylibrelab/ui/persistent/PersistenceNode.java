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

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.github.mylibrelab.collections.tree.TreeNode;

public class PersistenceNode {

    private final TreeNode<String, String> treeNode;

    public PersistenceNode() {
        this(new TreeNode<>(null));
    }

    protected PersistenceNode(@NotNull final TreeNode<String, String> treeNode) {
        this.treeNode = treeNode;
    }

    /**
     * Create the persistence node structure from a flattened map where paths in the tree structure are
     * represented by the edges joined with '.'.
     *
     * @see #toFlattenedMap()
     * @param map the map to construct the node from.
     * @return the created node.
     */
    public static PersistenceNode fromFlattenedMap(@NotNull final Map<String, String> map) {
        var node = new TreeNode<String, String>(null);
        map.forEach((k, v) -> {
            var path = k.split("\\.");
            var current = node;
            for (int i = 0; i < path.length - 1; i++) {
                current = getOrCreateSubTree(current, path[i]);
            }
            current.set(path[path.length - 1], v);
        });
        return new PersistenceNode(node);
    }

    /**
     * Converts this persistence node to a flattened map where paths along the tree are encoded as keys
     * where the edges are joined using '.'.
     *
     * @see #fromFlattenedMap(Map)
     * @return the flattened map.
     */
    public @NotNull Map<String, String> toFlattenedMap() {
        return treeNode.flatStream().filter(n -> n.getNodeValue() != null)
                .collect(Collectors.toMap(PersistenceNode::makePath, TreeNode::getNodeValue));
    }

    private static String makePath(final TreeNode<String, String> node) {
        StringBuilder sb = new StringBuilder();
        var curr = node;
        while (curr != null && !curr.isRoot()) {
            if (sb.length() > 0) {
                sb.insert(0, ".");
            }
            sb.insert(0, curr.getParentEdge());
            curr = curr.getParent();
        }
        return sb.toString();
    }

    private static TreeNode<String, String> getOrCreateSubTree(final TreeNode<String, String> node, final String edge) {
        if (node.hasEdge(edge)) {
            return node.getSubTree(edge);
        } else {
            return node.insert(edge, null);
        }
    }

    /**
     * Returns the sub node for the given key.
     *
     * @param key the key.
     * @return the sub node.
     */
    @NotNull
    public PersistenceNode getSubNode(@NotNull final String key) {
        checkKey(key);
        return new PersistenceNode(getOrCreateSubTree(treeNode, key));
    }

    /**
     * Inserts a new value into the node.
     *
     * @param key the key.
     * @param value the value.
     * @return the created node associated to the new key.
     */
    @NotNull
    public PersistenceNode insert(@NotNull final String key, @NotNull final Object value) {
        checkKey(key);
        return new PersistenceNode(treeNode.insert(key, value.toString()));
    }

    /**
     * Inserts a node into this node at the given key.
     *
     * @param key the key to insert at.
     * @param node the node to inserts.
     * @return the resulting inserted node.
     */
    @NotNull
    public PersistenceNode insert(@NotNull final String key, @NotNull final PersistenceNode node) {
        checkKey(key);
        return new PersistenceNode(treeNode.insertNode(key, node.treeNode));
    }

    private void checkKey(final String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null");
        }
        if (key.contains(".")) {
            throw new IllegalArgumentException("Key '" + key
                    + "' contains a period. Keys containing a period will be split into separate nodes after parsing.");
        }
    }

    /**
     * Get the object associated to this key.
     *
     * @param key the key.
     * @return the object associated to the key.
     */
    protected @Nullable Object get(@NotNull final String key) {
        return treeNode.getValueAtEdge(key);
    }

    @Override
    public String toString() {
        return treeNode.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersistenceNode)) return false;
        PersistenceNode node = (PersistenceNode) o;
        return Objects.equals(treeNode, node.treeNode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(treeNode);
    }


    /**
     * Get the value associated with the key.
     *
     * @param key the kay.
     * @param defaultValue default value to use when value could not be loaded.
     * @return the value.
     */
    @NotNull
    public String getString(@NotNull final String key, @NotNull final String defaultValue) {
        var value = get(key);
        return value != null ? value.toString() : defaultValue;
    }

    /**
     * Get the value associated with the key.
     *
     * @param key the kay.
     * @param defaultValue default value to use when value could not be loaded.
     * @return the value.
     */
    public double getDouble(@NotNull final String key, final double defaultValue) {
        Object value = get(key);
        if (value == null) return defaultValue;
        try {
            return Double.parseDouble(value.toString());
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
    public int getInt(@NotNull final String key, final int defaultValue) {
        Object value = get(key);
        if (value == null) return defaultValue;
        try {
            return Integer.parseInt(value.toString());
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
    public boolean getBoolean(@NotNull final String key, final boolean defaultValue) {
        Object value = get(key);
        return value != null ? Boolean.parseBoolean(value.toString()) : defaultValue;
    }
}
