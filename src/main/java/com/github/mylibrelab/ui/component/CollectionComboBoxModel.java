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

package com.github.mylibrelab.ui.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.jetbrains.annotations.Nullable;

/**
 * Convenience implementation of {@link ComboBoxModel} which simply wraps as given collection
 * without allocating any extra list. For the lifetime of a {@link CollectionComboBoxModel} it
 * assumes that it has ownership of the passed list. The given list should not be mutated.
 *
 * @param <T> the type of the items.
 */
public class CollectionComboBoxModel<T> implements ComboBoxModel<T> {

    private final List<ListDataListener> listenerList = new ArrayList<>();
    private final List<T> items;
    private T selectedItem;

    public CollectionComboBoxModel(final List<T> items) {
        this.items = items;
        if (!items.isEmpty()) selectedItem = getElementAt(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setSelectedItem(@Nullable final Object anItem) {
        if (!Objects.equals(selectedItem, anItem)) {
            this.selectedItem = (T) anItem;
            if (!listenerList.isEmpty()) {
                ListDataEvent e = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, -1, -1);
                for (ListDataListener l : listenerList) {
                    l.contentsChanged(e);
                }
            }
        }
    }

    @Override
    @Nullable
    public T getSelectedItem() {
        return selectedItem;
    }

    @Override
    public int getSize() {
        return items.size();
    }

    @Override
    public T getElementAt(int index) {
        return items.get(index);
    }

    @Override
    public void addListDataListener(final ListDataListener l) {
        listenerList.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listenerList.remove(l);
    }
}
