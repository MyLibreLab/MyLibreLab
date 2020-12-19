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

import org.tinylog.Logger;

/**
 * @author velas
 */
public class VSObjRef extends VSObject {

    private Object Obj;

    @Override
    public String toString() {
        return Obj.toString();
    }

    public void setValue(Object ObjIn) {
        {
            this.Obj = ObjIn;
            setChanged(true);
        }
    }

    public Object getValue() {
        return this.Obj;
    }

    public VSObjRef(Object ObjInput) {
        this.Obj = ObjInput;
    }

    public VSObjRef() {
        this.Obj = new Object();
    }

    @Override
    public void copyValueFrom(Object in) {
        if (in != null) {
            this.Obj = in;
            setChanged(true);
        }
    }

    @Override
    public void copyReferenceFrom(Object in) {
        copyValueFrom(in);
    }

    @Override
    public boolean equals(VSObject objIn) {
        return this.Obj.equals(objIn);
    }

    @Override
    public boolean isBigger(VSObject obj) {
        return false;
    }

    @Override
    public boolean isSmaller(VSObject obj) {
        return false;
    }

    @Override
    public void loadFromStream(java.io.FileInputStream fis) {
        try (java.io.DataInputStream dis = new java.io.DataInputStream(fis)) {


            this.Obj = dis.readUTF();
        } catch (IOException e) {
            Logger.error(e, "Error. Tried to read objref");
        }
    }

    @Override
    public void saveToStream(java.io.FileOutputStream fos) {
        try (java.io.DataOutputStream dos = new java.io.DataOutputStream(fos)) {

            dos.writeUTF(this.Obj.toString());
        } catch (IOException e) {
            Logger.error(e, "Error. Tried to save ObjRef");
        }
    }

    public void loadFromXML(String name, org.w3c.dom.Element nodeElement) {
        this.Obj = nodeElement.getAttribute("VSString" + name);
    }

    public void saveToXML(String name, org.w3c.dom.Element nodeElement) {
        nodeElement.setAttribute("VSString" + name, this.Obj.toString());
    }
}
