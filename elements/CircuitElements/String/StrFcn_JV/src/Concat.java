//*****************************************************************************
//* Element of MyOpenLab Library                                              *
//*                                                                           *
//* Copyright (C) 2004  Carmelo Salafia (cswi@gmx.de)                         *
//* Copyright (C) 2018  Javier Velásquez (javiervelasquez125@gnail.com)       *
//* This library is free software; you can redistribute it and/or modify      *
//* it under the terms of the GNU Lesser General Public License as published  *
//* by the Free Software Foundation; either version 2.1 of the License,       *
//* or (at your option) any later version.                                    *
//* http://www.gnu.org/licenses/lgpl.html                                     *
//*                                                                           *
//* This library is distributed in the hope that it will be useful,           *
//* but WITHOUTANY WARRANTY; without even the implied warranty of             *
//* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.                      *
//* See the GNU Lesser General Public License for more details.               *
//*                                                                           *
//* You should have received a copy of the GNU Lesser General Public License  *
//* along with this library; if not, write to the Free Software Foundation,   *
//* Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA                  *
//*****************************************************************************


import VisualLogic.*;
import VisualLogic.variables.*;
import tools.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;


public class Concat extends JVSMain
{
  private Image image;
  private VSString inAStr;
  private VSString inBStr;
  private VSString inCStr;
  private VSString inDStr;
  private VSInteger inAInt;
  private VSInteger inBInt;
  private VSInteger concatenateInputs = new VSInteger(2);
  private VSInteger selectedFcn = new VSInteger(0);

  
  private VSString strOut=new VSString();
  private VSInteger intOut=new VSInteger();
  private VSBoolean boolOut=new VSBoolean();
  private VS1DByte bytesOut=new VS1DByte(0);
  private VS1DString strArrayOut=new VS1DString(0);
  private String oldValue="";
  private VSComboBox Strfunction= new VSComboBox();

  public void onDispose()
  {
    if (image!=null)
    {
      image.flush();
      image=null;
    }
  }
  
  public void paint(java.awt.Graphics g)
  {
    //drawImageCentred(g,image);
    Graphics2D g2 = (Graphics2D) g;  
    Rectangle r = element.jGetBounds();
    String tempStr=Strfunction.getItem(Strfunction.selectedIndex);
    String tempStr2=tempStr.substring(0,tempStr.indexOf("("));
    if(g2!=null && element!=null){  
    g2.setFont(new Font("Dialog",1,12));
    FontMetrics fm = g2.getFontMetrics();
    Rectangle2D rFont = fm.getStringBounds(tempStr2, g2);
    setSize((int)rFont.getWidth()+30,(int) rFont.getHeight()+25);
    
    g2.setColor(new Color(255,242,181));
    g2.fillRoundRect(10, 1,element.jGetWidth()-20, element.jGetHeight()-2, 5, 5);
    g2.setColor(Color.BLACK);
     
    g2.drawString(tempStr2,(int) ((r.x+(r.width/2))-(rFont.getWidth()/2)), (int) ((r.y+(r.height/2))+(rFont.getHeight()/4)));
    
    }
  }

  public void init()
  {
    //image=element.jLoadImage(element.jGetSourcePath()+"circuitIcon.gif");

    setName("String Function JV");
    
//    initPins(0,5,0,5);
//setSize(50,50);
    initPinVisibility(false,true,false,true);
//    setPin(0,ExternalIF.C_VARIANT,element.PIN_OUTPUT);
//    setPin(1,ExternalIF.C_VARIANT,element.PIN_OUTPUT);
//    setPin(2,ExternalIF.C_VARIANT,element.PIN_OUTPUT);
//    setPin(3,ExternalIF.C_VARIANT,element.PIN_OUTPUT);
//    setPin(4,ExternalIF.C_VARIANT,element.PIN_OUTPUT);
//    setPin(0,ExternalIF.C_VARIANT,element.PIN_INPUT);
//    setPin(1,ExternalIF.C_VARIANT,element.PIN_INPUT);
//    setPin(2,ExternalIF.C_VARIANT,element.PIN_INPUT);
//    setPin(3,ExternalIF.C_VARIANT,element.PIN_INPUT);
//    setPin(4,ExternalIF.C_VARIANT,element.PIN_INPUT);
    
    Strfunction.addItem("charAt(index)");
    Strfunction.addItem("concat(str1,str2)");
    Strfunction.addItem("contains(string)");
    Strfunction.addItem("endsWith(suffix)");
    Strfunction.addItem("equals(string)");
    Strfunction.addItem("equalsIgnoreCase(Str)");
    Strfunction.addItem("getBytes()");
    Strfunction.addItem("indexOf(char)");
    Strfunction.addItem("indexOf(char,fIndex)");
    Strfunction.addItem("indexOf(str)");
    Strfunction.addItem("indexOf(str,fIndex)");
    Strfunction.addItem("isEmpty()");
    Strfunction.addItem("lastIndexOf(char)");
    Strfunction.addItem("lastIndexOf(char,fIndex)");
    Strfunction.addItem("lastIndexOf(Str)");
    Strfunction.addItem("lastIndexOf(Str,fIndex)");
    Strfunction.addItem("length()");
    Strfunction.addItem("matches(regex)");
    Strfunction.addItem("replace(oldCh,newCh)");
    Strfunction.addItem("replace(target,str)");
    Strfunction.addItem("replaceAll(regex,str)");
    Strfunction.addItem("replaceFirst(regex,str)");
    Strfunction.addItem("split(regex)");
    Strfunction.addItem("split(regex, int limit)");
    Strfunction.addItem("startsWith(prefix)");
    Strfunction.addItem("startsWith(prefix,toffset)");
    Strfunction.addItem("substring(bIndex)");
    Strfunction.addItem("substring(bIndex,eIndex)");
    Strfunction.addItem("toLowerCase()");
    Strfunction.addItem("toUpperCase()");
    Strfunction.addItem("trim()");
    Strfunction.addItem("trim_ln()");
    Strfunction.addItem("trim_All()");
    Strfunction.selectedIndex=selectedFcn.getValue();
    myInit();
    oldValue="-1";

  }
  
public void start(){
    super.start();
    oldValue="-1";
}  
  
  
  public void myInit(){
      
      switch(Strfunction.selectedIndex){ 
        case 0:
        initPins(0,1,0,2);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_STRING,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(2,ExternalIF.C_INTEGER,element.PIN_INPUT);
        element.jSetPinDescription(0,"Char At Index");
        element.jSetPinDescription(1,"String In");
        element.jSetPinDescription(2,"Index In");
        break;
        
        case 1:
        if(concatenateInputs.getValue()<=2){    
        initPins(0,1,0,2);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_STRING,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(2,ExternalIF.C_STRING,element.PIN_INPUT);

        element.jSetPinDescription(0,"=\"AB\"");
        element.jSetPinDescription(1,"\"A\"");
        element.jSetPinDescription(2,"\"B\""); 
        }
        if(concatenateInputs.getValue()==3){    
        initPins(0,1,0,3);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_STRING,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(2,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(3,ExternalIF.C_STRING,element.PIN_INPUT);

        element.jSetPinDescription(0,"=\"AB\"");
        element.jSetPinDescription(1,"\"A\"");
        element.jSetPinDescription(2,"\"B\""); 
        element.jSetPinDescription(3,"\"C\""); 
        }
        if(concatenateInputs.getValue()==4){    
        initPins(0,1,0,4);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_STRING,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(2,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(3,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(4,ExternalIF.C_STRING,element.PIN_INPUT);

        element.jSetPinDescription(0,"=\"AB\"");
        element.jSetPinDescription(1,"\"A\"");
        element.jSetPinDescription(2,"\"B\""); 
        element.jSetPinDescription(3,"\"C\""); 
        element.jSetPinDescription(4,"\"D\""); 
        }
        break;    
        case 2:  // Contains
        initPins(0,1,0,2);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(2,ExternalIF.C_STRING,element.PIN_INPUT);

        element.jSetPinDescription(0,"Contain?");
        element.jSetPinDescription(1,"String In");
        element.jSetPinDescription(2,"Char Sequence");    
        break;    
        case 3: // 
        initPins(0,1,0,2);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(2,ExternalIF.C_STRING,element.PIN_INPUT);

        element.jSetPinDescription(0,"Ends With?");
        element.jSetPinDescription(1,"String In");
        element.jSetPinDescription(2,"Suffix");    
        break;    
        case 4: // 
        initPins(0,1,0,2);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(2,ExternalIF.C_STRING,element.PIN_INPUT);

        element.jSetPinDescription(0,"Equals?");
        element.jSetPinDescription(1,"String 1 In");
        element.jSetPinDescription(2,"String 2 In");    
        break;    
        case 5: // 
        initPins(0,1,0,2);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(2,ExternalIF.C_STRING,element.PIN_INPUT);

        element.jSetPinDescription(0,"EqualsIgnoreCase?");
        element.jSetPinDescription(1,"String 1 In");
        element.jSetPinDescription(2,"String 2 In");    
        break;    
        case 6: // 
        initPins(0,1,0,1);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_ARRAY1D_BYTE,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        
        element.jSetPinDescription(0,"Byte Array Out");
        element.jSetPinDescription(1,"String In");
        break;    
        case 7: // Index Of (int Char)
        initPins(0,1,0,2);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_INTEGER,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(2,ExternalIF.C_INTEGER,element.PIN_INPUT);
        element.jSetPinDescription(0,"Index Out");
        element.jSetPinDescription(1,"String In");
        element.jSetPinDescription(2,"Char In");
        break;    
        case 8: // Index Of (int Char, int fromIndex)
        initPins(0,1,0,3);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_INTEGER,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(2,ExternalIF.C_INTEGER,element.PIN_INPUT);
        setPin(3,ExternalIF.C_INTEGER,element.PIN_INPUT);
        element.jSetPinDescription(0,"Index Out");
        element.jSetPinDescription(1,"String In");
        element.jSetPinDescription(2,"Char In");
        element.jSetPinDescription(3,"fromIndex In");
        break;    
        case 9: // Index Of (String in)
        initPins(0,1,0,2);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_INTEGER,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(2,ExternalIF.C_STRING,element.PIN_INPUT);
        element.jSetPinDescription(0,"Index Out");
        element.jSetPinDescription(1,"String 1 In");
        element.jSetPinDescription(2,"String 2 In");
        break;    
        case 10: // Index Of (int Char, int fromIndex)
        initPins(0,1,0,3);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_INTEGER,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(2,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(3,ExternalIF.C_INTEGER,element.PIN_INPUT);
        element.jSetPinDescription(0,"Index Out");
        element.jSetPinDescription(1,"String 1 In");
        element.jSetPinDescription(2,"String 2 In");
        element.jSetPinDescription(3,"fromIndex In");
        break;  
        
        case 11: // 
        initPins(0,1,0,1);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        element.jSetPinDescription(0,"IsEmpty?");
        element.jSetPinDescription(1,"String In");
        break;
        
        case 12: // LastIndex Of (int Char)
        initPins(0,1,0,2);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_INTEGER,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(2,ExternalIF.C_INTEGER,element.PIN_INPUT);
        element.jSetPinDescription(0,"LastIndex Out");
        element.jSetPinDescription(1,"String In");
        element.jSetPinDescription(2,"Char In");
        break;    
        case 13: // LastIndex Of (int Char, int fromIndex)
        initPins(0,1,0,3);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_INTEGER,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(2,ExternalIF.C_INTEGER,element.PIN_INPUT);
        setPin(3,ExternalIF.C_INTEGER,element.PIN_INPUT);
        element.jSetPinDescription(0,"LastIndex Out");
        element.jSetPinDescription(1,"String In");
        element.jSetPinDescription(2,"Char In");
        element.jSetPinDescription(3,"fromIndex In");
        break;    
        case 14: // LastIndex Of (String in)
        initPins(0,1,0,2);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_INTEGER,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(2,ExternalIF.C_STRING,element.PIN_INPUT);
        element.jSetPinDescription(0,"LastIndex Out");
        element.jSetPinDescription(1,"String 1 In");
        element.jSetPinDescription(2,"String 2 In");
        break;    
        case 15: // LastIndex Of (int Char, int fromIndex)
        initPins(0,1,0,3);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_INTEGER,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(2,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(3,ExternalIF.C_INTEGER,element.PIN_INPUT);
        element.jSetPinDescription(0,"LastIndex Out");
        element.jSetPinDescription(1,"String 1 In");
        element.jSetPinDescription(2,"String 2 In");
        element.jSetPinDescription(3,"fromIndex In");
        break; 
        case 16: // Length()
        initPins(0,1,0,1);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_INTEGER,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        element.jSetPinDescription(0,"Length Out");
        element.jSetPinDescription(1,"String In");
        break; 
        case 17: // matches(String regex)
        initPins(0,1,0,2);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(2,ExternalIF.C_STRING,element.PIN_INPUT);
        element.jSetPinDescription(0,"Match?");
        element.jSetPinDescription(1,"String 1 In");
        element.jSetPinDescription(2,"String Match In");
        break; 
        case 18: // replace(char oldChar, char newChar)
        initPins(0,1,0,3);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_STRING,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(2,ExternalIF.C_INTEGER,element.PIN_INPUT);
        setPin(3,ExternalIF.C_INTEGER,element.PIN_INPUT);
        element.jSetPinDescription(0,"Replaced String");
        element.jSetPinDescription(1,"String In");
        element.jSetPinDescription(2,"Old Char In");
        element.jSetPinDescription(3,"New Char In");
        break; 
        case 19: // replace(CharSequence target, CharSequence replacement)
        initPins(0,1,0,3);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_STRING,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(2,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(3,ExternalIF.C_STRING,element.PIN_INPUT);
        element.jSetPinDescription(0,"Replaced String");
        element.jSetPinDescription(1,"String In");
        element.jSetPinDescription(2,"Old String In");
        element.jSetPinDescription(3,"New String In");
        break; 
        case 20: // replaceAll(CharSequence target, CharSequence replacement)
        initPins(0,1,0,3);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_STRING,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(2,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(3,ExternalIF.C_STRING,element.PIN_INPUT);
        element.jSetPinDescription(0,"Replaced String");
        element.jSetPinDescription(1,"String In");
        element.jSetPinDescription(2,"Old String In");
        element.jSetPinDescription(3,"New String In");
        break; 
        case 21: // replaceFirst(String regex, String replacement)
        initPins(0,1,0,3);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_STRING,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(2,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(3,ExternalIF.C_STRING,element.PIN_INPUT);
        element.jSetPinDescription(0,"Replaced String");
        element.jSetPinDescription(1,"String In");
        element.jSetPinDescription(2,"Old String In");
        element.jSetPinDescription(3,"New String In");
        break; 
        case 22: // split(String regex)
        initPins(0,1,0,2);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_ARRAY1D_STRING,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(2,ExternalIF.C_STRING,element.PIN_INPUT);
        element.jSetPinDescription(0,"1D Array String");
        element.jSetPinDescription(1,"String In");
        element.jSetPinDescription(2,"String regex");
        break; 
        case 23: // split(String regex, int limit)
        initPins(0,1,0,3);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_ARRAY1D_STRING,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(2,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(3,ExternalIF.C_INTEGER,element.PIN_INPUT);
        element.jSetPinDescription(0,"1D Array String");
        element.jSetPinDescription(1,"String In");
        element.jSetPinDescription(2,"String regex");
        element.jSetPinDescription(3,"Limit");
        break; 
        case 24: //startsWith(String prefix)
        initPins(0,1,0,2);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(2,ExternalIF.C_STRING,element.PIN_INPUT);
        element.jSetPinDescription(0,"startsWith?");
        element.jSetPinDescription(1,"String In"); 
        element.jSetPinDescription(2,"String Prefix"); 
        break;    
        case 25: //startsWith(String prefix, int toffset)
        initPins(0,1,0,3);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_BOOLEAN,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(2,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(3,ExternalIF.C_INTEGER,element.PIN_INPUT);
        element.jSetPinDescription(0,"startsWith?");
        element.jSetPinDescription(1,"String In"); 
        element.jSetPinDescription(2,"String Prefix"); 
        element.jSetPinDescription(3,"tOffset"); 
        break;    
        case 26: //substring(int beginIndex)
        initPins(0,1,0,2);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_STRING,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(2,ExternalIF.C_INTEGER,element.PIN_INPUT);
        element.jSetPinDescription(0,"subString Out");
        element.jSetPinDescription(1,"String In"); 
        element.jSetPinDescription(2,"begin Index");  
        break;    
        case 27: //substring(int beginIndex, int endIndex)
        initPins(0,1,0,3);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_STRING,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        setPin(2,ExternalIF.C_INTEGER,element.PIN_INPUT);
        setPin(3,ExternalIF.C_INTEGER,element.PIN_INPUT);
        element.jSetPinDescription(0,"subString Out");
        element.jSetPinDescription(1,"String In"); 
        element.jSetPinDescription(2,"begin Index"); 
        element.jSetPinDescription(3,"end Index"); 
        break;    
        case 28: // toLoweCase()
        initPins(0,1,0,1);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_STRING,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        element.jSetPinDescription(0,"String Out");
        element.jSetPinDescription(1,"String In"); 
        break;    
        case 29: // toUpperCase()
        initPins(0,1,0,1);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_STRING,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        element.jSetPinDescription(0,"String Out");
        element.jSetPinDescription(1,"String In"); 
        break;    
        case 30: // trim()
        initPins(0,1,0,1);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_STRING,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        element.jSetPinDescription(0,"String Out");
        element.jSetPinDescription(1,"String In"); 
        break;    
        case 31: // trimln()
        initPins(0,1,0,1);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_STRING,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        element.jSetPinDescription(0,"String Out");
        element.jSetPinDescription(1,"String In"); 
        break;    
        case 32: // trimAll()
        initPins(0,1,0,1);
        //setSize(50,50);
        initPinVisibility(false,true,false,true);    
        setPin(0,ExternalIF.C_STRING,element.PIN_OUTPUT);
        setPin(1,ExternalIF.C_STRING,element.PIN_INPUT);
        element.jSetPinDescription(0,"String Out");
        element.jSetPinDescription(1,"String In"); 
        break;    
            
        
        default:  
            
        break;
    }
    element.jSetInnerBorderVisibility(true);  
    initInputPins();
    initOutputPins();
  }


  public void initInputPins()
  {
    switch(Strfunction.selectedIndex){
        case 0:
            inAStr=(VSString)element.getPinInputReference(1);
            inBInt=(VSInteger)element.getPinInputReference(2);
            break;
        case 1:
            if(concatenateInputs.getValue()<=2){
            inAStr=(VSString)element.getPinInputReference(1);
            inBStr=(VSString)element.getPinInputReference(2);    
            }
            if(concatenateInputs.getValue()==3){
            inAStr=(VSString)element.getPinInputReference(1);
            inBStr=(VSString)element.getPinInputReference(2);    
            inCStr=(VSString)element.getPinInputReference(3);    
            }
            if(concatenateInputs.getValue()==4){
            inAStr=(VSString)element.getPinInputReference(1);
            inBStr=(VSString)element.getPinInputReference(2); 
            inCStr=(VSString)element.getPinInputReference(3);
            inDStr=(VSString)element.getPinInputReference(4);
            }
            break;
        case 2: case 3:case 4: case 5:case 22:case 24:
            inAStr=(VSString)element.getPinInputReference(1);
            inBStr=(VSString)element.getPinInputReference(2);
            break;
        case 6: case 28: case 29: case 30: case 31: case 32:
            inAStr=(VSString)element.getPinInputReference(1);
            break;
        case 7:case 12: case 26:
            inAStr=(VSString)element.getPinInputReference(1);
            inAInt=(VSInteger)element.getPinInputReference(2);  
            break;
            
        case 8:case 13: case 27:
            inAStr=(VSString) element.getPinInputReference(1);
            inAInt=(VSInteger)element.getPinInputReference(2);    
            inBInt=(VSInteger)element.getPinInputReference(3); 
            break;
        case 9: case 14:case 17:
            inAStr=(VSString) element.getPinInputReference(1);
            inBStr=(VSString) element.getPinInputReference(2);  
            break;   
        case 10: case 15:case 25:
            inAStr=(VSString) element.getPinInputReference(1);
            inBStr=(VSString) element.getPinInputReference(2);    
            inAInt=(VSInteger)element.getPinInputReference(3); 
            break;
        case 11:case 16:
            inAStr=(VSString)element.getPinInputReference(1);
            break;    
        case 18:
            inAStr=(VSString)element.getPinInputReference(1);
            inAInt=(VSInteger)element.getPinInputReference(2);
            inBInt=(VSInteger)element.getPinInputReference(3);
            break;    
       
        case 19:case 20:case 21:
            inAStr=(VSString)element.getPinInputReference(1);
            inBStr=(VSString)element.getPinInputReference(2);
            inCStr=(VSString)element.getPinInputReference(3);
            break;
            
        case 23:
            inAStr=(VSString)element.getPinInputReference(1);
            inBStr=(VSString)element.getPinInputReference(2);
            inAInt=(VSInteger)element.getPinInputReference(3);
            break;
           
            
            
        default:
            
            break;
    }  
    
  }

  public void initOutputPins()
  {
    switch(Strfunction.selectedIndex){
        case 0:case 1:case 18:case 19:case 20:case 21:case 26:case 27:case 28:case 29:case 30:case 31:case 32:
            element.setPinOutputReference(0,strOut);
            break;  
        case 2:case 3:case 4:case 5:case 11:case 17: case 24: case 25:
            element.setPinOutputReference(0,boolOut);
            break;
        case 6:
            element.setPinOutputReference(0,bytesOut);
            break; 
        case 7:case 8:case 9:case 10:case 12:case 13:case 14:case 15:case 16:
            element.setPinOutputReference(0,intOut);
            break; 
        case 22:case 23:
            element.setPinOutputReference(0,strArrayOut);
            break;    
            
        default:
            
        break;
    }  
  }

  public void process()
  {
    switch(Strfunction.selectedIndex){
        case 0:
            if (inAStr!=null && inBInt!=null)
            {
              String temp= "";
              try{
              char  tempc=inAStr.getValue().charAt(inBInt.getValue());
              temp= ""+tempc;
              }catch(Exception e){
              temp="";    
              }
              if (temp!=oldValue)
              {
                oldValue=temp;
                strOut.setValue(temp);
                strOut.setChanged(true);
                element.notifyPin(0);
              }
            }
        
            break;
            
        case 1:
            
            if (inAStr==null) inAStr=new VSString("");
            if (inBStr==null) inBStr=new VSString("");
            if (inCStr==null) inCStr=new VSString("");
            if (inDStr==null) inDStr=new VSString("");
            
            String tempC1=inAStr.getValue()+inBStr.getValue() +inCStr.getValue() +inDStr.getValue();
            if (tempC1!=oldValue)
            {
              oldValue=tempC1;
              strOut.setValue(tempC1);
              strOut.setChanged(true);
              element.notifyPin(0);
              //System.err.println("Notified Value" + tempC1);
            }
            
            break;
            
        case 2:
            if (inAStr!=null && inBStr!=null)
            {
              String temp=inAStr.getValue()+inBStr.getValue(); // To detect if A or B Changes
              boolean contain=false;
              try{
              contain = inAStr.getValue().contains(inBStr.getValue());
              }catch(Exception e){
              temp=""; 
              contain=false;
              }
              if(inBStr.getValue().isEmpty()) contain=false;
              if (temp!=oldValue)
              {
                oldValue=temp;
                boolOut.setValue(contain);
                boolOut.setChanged(true);
                element.notifyPin(0);
              }
            }
        
            break;
        case 3:  //-----------------------------------------------------------------------------------ENDS WITH
            if (inAStr!=null && inBStr!=null)
            {
              String temp=inAStr.getValue()+inBStr.getValue(); // To detect if A or B Changes
              boolean endsWith=false;
              try{
              endsWith = inAStr.getValue().endsWith(inBStr.getValue());
              }catch(Exception e){
              temp=""; 
              endsWith=false;
              }
              if(inBStr.getValue().isEmpty()) endsWith=false;
              if (temp!=oldValue)
              {
                oldValue=temp;
                boolOut.setValue(endsWith);
                boolOut.setChanged(true);
                element.notifyPin(0);
              }
            }
        
            break;
        case 4:  //-----------------------------------------------------------------------------------EQUALS
            if (inAStr!=null && inBStr!=null)
            {
              String temp=inAStr.getValue()+inBStr.getValue(); // To detect if A or B Changes
              boolean equalsStr=false;
              try{
              equalsStr = inAStr.getValue().equals(inBStr.getValue());
              }catch(Exception e){
              temp=""; 
              equalsStr=false;
              }
              if(inBStr.getValue().isEmpty()) equalsStr=false;
              if (temp!=oldValue)
              {
                oldValue=temp;
                boolOut.setValue(equalsStr);
                boolOut.setChanged(true);
                element.notifyPin(0);
              }
            }
        
            break;
        case 5:  //--------------------------------------------------------------------------EQUALS_IGNORE_CASE
            if (inAStr!=null && inBStr!=null)
            {
              String temp=inAStr.getValue()+inBStr.getValue(); // To detect if A or B Changes
              boolean equalsStr=false;
              try{
              equalsStr = inAStr.getValue().equalsIgnoreCase(inBStr.getValue());
              }catch(Exception e){
              temp=""; 
              equalsStr=false;
              }
              if(inBStr.getValue().isEmpty()) equalsStr=false;
              if (temp!=oldValue)
              {
                oldValue=temp;
                boolOut.setValue(equalsStr);
                boolOut.setChanged(true);
                element.notifyPin(0);
              }
            }
        
            break;
        case 6:  //--------------------------------------------------------------------------GET BYTES
            if (inAStr!=null)
            {
              String temp=inAStr.getValue(); // To detect if A or B Changes
              byte[] bytesArray=new byte[temp.length()];
              try{
              bytesArray=temp.getBytes();
              }catch(Exception e){
              temp=""; 
              }
              if (temp!=oldValue)
              {
                oldValue=temp;
                bytesOut=new VS1DByte(bytesArray.length);
                bytesOut.setValues(bytesArray);
                bytesOut.setChanged(true);
                element.notifyPin(0);
              }
            }
        
            break;
        case 7:  //--------------------------------------------------------------------------INDEX OF (int Char)
            if (inAStr!=null && inAInt!=null)
            {
              String temp=inAStr.getValue()+inAInt.getValue(); // To detect if A or B Changes
              int indexOut=0;
              try{
              indexOut=inAStr.getValue().indexOf(inAInt.getValue());
              }catch(Exception e){
              temp=""; 
              }
              if (temp!=oldValue)
              {
                oldValue=temp;
                intOut.setValue(indexOut);
                intOut.setChanged(true);
                element.notifyPin(0);
              }
            }
        
            break;
        case 8:  //----------------------------------------------------------INDEX OF (int Char, int fromIndex)
            if (inAStr!=null && inAInt!=null && inBInt!=null)
            {
              String temp=inAStr.getValue()+inAInt.getValue()+inBInt.getValue(); // To detect if A or B Changes
              int indexOut=0;
              try{
              indexOut=inAStr.getValue().indexOf(inAInt.getValue(),inBInt.getValue());
              }catch(Exception e){
              temp=""; 
              }
              if (temp!=oldValue)
              {
                oldValue=temp;
                intOut.setValue(indexOut);
                intOut.setChanged(true);
                element.notifyPin(0);
              }
            }
        
            break;
        case 9:  //---------------------------------------------------------------INDEX OF (String str)
            if (inAStr!=null && inBStr!=null)
            {
              String temp=inAStr.getValue()+inBStr.getValue(); // To detect if A or B Changes
              int indexOut=0;
              try{
              indexOut=inAStr.getValue().indexOf(inBStr.getValue());
              }catch(Exception e){
              temp=""; 
              }
              if (temp!=oldValue)
              {
                oldValue=temp;
                intOut.setValue(indexOut);
                intOut.setChanged(true);
                element.notifyPin(0);
              }
            }
        
            break;
        case 10:  //------------------------------------------------INDEX OF (String str, int fromIndex)
            if (inAStr!=null && inBStr!=null && inAInt!=null)
            {
              String temp=inAStr.getValue()+inBStr.getValue()+inAInt.getValue(); // To detect if A or B Changes
              int indexOut=0;
              try{
              indexOut=inAStr.getValue().indexOf(inBStr.getValue(),inAInt.getValue());
              }catch(Exception e){
              temp=""; 
              }
              if (temp!=oldValue)
              {
                oldValue=temp;
                intOut.setValue(indexOut);
                intOut.setChanged(true);
                element.notifyPin(0);
              }
            }
        
            break;
        case 11:  //-----------------------------------------------------------------------IS_EMPTY
            if (inAStr!=null)
            {
              String temp=inAStr.getValue()+""; // To detect if A or B Changes
              boolean emptyStr=false;
              try{
              emptyStr = inAStr.getValue().isEmpty();
              }catch(Exception e){
              temp=""; 
              emptyStr=false;
              }
              
              if (temp!=oldValue)
              {
                oldValue=temp;
                boolOut.setValue(emptyStr);
                boolOut.setChanged(true);
                element.notifyPin(0);
              }
            }
        
            break;  
            
        case 12:  //-------------------------------------------------------------------LASTINDEX OF (int Char)
            if (inAStr!=null && inAInt!=null)
            {
              String temp=inAStr.getValue()+inAInt.getValue(); // To detect if A or B Changes
              int indexOut=0;
              try{
              indexOut=inAStr.getValue().lastIndexOf(inAInt.getValue());
              }catch(Exception e){
              temp=""; 
              }
              if (temp!=oldValue)
              {
                oldValue=temp;
                intOut.setValue(indexOut);
                intOut.setChanged(true);
                element.notifyPin(0);
              }
            }
        
            break;
        case 13:  //---------------------------------------------------LASTINDEX OF (int Char, int fromIndex)
            if (inAStr!=null && inAInt!=null && inBInt!=null)
            {
              String temp=inAStr.getValue()+inAInt.getValue()+inBInt.getValue(); // To detect if A or B Changes
              int indexOut=0;
              try{
              indexOut=inAStr.getValue().lastIndexOf(inAInt.getValue(),inBInt.getValue());
              }catch(Exception e){
              temp=""; 
              }
              if (temp!=oldValue)
              {
                oldValue=temp;
                intOut.setValue(indexOut);
                intOut.setChanged(true);
                element.notifyPin(0);
              }
            }
        
            break;
        case 14:  //------------------------------------------------------------LASTINDEX OF (String str)
            if (inAStr!=null && inBStr!=null)
            {
              String temp=inAStr.getValue()+inBStr.getValue(); // To detect if A or B Changes
              int indexOut=0;
              try{
              indexOut=inAStr.getValue().lastIndexOf(inBStr.getValue());
              }catch(Exception e){
              temp=""; 
              }
              if (temp!=oldValue)
              {
                oldValue=temp;
                intOut.setValue(indexOut);
                intOut.setChanged(true);
                element.notifyPin(0);
              }
            }
        
            break;
        case 15:  //------------------------------------------------LASTINDEX OF (String str, int fromIndex)
            if (inAStr!=null && inBStr!=null && inAInt!=null)
            {
              String temp=inAStr.getValue()+inBStr.getValue()+inAInt.getValue(); // To detect if A or B Changes
              int indexOut=0;
              try{
              indexOut=inAStr.getValue().lastIndexOf(inBStr.getValue(),inAInt.getValue());
              }catch(Exception e){
              temp=""; 
              }
              if (temp!=oldValue)
              {
                oldValue=temp;
                intOut.setValue(indexOut);
                intOut.setChanged(true);
                element.notifyPin(0);
              }
            }
        
            break;  
            
        case 16:  //--------------------------------------------------------------LENGTH()
            if (inAStr!=null)
            {
              String temp=inAStr.getValue(); // To detect if A or B Changes
              int lenOut=0;
              try{
              lenOut=inAStr.getValue().length();
              }catch(Exception e){
              temp=""; 
              lenOut=0;
              }
              if (temp!=oldValue)
              {
                oldValue=temp;
                intOut.setValue(lenOut);
                intOut.setChanged(true);
                element.notifyPin(0);
              }
            }
        
            break;    
        
        case 17: //Matches(STR)
            if (inAStr!=null && inBStr!=null)
            {
              String temp=inAStr.getValue()+inBStr.getValue(); // To detect if A or B Changes
              boolean match=false;
              try{
              match = inAStr.getValue().matches(inBStr.getValue());
              }catch(Exception e){
              temp=""; 
              match=false;
              }
              if(inBStr.getValue().isEmpty()) match=false;
              if (temp!=oldValue)
              {
                oldValue=temp;
                boolOut.setValue(match);
                boolOut.setChanged(true);
                element.notifyPin(0);
              }
            }
        
            break;    
         
        case 18: // replace(char oldChar, char newChar)
            if (inAStr!=null && inAInt!=null && inBInt!=null)
            {
              String temp=inAStr.getValue()+inAInt.getValue()+inBInt.getValue(); //To Detect Changes
              String outStr="";
              try{
              outStr=inAStr.getValue().replace((char)inAInt.getValue(),(char)inBInt.getValue());
              }catch(Exception e){
              temp=""; 
              outStr="";
              }
              if (temp!=oldValue)
              {
                oldValue=temp;
                strOut.setValue(outStr);
                strOut.setChanged(true);
                element.notifyPin(0);
              }
            }
            break;
        case 19: // replace(CharSequence target, CharSequence replacement)
            if (inAStr!=null && inBStr!=null && inCStr!=null)
            {
              String temp=inAStr.getValue()+inBStr.getValue()+inCStr.getValue(); //To Detect Changes
              String outStr="";
              try{
              outStr=inAStr.getValue().replace(inBStr.getValue(),inCStr.getValue());
              }catch(Exception e){
              temp=""; 
              outStr="";
              }
              if (temp!=oldValue)
              {
                oldValue=temp;
                strOut.setValue(outStr);
                strOut.setChanged(true);
                element.notifyPin(0);
              }
            }
            break;
        case 20: // replaceAll(CharSequence target, CharSequence replacement)
            if (inAStr!=null && inBStr!=null && inCStr!=null)
            {
              String temp=inAStr.getValue()+inBStr.getValue()+inCStr.getValue(); //To Detect Changes
              String outStr="";
              try{
              outStr=inAStr.getValue().replaceAll(inBStr.getValue(),inCStr.getValue());
              }catch(Exception e){
              temp=""; 
              outStr="";
              }
              if (temp!=oldValue)
              {
                oldValue=temp;
                strOut.setValue(outStr);
                strOut.setChanged(true);
                element.notifyPin(0);
              }
            }
            break;
        case 21: // replaceFirst(String regex, String replacement)
            if (inAStr!=null && inBStr!=null && inCStr!=null)
            {
              String temp=inAStr.getValue()+inBStr.getValue()+inCStr.getValue(); //To Detect Changes
              String outStr="";
              try{
              outStr=inAStr.getValue().replaceFirst(inBStr.getValue(),inCStr.getValue());
              }catch(Exception e){
              temp=""; 
              outStr="";
              }
              if (temp!=oldValue)
              {
                oldValue=temp;
                strOut.setValue(outStr);
                strOut.setChanged(true);
                element.notifyPin(0);
              }
            }
            break;
            
        case 22:  //--------------------------------------------------------------------------split(str regex)
            if (inAStr!=null && inBStr!=null)
            {
              String temp=inAStr.getValue() + inBStr.getValue(); // To detect if A or B Changes
              String[] strArray;
              
              try{
                strArray=new String[inAStr.getValue().split(inBStr.getValue()).length];    
                strArray=inAStr.getValue().split(inBStr.getValue());
                }catch(Exception e){
                temp=""; 
                strArray=new String[0];
                }
              if (temp!=oldValue)
              {
                  oldValue=temp;
                  strArrayOut= new VS1DString(strArray.length);
                  strArrayOut.setValues(strArray);
                  strArrayOut.setChanged(true);
                  element.notifyPin(0);
                  //element.jConsolePrintln("Notified!"+strArray[0]);
//                  System.err.println("Notified!"+strArray[0]);
//                  for(String str:strArray){
//                  System.err.println(str);    
//                  }
//                  System.err.println("");
                  
              }
            }
        
            break;
        case 23:  //----------------------------------------------------------------split(str regex, int limit)
            if (inAStr!=null && inBStr!=null && inAInt!=null)
            {
              String temp=inAStr.getValue() + inBStr.getValue()+inAInt.getValue(); // To detect if A or B Changes
              String[] strArray;
              
              try{
                strArray=new String[inAStr.getValue().split(inBStr.getValue()).length];    
                strArray=inAStr.getValue().split(inBStr.getValue(),inAInt.getValue());
                }catch(Exception e){
                temp=""; 
                strArray=new String[0];
                }
              if (temp!=oldValue)
              {
                  oldValue=temp;
                  strArrayOut= new VS1DString(strArray.length);
                  strArrayOut.setValues(strArray);
                  strArrayOut.setChanged(true);
                  element.notifyPin(0);
//                  System.err.println("Notified!");
//                  for(String str:strArray){
//                  System.err.println(str);    
//                  }
//                  System.err.println("");
                  
              }
            }
        
            break;
        
        case 24: //---------------------------------------------------------------startsWith(String prefix)
            if (inAStr!=null && inBStr!=null)
            {
              String temp=inAStr.getValue()+inBStr.getValue(); // To detect if A or B Changes
              boolean startsWith=false;
              try{
              startsWith = inAStr.getValue().startsWith(inBStr.getValue());
              }catch(Exception e){
              temp=""; 
              startsWith=false;
              }
              if(inBStr.getValue().isEmpty()) startsWith=false;
              if (temp!=oldValue)
              {
                oldValue=temp;
                boolOut.setValue(startsWith);
                boolOut.setChanged(true);
                element.notifyPin(0);
              }
            }
        
            break;    
        case 25: //---------------------------------------------------startsWith(String prefix, int toffset)
            if (inAStr!=null && inBStr!=null && inAInt!=null)
            {
              String temp=inAStr.getValue()+inBStr.getValue(); // To detect if A or B Changes
              boolean startsWith=false;
              try{
              startsWith = inAStr.getValue().startsWith(inBStr.getValue(),inAInt.getValue());
              }catch(Exception e){
              temp=""; 
              startsWith=false;
              }
              if(inBStr.getValue().isEmpty()) startsWith=false;
              if (temp!=oldValue)
              {
                oldValue=temp;
                boolOut.setValue(startsWith);
                boolOut.setChanged(true);
                element.notifyPin(0);
              }
            }
        
            break;  
            
        case 26: // substring(int beginIndex)
            if (inAStr!=null && inAInt!=null)
            {
              String temp=inAStr.getValue()+inAInt.getValue(); //To Detect Changes
              String outStr="";
              try{
              outStr=inAStr.getValue().substring(inAInt.getValue());
              }catch(Exception e){
              temp=""; 
              outStr="";
              }
              if (temp!=oldValue)
              {
                oldValue=temp;
                strOut.setValue(outStr);
                strOut.setChanged(true);
                element.notifyPin(0);
              }
            }
            break;    
        case 27: // substring(int beginIndex, int endIndex)
            if (inAStr!=null && inAInt!=null && inBInt!=null)
            {
              String temp=inAStr.getValue()+inAInt.getValue() +inBInt.getValue(); //To Detect Changes
              String outStr="";
              try{
              outStr=inAStr.getValue().substring(inAInt.getValue(),inBInt.getValue());
              }catch(Exception e){
              temp=""; 
              outStr="";
              }
              if (temp!=oldValue)
              {
                oldValue=temp;
                strOut.setValue(outStr);
                strOut.setChanged(true);
                element.notifyPin(0);
              }
            }
            break;    
        case 28: // ---------------------------------------------------------------------toLoweCase()
            if (inAStr!=null)
            {
              String temp=inAStr.getValue(); //To Detect Changes
              String outStr="";
              try{
              outStr=inAStr.getValue().toLowerCase();
              }catch(Exception e){
              temp=""; 
              outStr="";
              }
              if (temp!=oldValue)
              {
                oldValue=temp;
                strOut.setValue(outStr);
                strOut.setChanged(true);
                element.notifyPin(0);
              }
            }
            break;    
        case 29: // ---------------------------------------------------------------------toUpperCase()
            if (inAStr!=null)
            {
              String temp=inAStr.getValue(); //To Detect Changes
              String outStr="";
              try{
              outStr=inAStr.getValue().toUpperCase();
              }catch(Exception e){
              temp=""; 
              outStr="";
              }
              if (temp!=oldValue)
              {
                oldValue=temp;
                strOut.setValue(outStr);
                strOut.setChanged(true);
                element.notifyPin(0);
              }
            }
            break;    
        case 30: // --------------------------------------------------------------------trim()
            if (inAStr!=null)
            {
              String temp=inAStr.getValue(); //To Detect Changes
              String outStr="";
              try{
              outStr=inAStr.getValue().trim();
              }catch(Exception e){
              temp=""; 
              outStr="";
              }
              if (temp!=oldValue)
              {
                oldValue=temp;
                strOut.setValue(outStr);
                strOut.setChanged(true);
                element.notifyPin(0);
              }
            }
            break;    
        case 31: // --------------------------------------------------------------------trimln()
            if (inAStr!=null)
            {
              String temp=inAStr.getValue(); //To Detect Changes
              String outStr="";
              String outStr2="";
              
              try{
                  
              outStr=inAStr.getValue();
              outStr2=outStr.replace("\n", "").replace("\r", "").trim();
              
              }catch(Exception e){
              temp=""; 
              outStr="";
              outStr2="";
              }
              if (temp!=oldValue)
              {
                oldValue=temp;
                strOut.setValue(outStr2);
                strOut.setChanged(true);
                element.notifyPin(0);
              }
            }
            break;    
        case 32: // --------------------------------------------------------------------trimAll()
            if (inAStr!=null)
            {
              String temp=inAStr.getValue(); //To Detect Changes
              String outStr="";
              String outStr2="";
              
              try{
                  
              outStr=inAStr.getValue();
              outStr2=outStr.replace("\n", "").replace("\r", "").replace("\t", "").replace(" ", "").trim();
              
              }catch(Exception e){
              temp=""; 
              outStr="";
              outStr2="";
              }
              if (temp!=oldValue)
              {
                oldValue=temp;
                strOut.setValue(outStr2);
                strOut.setChanged(true);
                element.notifyPin(0);
              }
            }
            break;    
            
        default:
            
            break;
    }
      
    
  }
  
  
  public void setPropertyEditor()
  {
    element.jAddPEItem("String Function",Strfunction, 0,50,true);
    boolean enableEdit1=false;
    if(selectedFcn.getValue()==1){ //If Concatenate
    enableEdit1=true;  
    }
    element.jAddPEItem("Concatenate Inputs 2-4",concatenateInputs, 2,4,enableEdit1);
   
    localize();
  }


  private void localize()
  {
    int d=6;
    String language;

    language="en_US";

    element.jSetPEItemLocale(d+0,language,"String Function");
    element.jSetPEItemLocale(d+1,language,"Concatenate Inputs 2-5");    


    language="es_ES";

    element.jSetPEItemLocale(d+0,language,"Funcion");
    element.jSetPEItemLocale(d+1,language,"Entradas 2-5");    
  }

  public void propertyChanged(Object o)
  { 
    if(o.equals(Strfunction)){    
    selectedFcn.setValue(Strfunction.selectedIndex);
    if(Strfunction.selectedIndex==1){
    element.getElementOwner().processPropertyEditor();    
    }   
    }
    myInit();
    element.jRepaint();
  }

   public void loadFromStream(java.io.FileInputStream fis)
  {
      Strfunction.loadFromStream(fis);
      selectedFcn.loadFromStream(fis);
      concatenateInputs.loadFromStream(fis);
      selectedFcn.setValue(Strfunction.selectedIndex);
      
      myInit();
      element.jRepaint();
  }

  public void saveToStream(java.io.FileOutputStream fos)
  {
      Strfunction.saveToStream(fos);
      selectedFcn.saveToStream(fos);
      concatenateInputs.saveToStream(fos);
  }

  
}

