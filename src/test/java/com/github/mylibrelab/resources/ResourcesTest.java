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

package com.github.mylibrelab.resources;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ResourcesTest {

    private static final String TEST_FILE = "testFile.txt";
    private static final String TEST_FILE_CONTENT =
            "Test Content" + System.lineSeparator() + "Second Line" + System.lineSeparator();

    @Test
    void testGetFileContentImplicit() {
        var content = Resources.getFileContent(TEST_FILE);
        Assertions.assertEquals(TEST_FILE_CONTENT, content);
    }

    @Test
    void testGetFileContent() {
        var content = Resources.getFileContent(getClass(), TEST_FILE, StandardCharsets.UTF_8);
        Assertions.assertEquals(TEST_FILE_CONTENT, content);
    }
}
