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


package ExpressionParser;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;


class Token
{
    public boolean isNum=false;
    public boolean isOp=false;
    public boolean isFunc=false;
    public boolean isVar=false;
    public boolean isKlammer=false;
        
    public double num;
    public String func;
    public String op="";
    public String str;
    public String klammer;
    
    public void setNum(String num)
    {
        this.num=Double.parseDouble(num);
        isNum=true;
    }    
    public void setOp(String op)
    {
        this.op=op;
        isOp=true;
    }        
    public void setVar(String str)
    {
        this.str=str;
        isVar=true;
    }        
    public void setKlammer(String klammer)
    {
        this.klammer=klammer;
        isKlammer=true;
    }
    public void setFunc(String func)
    {
        this.func=func;
        isFunc=true;
    }
    
}

public class Parser 
{
    private static final String ALPHA="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";    
    private static final String OPERATOREN="+-*/^%";
    private static final String KLAMMER="()";
    private StringTokenizer tokenizer;
    private ArrayList mainVector = new ArrayList();
    private ArrayList optVector;
    private int pointer=0;
    private ArrayList vars = new ArrayList();
    private String errorMessage="";
    
    
    public String getErrorMessage()
    {
      return errorMessage;
    }
    public void setErrorMessage(String message)
    {
      errorMessage=message;
    }
    
    public void delErrorMessage()
    {
      errorMessage="";
    }
    
    public void setExpression(String expression)
    {
        expression=expression.trim();
        tokenizer = new StringTokenizer( expression ,"+-*/()%^",true);
        pointer=0;
        mainVector.clear();
        tokensToVector(mainVector);
        handleMinusOperator(mainVector);
        klammere(mainVector,0); // klammere zuerst die Punkte
        klammere(mainVector,1); // klammere zuerst die Punkte
        optVector=optimize(mainVector);
        int count=getKlammerPlus(mainVector);

        if (count>0)
        {
          setErrorMessage("Fehler in String : zuviel geöffnete klammern!");
        }

        if (count<0)
        {
          setErrorMessage("Fehler in String : zuviel geschlossene klammern!");
        }
    }
    
    public Parser(String expression)
    {
      setExpression(expression);
    }
    
    private static double fakultaet (double a)
    {
      double i,r;
      r=1.0;
      for(i=2 ; i <= a ; i++)
      {
        r *= i;
      }
      return r;
    }

    
    private ArrayList optimize(ArrayList vector)
    {    
        ArrayList result = new ArrayList();
        
        for (int i=0;i<vector.size();i++)
        {            
            String str=(String)vector.get(i);
            
            Token token= new Token();
            if (isNum(str)) token.setNum(str); else
            if (isString(str)) 
            {
                token.setVar(str);
            }else
            if (isOP(str)) token.setOp(str);else
            if (isFunc(str)) token.setFunc(str);else
            if (isKLAMMER(str)) token.setKlammer(str);else            
            {
               setErrorMessage("es ist ein unbekannter Typ vorhanden!");
            }
            
            result.add(token);            
        }    
        return result;
      }
    
    
    private double calc(ArrayList vector)
    {
        double num=0.0;
        
        while (pointer<vector.size())
        {
            Token token= (Token)vector.get(pointer++);                        
            
            if (token.isNum || token.isVar) num=token.num; else                            
            if (token.isKlammer)
            {
                if (token.klammer.equals("(")) num=calc(vector);else
                if (token.klammer.equals(")")) return num;
            }else
            if (token.isOp)
            {
                if (token.op.equals("%")) return num%calc(vector);else
                if (token.op.equals("^")) return Math.pow(num,calc(vector));else
                if (token.op.equals("+")) return num+calc(vector);else
                if (token.op.equals("-")) return num-calc(vector);else
                if (token.op.equals("/")) return num/calc(vector);else
                if (token.op.equals("*")) return num*calc(vector);
            }else
            if (token.isFunc)
            {
                if (token.func.equalsIgnoreCase("sin")) return Math.sin(calc(vector));else
                if (token.func.equalsIgnoreCase("cos")) return Math.cos(calc(vector));else
                if (token.func.equalsIgnoreCase("tan")) return Math.tan(calc(vector));else
                    
                if (token.func.equalsIgnoreCase("asin")) return Math.asin(calc(vector));else
                if (token.func.equalsIgnoreCase("acos")) return Math.acos(calc(vector));else
                if (token.func.equalsIgnoreCase("atan")) return Math.atan(calc(vector));

                if (token.func.equalsIgnoreCase("sinh")) return Math.sinh(calc(vector));else
                if (token.func.equalsIgnoreCase("cosh")) return Math.cosh(calc(vector));else
                if (token.func.equalsIgnoreCase("tanh")) return Math.tanh(calc(vector));else
                    
                if (token.func.equalsIgnoreCase("abs")) return Math.abs(calc(vector));else
                if (token.func.equalsIgnoreCase("log")) return Math.log10(calc(vector));else
                if (token.func.equalsIgnoreCase("ln")) return Math.log(calc(vector));else
                if (token.func.equalsIgnoreCase("exp")) return Math.exp(calc(vector));else
                
                if (token.func.equalsIgnoreCase("sqrt")) return Math.sqrt(calc(vector));else
                if (token.func.equalsIgnoreCase("round")) return Math.round(calc(vector));else
                if (token.func.equalsIgnoreCase("fak")) return fakultaet(calc(vector));else
                if (token.func.equalsIgnoreCase("toDeg")) return Math.toDegrees(calc(vector));else
                if (token.func.equalsIgnoreCase("toRad")) return Math.toRadians(calc(vector));                
            }
        }
        
        return num;
    }
                
    private int sucheRechtsNachKlammer(ArrayList tokenListe, int pos)
    {
        int c=0;
        for (int i=pos;i<tokenListe.size();i++)
        {
           String str=(String)tokenListe.get(i); 
           
           if (str.equals("(")) c++;
           if (str.equals(")")) c--;
           
           if (c==0)
           {
             return i;   
           }
        }
        return -1;
                
    }            
    
    private int sucheLinksNachKlammer(ArrayList tokenListe, int pos)
    {
        int c=0;
        for (int i=pos;i>=0;i--)
        {
           String str=(String)tokenListe.get(i); 
           
           if (str.equals(")")) c++;
           if (str.equals("(")) c--;
           
           if (c==0)
           {
             return i;   
           }
        }
        return -1;
    }
    
    
    /* liefert die anzahl der zuviele oder zu wenige Klammern
     * zb: wenn eine Klammer nicht geschlossen oder nicht geöffnet
     * worden ist
     * Liefert bei zu viel geöffneten Klammern > 0
     * Liefert bei zu viel geschlossenen Klammern < 0
     */
    private int getKlammerPlus(ArrayList tokenListe)
    {
        int c=0;
        for (int i=0;i<tokenListe.size();i++)
        {
           String str=(String)tokenListe.get(i);
           
           if (str.equals("(")) c++;
           if (str.equals(")")) c--;           
        }
        return c;
    }
        
    
    // bei -5+1     -> 0-5+1
    // bei -5+(-1)  -> 0-5+(0-1)
    private void handleMinusOperator(ArrayList tokenListe)
    {
       
        int i=0;
        
        while(i<tokenListe.size()-1)
        {
            String str=(String)tokenListe.get(i);
            String str2=(String)tokenListe.get(i+1);

            
            if (str.equals("(") && str2.equals("-"))
            {
                tokenListe.add(i+1,"0");
                i++;
            }
            if (str.equals("-"))
            {
                if (i==0)
                {
                    tokenListe.add(0,"0");
                    i++;
                }                                
            }
           i++;
        }
    }
                
    
    
    private void klammere(ArrayList tokenListe, int ops)
    {
       
        int i=0;
        
        while(i<tokenListe.size())
        {
            String str=(String)tokenListe.get(i);
            
            if (isFunc(str))
            {
                  int resR=sucheRechtsNachKlammer(tokenListe,i+1);
                  if (resR>-1)
                  {
                    tokenListe.add(i,"(");
                    tokenListe.add(resR+1,")");                        
                    i++;
                  }                      
                
            }else
            {
                boolean oki=false;                
                if (ops==0 && ( str.equals("*") || str.equals("/") || str.equals("^") || str.equals("%") ) )  oki=true; 
                if (ops==1 && (str.equals("+") || str.equals("-"))) oki=true;
                
                if (oki==true && i>=1)
                {
                    String strA=(String)tokenListe.get(i-1);
                    String strB=(String)tokenListe.get(i+1);

                    if (isNum(strA) || strA.equals(")") || isString(strA))
                    {
                        int resL=sucheLinksNachKlammer(tokenListe,i-1);
                        if (resL>-1)
                        { 
                          int resR=0;
                          if (isFunc(strB))
                          {
                            resR=sucheRechtsNachKlammer(tokenListe,i+2);
                          } else resR=sucheRechtsNachKlammer(tokenListe,i+1);

                          if (resR>-1)
                          {
                            tokenListe.add(resL,"(");
                            tokenListe.add(resR+2,")");                        
                            i++;
                          }                      
                        }
                    }
                }
            }
            i++;
        }
        
        
    }
    
    
    private void setVariablen(ArrayList tokenListe)
    {
      for (int i=0;i<tokenListe.size();i++)
      {
          Token token=(Token)tokenListe.get(i);
          
          if (token.isVar)
          {
            if (varExist(token.str))
            {
              double value=getVarValue(token.str);
              token.num=value;
            } else setErrorMessage("Variable "+token.str+" existiert nicht!");

          }
      }
    }
    


    
    private void tokensToVector(ArrayList vector)
    {
        while ( tokenizer.hasMoreTokens() )
        {            
            String str=tokenizer.nextToken();  
            

            if (str.equalsIgnoreCase("E")) str=""+Math.E; 
            if (str.equalsIgnoreCase("PI")) str=""+Math.PI; 
            
            if (str!=" ") vector.add(str);
        }
    }
    
    
    public void clearVars()
    {
        vars.clear();
    }
    
    public void addVar(String name, double value)
    {
        Var var= new Var(name,value);
        vars.add(var);
    }
    
    public Var getVar(int index)
    {
        return (Var)vars.get(index);
    }
    
    public double getVarValue(String name)
    {
        for (int i=0;i<vars.size();i++)
        {
            Var var=getVar(i);
            if (var.name.equalsIgnoreCase(name))
            {
                return var.value;
            }
        }
        return 0;
    }


    public boolean varExist(String name)
    {
        for (int i=0;i<vars.size();i++)
        {
            Var var=getVar(i);
            if (var.name.equalsIgnoreCase(name))
            {
                return true;
            }
        }
        return false;
    }

    public double parseString()
    {        
        ArrayList vector = new ArrayList(optVector);                
        setVariablen(vector);
        pointer=0;
        return calc(vector);
    }
    
    private boolean isNum(String val)
    {
        try
        {
            double x=Double.parseDouble(val);
            return true;
        }catch(Exception ex)
        {            
            return false;
        }
        
    }
    
    
    public String getTokens()
    {
        String res="";
        for (int i=0;i<mainVector.size();i++)
        {
            String str=(String)mainVector.get(i);
            res+=str;
        }
        return res;        
    }
    
    private boolean isFunc(String val)
    {
        if (val.equalsIgnoreCase("sin")  || 
            val.equalsIgnoreCase("cos")  || 
            val.equalsIgnoreCase("tan")  ||
            val.equalsIgnoreCase("asin") || 
            val.equalsIgnoreCase("acos") || 
            val.equalsIgnoreCase("atan") ||
            val.equalsIgnoreCase("sinh") || 
            val.equalsIgnoreCase("cosh") || 
            val.equalsIgnoreCase("tanh") ||
            val.equalsIgnoreCase("abs")  || 
            val.equalsIgnoreCase("log")  || 
            val.equalsIgnoreCase("ln")   ||
            val.equalsIgnoreCase("exp")  || 
            val.equalsIgnoreCase("sqrt") || 
            val.equalsIgnoreCase("round")||
            val.equalsIgnoreCase("fak")  ||
            val.equalsIgnoreCase("toDeg")|| 
            val.equalsIgnoreCase("toRad") )
        {
            return true;
        } else return false;
    }
    
    private boolean isIn(String a, String set)
    {
        if (a.length()>0) return set.indexOf(a)>=0;
        return false;
    }
    
    private boolean isOP(String val)
    {
      return isIn(val,OPERATOREN);
    }
    
    private boolean isKLAMMER(String val)
    {
      return isIn(val,KLAMMER);
    }    
    
    private boolean isString(String val)
    {
      for(int i=0;i<val.length();i++)
      {
          if (isIn(val,ALPHA)==false) return false;
      }
      return true;
    }    
    
    
}
