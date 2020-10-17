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

package com.github.mylibrelab.collections.tree;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Node for creating tree structures.
 *
 * @param <N> the node value type.
 * @param <E> the edge value type.
 */
public class TreeNode<N, E> implements Iterable<TreeNode<N, E>> {

    private final Map<E, TreeNode<N, E>> nodes;
    private TreeNode<N, E> parent;
    private E parentEdge;
    private N value;

    /**
     * Create a new root node with the given value.
     *
     * @param value the value of the node.
     */
    public TreeNode(@Nullable final N value) {
        this(null, null, value);
    }

    /**
     * Create a new node with the given parent and value.
     *
     * @param parent the parent node.
     * @param parentEdge the edge going from the parent to this node.
     * @param value the value of the node.
     */
    public TreeNode(@Nullable final TreeNode<N, E> parent, @Nullable final E parentEdge, @Nullable final N value) {
        this.value = value;
        this.parent = parent;
        this.parentEdge = parentEdge;
        this.nodes = new HashMap<>();
    }

    protected TreeNode<N, E> createNode(@Nullable final TreeNode<N, E> parent, @Nullable final E edge,
            @Nullable final N value) {
        return new TreeNode<>(parent, edge, value);
    }

    /**
     * If this root doesn't have a parent then it is considered a root node.
     *
     * @return true if this node is a root node.
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * Get the parent node or null if this is a root node.
     *
     * @return the parent or null
     */
    @Nullable
    public TreeNode<N, E> getParent() {
        return parent;
    }

    /**
     * Returns the edge going from the parent node to this node or null if this node is a root node.
     *
     * @return the edge or null
     */
    @Nullable
    public E getParentEdge() {
        return parentEdge;
    }

    /**
     * Get the value of this node.
     *
     * @return the value of this node.
     */
    @Nullable
    public N getNodeValue() {
        return value;
    }

    /**
     * Set the value of this node.
     *
     * @param value the value of this node.
     */
    public void setNodeValue(@Nullable final N value) {
        this.value = value;
    }

    /**
     * Returns the number of edges going out of this node.
     *
     * @return the number of edges going out of the node.
     */
    public int getEdgeCount() {
        return nodes.size();
    }

    /**
     * Returns a view to the edges going out of this node.
     *
     * @return the edges going out of this node.
     */
    @NotNull
    public Set<E> edges() {
        return nodes.keySet();
    }

    /**
     * Inserts a new node with the given value connected to through the given edge.
     *
     * @param edge the edge.
     * @param value the node value.
     * @return the inserted node.
     */
    @NotNull
    public TreeNode<N, E> insert(@NotNull final E edge, @Nullable final N value) {
        var node = createNode(this, edge, value);
        insertNode(edge, node);
        return node;
    }

    /**
     * Sets the value at the node connected to the given edge. If no node is associated to this edge a
     * new node will be created.
     *
     * @param edge the edge.
     * @param value the new node value.
     */
    public void set(@NotNull final E edge, @Nullable final N value) {
        if (hasEdge(edge)) {
            getSubTree(edge).setNodeValue(value);
        } else {
            insert(edge, value);
        }
    }

    /**
     * Inserts a new node connected with the given edge.
     *
     * @param edge the edge.
     * @param subtree the subtree node to insert.
     * @return the inserted node.
     */
    @NotNull
    public TreeNode<N, E> insertNode(@NotNull final E edge, @NotNull final TreeNode<N, E> subtree) {
        subtree.parentEdge = edge;
        subtree.parent = this;
        nodes.put(edge, subtree);
        return subtree;
    }

    /**
     * Removes an edge from the node.
     *
     * @param edge the edge to remove.
     */
    public void removeEdge(@Nullable final E edge) {
        nodes.remove(edge);
    }

    /**
     * Returns the subtree starting from the node connected to the given edge.
     *
     * @param edge the edge.
     * @return the subtree.
     * @throws NoSuchElementException if there is no node connected to the given edge.
     */
    @NotNull
    public TreeNode<N, E> getSubTree(@NotNull final E edge) {
        if (!hasEdge(edge)) {
            throw new NoSuchElementException("Edge '" + edge + "' does not exist.");
        }
        return nodes.get(edge);
    }

    /**
     * Returns the value at the node connected to the given edge or null if there is no such node.
     *
     * @param edge the edge.
     * @return the value at the node or null
     */
    @Nullable
    public N getValueAtEdge(@Nullable final E edge) {
        if (!hasEdge(edge)) return null;
        return nodes.get(edge).getNodeValue();
    }

    /**
     * Returns whether the given edge exists.
     *
     * @param edge the edge.
     * @return true if it exists.
     */
    public boolean hasEdge(@Nullable final E edge) {
        return nodes.containsKey(edge);
    }

    @Override
    @NotNull
    public Iterator<TreeNode<N, E>> iterator() {
        return nodes.values().iterator();
    }

    @Override
    public void forEach(final Consumer<? super TreeNode<N, E>> action) {
        nodes.values().forEach(action);
    }

    @Override
    @NotNull
    public Spliterator<TreeNode<N, E>> spliterator() {
        return nodes.values().spliterator();
    }

    @NotNull
    public Stream<TreeNode<N, E>> stream() {
        return nodes.values().stream();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(Objects.toString(value, ""));
        nodes.entrySet().stream().sorted(Comparator.comparing(n -> Objects.toString(n.getKey(), ""))).forEach(e -> {
            var keyStr = e.getKey().toString();
            var valueStr = e.getValue().toString().replace("\n", "\n\t");
            sb.append('\n');
            sb.append(keyStr).append(" -> ").append(valueStr);
        });
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TreeNode)) return false;
        TreeNode<?, ?> treeNode = (TreeNode<?, ?>) o;
        return Objects.equals(value, treeNode.value) && nodes.equals(treeNode.nodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, nodes);
    }
}
