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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.NotNull;


/**
 * Map that masks the contained values by matching the string representation of the key against a
 * mask.
 *
 * @author Jannis Weis
 * @since 2019
 */
class MaskedMap<K, V> implements Map<K, V> {

    private final String mask;
    private final Map<K, V> map;


    MaskedMap(final Map<K, V> map, final String mask) {
        this.map = map;
        this.mask = mask;
    }

    @Override
    public int size() {
        return map.size();
    }


    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }


    @Override
    public boolean containsKey(final Object key) {
        return key.toString().startsWith(mask) && map.containsKey(key);
    }


    @Override
    public boolean containsValue(final Object value) {
        for (var entry : map.entrySet()) {
            if (entry.toString().startsWith(mask) && entry.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(final Object key) {
        return key.toString().startsWith(mask) ? map.get(key) : null;
    }


    @Override
    public V put(final K key, final V value) {
        return map.put(key, value);
    }

    @Override
    public V remove(final Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }


    @NotNull
    @Override
    public Set<K> keySet() {
        return map.keySet();
    }


    @NotNull
    @Override
    public Collection<V> values() {
        return map.values();
    }


    @NotNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }
}
