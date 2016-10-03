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
package VisualLogic.variables;

import MyParser.OpenVariable;
import VisualLogic.*;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.script.Bindings;

public class VSFlowInfo extends VSObject
{
    public ArrayList parameterDefinitions = new ArrayList(); // enthält OpenVariable
    public ArrayList variablenListe = new ArrayList();       // enthält OpenVariable
    public Hashtable tags = new Hashtable();       // Für irgendwelche Parameter die durch die ganze Linie laufen
    public Object returnValue=null;
    public ExternalIF source=null;
    
    public Bindings bindings; // Das einzige wirklich wichtige! NEU! 17.08.2016
    
          
    public VSFlowInfo()
    {
        //value=new String[columns][rows];
    }
    
    @Override
    public void copyReferenceFrom(Object in)
    {
      copyValueFrom(in);
    }    
    
    @Override
    public void copyValueFrom(Object in)
    {
        if (in!=null)
        {
          VSFlowInfo temp =(VSFlowInfo)in;
          this.parameterDefinitions=temp.parameterDefinitions;
          this.variablenListe=temp.variablenListe;
          this.tags=temp.tags;          
          this.returnValue=temp.returnValue;
          this.bindings=temp.bindings;
                    
          this.source = temp.source;          
          setChanged(temp.isChanged());
        } else 
        {
            this.variablenListe.clear();
        }
    }     
    
    @Override
    public void saveToStream(java.io.FileOutputStream fos)
    {
    }        
    
    @Override
    public void loadFromStream(java.io.FileInputStream fis) 
    {
            
    }       
    
    
    public int getDataType(Object datatype)
    {
        if (datatype instanceof Double)
        {
            return OpenVariable.C_DOUBLE;
        }else
        if (datatype instanceof String)
        {
            return OpenVariable.C_STRING;
        }else
        if (datatype instanceof Boolean)
        {
            return OpenVariable.C_BOOLEAN;
        }else return -1;        
        
    }    
    
    public int getDataType(String datatype)
    {
        datatype=datatype.trim();
        
        if (datatype.equalsIgnoreCase("DOUBLE"))
        {
            return OpenVariable.C_DOUBLE;
        }else
        if (datatype.equalsIgnoreCase("STRING"))
        {
            return OpenVariable.C_STRING;
        }else
        if (datatype.equalsIgnoreCase("BOOLEAN"))
        {
            return OpenVariable.C_BOOLEAN;
        }else return -1;        
        
    }
    
    public OpenVariable addVariable(String varName, int datatype)
    {
        OpenVariable result = new OpenVariable();
        result.name=varName;
        result.datatype=datatype;
        variablenListe.add(result);
                
        generateVariable(result);
        return result;
    }
    
    public OpenVariable addParamter(String varName, int datatype)
    {
        OpenVariable result = new OpenVariable();
        result.name=varName;
        result.datatype=datatype;
        parameterDefinitions.add(result);                
        return result;
    }
    
    
    /*public OpenVariable addVariable()
    {
        OpenVariable result = new OpenVariable();
        result.name=varName;
        result.datatype=datatype;
        variablenListe.add(result);
        
        return result;
    }*/
    
    public void generateVariable(OpenVariable node)
    {
        //OpenVariable node;
        //for (int i=0;i<variablenListe.size();i++)
        {
            //node= (OpenVariable)variablenListe.get(i);
            
            if (node.datatype==0)
            {
                node.value= new Double(0);
            }
            if (node.datatype==1)
            {
                node.value= new String("");
            }
            if (node.datatype==2)
            {
                node.value= new Boolean(false);
            }
        }        
    }    
    
    public OpenVariable getVariable(String varname)
    {
        //System.out.println("XXXXXXX Suche nach:"+varname);
        varname=varname.trim();
        OpenVariable node;
        for (Object variablenListe1 : variablenListe) {
            node = (OpenVariable) variablenListe1;
            
            //System.out.println("XXXXXXX var item:"+node.name);
            
            if (varname.equals(node.name.trim()))
            {
                //System.out.println("XXXXXXX:"+node.name);
                return node;
            }
        }
        return null;
    }
    
    
    public void setVariable(String varname,Object value)
    {
        OpenVariable o = getVariable(varname);
        if (o!=null)
        {
            if (o.value instanceof Boolean && value instanceof Boolean)
            {                
                o.value = value;
            }else
            if (o.value instanceof Double && value instanceof Double)
            {                
                o.value = value;
            }else
            if (o.value instanceof String && value instanceof String)
            {
                o.value = value;
            }else
            {
                System.out.println("Error setting Variable");
            }
        } else
        {            
         //   Tools.showMessage("Variable \""+varname+"\" not definied! \nPlease define the variable");
        }

    }
    
    
}
