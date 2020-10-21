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

package com.github.mylibrelab.text;

import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InternationalizedTextTest {

    private static final String BUNDLE = "com.github.mylibrelab.text.internationalizedTextTest";

    @Test
    void testTextChangesValueIfLocaleChanged() {
        var bundle = new DynamicResourceBundle(BUNDLE);
        var text = new InternationalizedText(bundle, "key");
        Locale.setDefault(Locale.ENGLISH);
        Assertions.assertEquals("EN", text.getText());
        Locale.setDefault(Locale.GERMAN);
        Assertions.assertEquals("DE", text.getText());
        Locale.setDefault(Locale.FRENCH);
        Assertions.assertEquals("Default", text.getText());
    }

    @Test
    void testTextIsEqualIfBundleAndKeyNameAreTheSame() {
        var bundle = new DynamicResourceBundle("com.github.mylibrelab.text.internationalizedTextTest");
        Assertions.assertEquals(new InternationalizedText(bundle, "key"), new InternationalizedText(BUNDLE, "key"));
    }
}
