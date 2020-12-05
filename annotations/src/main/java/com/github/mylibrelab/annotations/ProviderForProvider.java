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

package com.github.mylibrelab.annotations;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

/**
 * Wrapper class for {@link ProviderFor} classes which ensure an most to least specific sorting when
 * used.
 *
 * @param <T> the input type
 * @param <K> the output type
 */
public abstract class ProviderForProvider<T, K> implements Comparable<ProviderForProvider<?, ?>> {

    private final Class<T> targetType;
    private final Class<K> valueType;

    protected ProviderForProvider(@NotNull final Class<T> targetType, final Class<K> valueType) {
        this.targetType = targetType;
        this.valueType = valueType;
    }

    public boolean canProvide(final Class<?> valueType, final Object target) {
        return targetType.isInstance(target) && valueType.isAssignableFrom(this.valueType);
    }

    @Override
    public int compareTo(@NotNull final ProviderForProvider<?, ?> o) {
        /*
         * We want to sort such that the target type is the most specific and the provided type is the least
         * specific.
         */
        int comparison = compare(targetType, o.targetType, o);
        if (comparison == 0) {
            return compare(o.valueType, valueType, o);
        } else {
            return comparison;
        }
    }

    /*
     * Sort types in ascending order. If the types can't be compared then returns 0.
     */
    private int compare(final Class<?> a, final Class<?> b, final ProviderForProvider<?, ?> other) {
        boolean aGreaterThanB = a.isAssignableFrom(b);
        boolean bGreaterThanA = b.isAssignableFrom(a);
        if (aGreaterThanB && bGreaterThanA) return 0;
        if (aGreaterThanB) return 1;
        if (bGreaterThanA) return -1;
        Logger.warn("Both providers '" + this + "' and '" + other + "' qualify for the same service request.\n "
                + "Choosing the provider with the alphabetically first value type.\n"
                + "Either make your request more specific or remove one of the given providers.");
        // To ensure a total order we find the top most types before their nearest common ancestor
        // and compare their name. This ensures that out of each chain in the class hierarchy poset we
        // get the top most element before the chains meet.
        var aCurr = a;
        var aParent = aCurr;
        while (!aParent.isAssignableFrom(b)) {
            aCurr = aParent;
            aParent = getSuperClass(aCurr);
        }
        var bCurr = b;
        var bParent = bCurr;
        while (!(bParent.isAssignableFrom(aParent))) {
            bCurr = bParent;
            bParent = getSuperClass(bCurr);
        }
        return aCurr.getName().compareTo(bCurr.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProviderForProvider<?, ?> that = (ProviderForProvider<?, ?>) o;
        return targetType.equals(that.targetType) && valueType.equals(that.valueType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetType, valueType);
    }

    private static Class<?> getSuperClass(final Class<?> c) {
        return c.isInterface() ? Object.class : c.getSuperclass();
    }

    public abstract ProviderFor<T, K> getProvider();

    @Override
    public String toString() {
        return "ProviderForProvider{" + "targetType=" + targetType + ", valueType=" + valueType + '}';
    }
}
