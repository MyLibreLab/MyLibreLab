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

package de.myopenlab.update;

public class TestItem {
    public String name = "";
    public String type = "";
    public String caption_de = "";
    public String caption_en = "";
    public String caption_es = "";

    public TestItem(String name, String type, String caption_de, String caption_en, String caption_es) {
        this.name = name;
        this.type = type;

        this.caption_de = caption_de;
        this.caption_en = caption_en;
        this.caption_es = caption_es;
    }
}
