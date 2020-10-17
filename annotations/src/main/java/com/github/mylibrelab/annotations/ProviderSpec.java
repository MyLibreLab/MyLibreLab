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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Meta annotation used to declare new provider annotations which enforce compile time bounds on the
 * type parameters of {@link ProviderFor}.
 * <p>
 * If the newly declared provider annotation needs to be available from a separate module it should
 * declare it's retention policy as {@code @Retention(RetentionPolicy.CLASS)} otherwise
 * {@code @Retention(RetentionPolicy.SOURCE)}
 * <p>
 * Provider specification are used for service requests where the provided value is dependent on an
 * object of specific type. Request are handled as follows:
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
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ProviderSpec {

    /**
     * The bound for the input type. This is a contravariant bound. i.e. if the bound is of type B and A
     * is a supertype of B then A will be an accepted type.
     *
     * This value can't be a primitive type.
     *
     * The default value is {@code void.class}. This value is arbitrarily chosen as there is no bottom
     * type in java. However primitives types aren't allowed.
     *
     * @return the bound for the input type.
     */
    Class<?> inputBound() default void.class;

    /**
     * The bound for the input type. This is a covariant bound. i.e. if the bound is of type A and B is
     * a subtype of A then B will be an accepted type.
     *
     * This value can't be a primitive type.
     *
     * The default value is {@code Object.class} as it's the top type.
     *
     * @return the bound for the output type.
     */
    Class<?> outputBound() default Object.class;
}
