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
import VisualLogic.Basis;
import VisualLogic.variables.VSFlowInfo;
import java.util.ArrayList;
import java.util.StringTokenizer;


class Token
{
    public boolean isNum=false;
    public boolean isOp=false;
    public boolean isFunc=false;
    public boolean isKlammer=false;
    public boolean isBoolean=false;
    public boolean isVar=false;
    public boolean isString=false;
        
    public Object value;
    public String func;
    public String op="";    
    public String klammer;    
    public String varName;
    
    public void setValue(Object value)
    {
        this.value=value;
        isNum=true;
    }        
    public void setOp(String op)
    {
        this.op=op;
        isOp=true;
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
    private static final String OPERATOREN="<>+-*/^%=&|!";    
    private static final String KLAMMER="()";
    private StringTokenizer tokenizer;
    private ArrayList mainVector = new ArrayList();
    private ArrayList optVector;
    private int pointer=0;
    private ArrayList varsGlobal = new ArrayList();
    private ArrayList varsLocal = new ArrayList();
    private String errorMessage="";
    public String resultVariable="";
    private static final int C_DOUBLE = 1;
    private static final int C_BOOLEAN = 2;
    private static final int C_STRING = 3;
    private int modus = C_DOUBLE;
    public boolean gleichheitsZeichenGefunden=false;
    
       
    private Basis basis=null;
    public VSFlowInfo flowInfo ;
    
    private boolean isIn(String ch, String set)
    {
       String cha;
       for (int i=0;i<set.length();i++)
       {
         cha=set.substring(i,i+1);
         
         if (cha.equals(ch))
         {
           return true;
         }
       }
       return false;
    }
    
    /*private boolean isVariable(String val)
    {
      String ch;
      for(int i=0;i<val.length();i++)
      {
         ch=val.substring(i,i+1);
         if (isIn(ch,ALPHA)==false) return false;          
      }
      return true;
    } */   
    
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
    
    public boolean isMyString(String val)
    {
        val=val.trim();

        int i=0;
        String ch=val.substring(i,i+1);
        if (ch.equalsIgnoreCase("\""))
        {   
            i=val.length()-1;
            ch=val.substring(i,i+1);
            if (ch.equalsIgnoreCase("\""))
            {   
                return true;
            } else return false;            
        }else
        return false;
    } 
    
    public boolean isBoolean(String val)
    {
        val=val.trim();
        
        if (val.equalsIgnoreCase("TRUE") || val.equalsIgnoreCase("FALSE"))
        {
            return true;
        }
        return false;
    }
    
    
    public String getErrorMessage()
    {
      return errorMessage;
    }
    public void setErrorMessage(String message)
    {
      errorMessage+=message+"\n";
    }
    
    public void delErrorMessage()
    {
      errorMessage="";
    }
    
    private String entfernealleleerzeichen(String str)
    {
        String ch="";
        String result="";
        boolean inString=false;
        
        for (int i=0;i<str.length();i++)
        {  
          ch=str.substring(i,i+1);
          
          if (inString==false &&  ch.equalsIgnoreCase("\"")) 
          {
              inString=true;
          }else
          if (inString==true &&  ch.equalsIgnoreCase("\"")) 
          {
              inString=false;
          }
                  
          if (inString)
          {
              result+=ch;
          }else
          if (!ch.equalsIgnoreCase(" ") )
          {
              result+=ch;
          }
        }
        
        return result;
    }
    public void setExpression(String expression)
    {                  
        expression=entfernealleleerzeichen(expression);
        //tokenizer = new StringTokenizer( expression ,"=<>+-*/()%^&|!",true);
        pointer=0;
        mainVector.clear();
        tokensToVector(expression, mainVector);
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
    
    public Parser(Basis basis)
    {
        this.basis=basis;
      //setExpression(expression);
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
            if (isMyString(str))
            {
                token.isString=true;
                token.setValue(new String(str.substring(1,str.length()-1)));
            }else
            if (isBoolean(str)) 
            {
                boolean x=Boolean.valueOf(str);
                token.setValue(new Boolean(x));
            }else                       
            if (isNum(str)) 
            {
                double x =Double.parseDouble(str);
                token.setValue(new Double(x));
            }else
            if (isOP(str)) token.setOp(str);else
            if (isFunc(str)) token.setFunc(str);else
            if (isString(str)) 
            {
                Object o=getVarValue(str);
                if (o!=null)
                {
                    token.setValue(o);
                    token.isVar=true;
                    token.varName=str;
                }else
                {
                    setErrorMessage("Variable "+str+" is unknown!");
                }
                //token.setVar(str);
            }else
            if (isKLAMMER(str)) token.setKlammer(str);else            
            {
               setErrorMessage("There is an unknown datatype : "+str);
            }
            
            result.add(token);            
        }    
        return result;
      }
    
    String lastVar="";
    
    
    private String calcString(ArrayList vector)
    {
        String value="";
        
        while (pointer<vector.size())
        {
            Token token= (Token)vector.get(pointer++);                        
            
            if (token.value instanceof String)
            {
                value=(String)token.value;
                if (token.isVar)
                {
                    lastVar=token.varName;
                }
            }else
            if (token.isKlammer)
            {
                if (token.klammer.equals("(")) value=calcString(vector);else
                if (token.klammer.equals(")")) return value;
            }else
            if (token.isOp)
            {
                if (token.op.equals("=")) 
                {                    
                    OpenVariable v=getVarByName(lastVar);  
                    if (v!=null)
                    {
                        if (v.global==false)
                        {
                          flowInfo.setVariable(lastVar,calcString(vector)); 
                        }else
                        {
                          basis.vsSetVar(lastVar,calcString(vector));    
                        }
                        
                    } else
                    {
                        setErrorMessage("Variable "+lastVar+" not exist!");
                    }                    
                    
                }else
                if (token.op.equals("+")) return value + calcString(vector); else
                {
                    setErrorMessage("Operator "+token.op+" not found!");
                }
            }else
            {
                setErrorMessage(token.varName+" not found!");
            }            
        }
        
        return value;
    }
            
            
            
    private boolean calcBoolean(ArrayList vector)
    {
        boolean value=false;
        
        while (pointer<vector.size())
        {
            Token token= (Token)vector.get(pointer++);                        
            
            if (token.value instanceof Boolean)
            {
                value=((Boolean)token.value).booleanValue();            
                if (token.isVar)
                {
                    lastVar=token.varName;
                }
            }else
            if (token.isKlammer)
            {
                if (token.klammer.equals("(")) value=calcBoolean(vector);else
                if (token.klammer.equals(")")) return value;
            }else
            if (token.isOp)
            {
                if (token.op.equals("=")) 
                {                   
                    
                    OpenVariable v=getVarByName(lastVar);  
                    if (v!=null)
                    {
                        if (v.global==false)
                        {
                          flowInfo.setVariable(lastVar,calcBoolean(vector)); 
                        }else
                        {
                          basis.vsSetVar(lastVar,calcBoolean(vector));    
                        }
                    }else
                    {
                        setErrorMessage("Variable "+lastVar+" not exist!");                        
                    }
             
                }else
                if (token.op.equals("&")) return value && calcBoolean(vector);else
                if (token.op.equals("=")) 
                {                    
                    return (value==calcBoolean(vector));
                }else                    
                if (token.op.equals("|")) return value || calcBoolean(vector);else
                if (token.op.equals("!")) return !calcBoolean(vector); else
                {
                    setErrorMessage("Operator "+token.varName+" not found!");
                }
            }else
            {
                setErrorMessage(token.varName+" not found!");
            }            
        }
        
        return value;
    }
    
    
    
    private double calcDouble(ArrayList vector)
    {
        double num=0.0;
        
        while (pointer<vector.size())
        {
            Token token= (Token)vector.get(pointer++);                        
            
            if (token.isNum) 
            {
                if (token.value instanceof Double)
                {
                    num=((Double)token.value).doubleValue();                    
                    if (token.isVar)
                    {
                        lastVar=token.varName;
                    }                    
                }                
            }else
            if (token.isKlammer)
            {
                if (token.klammer.equals("(")) num=calcDouble(vector);else
                if (token.klammer.equals(")")) return num;
            }else
            if (token.isOp)
            {
                if (token.op.equals("=")) 
                {                    
                    OpenVariable v=getVarByName(lastVar);  
                    if (v!=null)
                    {
                        if (v.global==false)
                        {
                          flowInfo.setVariable(lastVar,calcDouble(vector)); 
                        }else
                        {
                          basis.vsSetVar(lastVar,calcDouble(vector));    
                        }
                        
                    } else
                    {
                        setErrorMessage("Variable "+lastVar+" not exist!");
                    }                    
                }else                
                if (token.op.equals("%")) return num%calcDouble(vector);else
                if (token.op.equals("^")) return Math.pow(num,calcDouble(vector));else
                if (token.op.equals("+")) return num+calcDouble(vector);else
                if (token.op.equals("-")) return num-calcDouble(vector);else
                if (token.op.equals("/")) return num/calcDouble(vector);else
                if (token.op.equals("*")) return num*calcDouble(vector);
                
            }else
            if (token.isFunc)
            {
                if (token.func.equalsIgnoreCase("sin")) return Math.sin(calcDouble(vector));else
                if (token.func.equalsIgnoreCase("cos")) return Math.cos(calcDouble(vector));else
                if (token.func.equalsIgnoreCase("tan")) return Math.tan(calcDouble(vector));else
                    
                if (token.func.equalsIgnoreCase("asin")) return Math.asin(calcDouble(vector));else
                if (token.func.equalsIgnoreCase("acos")) return Math.acos(calcDouble(vector));else
                if (token.func.equalsIgnoreCase("atan")) return Math.atan(calcDouble(vector));

                if (token.func.equalsIgnoreCase("sinh")) return Math.sinh(calcDouble(vector));else
                if (token.func.equalsIgnoreCase("cosh")) return Math.cosh(calcDouble(vector));else
                if (token.func.equalsIgnoreCase("tanh")) return Math.tanh(calcDouble(vector));else
                    
                if (token.func.equalsIgnoreCase("abs")) return Math.abs(calcDouble(vector));else
                if (token.func.equalsIgnoreCase("log")) return Math.log10(calcDouble(vector));else
                if (token.func.equalsIgnoreCase("ln")) return Math.log(calcDouble(vector));else
                if (token.func.equalsIgnoreCase("exp")) return Math.exp(calcDouble(vector));else
                
                if (token.func.equalsIgnoreCase("sqrt")) return Math.sqrt(calcDouble(vector));else
                if (token.func.equalsIgnoreCase("round")) return Math.round(calcDouble(vector));else
                if (token.func.equalsIgnoreCase("fak")) return fakultaet(calcDouble(vector));else
                if (token.func.equalsIgnoreCase("toDeg")) return Math.toDegrees(calcDouble(vector));else
                if (token.func.equalsIgnoreCase("toRad")) return Math.toRadians(calcDouble(vector)); else
                {
                    setErrorMessage("Function "+token.varName+" not found!");
                }
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
                
    
    public void print()
    {
        int i=0;
        System.out.println("--------------------------");
        while(i<mainVector.size())
        {
            String str=(String)mainVector.get(i);
            System.out.print(str+",");
            i++;
        }
        System.out.println();
        System.out.println("--------------------------");        
    }
    
    private void klammere(ArrayList tokenListe, int ops)
    {
       boolean inKlammer=false;
       int i=0;
        
        while(i<tokenListe.size())
        {
            String str=(String)tokenListe.get(i);
            
            if (inKlammer==false && str.equalsIgnoreCase("\"")) inKlammer=true;else
            if (inKlammer==true && str.equalsIgnoreCase("\"")) inKlammer=false;
            
            if (!inKlammer)
            {
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
            if (varExistLocal(token.varName))
            {
              Object value=getVarValue(token.varName);
              token.value=value;
              OpenVariable v = getVarByName(token.varName);
              v.global=false;              
            } else
            if (varExistGlobal(token.varName))
            {
              Object value=getVarValue(token.varName);
              token.value=value;
              OpenVariable v = getVarByName(token.varName);
              v.global=true;

            } else setErrorMessage("Variable "+token.varName+" not found!");
                
          }
      }
    }
    
    int counter=0;
    boolean eof=false;
    String expr;

    public boolean hasMoreTokens()
    {
      return eof;
    }
    
    public String nextToken()
    {
        boolean inString=false;
        int start=counter;
        String ch;        
        while(counter<expr.length())
        {
          ch = expr.substring(counter,counter+1);
                    
          if (inString==false && ch.equalsIgnoreCase("\""))
          {
              inString=true;
              start=counter;
          }else
          if (inString==true && ch.equalsIgnoreCase("\""))
          {
              inString=false;
              counter++;
              return expr.substring(start,counter);
          }
          
          if (inString==false)
          {              
              if (isOP(ch) || isKLAMMER(ch))
              {   
                  if (counter-start==0)
                  {
                      counter++;
                      return expr.substring(start,counter);
                  } else
                  {
                    //counter--;
                    return expr.substring(start,counter);
                  }                  
              }
          }
          
          counter++;
        }
        
        if (inString) setErrorMessage("\"(\" not closed!");
        eof=false;
        return expr.substring(start,counter);
    }

    
    private void tokensToVector(String expression, ArrayList vector)
    {
 
        counter=0;
        expr=expression;
        eof=true;
        int len=expr.length();
        while (counter<len )               
        {            
            String str=nextToken();  
            
            /*if (str.equalsIgnoreCase("E")) str=""+Math.E; 
            if (str.equalsIgnoreCase("PI")) str=""+Math.PI; */
            
            if (str!=" ") vector.add(str);
        }
    }
    
    public void setGlobalVariables(ArrayList variablen)
    {
        this.varsGlobal=variablen;
    }
    
    public void setLocalVariables(ArrayList variablen)
    {
        this.varsLocal=variablen;
    }
    
    
    public void clearVars()
    {
        varsGlobal.clear();
    }
    
    public void addVar(String name, Object value)
    {
        OpenVariable var= new OpenVariable(name,value);
        varsGlobal.add(var);
    }
    
    public OpenVariable getLocalVar(int index)
    {
        return (OpenVariable)varsLocal.get(index);
    }
    
    public OpenVariable getGlobaVar(int index)
    {
        return (OpenVariable)varsGlobal.get(index);
    }
    
    
    public OpenVariable getVarByName(String name)
    {
        for (int i=0;i<varsLocal.size();i++)
        {
            OpenVariable var=getLocalVar(i);
            if (var.name.equalsIgnoreCase(name))
            {
                return var;
            }
        }
        for (int i=0;i<varsGlobal.size();i++)
        {
            OpenVariable var=getGlobaVar(i);
            if (var.name.equalsIgnoreCase(name))
            {
                return var;
            }
        }
        return null;
    }
    
    public Object getVarValue(String name)
    {
        OpenVariable v = getVarByName(name);
        if (v!=null)
        {
            return v.value;
        } else return null;
    }


    public boolean varExistGlobal(String name)
    {
        for (int i=0;i<varsGlobal.size();i++)
        {
            OpenVariable var=getGlobaVar(i);
            if (var.name.equalsIgnoreCase(name))
            {
                return true;
            }
        }
        return false;
    }

    public boolean varExistLocal(String name)
    {
        for (int i=0;i<varsLocal.size();i++)
        {
            OpenVariable var=getLocalVar(i);
            if (var.name.equalsIgnoreCase(name))
            {
                return true;
            }
        }
        return false;
    }
    
    
    
    // liefert 0 für Double
    // liefert 1 für Boolean
    // liefert 2 für String
    // liefert -1 für unbekannt oder gemixt!
    private int getToParseType(ArrayList vector)
    {
        int result=-1;
        for (int i=0;i<vector.size();i++)
        {
            Token token= (Token)vector.get(i);
        
            if (token.value instanceof Double)
            {
                if (result==0 || result==-1) result=0;
                else return -1;
            }else
            if (token.value instanceof Boolean)
            {
                if (result==1 || result==-1) result=1;
                else return -1;
            }else
            if (token.value instanceof String)
            {
                if (result==2 || result==-1) result=2;
                else return -1;
            }
        }
        
        return result;
    }
    

    public Object parseString(String expr)
    {        
        setExpression(expr);
        lastVar="";
        ArrayList vector = new ArrayList(optVector);
        setVariablen(vector);
        pointer=0;
        
        int type=getToParseType(vector);
        if (type>-1)
        {
            switch (type)
            {
                case 0 : return new Double(calcDouble(vector)); 
                case 1 : return new Boolean(calcBoolean(vector));
                case 2 : return new String(calcString(vector));
            }
            
        }else
        {
            setErrorMessage("you cannot mix Double, Boolean and String");
        }
        
        return null;
        
    }
    
    public boolean parseBoolean()
    {        
        ArrayList vector = new ArrayList(optVector);
        setVariablen(vector);
        pointer=0;
        return calcBoolean(vector);        
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
      String ch="";
      for(int i=0;i<val.length();i++)
      {
          ch=val.substring(i,i+1);
          if (isIn(ch,ALPHA)==false) return false;
      }
      return true;
    }  
    
    
}
