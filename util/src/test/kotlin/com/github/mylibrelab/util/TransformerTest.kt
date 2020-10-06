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

package com.github.mylibrelab.util

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TransformerTest {

    @Test
    fun `intParser is invertible`() {
        val parser = intParser()
        listOf(0, 1, Int.MAX_VALUE, Int.MIN_VALUE).forEach {
            assertInvertible(it, it.toString(), parser)
        }
    }

    @Test
    fun `longParser is invertible`() {
        val parser = longParser()
        listOf(0, 1, Int.MAX_VALUE.toLong(), Int.MIN_VALUE.toLong(), Long.MIN_VALUE, Long.MAX_VALUE).forEach {
            assertInvertible(it, it.toString(), parser)
        }
    }

    @Test
    fun `floatParser is invertible`() {
        val parser = floatParser()
        listOf(
            0f,
            1f,
            Float.MIN_VALUE,
            Float.MAX_VALUE,
            Float.NEGATIVE_INFINITY,
            Float.POSITIVE_INFINITY,
            Float.NaN
        ).forEach {
            assertInvertible(it, it.toString(), parser)
        }
    }

    @Test
    fun `doubleParser is invertible`() {
        val parser = doubleParser()
        listOf(
            0.0,
            1.0,
            Float.MIN_VALUE.toDouble(),
            Float.MAX_VALUE.toDouble(),
            Float.NEGATIVE_INFINITY.toDouble(),
            Float.POSITIVE_INFINITY.toDouble(),
            Double.NaN,
            Double.MAX_VALUE,
            Double.MIN_VALUE,
            Double.NEGATIVE_INFINITY,
            Double.POSITIVE_INFINITY
        ).forEach {
            assertInvertible(it, it.toString(), parser)
        }
    }

    @Test
    fun `booleanParser is invertible`() {
        val parser = boolParser()
        listOf(true, false).forEach {
            assertInvertible(it, it.toString(), parser)
        }
    }

    @Test
    fun `identityTransformer returns same object`() {
        val transformer = identityTransformer<Any>()
        val any = Any()
        assertInvertible(any, any, transformer)
    }

    @Test
    fun `listParser is invertible`() {
        val list = listOf(1, 2, 3, 4)
        val mutableList = mutableListOf(1, 2, 3, 4)
        val listStr = "1@2@3@4"
        val parser = listParser(
            elementParser = intParser(),
            delimiter = '@'
        )
        val mutableParser = mutableListParser(
            elementParser = intParser(),
            delimiter = '@'
        )
        assertInvertible(list, listStr, parser)
        assertInvertible(mutableList, listStr, mutableParser)
    }

    @Test
    fun `listParser fails if entry contains delimiter`() {
        Assertions.assertThrows(IllegalStateException::class.java) {
            listParser(
                elementParser = identityTransformer(),
                delimiter = ','
            ).read(listOf("a", ",", "b"))
        }
    }

    @Test
    fun `mapParser is invertible`() {
        val map = mapOf(1 to "1", 2 to "2", 3 to "3", 4 to "4")
        val mutableMap = mutableMapOf(1 to "1", 2 to "2", 3 to "3", 4 to "4")
        val mapStr = "1:1,2:2,3:3,4:4"
        val parser = mapParser(
            keyParser = intParser(),
            valueParser = identityTransformer(),
            entryDelimiter = ':',
            entryListDelimiter = ','
        )
        val mutableParser = mutableMapParser(
            keyParser = intParser(),
            valueParser = identityTransformer(),
            entryDelimiter = ':',
            entryListDelimiter = ','
        )
        assertInvertible(map, mapStr, parser)
        assertInvertible(mutableMap, mapStr, mutableParser)
    }

    @Test
    fun `pairParser is invertible`() {
        data class Dummy(val a: Float, val b: Float)

        val dummy = Dummy(5.0f, 3.0f)
        val dummyStr = "5.0,3.0"
        val parser = pairParser(
            toType = ::Dummy,
            firstAccessor = Dummy::a,
            secondAccessor = Dummy::b,
            parser = floatParser(),
            delimiter = ','
        )
        assertInvertible(dummy, dummyStr, parser)
    }

    @Test
    fun `componentParser is invertible`() {
        val pair = 1 to 2
        val pairStr = "1,2"
        val parser = componentParser(
            componentParser = intParser(),
            listTransformer = pairToList(),
            delimiter = ','
        )
        assertInvertible(pair, pairStr, parser)
    }

    @Test
    fun `listToPair is invertible`() {
        val list = listOf(1, 2)
        val pair = 1 to 2
        val transformer = listToPair<Int>()
        assertInvertible(list, pair, transformer)
    }

    @Test
    fun `listToPair fails too few elements`() {
        Assertions.assertThrows(IndexOutOfBoundsException::class.java) {
            pairToList<Int>().write(listOf(1))
        }
    }

    @Test
    fun `listToPair ignores excessive elements`() {
        val list = listOf(1, 2, 3)
        Assertions.assertEquals(1 to 2, listToPair<Int>().read(list))
    }

    @Test
    fun `splitString is invertible`() {
        val list = listOf("a", "b", "c")
        val listStr = "a,b,c"
        val transformer = splitString(delimiter = ',')
        assertInvertible(listStr, list, transformer)
    }

    private fun <T, R> assertInvertible(
        tValue: T,
        rValue: R,
        transformer: Transformer<T, R>
    ) {
        Assertions.assertEquals(rValue, transformer.read(tValue))
        Assertions.assertEquals(tValue, transformer.write(rValue))
        Assertions.assertEquals(rValue, transformer.read(transformer.write(rValue)))
        Assertions.assertEquals(tValue, transformer.write(transformer.read(tValue)))
    }
}
