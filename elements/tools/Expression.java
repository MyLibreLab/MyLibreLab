// created by jay 1.1.0 (c) 2002-2006 ats@cs.rit.edu
// skeleton Java 1.1.0 (c) 2002-2006 ats@cs.rit.edu

					// line 2 "Expression.jay"


  package tools;

  import java.io.InputStreamReader;
  import java.io.IOException;
  import java.io.Reader;
  import java.io.ObjectOutputStream;
  import java.io.StreamTokenizer;
  import java.util.ArrayList;

  public class Expression
  {
    public static ArrayList code = new ArrayList();

    /*public static void main (String args []) throws Exception
    {
      Expression parser = new Expression();
      Scanner scanner = new Scanner(new java.io.StringReader("10<20"));
      while (scanner.ttype != scanner.TT_EOF)
      try
      {
        parser.yyparse(scanner, null);

        for (int i=0;i<liste.size();i++)
        {
          System.err.println(""+liste.get(i));
        }
        break;

      }catch (Exception ye)
      {
        System.err.println(scanner+": "+ye);
      }
    } */


					// line 43 "-"
  // %token constants
  public static final int BYTE = 257;
  public static final int WORD = 258;
  public static final int Real = 259;
  public static final int Variable = 260;
  public static final int SIN = 261;
  public static final int COS = 262;
  public static final int TAN = 263;
  public static final int ASIN = 264;
  public static final int ACOS = 265;
  public static final int ATAN = 266;
  public static final int IF = 267;
  public static final int THEN = 268;
  public static final int DIM = 269;
  public static final int RBYTE = 270;
  public static final int RWORD = 271;
  public static final int LCD_PRINT_NUM = 272;
  public static final int UNARY = 273;
  public static final int condition = 274;
  public static final int yyErrorCode = 256;

  /** number of final state.
    */
  protected static final int yyFinal = 9;

  /** parser tables.
      Order is mandated by <i>jay</i>.
    */
  protected static final short[] yyLhs = {
//yyLhs 29
    -1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     2,     2,     3,     3,     0,     0,     0,
    }, yyLen = {
//yyLen 29
     2,     3,     3,     3,     3,     3,     3,     3,     3,     2,
     2,     2,     2,     3,     1,     1,     3,     3,     4,     4,
     4,     4,     3,     3,     1,     1,     1,     1,     4,
    }, yyDefRed = {
//yyDefRed 57
     0,    14,     0,     0,     0,     0,     0,     0,     0,     0,
     0,    27,     0,     0,    15,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,    22,     0,     0,    13,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
    24,    25,    28,     0,     0,     0,     0,
    }, yyDgoto = {
//yyDgoto 4
     9,    10,    11,    52,
    }, yySindex = {
//yySindex 57
   -32,     0,   -59,  -240,   -10,   -10,   -10,   -10,   -10,     0,
    45,     0,   -33,   -20,     0,    84,    84,    45,    45,    19,
   -10,   -10,   -10,   -10,   -10,   -10,   -10,   -10,   -35,   -24,
   -18,   -30,     0,    45,  -266,     0,    62,    62,    84,    84,
    84,    45,    45,    45,   -10,   -10,    45,   -10,    45,   -10,
     0,     0,     0,    45,    45,    45,    45,
    }, yyRindex = {
//yyRindex 57
     0,     0,     3,     0,     0,     0,     0,     0,     0,     0,
    34,     0,     0,     0,     0,    29,    91,    14,    17,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,    38,     0,     0,     6,    32,   110,   120,
   130,    18,    28,    44,     0,     0,    96,     0,   101,     0,
     0,     0,     0,   118,   119,   125,   135,
    }, yyGindex = {
//yyGindex 4
     0,   175,     0,     0,
    }, yyTable = {
//yyTable 251
     6,     6,    12,    15,    50,    51,     1,     8,     8,     6,
     4,     4,     5,     5,     9,     6,     8,    10,     5,     4,
    13,     5,     8,     6,    34,     4,    44,     5,     7,    11,
     8,    49,     2,     4,    26,     5,    15,    45,    23,     0,
    15,    15,     0,    47,     8,    15,    15,     1,    15,     1,
    15,     1,    28,     0,     0,     9,    25,    22,    10,     5,
    35,    23,    20,    15,    21,    15,    24,    11,     0,     7,
    11,    11,    11,     2,    11,     2,    11,     2,    28,    29,
    31,    30,    25,    22,     0,     8,     0,    23,    20,     0,
    21,    12,    24,     7,     7,    28,    16,    15,     0,    25,
    22,    17,     7,     0,    23,    29,    31,    30,     7,    24,
     6,     0,     0,    27,     0,     0,     7,    28,    21,    18,
     3,    25,    29,    31,    30,    19,     0,    15,     0,    12,
     4,     0,    12,    12,    12,    20,    12,    16,    12,    27,
     0,     0,    17,    26,    29,    31,    30,     0,     6,     0,
     0,     6,     6,     6,     0,     6,    27,     6,     3,    21,
    18,     3,     3,     3,     0,     3,    19,     3,     4,    26,
     0,     4,     4,     4,     0,     4,    20,     4,    27,    15,
    16,    17,    18,    19,     0,     0,    26,    33,     0,     0,
     0,     0,     0,     0,     0,    36,    37,    38,    39,    40,
    41,    42,    43,     0,    46,    48,     0,     0,    26,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,    53,
    54,     0,    55,     0,    56,     1,     1,    14,     2,     0,
     0,     0,     0,     0,     1,     0,    14,     3,     0,     0,
     1,    32,    14,     0,     0,     0,     0,     0,     1,     0,
    14,
    }, yyCheck = {
//yyCheck 251
    33,    33,    61,     0,   270,   271,     0,    40,    40,    33,
    43,    43,    45,    45,     0,    33,    40,     0,     0,    43,
   260,    45,    40,    33,    44,    43,    61,    45,     0,     0,
    40,    61,     0,    43,     0,    45,    33,    61,     0,    -1,
    37,    38,    -1,    61,     0,    42,    43,    41,    45,    43,
    47,    45,    33,    -1,    -1,    41,    37,    38,    41,    41,
    41,    42,    43,    60,    45,    62,    47,    38,    -1,    41,
    41,    42,    43,    41,    45,    43,    47,    45,    33,    60,
    61,    62,    37,    38,    -1,    41,    -1,    42,    43,    -1,
    45,     0,    47,   126,   126,    33,     0,    94,    -1,    37,
    38,     0,   126,    -1,    42,    60,    61,    62,   126,    47,
     0,    -1,    -1,    94,    -1,    -1,   126,    33,     0,     0,
     0,    37,    60,    61,    62,     0,    -1,   124,    -1,    38,
     0,    -1,    41,    42,    43,     0,    45,    41,    47,    94,
    -1,    -1,    41,   124,    60,    61,    62,    -1,    38,    -1,
    -1,    41,    42,    43,    -1,    45,    94,    47,    38,    41,
    41,    41,    42,    43,    -1,    45,    41,    47,    38,   124,
    -1,    41,    42,    43,    -1,    45,    41,    47,    94,     4,
     5,     6,     7,     8,    -1,    -1,   124,    12,    -1,    -1,
    -1,    -1,    -1,    -1,    -1,    20,    21,    22,    23,    24,
    25,    26,    27,    -1,    29,    30,    -1,    -1,   124,    -1,
    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    44,
    45,    -1,    47,    -1,    49,   258,   258,   260,   260,    -1,
    -1,    -1,    -1,    -1,   258,    -1,   260,   269,    -1,    -1,
   258,   274,   260,    -1,    -1,    -1,    -1,    -1,   258,    -1,
   260,
    };

  /** maps symbol value to printable name.
      @see #yyExpecting
    */
  protected static final String[] yyNames = {
    "end-of-file",null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,"'!'",null,null,null,"'%'","'&'",
    null,"'('","')'","'*'","'+'","','","'-'",null,"'/'",null,null,null,
    null,null,null,null,null,null,null,null,null,"'<'","'='","'>'",null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,"'^'",null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,"'|'",null,"'~'",null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,null,
    null,null,null,null,null,null,null,null,null,null,null,null,null,
    "BYTE","WORD","Real","Variable","SIN","COS","TAN","ASIN","ACOS",
    "ATAN","IF","THEN","DIM","RBYTE","RWORD","LCD_PRINT_NUM","UNARY",
    "condition",
    };

//t  /** printable rules for debugging.
//t    */
//t  protected static final String [] yyRule = {
//t    "$accept : prog",
//t    "expr : expr '+' expr",
//t    "expr : expr '-' expr",
//t    "expr : expr '*' expr",
//t    "expr : expr '/' expr",
//t    "expr : expr '%' expr",
//t    "expr : expr '&' expr",
//t    "expr : expr '|' expr",
//t    "expr : expr '^' expr",
//t    "expr : '!' expr",
//t    "expr : '~' expr",
//t    "expr : '+' expr",
//t    "expr : '-' expr",
//t    "expr : '(' expr ')'",
//t    "expr : WORD",
//t    "expr : Variable",
//t    "expr : expr '<' expr",
//t    "expr : expr '>' expr",
//t    "expr : expr '<' '=' expr",
//t    "expr : expr '>' '=' expr",
//t    "expr : expr '=' '=' expr",
//t    "expr : expr '!' '=' expr",
//t    "statement : Variable '=' condition",
//t    "statement : Variable '=' expr",
//t    "DataType : RBYTE",
//t    "DataType : RWORD",
//t    "prog : expr",
//t    "prog : statement",
//t    "prog : DIM Variable ',' DataType",
//t    };
//t
//t  /** debugging support, requires the package <tt>jay.yydebug</tt>.
//t      Set to <tt>null</tt> to suppress debugging messages.
//t    */
//t  protected jay.yydebug.yyDebug yydebug;
//t
//t  /** index-checked interface to {@link #yyNames}.
//t      @param token single character or <tt>%token</tt> value.
//t      @return token name or <tt>[illegal]</tt> or <tt>[unknown]</tt>.
//t    */
//t  public static final String yyName (int token) {
//t    if (token < 0 || token > yyNames.length) return "[illegal]";
//t    String name;
//t    if ((name = yyNames[token]) != null) return name;
//t    return "[unknown]";
//t  }
//t
  /** thrown for irrecoverable syntax errors and stack overflow.
      Nested for convenience, does not depend on parser class.
    */
  public static class yyException extends java.lang.Exception {
    public yyException (String message) {
      super(message);
    }
  }

  /** must be implemented by a scanner object to supply input to the parser.
      Nested for convenience, does not depend on parser class.
    */
  public interface yyInput {

    /** move on to next token.
        @return <tt>false</tt> if positioned beyond tokens.
        @throws IOException on input error.
      */
    boolean advance () throws java.io.IOException;

    /** classifies current token.
        Should not be called if {@link #advance()} returned <tt>false</tt>.
        @return current <tt>%token</tt> or single character.
      */
    int token ();

    /** associated with current token.
        Should not be called if {@link #advance()} returned <tt>false</tt>.
        @return value for {@link #token()}.
      */
    Object value ();
  }

  /** simplified error message.
      @see #yyerror(java.lang.String, java.lang.String[])
    */
  public void yyerror (String message) {
    yyerror(message, null);
  }

  /** (syntax) error message.
      Can be overwritten to control message format.
      @param message text to be displayed.
      @param expected list of acceptable tokens, if available.
    */
  public void yyerror (String message, String[] expected) {
    if (expected != null && expected.length > 0) {
      System.err.print(message+", expecting");
      for (int n = 0; n < expected.length; ++ n)
        System.err.print(" "+expected[n]);
      System.err.println();
    } else
      System.err.println(message);
  }

  /** computes list of expected tokens on error by tracing the tables.
      @param state for which to compute the list.
      @return list of token names.
    */
  protected String[] yyExpecting (int state) {
    int token, n, len = 0;
    boolean[] ok = new boolean[yyNames.length];

    if ((n = yySindex[state]) != 0)
      for (token = n < 0 ? -n : 0;
           token < yyNames.length && n+token < yyTable.length; ++ token)
        if (yyCheck[n+token] == token && !ok[token] && yyNames[token] != null) {
          ++ len;
          ok[token] = true;
        }
    if ((n = yyRindex[state]) != 0)
      for (token = n < 0 ? -n : 0;
           token < yyNames.length && n+token < yyTable.length; ++ token)
        if (yyCheck[n+token] == token && !ok[token] && yyNames[token] != null) {
          ++ len;
          ok[token] = true;
        }

    String result[] = new String[len];
    for (n = token = 0; n < len;  ++ token)
      if (ok[token]) result[n++] = yyNames[token];
    return result;
  }

  /** the generated parser, with debugging messages.
      Maintains a dynamic state and value stack.
      @param yyLex scanner.
      @param yydebug debug message writer implementing <tt>yyDebug</tt>, or <tt>null</tt>.
      @return result of the last reduction, if any.
      @throws yyException on irrecoverable parse error.
    */
  public Object yyparse (yyInput yyLex, Object yydebug)
				throws java.io.IOException, yyException {
//t    this.yydebug = (jay.yydebug.yyDebug)yydebug;
    return yyparse(yyLex);
  }

  /** initial size and increment of the state/value stack [default 256].
      This is not final so that it can be overwritten outside of invocations
      of {@link #yyparse}.
    */
  protected int yyMax;

  /** executed at the beginning of a reduce action.
      Used as <tt>$$ = yyDefault($1)</tt>, prior to the user-specified action, if any.
      Can be overwritten to provide deep copy, etc.
      @param first value for <tt>$1</tt>, or <tt>null</tt>.
      @return first.
    */
  protected Object yyDefault (Object first) {
    return first;
  }

  /** the generated parser.
      Maintains a dynamic state and value stack.
      @param yyLex scanner.
      @return result of the last reduction, if any.
      @throws yyException on irrecoverable parse error.
    */
  public Object yyparse (yyInput yyLex) throws java.io.IOException, yyException {
    if (yyMax <= 0) yyMax = 256;			// initial size
    int yyState = 0, yyStates[] = new int[yyMax];	// state stack
    Object yyVal = null, yyVals[] = new Object[yyMax];	// value stack
    int yyToken = -1;					// current input
    int yyErrorFlag = 0;				// #tokens to shift

    yyLoop: for (int yyTop = 0;; ++ yyTop) {
      if (yyTop >= yyStates.length) {			// dynamically increase
        int[] i = new int[yyStates.length+yyMax];
        System.arraycopy(yyStates, 0, i, 0, yyStates.length);
        yyStates = i;
        Object[] o = new Object[yyVals.length+yyMax];
        System.arraycopy(yyVals, 0, o, 0, yyVals.length);
        yyVals = o;
      }
      yyStates[yyTop] = yyState;
      yyVals[yyTop] = yyVal;
//t      if (yydebug != null) yydebug.push(yyState, yyVal);

      yyDiscarded: for (;;) {	// discarding a token does not change stack
        int yyN;
        if ((yyN = yyDefRed[yyState]) == 0) {	// else [default] reduce (yyN)
          if (yyToken < 0) {
            yyToken = yyLex.advance() ? yyLex.token() : 0;
//t            if (yydebug != null)
//t              yydebug.lex(yyState, yyToken, yyName(yyToken), yyLex.value());
          }
          if ((yyN = yySindex[yyState]) != 0 && (yyN += yyToken) >= 0
              && yyN < yyTable.length && yyCheck[yyN] == yyToken) {
//t            if (yydebug != null)
//t              yydebug.shift(yyState, yyTable[yyN], yyErrorFlag > 0 ? yyErrorFlag-1 : 0);
            yyState = yyTable[yyN];		// shift to yyN
            yyVal = yyLex.value();
            yyToken = -1;
            if (yyErrorFlag > 0) -- yyErrorFlag;
            continue yyLoop;
          }
          if ((yyN = yyRindex[yyState]) != 0 && (yyN += yyToken) >= 0
              && yyN < yyTable.length && yyCheck[yyN] == yyToken)
            yyN = yyTable[yyN];			// reduce (yyN)
          else
            switch (yyErrorFlag) {
  
            case 0:
              yyerror("syntax error", yyExpecting(yyState));
//t              if (yydebug != null) yydebug.error("syntax error");
  
            case 1: case 2:
              yyErrorFlag = 3;
              do {
                if ((yyN = yySindex[yyStates[yyTop]]) != 0
                    && (yyN += yyErrorCode) >= 0 && yyN < yyTable.length
                    && yyCheck[yyN] == yyErrorCode) {
//t                  if (yydebug != null)
//t                    yydebug.shift(yyStates[yyTop], yyTable[yyN], 3);
                  yyState = yyTable[yyN];
                  yyVal = yyLex.value();
                  continue yyLoop;
                }
//t                if (yydebug != null) yydebug.pop(yyStates[yyTop]);
              } while (-- yyTop >= 0);
//t              if (yydebug != null) yydebug.reject();
              throw new yyException("irrecoverable syntax error");
  
            case 3:
              if (yyToken == 0) {
//t                if (yydebug != null) yydebug.reject();
                throw new yyException("irrecoverable syntax error at end-of-file");
              }
//t              if (yydebug != null)
//t                yydebug.discard(yyState, yyToken, yyName(yyToken), yyLex.value());
              yyToken = -1;
              continue yyDiscarded;		// leave stack alone
            }
        }
        int yyV = yyTop + 1-yyLen[yyN];
//t        if (yydebug != null)
//t          yydebug.reduce(yyState, yyStates[yyV-1], yyN, yyRule[yyN], yyLen[yyN]);
        yyVal = yyDefault(yyV > yyTop ? null : yyVals[yyV]);
        switch (yyN) {
case 1:
					// line 55 "Expression.jay"
  { code.add("ADD_I");}
  break;
case 2:
					// line 56 "Expression.jay"
  { code.add("SUB_I");}
  break;
case 3:
					// line 57 "Expression.jay"
  { code.add("MUL_I");}
  break;
case 4:
					// line 58 "Expression.jay"
  { code.add("DIV_I");}
  break;
case 5:
					// line 59 "Expression.jay"
  { code.add("MOD_I");}
  break;
case 6:
					// line 62 "Expression.jay"
  { code.add("AND");}
  break;
case 7:
					// line 63 "Expression.jay"
  { code.add("OR");}
  break;
case 8:
					// line 64 "Expression.jay"
  { code.add("XOR");}
  break;
case 9:
					// line 65 "Expression.jay"
  { code.add("LOGIC_NOT");}
  break;
case 10:
					// line 66 "Expression.jay"
  { code.add("NOT");}
  break;
case 12:
					// line 76 "Expression.jay"
  { code.add("MINUS ");}
  break;
case 14:
					// line 80 "Expression.jay"
  { code.add("PUSH_SI_C "+((String)yyVals[0+yyTop]));}
  break;
case 15:
					// line 81 "Expression.jay"
  { code.add("LOAD_I "+((String)yyVals[0+yyTop]));}
  break;
case 16:
					// line 83 "Expression.jay"
  { code.add("IF_A<B"); }
  break;
case 17:
					// line 84 "Expression.jay"
  { code.add("IF_A>B"); }
  break;
case 18:
					// line 85 "Expression.jay"
  { code.add("IF_A<=B");}
  break;
case 19:
					// line 86 "Expression.jay"
  { code.add("IF_A>=B");}
  break;
case 20:
					// line 87 "Expression.jay"
  { code.add("IF_A=B "); }
  break;
case 21:
					// line 88 "Expression.jay"
  { code.add("IF_A!=B "); }
  break;
case 23:
					// line 92 "Expression.jay"
  { code.add("STORE_I "+((String)yyVals[-2+yyTop]));}
  break;
case 28:
					// line 100 "Expression.jay"
  { code.add("DIM "+((String)yyVals[-3+yyTop])+","+((String)yyVals[-2+yyTop]));}
  break;
					// line 533 "-"
        }
        yyTop -= yyLen[yyN];
        yyState = yyStates[yyTop];
        int yyM = yyLhs[yyN];
        if (yyState == 0 && yyM == 0) {
//t          if (yydebug != null) yydebug.shift(0, yyFinal);
          yyState = yyFinal;
          if (yyToken < 0) {
            yyToken = yyLex.advance() ? yyLex.token() : 0;
//t            if (yydebug != null)
//t               yydebug.lex(yyState, yyToken,yyName(yyToken), yyLex.value());
          }
          if (yyToken == 0) {
//t            if (yydebug != null) yydebug.accept(yyVal);
            return yyVal;
          }
          continue yyLoop;
        }
        if ((yyN = yyGindex[yyM]) != 0 && (yyN += yyState) >= 0
            && yyN < yyTable.length && yyCheck[yyN] == yyState)
          yyState = yyTable[yyN];
        else
          yyState = yyDgoto[yyM];
//t        if (yydebug != null) yydebug.shift(yyStates[yyTop], yyState);
        continue yyLoop;
      }
    }
  }

					// line 110 "Expression.jay"

}
					// line 566 "-"
