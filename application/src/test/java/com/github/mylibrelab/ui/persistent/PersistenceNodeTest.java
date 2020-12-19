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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PersistenceNodeTest {

    private PersistenceNode node;
    private Map<String, String> map;

    @BeforeEach
    void setup() {
        node = new PersistenceNode();
        node.insert("A1", 1);
        node.insert("B1", "B").insert("B2", 4);
        node.insert("C1", "3");
        map = Map.of("A1", "1", "B1", "B", "B1/B2", "4", "C1", "3");
    }

    @Test
    void testElementAccess() {
        Assertions.assertEquals(1, node.getInt("A1", -1));
        Assertions.assertEquals(-1, node.getInt("B1", -1));
        Assertions.assertEquals(4, node.getSubNode("B1").getInt("B2", -1));
    }

    @Test
    void convertToNode() {
        Assertions.assertEquals(node, PersistenceNode.fromFlattenedMap(map));
    }

    @Test
    void convertFromNode() {
        Assertions.assertEquals(map, node.toFlattenedMap());
    }

    @Test
    void leftInverse() {
        var asMap = node.toFlattenedMap();
        Assertions.assertEquals(node, PersistenceNode.fromFlattenedMap(asMap));
    }

    @Test
    void rightInverse() {
        Assertions.assertEquals(map, PersistenceNode.fromFlattenedMap(map).toFlattenedMap());
    }
}
