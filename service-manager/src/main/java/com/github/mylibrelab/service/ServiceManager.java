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

package com.github.mylibrelab.service;

import java.util.*;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import com.github.mylibrelab.annotations.ProviderForProvider;

/**
 * Utility class to handle service loading.
 */
public class ServiceManager {

    private static final List<ProviderForProvider<Object, Object>> providerList;
    private static final Map<Class<?>, Map<Object, Object>> contextualServiceCache = new HashMap<>();
    private static final Map<Class<?>, ServiceLoader<?>> serviceLoaderMap = new HashMap<>();

    static {
        // noinspection unchecked
        providerList = ServiceLoader.load(ProviderForProvider.class).stream().map(ServiceLoader.Provider::get)
                .map(p -> (ProviderForProvider<Object, Object>) p).collect(Collectors.toList());
    }

    private ServiceManager() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Returns a service which is constructed from the given target value.
     * <p>
     * Providers can be registered with the {@link com.github.mylibrelab.annotations.Provider}
     * annotation or any other annotation which is annotated with
     * {@link com.github.mylibrelab.annotations.ProviderSpec}.
     * <p>
     * Providers need to implement {@link com.github.mylibrelab.annotations.ProviderFor} with the
     * fitting type bounds.
     * <p>
     * Request are handled as follows:
     * <p>
     * For an object a of type {@code A} which request an object of type {@code B} a provider which is
     * "assignable" to {@code ProviderFor<? super A, ? extend B>}.
     * <p>
     * "assignable" in quotes because java won't actually let one assign one to the other, but
     * assignable is meant in the way that the following is legal:
     *
     * <pre>
     * {@code
     *     ProviderFor<? super A, ? extends B> provider = ...
     *     A a = ...
     *     B b = provider.provide(a);
     * }
     * </pre>
     *
     * If there are multiple candidates which match, then the provider with the most specific inout and
     * the least specific output type is used. i.e. if there are type {@code C < B < A} (where {@code <}
     * means "is subtype of"), and {@code P<T, K> := ProviderFor<T, K>} then
     * <p>
     * <ul>
     * <li>requesting {@code P<C, A>} with {@code P<A, A>}, {@code P<B, A>} and {@code P<C, A>}
     * available will result in {@code P<C, A>} being selected.</li>
     * <li>requesting {@code P<A, A>} with {@code P<A, A>}, {@code P<A, B>} and {@code P<A, C>}
     * available will result in {@code P<C, A>} being selected.</li>
     * </ul>
     * <p>
     * If there are multiple providers which fulfill the requested specification which cannot be
     * compared (e.g. {@code P<A1, B>}, {@code P<A2, B>} where {@code A1} and {@code A2} both are
     * subtypes of {@code A} but neither is a subtype of the other) then a warning is issued and no
     * guarantee is given on which provider will be used.
     *
     * @param type the class of the requested value.
     * @param target the target value.
     * @param <T> the type of the requested value.
     * @return the provided value.
     */
    @NotNull
    public static <T> T getProvidedServiceFor(@NotNull final Class<T> type, final Object target) {
        var map = contextualServiceCache.get(type);
        if (map != null) {
            var provided = map.get(target);
            if (type.isInstance(provided)) {
                return type.cast(provided);
            }
        }
        var provider = providerList.stream().filter(p -> p.canProvide(type, target)).sorted().findFirst()
                .orElseThrow(IllegalStateException::new);
        var providedValue = provider.getProvider().provide(target);
        contextualServiceCache.computeIfAbsent(type, t -> new WeakHashMap<>()).put(target, providedValue);
        T service = type.cast(providedValue);
        if (service == null) {
            throw new IllegalStateException(
                    "No provider with return type " + type + " for target " + target + " was found");
        }
        return service;
    }

    /**
     * Returns all registered services for the given type.
     * <p>
     * Services can be registered using the {@link com.github.mylibrelab.annotations.Service @Service}
     * annotation or any other annotation which is annotated by
     * {@link com.github.mylibrelab.annotations.ServiceSpec @Service}.
     *
     * @param type the class of the service.
     * @param <T> the service type.
     * @return all registered services.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public static <T> Iterable<T> getAllServices(@NotNull final Class<T> type) {
        return (Iterable<T>) serviceLoaderMap.computeIfAbsent(type, ServiceLoader::load);
    }

    /**
     * Returns the service for the given type.
     *
     * @param type the service class.
     * @param <T> the service type
     * @return the registered service. If there are more than one registered services one is chosen and
     *         a warning is issued.
     */
    @NotNull
    public static <T> T getService(@NotNull final Class<T> type) {
        var sl = getAllServices(type).iterator();
        T service = sl.next();
        if (sl.hasNext()) {
            Logger.warn("Service type '" + type
                    + "' has multiple implementation but is requested as a single service. Provided implementation is non deterministic.");
        }
        return service;
    }

    /**
     * Returns the service for the given type if it already has been created.
     *
     * @param type the service type.
     * @param <T> the service type.
     * @return the registered service. If there are more than one registered services one is chosen and
     *         a warning is issued.
     * @see #getService(Class)
     */
    @Nullable
    public static <T> T getServiceIfCreated(@NotNull final Class<T> type) {
        if (serviceLoaderMap.containsKey(type)) {
            return getService(type);
        } else {
            return null;
        }
    }

}
