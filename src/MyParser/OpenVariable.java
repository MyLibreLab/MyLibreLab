/*
MyOpenLab by Carmelo Salafia www.myopenlab.de
Copyright (C) 2004  Carmelo Salafia cswi@gmx.de

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package MyParser;

import VisualLogic.variables.VS1DBoolean;
import VisualLogic.variables.VS1DDouble;
import VisualLogic.variables.VS1DInteger;
import VisualLogic.variables.VS1DString;
import VisualLogic.variables.VS2DBoolean;
import VisualLogic.variables.VS2DDouble;
import VisualLogic.variables.VS2DInteger;
import VisualLogic.variables.VS2DString;
import VisualLogic.variables.VSBoolean;
import VisualLogic.variables.VSDouble;
import VisualLogic.variables.VSInteger;

public class OpenVariable {

    public String name;
    public int datatype;

    public int size1;
    public int size2;

    public Object value;
    public boolean global;

    public static final int C_DOUBLE = 0;
    public static final int C_STRING = 1;
    public static final int C_BOOLEAN = 2;
    public static final int C_INTEGER = 3;
    public static final int C_DOUBLE_1D = 4;
    public static final int C_STRING_1D = 5;
    public static final int C_BOOLEAN_1D = 6;
    public static final int C_INTEGER_1D = 7;
    public static final int C_DOUBLE_2D = 8;
    public static final int C_STRING_2D = 9;
    public static final int C_BOOLEAN_2D = 10;
    public static final int C_INTEGER_2D = 11;

    public String dtToString(int datatype) {
        String dt = "";

        switch (datatype) {
            case C_DOUBLE:
                dt = "Double";
                break;
            case C_STRING:
                dt = "String";
                break;
            case C_BOOLEAN:
                dt = "Boolean";
                break;
            case C_INTEGER:
                dt = "Integer";
                break;
            case C_DOUBLE_1D:
                dt = "1D_Double";
                break;
            case C_STRING_1D:
                dt = "1D_String";
                break;
            case C_BOOLEAN_1D:
                dt = "1D_Boolean";
                break;
            case C_INTEGER_1D:
                dt = "1D_Integer";
                break;
            case C_DOUBLE_2D:
                dt = "2D_Double";
                break;
            case C_STRING_2D:
                dt = "2D_String";
                break;
            case C_BOOLEAN_2D:
                dt = "2D_Boolean";
                break;
            case C_INTEGER_2D:
                dt = "2D_Integer";
                break;

        }

        return dt;
    }

    @Override
    public String toString() {
        String dt = dtToString(datatype);

        if (dt.indexOf("1D_") > -1) {
            return name + " (" + dt + "[" + size1 + "])";
        } else if (dt.indexOf("2D_") > -1) {
            return name + " (" + dt + "[" + size1 + "][" + size2 + "])";
        } else {
            return name + " (" + dt + ")";
        }
    }

    public void createVariableByDt() {
        
        switch (datatype) {
            case C_DOUBLE:
                
                value = new Double(0);
                break;
            case C_STRING:
                value = "";
                break;
            case C_BOOLEAN:
                 value = new Boolean(false);
                break;
            case C_INTEGER:
                value = new Integer(0);
                break;
            case C_DOUBLE_1D:
               value = new VS1DDouble(size1);
                break;
            case C_STRING_1D:
                value = new VS1DString(size1);
                break;
            case C_BOOLEAN_1D:
                value = new VS1DBoolean(size1);
                break;
            case C_INTEGER_1D:
               value = new VS1DInteger(size1);
                break;
            case C_DOUBLE_2D:
               value = new VS2DDouble(size1,size2);
                break;
            case C_STRING_2D:
              value = new VS2DString(size1,size2);
                break;
            case C_BOOLEAN_2D:
               value = new VS2DBoolean(size1,size2);
                break;
            case C_INTEGER_2D:
                value = new VS2DInteger(size1,size2);
                break;

        }
    }

    public OpenVariable() {

    }

    public OpenVariable(String name, Object value) {
        this.name = name;
        this.value = value;
    }
}
