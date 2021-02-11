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

package VisualLogic.variables;

import java.io.IOException;

public class VSBoolean extends VSObject {
    private boolean value;

    public VSBoolean() {}

    public VSBoolean(boolean value) {
        this.value = value;
    }

    public String toString() {
        return "" + value;
    }

    public void setValue(boolean value) {
        // if (this.value!=value)
        {
            this.value = value;
            setChanged(true);
        }
    }

    public boolean getValue() {
        return value;
    }

    public void copyValueFrom(Object in) {
        if (in != null) {
            VSBoolean temp = (VSBoolean) in;
            value = temp.value;
            setChanged(temp.isChanged());
        } else
            value = false;
    }

    public void copyReferenceFrom(Object in) {
        copyValueFrom(in);
    }

    public boolean equals(VSObject obj) {
        VSBoolean temp = (VSBoolean) obj;
        return temp.value == value;
    }

    public boolean isBigger(VSObject obj) {
        VSBoolean temp = (VSBoolean) obj;
        return value == true && temp.value == false;
    }

    public boolean isSmaller(VSObject obj) {
        VSBoolean temp = (VSBoolean) obj;
        return value == false && temp.value == true;
    }

    public void loadFromStream(java.io.FileInputStream fis) {
        try (java.io.DataInputStream dis = new java.io.DataInputStream(fis)) {
            value = dis.readBoolean();
        } catch (IOException ex) {
            Logger.error(ex, "Erros. Tried to read boolean");
        }
    }

    public void saveToStream(java.io.FileOutputStream fos) {
        try (java.io.DataOutputStream dos = new java.io.DataOutputStream(fos)) {

            dos.writeBoolean(value);
        } catch (IOException ex) {
            Logger.error(ex, "Error. Tried to read boolean.");
        }
    }

    public void loadFromXML(String name, org.w3c.dom.Element nodeElement) {
        value = Boolean.parseBoolean(nodeElement.getAttribute("VSBoolean" + name));
    }

    public void saveToXML(String name, org.w3c.dom.Element nodeElement) {
        nodeElement.setAttribute("VSBoolean" + name, "" + value);
    }
}
