//*****************************************************************************
//* Element of MyOpenLab Library                                              *
//*                                                                           *
//* Copyright (C) 2004  Carmelo Salafia (cswi@gmx.de)                         *
//*                                                                           *
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



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import VisualLogic.*;
import VisualLogic.variables.*;


class Property
{
  public String name="";
  public int dt;
  public int min;
  public int max;
  public VSObject vsObject;
}


public class FileConfigParser
{

    private ArrayList<String> liste = new ArrayList();
    private int pc = 0;
    
    public String strName = "";
    public String strGlobals="";
    public String strInit="";
    public String strEventHandler="";
    public String strProcedure="";
    public String strPaint="";
    
    public String strTopPins="";
    public String strRightPins="";
    public String strBottomPins="";
    public String strLeftPins="";
    
    public String strWidth="";
    public String strHeight="";
    
    public String strPINS_VISIBLE_TOP="";
    public String strPINS_VISIBLE_RIGHT="";
    public String strPINS_VISIBLE_BOTTOM="";
    public String strPINS_VISIBLE_LEFT="";

    public int pinTypes[] = new int[100];
    public String pinInputOutput[] = new String[100];
    public String pinDescription[] = new String[100];
    
    public Property properties[] = new Property[100];
    public int propertiesC=0;
        
    public final int PIN_NOP=0;
    public final int PIN_INTEGER=1;
    public final int PIN_BOOLEAN=2;
    public final int PIN_DOUBLE=3;
    public final int PIN_FLOWINFO=4;

    
    public final int DT_NOP=0;
    public final int DT_BYTE=1;
    public final int DT_WORD=2;
    public final int DT_BOOLEAN=3;
    public final int DT_STRING=4;


    

    private String getBlock()
    {
        boolean begin=false;
        String str="";
        
        while(pc<liste.size())
        {
            String inputString=liste.get(pc);

            if (inputString.equalsIgnoreCase("}"))
            {
                begin = false;
                return str;
            }
            if (begin)
            {
                str += inputString + "\n";
            }
            if (inputString.equalsIgnoreCase("{"))
            {
                begin = true;
                str = "";
            }
            pc++;
        }
        
        return "";
    }
    
    public int getInteger(String num)
    {
        try
        {
            return Integer.parseInt(num);
        }catch(Exception ex)
        {
            
        }
        return -1;
    }

    public void showMessage(String message)
    {
        System.out.println(message);
    }
    
    
    private String[] reduceTokens(String tokens[])
    {
        ArrayList<String> result= new ArrayList();
        
        for (int i=0;i<tokens.length;i++)
        {
            if (!tokens[i].trim().equalsIgnoreCase(""))
            {
                result.add(tokens[i].trim());
            }
        }
        
        String res[]=new String[result.size()];
        for (int i=0;i<result.size();i++)
        {
            res[i]=result.get(i);
        }
        
        return res;
    }
    
    private int getNumber(String value)
    {
      try
      {
        return Integer.valueOf(value);
      } catch(Exception ex)
      {

      }
      return 0;
    }
    
    
    private void interp()
    {
        String inputString;
        pc = 0;
        
        propertiesC=0;

        while (pc < liste.size())
        {
            inputString = liste.get(pc);    
                        
            String tokenX[] = inputString.split("\\s");    // Trenner ist das leerzeichen 
            
            String tokens[]=reduceTokens(tokenX);

            if (tokens.length > 0)
            {
                String token = tokens[0].trim();
                
                if (token.equalsIgnoreCase("PROPERTY"))
                {
                    System.out.println("---->"+tokens.length);
                    
                    if (tokens.length == 4+1)
                    {
                        String strName=tokens[1];
                        String strType=tokens[2];
                        String strRangeMin=tokens[3];
                        String strRangeMax=tokens[4];
                        
                        System.out.println("1--->"+strName);
                        System.out.println("2--->"+strType);
                        System.out.println("3--->"+strRangeMin);
                        System.out.println("4--->"+strRangeMax);


                        int dt=DT_NOP; // nicht belegt, also Pin ist frei!
                        
                        if (strType.equalsIgnoreCase("STRING")) dt=DT_STRING;else
                        if (strType.equalsIgnoreCase("BOOLEAN")) dt=DT_BOOLEAN;else
                        if (strType.equalsIgnoreCase("BYTE")) dt=DT_BYTE;else
                        if (strType.equalsIgnoreCase("WORD")) dt=DT_WORD;else
                        //if (strType.equalsIgnoreCase("FLOAT")) dt=DT_FLOAT;else
                        {
                            showMessage("PROPERTY : one or more params are not correct.");
                        }
                        properties[propertiesC]= new Property();
                        properties[propertiesC].name=strName;
                        properties[propertiesC].dt=dt;
                        properties[propertiesC].min=getNumber(strRangeMin);
                        properties[propertiesC].max=getNumber(strRangeMax);
                        
                        propertiesC++;

                    }else
                    {
                        showMessage("PROPERTY : 4 Params excepted!");
                    }
                }else
                if (token.equalsIgnoreCase("PIN_TYPE"))
                {
                    if (tokens.length == 4)
                    {
                        String strPinIndex=tokens[1];
                        String strType=tokens[2];
                        String strInputOutput=tokens[3];
                        
                        int pinIndex=getInteger(strPinIndex);
                        
                        if (pinIndex>-1)
                        {
                            int pinType=PIN_NOP; // nicht belegt, also Pin ist frei!
                            if (strType.equalsIgnoreCase("INTEGER")) pinType=PIN_INTEGER;else
                            if (strType.equalsIgnoreCase("BOOLEAN")) pinType=PIN_BOOLEAN;else
                            if (strType.equalsIgnoreCase("DOUBLE")) pinType=PIN_DOUBLE;else
                            if (strType.equalsIgnoreCase("FLOWINFO")) pinType=PIN_FLOWINFO;else
                            {
                                showMessage("PIN_TYPE : 2 Param is not correct!");                                
                            }
                            pinTypes[pinIndex]=pinType;
                            pinInputOutput[pinIndex]=strInputOutput;
                        }else
                        {
                            showMessage("PIN_TYPE : 1 Param is not correct!");
                        }
                    }else
                    {
                        showMessage("PIN_TYPE : 2 Params excepted!");
                    }
                    strName = tokens[1].trim();
                }else
                if (token.equalsIgnoreCase("PIN_DESC"))
                {
                    if (tokens.length == 3)
                    {
                        String strPinIndex=tokens[1];
                        String strDescription=tokens[2];

                        int pinIndex=getInteger(strPinIndex);

                        if (pinIndex>-1)
                        {
                            pinDescription[pinIndex]=strDescription;
                        }else
                        {
                            showMessage("PIN_DESC : 1 Param is not correct!");
                        }
                    }else
                    {
                        showMessage("PIN_DESC : 2 Params excepted!");
                    }
                    strName = tokens[1].trim();
                }else
                if (token.equalsIgnoreCase("NAME"))
                {
                    strName = tokens[1].trim();
                }else
                if (token.equalsIgnoreCase("WIDTH"))
                {
                    strWidth = tokens[1].trim();
                }else
                if (token.equalsIgnoreCase("HEIGHT"))
                {
                    strHeight = tokens[1].trim();
                }else
                if (token.equalsIgnoreCase("PINS_VISIBLE_TOP"))
                {
                    strPINS_VISIBLE_TOP = tokens[1].trim();
                }else
                if (token.equalsIgnoreCase("PINS_VISIBLE_RIGHT"))
                {
                    strPINS_VISIBLE_RIGHT = tokens[1].trim();
                }else
                if (token.equalsIgnoreCase("PINS_VISIBLE_BOTTOM"))
                {
                    strPINS_VISIBLE_BOTTOM = tokens[1].trim();
                }else
                if (token.equalsIgnoreCase("PINS_VISIBLE_LEFT"))
                {
                    strPINS_VISIBLE_LEFT = tokens[1].trim();
                }else
                if (token.equalsIgnoreCase("TopPins"))
                {
                    strTopPins = tokens[1].trim();
                }else
                if (token.equalsIgnoreCase("BottomPins"))
                {
                    strBottomPins = tokens[1].trim();
                }else
                if (token.equalsIgnoreCase("InputPins") || token.equalsIgnoreCase("LeftPins"))
                {
                    strLeftPins = tokens[1].trim();
                }else
                if (token.equalsIgnoreCase("OutPutPins") || token.equalsIgnoreCase("RightPins"))
                {
                    strRightPins = tokens[1].trim();
                }else
                if (inputString.equalsIgnoreCase("GLOBALS"))
                {
                    strGlobals=getBlock();
                }else
                if (inputString.equalsIgnoreCase("PAINT"))
                {
                    strPaint=getBlock();
                }else
                if (inputString.equalsIgnoreCase("INIT"))
                {
                    strInit=getBlock();
                }else
                if (inputString.equalsIgnoreCase("EVENT-HANDLER"))
                {
                    strEventHandler=getBlock();
                }else
                if (inputString.equalsIgnoreCase("PROCEDURE"))
                {
                    strProcedure=getBlock();
                }
            }
            pc++;
        }

    }

    public void loadTextFile(File file)
    {
        if (file.exists())
        {
            try
            {
                BufferedReader input = new BufferedReader(new FileReader(file));
                String inputString;
                while ((inputString = input.readLine()) != null)
                {
                    if (inputString == null)
                    {
                        break;
                    }
                    inputString = inputString.trim();
                    liste.add(inputString);
                }
                input.close();
            }
            catch (Exception ex)
            {
                showMessage(ex.toString());
            }
        }
    }

    public FileConfigParser(String filename)
    {
        loadTextFile(new File(filename));
        interp();
    }

}

