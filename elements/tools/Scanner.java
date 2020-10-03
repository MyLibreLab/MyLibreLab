
package tools;
import VisualLogic.*;
import VisualLogic.variables.*;

import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.io.ObjectOutputStream;
import java.io.StreamTokenizer;
import java.util.ArrayList;
  
  
 public class Scanner extends StreamTokenizer implements Expression.yyInput
  {
    ExternalIF element;
    
    public Scanner (ExternalIF element, Reader r)
    {
      super(r);
      this.element=element;
      
      resetSyntax();
      commentChar('#');            // comments from # to end-of-line
      wordChars('0', '9');         // parse decimal numbers as words
      wordChars('a', 'z');         // Var Names
      wordChars('A', 'Z');         // Var Names
      wordChars('.', '.');         // Decimal Point for Real
      whitespaceChars(0, ' ');     // ignore control-* and space

      eolIsSignificant(true);      // need '\n'
    }
    /** moves to next input token.
        Consumes end of line and pretends (once) that it is end of file.
        @return false at end of file and once at each end of line.
      */
    public boolean advance () throws IOException
    {
      if (ttype != TT_EOF) nextToken();
      return ttype != TT_EOF && ttype != TT_EOL;
    }
    

    //Überprüft ob der String eine Zahl von -32... + 32... (16 Bit)
    public boolean isConst(String value)
    {
      try
      {
        int num=Integer.valueOf(value);
        if (num>=-32768 && num<= 32767)
        {
          return true;
        }
      } catch(Exception ex) {}
      return false;
    }

    // Überprüft ob der String als Variable definiert worden ist
    public boolean isVariable(String value)
    {
      VSBasisIF basis=element.jGetBasis();

      if (basis!=null)
      {
        int varDT=basis.vsGetVariableDT(value);
        if (varDT>-1)
        {
          return true;
        }
      }
      // oder auch nicht!
      return false;
    }
    
    
    /** determines current input, sets value to String for Int and Real.
        @return Int, Real or token's character value.
      */
    public int token ()
    {
      switch (ttype) {
      case TT_EOL:    System.out.println("EOL ");
      case TT_EOF:    assert true;   // should not happen
      case TT_WORD:
      {
           value = sval;

           //System.out.println("VALUE="+sval);

           /*try
           {
             int c=Integer.valueOf(sval);

             //if (c>=0 && c<=255) return BYTE; else
             //if (c>255 && c<=65535) return WORD; else

             if (c>=-32768 && c<=32767) return Expression.WORD; else
              System.out.println("Error: Integer excepted ");

             //return sval.indexOf(".") < 0 ? Int : Real;
           } catch(Exception ex)
           {
           }*/
           
           if (isConst(sval)) return Expression.WORD;
           if (isVariable(sval)) return Expression.Variable;else
           {
             element.jShowMessage("not a valid expresssion!");
           }

           /*if (sval.equalsIgnoreCase("LCDPRINTNUM")) return LCD_PRINT_NUM;

           if (sval.equalsIgnoreCase("DIM")) return DIM;
           if (sval.equalsIgnoreCase("BYTE")) return RBYTE;
           if (sval.equalsIgnoreCase("WORD")) return RWORD;

           if (sval.equalsIgnoreCase("IF")) return IF;
           if (sval.equalsIgnoreCase("THEN")) return THEN;


           if (sval.equalsIgnoreCase("SIN")) return SIN;
           if (sval.equalsIgnoreCase("COS")) return COS;
           if (sval.equalsIgnoreCase("TAN")) return TAN;

           if (sval.equalsIgnoreCase("ASIN")) return ASIN;
           if (sval.equalsIgnoreCase("ACOS")) return ACOS;
           if (sval.equalsIgnoreCase("ATAN")) return ATAN;*/


           return Expression.Variable;

      }
      default:          value = null;
                        return ttype;
      }
    }
    /** value associated with current input.
      */
    protected Object value;
    /** produces value associated with current input.
        @return value.
      */
    public Object value ()
    {
      return value;
    }
  }

