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


package VisualLogic;

/**
 *
 * @author Homer
 */

import java.awt.*;
import VisualLogic.variables.*;


public class VSDataType
{    
    
    private static float dash1[] = {2.0f,1.0f};    
    private static BasicStroke dashed1D = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 2.0f, dash1, 1.0f);            
    private static float dash2[] = {2.0f,1.0f};
    private static BasicStroke dashed2D = new BasicStroke(3.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1.0f, dash2, 1.0f);        
    private static BasicStroke dashedGrp = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 2.0f, dash2, 1.0f);        
    private static Stroke standard = new BasicStroke( 2 );        
    private static Stroke lineStyle=standard;
        
    public static VSObject createPinDataType(int dataType)
    {
        switch (dataType)
        {                      
          case ExternalIF.C_GROUP            : return new VSGroup();
          case ExternalIF.C_BOOLEAN          : return new VSBoolean();
          case ExternalIF.C_INTEGER          : return new VSInteger();
          case ExternalIF.C_DOUBLE           : return new VSDouble();
          case ExternalIF.C_STRING           : return new VSString();
          case ExternalIF.C_BYTE             : return new VSByte();          
          case ExternalIF.C_COLOR            : return new VSColor(Color.BLACK);
          case ExternalIF.C_ARRAY1D_BOOLEAN  : return new VS1DBoolean(1);
          case ExternalIF.C_ARRAY1D_INTEGER  : return new VS1DInteger(1);
          case ExternalIF.C_ARRAY1D_DOUBLE   : return new VS1DDouble(1);
          case ExternalIF.C_ARRAY1D_STRING   : return new VS1DString(1);
          case ExternalIF.C_ARRAY2D_BYTE     : return new VS2DByte(1,1);
          case ExternalIF.C_ARRAY2D_BOOLEAN  : return new VS2DBoolean(1,1);
          case ExternalIF.C_ARRAY2D_INTEGER  : return new VS2DInteger(1,1);
          case ExternalIF.C_ARRAY2D_DOUBLE   : return new VS2DDouble(1,1);
          case ExternalIF.C_ARRAY2D_STRING   : return new VS2DString(1,1);
          case ExternalIF.C_IMAGE            : return new VSImage24(1,1);
          case ExternalIF.C_ARRAY1D_BYTE     : return  new VS1DByte(1);
          case ExternalIF.C_CANVAS           : return  new VSCanvas();
          case ExternalIF.C_FONT             : return  new VSFont(new Font("Arial",Font.PLAIN,10));
          case ExternalIF.C_FLOWINFO         : return  new VSFlowInfo();
          case ExternalIF.C_OBJECT           : return  new VSObjRef();
          
          default: return null;
        }
    }
    
    
    public static String getPinDataType(int dataType)
    {
        switch (dataType)
        {                      
          case ExternalIF.C_VARIANT          : return "VSObject";
          case ExternalIF.C_GROUP            : return "VSGroup";
          case ExternalIF.C_BOOLEAN          : return "VSBoolean";
          case ExternalIF.C_INTEGER          : return "VSInteger";
          case ExternalIF.C_DOUBLE           : return "VSDouble";
          case ExternalIF.C_STRING           : return "VSString";
          case ExternalIF.C_BYTE             : return "VSByte";
          case ExternalIF.C_COLOR            : return "VSColor";
          case ExternalIF.C_ARRAY1D_BOOLEAN  : return "VS1DBoolean";
          case ExternalIF.C_ARRAY1D_INTEGER  : return "VS1DInteger";
          case ExternalIF.C_ARRAY1D_DOUBLE   : return "VS1DDouble";
          case ExternalIF.C_ARRAY1D_STRING   : return "VS1DString";
          case ExternalIF.C_ARRAY2D_BYTE     : return "VS2DByte";
          case ExternalIF.C_ARRAY2D_BOOLEAN  : return "VS2DBoolean";
          case ExternalIF.C_ARRAY2D_INTEGER  : return "VS2DInteger";
          case ExternalIF.C_ARRAY2D_DOUBLE   : return "VS2DDouble";
          case ExternalIF.C_ARRAY2D_STRING   : return "VS2DString";
          case ExternalIF.C_IMAGE            : return "VSImage24";
          case ExternalIF.C_ARRAY1D_BYTE     : return "VS1DByte";
          case ExternalIF.C_CANVAS           : return "VSCanvas";
          case ExternalIF.C_FONT             : return "VSFont";
          case ExternalIF.C_FLOWINFO         : return "VSFlowInfo";
          case ExternalIF.C_OBJECT           : return "VSObjReference";
          
          default: return "";
        }
    }    
    
    
    public static String getPinDataTypeWithDefaultType(int dataType)
    {
        switch (dataType)
        {                      
          case ExternalIF.C_VARIANT          : return "VSObject()";
          case ExternalIF.C_GROUP            : return "VSGroup()";
          case ExternalIF.C_BOOLEAN          : return "VSBoolean(false)";
          case ExternalIF.C_INTEGER          : return "VSInteger(0)";
          case ExternalIF.C_DOUBLE           : return "VSDouble(0.0)";
          case ExternalIF.C_STRING           : return "VSString()";
          case ExternalIF.C_BYTE             : return "VSByte()";
          case ExternalIF.C_COLOR            : return "VSColor(Color.BLACK)";
          case ExternalIF.C_ARRAY1D_BOOLEAN  : return "VS1DBoolean(1)";
          case ExternalIF.C_ARRAY1D_INTEGER  : return "VS1DInteger(1)";
          case ExternalIF.C_ARRAY1D_DOUBLE   : return "VS1DDouble(1)";
          case ExternalIF.C_ARRAY1D_STRING   : return "VS1DString(1)";
          case ExternalIF.C_ARRAY2D_BYTE     : return "VS2DByte(1,1)";
          case ExternalIF.C_ARRAY2D_BOOLEAN  : return "VS2DBoolean(1,1)";
          case ExternalIF.C_ARRAY2D_INTEGER  : return "VS2DInteger(1,1)";
          case ExternalIF.C_ARRAY2D_DOUBLE   : return "VS2DDouble(1,1)";
          case ExternalIF.C_ARRAY2D_STRING   : return "VS2DString(1,1)";
          case ExternalIF.C_IMAGE            : return "VSImage24(1,1)";
          case ExternalIF.C_ARRAY1D_BYTE     : return "VS1DByte(1)";
          case ExternalIF.C_CANVAS           : return "VSCanvas()";
          case ExternalIF.C_FONT             : return "VSFont(new Font(\"Arial\",Font.PLAIN,10))";
          case ExternalIF.C_FLOWINFO         : return "VSFlowInfo()";
          case ExternalIF.C_OBJECT           : return "VSObjRef()";
          
          default: return "";
        }
    }    
    
    public static String[] getDataTypeList()
    {
        String[] list= new String[23];
        
        list[0]="C_VARIANT";
        list[1]="C_GROUP";               
        list[2]="C_BOOLEAN";
        list[3]="C_INTEGER";
        list[4]="C_DOUBLE";
        list[5]="C_STRING";
        list[6]="C_BYTE";                 
        list[7]="C_ARRAY1D_BOOLEAN";
        list[8]="C_ARRAY1D_INTEGER";
        list[9]="C_ARRAY1D_DOUBLE";
        list[10]="C_ARRAY1D_STRING";
        list[11]="C_ARRAY2D_BYTE";
        list[12]="C_ARRAY2D_BOOLEAN";
        list[13]="C_ARRAY2D_INTEGER";
        list[14]="C_ARRAY2D_DOUBLE";
        list[15]="C_ARRAY2D_STRING";
        list[16]="C_COLOR";
        list[17]="C_IMAGE";
        list[18]="C_ARRAY1D_BYTE";
        list[19]="C_CANVAS";
        list[20]="C_FONT";
        list[21]="C_FLOWINFO";
        list[22]="C_OBJECT";
                
        return list;        
    }
    
    public static String getDataTypeShortCut(int dataType)
    {
        switch (dataType)
        {                      
          case ExternalIF.C_GROUP            : return "grp";
          case ExternalIF.C_BOOLEAN          : return "bol";
          case ExternalIF.C_INTEGER          : return "int";
          case ExternalIF.C_DOUBLE           : return "dbl";
          case ExternalIF.C_STRING           : return "str";
          case ExternalIF.C_BYTE             : return "byte";
          
          case ExternalIF.C_COLOR            : return "col";

          case ExternalIF.C_ARRAY1D_BOOLEAN  : return "1D.bol";
          case ExternalIF.C_ARRAY1D_INTEGER  : return "1D.int";
          case ExternalIF.C_ARRAY1D_DOUBLE   : return "1D.dbl";
          case ExternalIF.C_ARRAY1D_STRING   : return "1D.str";

          case ExternalIF.C_ARRAY2D_BYTE     : return "2D.byte";
          case ExternalIF.C_ARRAY2D_BOOLEAN  : return "2D.bol";
          case ExternalIF.C_ARRAY2D_INTEGER  : return "2D.int";
          case ExternalIF.C_ARRAY2D_DOUBLE   : return "2D.dbl";
          case ExternalIF.C_ARRAY2D_STRING   : return "2D.str";
          case ExternalIF.C_IMAGE            : return "img";
          case ExternalIF.C_ARRAY1D_BYTE     : return "1D.byte";
          case ExternalIF.C_CANVAS           : return "canvas";
          case ExternalIF.C_FONT             : return "font";
          case ExternalIF.C_FLOWINFO         : return "Flow";
          case ExternalIF.C_OBJECT           : return "Obj.Ref";
          
          default: return "var";
        }
    }
    

    public static int getDataType(VSObject obj)
    {
        if (obj instanceof VSGroup) return ExternalIF.C_GROUP; else
        if (obj instanceof VSBoolean) return ExternalIF.C_BOOLEAN;else
        if (obj instanceof VSInteger) return ExternalIF.C_INTEGER;else
        if (obj instanceof VSDouble) return ExternalIF.C_DOUBLE;else
        if (obj instanceof VSString) return ExternalIF.C_STRING;else
        if (obj instanceof VSByte) return ExternalIF.C_BYTE;else
        
        if (obj instanceof VS1DBoolean) return ExternalIF.C_ARRAY1D_BOOLEAN;else
        if (obj instanceof VS1DInteger) return ExternalIF.C_ARRAY1D_INTEGER;else
        if (obj instanceof VS1DDouble) return ExternalIF.C_ARRAY1D_DOUBLE;else
        if (obj instanceof VS1DString) return ExternalIF.C_ARRAY1D_STRING;else
        
        if (obj instanceof VS2DByte) return ExternalIF.C_ARRAY2D_BYTE;else
        if (obj instanceof VS2DBoolean) return ExternalIF.C_ARRAY2D_BOOLEAN;else
        if (obj instanceof VS2DInteger) return ExternalIF.C_ARRAY2D_INTEGER;else
        if (obj instanceof VS2DDouble) return ExternalIF.C_ARRAY2D_DOUBLE;else
        if (obj instanceof VS2DString) return ExternalIF.C_ARRAY2D_STRING;else
        if (obj instanceof VSColor) return ExternalIF.C_COLOR;else
        if (obj instanceof VSImage24) return ExternalIF.C_IMAGE;else 
        if (obj instanceof VS1DByte) return ExternalIF.C_ARRAY1D_BYTE;else 
        if (obj instanceof VSCanvas) return ExternalIF.C_CANVAS;else 
        if (obj instanceof VSFont) return ExternalIF.C_FONT;else 
        if (obj instanceof VSObjRef) return ExternalIF.C_OBJECT;else 
        if (obj instanceof VSFlowInfo) return ExternalIF.C_FLOWINFO;else return -1;
        
    }    
    
    public static void setColorStrokeFromDataType(java.awt.Graphics2D g, int dataType)
    {
        Color color=Color.black;

        Color orange = new Color(248,197,140);
        Color pur = new Color(192,0,192);
        Color strColor = new Color(245,101,234);
        Color ObjColor = new Color(51,254,204);
        
        switch (dataType)
        {
          case ExternalIF.C_VARIANT          : color=Color.BLACK;           lineStyle=standard; break;
          case ExternalIF.C_GROUP            : color=new Color(0,150,150);  lineStyle=dashedGrp; break;
          
          case ExternalIF.C_BOOLEAN          : color=Color.GREEN;           lineStyle=standard; break;
          case ExternalIF.C_INTEGER          : color=Color.BLUE;            lineStyle=standard; break;
          case ExternalIF.C_DOUBLE           : color=orange;                lineStyle=standard; break;
          case ExternalIF.C_STRING           : color=strColor;              lineStyle=standard; break;          
          case ExternalIF.C_BYTE             : color=new Color(150,120,10); lineStyle=standard; break;
          
          case ExternalIF.C_ARRAY1D_BOOLEAN  : color=Color.GREEN;           lineStyle=dashed1D; break;
          case ExternalIF.C_ARRAY1D_INTEGER  : color=Color.BLUE;            lineStyle=dashed1D; break;
          case ExternalIF.C_ARRAY1D_DOUBLE   : color=orange;                lineStyle=dashed1D; break;
          case ExternalIF.C_ARRAY1D_STRING   : color=strColor;              lineStyle=dashed1D; break; 
          case ExternalIF.C_ARRAY1D_BYTE     : color=new Color(150,120,10); lineStyle=dashed1D; break;
          
          case ExternalIF.C_ARRAY2D_BYTE     : color=new Color(150,120,10); lineStyle=dashed2D; break;          
          case ExternalIF.C_ARRAY2D_BOOLEAN  : color=Color.GREEN;           lineStyle=dashed2D; break;
          case ExternalIF.C_ARRAY2D_INTEGER  : color=Color.BLUE;            lineStyle=dashed2D; break;
          case ExternalIF.C_ARRAY2D_DOUBLE   : color=orange;                lineStyle=dashed2D; break;
          case ExternalIF.C_ARRAY2D_STRING   : color=strColor;              lineStyle=dashed2D; break;
          
          case ExternalIF.C_COLOR            : color=new Color(12,117,109); lineStyle=standard; break;
          case ExternalIF.C_IMAGE            : color=new Color(252,58,131); lineStyle=standard; break;          
          case ExternalIF.C_CANVAS           : color=new Color(52,158,31);  lineStyle=dashed1D; break;
          case ExternalIF.C_FONT             : color=new Color(128,128,128); lineStyle=standard; break;
          case ExternalIF.C_FLOWINFO         : color=new Color(28,228,28);  lineStyle=dashed1D; break;
          case ExternalIF.C_OBJECT           : color=ObjColor;              lineStyle=standard; break;
        }

        g.setColor(color);        
        g.setStroke(lineStyle);       
        
   } 
    
    
    
    
    public static Color getCoorFromDataType(int dataType)
    {
        Color color=Color.black;

        Color orange = new Color(248,197,140);
        Color pur = new Color(192,0,192);
        Color strColor = new Color(245,101,234);
        Color ObjColor = new Color(51,254,204);
        
        switch (dataType)
        {
          case ExternalIF.C_VARIANT          : color=Color.BLACK;          break;
          case ExternalIF.C_GROUP            : color=new Color(0,150,150); break;
          
          case ExternalIF.C_BOOLEAN          : color=Color.GREEN;          break;
          case ExternalIF.C_INTEGER          : color=Color.BLUE;           break;
          case ExternalIF.C_DOUBLE           : color=orange;               break;
          case ExternalIF.C_STRING           : color=strColor;             break;          
          case ExternalIF.C_BYTE             : color=new Color(150,120,10);break;
          
          case ExternalIF.C_ARRAY1D_BOOLEAN  : color=Color.GREEN;          break;
          case ExternalIF.C_ARRAY1D_INTEGER  : color=Color.BLUE;           break;
          case ExternalIF.C_ARRAY1D_DOUBLE   : color=orange;               break;
          case ExternalIF.C_ARRAY1D_STRING   : color=strColor;             break;          
          case ExternalIF.C_ARRAY1D_BYTE     : color=new Color(150,120,10);break;
          
          case ExternalIF.C_ARRAY2D_BYTE     : color=new Color(150,120,10);break;                    
          case ExternalIF.C_ARRAY2D_BOOLEAN  : color=Color.GREEN;          break;
          case ExternalIF.C_ARRAY2D_INTEGER  : color=Color.BLUE;           break;
          case ExternalIF.C_ARRAY2D_DOUBLE   : color=orange;               break;
          case ExternalIF.C_ARRAY2D_STRING   : color=strColor;             break;
          
          case ExternalIF.C_COLOR            : color=new Color(12,117,109);break;
          case ExternalIF.C_IMAGE            : color=new Color(252,58,131);break;          
          case ExternalIF.C_CANVAS           : color=new Color(52,158,31); break;
          case ExternalIF.C_FONT             : color=new Color(128,128,128); break;  
          case ExternalIF.C_FLOWINFO         : color=new Color(28,228,28); break;  
          case ExternalIF.C_OBJECT           : color=ObjColor; break;  
        }

        return color;        
   }     
        
}
