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
package ParserCode;

// created by jay 1.1.0 (c) 2002-2006 ats@cs.rit.edu
// skeleton Java 1.1.0 (c) 2002-2006 ats@cs.rit.edu

					// line 2 "Expression.jay"

  import java.io.IOException;
  import java.io.Reader;
  import java.io.StreamTokenizer;
  import java.util.ArrayList;

  /** recognizes, stores, and evaluates arithmetic expressions
      using a parser generated with jay.
    */
  public class Expression
  {

    public static ArrayList liste = new ArrayList();

    public static void main (String args []) throws Exception
    {
      Expression parser = new Expression();
      Scanner scanner = new Scanner(new java.io.StringReader("1+2"));
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
    }
					// line 42 "-"
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
  protected static final int yyFinal = 15;

  /** parser tables.
      Order is mandated by <i>jay</i>.
    */
  protected static final short[] yyLhs = {
//yyLhs 34
    -1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
     1,     1,     1,     1,     1,     1,     1,     2,     2,     3,
     3,     0,     0,     0,
    }, yyLen = {
//yyLen 34
     2,     3,     3,     3,     3,     3,     3,     3,     3,     2,
     4,     4,     4,     4,     4,     4,     2,     2,     3,     1,
     1,     1,     3,     3,     4,     4,     4,     3,     3,     1,
     1,     1,     1,     4,
    }, yyDefRed = {
//yyDefRed 77
     0,    19,    20,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,    32,     0,     0,
     0,     0,     0,     0,     0,     0,    21,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,    27,     0,     0,     0,     0,     0,     0,     0,
     0,    18,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,    10,    11,    12,    13,    14,
    15,    29,    30,    33,     0,     0,     0,
    }, yyDgoto = {
//yyDgoto 4
    15,    16,    17,    73,
    }, yySindex = {
//yySindex 77
   -18,     0,     0,   -55,   -32,   -31,   -27,   -23,   -22,   -21,
  -240,    17,    17,    17,    17,     0,   285,     0,   -33,    17,
    17,    17,    17,    17,    17,   -20,     0,    82,    82,   285,
    27,    17,    17,    17,    17,    17,    17,    17,    17,    -5,
     6,   -40,     0,   285,    53,    64,    92,   160,   248,   259,
  -266,     0,   296,   296,    82,    82,   285,   285,   285,   285,
    17,   285,    17,   285,    17,     0,     0,     0,     0,     0,
     0,     0,     0,     0,   285,   285,   285,
    }, yyRindex = {
//yyRindex 77
     0,     0,     0,    16,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,    23,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,   118,   127,     1,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,    26,     0,     0,     0,     0,     0,     0,
     0,     0,     2,    30,   138,   148,     3,    11,    14,    36,
     0,    41,     0,    81,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,   104,   108,   116,
    }, yyGindex = {
//yyGindex 4
     0,   392,     0,     0,
    }, yyTable = {
//yyTable 457
    13,     9,     1,     5,    71,    72,    18,    14,    19,    20,
    11,     6,    12,    21,     7,    13,    21,    22,    23,    24,
    25,    64,    14,    31,    50,    11,    28,    12,    13,     0,
     2,     0,     0,     0,     0,    14,     8,     0,    11,    13,
    12,    22,     9,     1,     5,     1,    14,     1,     0,    11,
    13,    12,     6,    21,    21,     7,    60,    14,    21,    21,
    11,    21,    12,    21,    35,    36,     0,    62,    51,    33,
    31,     2,    32,     2,    34,     2,    21,     8,    21,     0,
     0,    23,    22,     0,     0,     0,     0,    39,    41,    40,
    35,    36,     0,     0,    65,    33,    31,     0,    32,     0,
    34,    35,    36,     0,    24,    66,    33,    31,    25,    32,
    21,    34,     0,    39,    41,    40,    26,     0,    16,    35,
    36,    38,    23,     0,    39,    41,    40,    17,     0,    35,
    36,     0,     0,    67,    33,    31,     0,    32,     3,    34,
    21,     0,    39,    41,    40,    24,     0,    38,     4,    25,
     0,    37,    39,    41,    40,     0,     0,    26,    38,    16,
    16,    16,     0,    16,     0,    16,     0,     0,    17,    17,
    17,     0,    17,     0,    17,     0,    38,    37,     0,     3,
     3,     3,     0,     3,     0,     3,    38,     0,    37,     4,
     4,     4,     0,     4,     0,     4,     0,    35,    36,     0,
     0,    68,    33,    31,     0,    32,    37,    34,     0,     0,
     0,     0,     0,     0,     0,     0,    37,     0,     0,     0,
    39,    41,    40,     0,     1,     2,     0,    26,     4,     5,
     6,     7,     8,     9,     0,     0,     0,     0,     0,     1,
     2,    42,     3,     4,     5,     6,     7,     8,     9,     0,
     0,    10,     1,     2,    38,    26,     4,     5,     6,     7,
     8,     9,     0,     1,     2,     0,    26,     4,     5,     6,
     7,     8,     9,     0,     1,     2,     0,    26,     4,     5,
     6,     7,     8,     9,    37,    35,    36,     0,     0,    69,
    33,    31,     0,    32,     0,    34,    35,    36,     0,     0,
    70,    33,    31,     0,    32,     0,    34,     0,    39,    41,
    40,     0,     0,     0,     0,     0,     0,     0,     0,    39,
    41,    40,    35,    36,     0,     0,     0,    33,    31,     0,
    32,     0,    34,    35,    36,     0,     0,     0,    33,     0,
     0,     0,    38,    34,     0,    39,    41,    40,     0,     0,
     0,     0,     0,    38,     0,     0,    39,    41,    40,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,    37,     0,     0,     0,     0,     0,     0,    38,
     0,     0,     0,    37,     0,     0,     0,     0,     0,     0,
    38,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,    27,    28,    29,    30,     0,     0,    37,
    43,    44,    45,    46,    47,    48,    49,     0,     0,     0,
    37,     0,     0,    52,    53,    54,    55,    56,    57,    58,
    59,    61,    63,     0,     0,     0,     0,     0,     0,     0,
     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
     0,     0,    74,     0,    75,     0,    76,
    }, yyCheck = {
//yyCheck 457
    33,     0,     0,     0,   270,   271,    61,    40,    40,    40,
    43,     0,    45,    40,     0,    33,     0,    40,    40,    40,
   260,    61,    40,     0,    44,    43,     0,    45,    33,    -1,
     0,    -1,    -1,    -1,    -1,    40,     0,    -1,    43,    33,
    45,     0,    41,    41,    41,    43,    40,    45,    -1,    43,
    33,    45,    41,    37,    38,    41,    61,    40,    42,    43,
    43,    45,    45,    47,    37,    38,    -1,    61,    41,    42,
    43,    41,    45,    43,    47,    45,    60,    41,    62,    -1,
    -1,     0,    41,    -1,    -1,    -1,    -1,    60,    61,    62,
    37,    38,    -1,    -1,    41,    42,    43,    -1,    45,    -1,
    47,    37,    38,    -1,     0,    41,    42,    43,     0,    45,
    94,    47,    -1,    60,    61,    62,     0,    -1,     0,    37,
    38,    94,    41,    -1,    60,    61,    62,     0,    -1,    37,
    38,    -1,    -1,    41,    42,    43,    -1,    45,     0,    47,
   124,    -1,    60,    61,    62,    41,    -1,    94,     0,    41,
    -1,   124,    60,    61,    62,    -1,    -1,    41,    94,    41,
    42,    43,    -1,    45,    -1,    47,    -1,    -1,    41,    42,
    43,    -1,    45,    -1,    47,    -1,    94,   124,    -1,    41,
    42,    43,    -1,    45,    -1,    47,    94,    -1,   124,    41,
    42,    43,    -1,    45,    -1,    47,    -1,    37,    38,    -1,
    -1,    41,    42,    43,    -1,    45,   124,    47,    -1,    -1,
    -1,    -1,    -1,    -1,    -1,    -1,   124,    -1,    -1,    -1,
    60,    61,    62,    -1,   257,   258,    -1,   260,   261,   262,
   263,   264,   265,   266,    -1,    -1,    -1,    -1,    -1,   257,
   258,   274,   260,   261,   262,   263,   264,   265,   266,    -1,
    -1,   269,   257,   258,    94,   260,   261,   262,   263,   264,
   265,   266,    -1,   257,   258,    -1,   260,   261,   262,   263,
   264,   265,   266,    -1,   257,   258,    -1,   260,   261,   262,
   263,   264,   265,   266,   124,    37,    38,    -1,    -1,    41,
    42,    43,    -1,    45,    -1,    47,    37,    38,    -1,    -1,
    41,    42,    43,    -1,    45,    -1,    47,    -1,    60,    61,
    62,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    60,
    61,    62,    37,    38,    -1,    -1,    -1,    42,    43,    -1,
    45,    -1,    47,    37,    38,    -1,    -1,    -1,    42,    -1,
    -1,    -1,    94,    47,    -1,    60,    61,    62,    -1,    -1,
    -1,    -1,    -1,    94,    -1,    -1,    60,    61,    62,    -1,
    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
    -1,    -1,   124,    -1,    -1,    -1,    -1,    -1,    -1,    94,
    -1,    -1,    -1,   124,    -1,    -1,    -1,    -1,    -1,    -1,
    94,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
    -1,    -1,    -1,    11,    12,    13,    14,    -1,    -1,   124,
    18,    19,    20,    21,    22,    23,    24,    -1,    -1,    -1,
   124,    -1,    -1,    31,    32,    33,    34,    35,    36,    37,
    38,    39,    40,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,
    -1,    -1,    60,    -1,    62,    -1,    64,
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
    null,null,null,null,null,"'|'",null,null,null,null,null,null,null,
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
//t    "expr : SIN '(' expr ')'",
//t    "expr : COS '(' expr ')'",
//t    "expr : TAN '(' expr ')'",
//t    "expr : ASIN '(' expr ')'",
//t    "expr : ACOS '(' expr ')'",
//t    "expr : ATAN '(' expr ')'",
//t    "expr : '+' expr",
//t    "expr : '-' expr",
//t    "expr : '(' expr ')'",
//t    "expr : BYTE",
//t    "expr : WORD",
//t    "expr : Variable",
//t    "expr : expr '<' expr",
//t    "expr : expr '>' expr",
//t    "expr : expr '<' '=' expr",
//t    "expr : expr '>' '=' expr",
//t    "expr : expr '=' '=' expr",
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
					// line 51 "Expression.jay"
  { liste.add("ADD");}
  break;
case 2:
					// line 52 "Expression.jay"
  { liste.add("SUB");}
  break;
case 3:
					// line 53 "Expression.jay"
  { liste.add("MUL");}
  break;
case 4:
					// line 54 "Expression.jay"
  { liste.add("DIV");}
  break;
case 5:
					// line 55 "Expression.jay"
  { liste.add("MOD");}
  break;
case 6:
					// line 58 "Expression.jay"
  { liste.add("AND");}
  break;
case 7:
					// line 59 "Expression.jay"
  { liste.add("OR");}
  break;
case 8:
					// line 60 "Expression.jay"
  { liste.add("XOR");}
  break;
case 9:
					// line 61 "Expression.jay"
  { liste.add("NOT");}
  break;
case 10:
					// line 63 "Expression.jay"
  { liste.add("SIN ");}
  break;
case 11:
					// line 64 "Expression.jay"
  { liste.add("COS ");}
  break;
case 12:
					// line 65 "Expression.jay"
  { liste.add("TAN ");}
  break;
case 13:
					// line 66 "Expression.jay"
  { liste.add("ASIN ");}
  break;
case 14:
					// line 67 "Expression.jay"
  { liste.add("ACOS ");}
  break;
case 15:
					// line 68 "Expression.jay"
  { liste.add("ATAN ");}
  break;
case 17:
					// line 71 "Expression.jay"
  { liste.add("MINUS ");}
  break;
case 19:
					// line 73 "Expression.jay"
  { liste.add("PUSHB "+((String)yyVals[0+yyTop]));}
  break;
case 20:
					// line 74 "Expression.jay"
  { liste.add("PUSHI "+((String)yyVals[0+yyTop]));}
  break;
case 21:
					// line 75 "Expression.jay"
  { liste.add("PUSHI "+((String)yyVals[0+yyTop]));}
  break;
case 22:
					// line 77 "Expression.jay"
  { liste.add("IF_A<B "); }
  break;
case 23:
					// line 78 "Expression.jay"
  { liste.add("IF_A>B "); }
  break;
case 24:
					// line 79 "Expression.jay"
  { liste.add("IF_A<=B ");}
  break;
case 25:
					// line 80 "Expression.jay"
  { liste.add("IF_A>=B ");}
  break;
case 26:
					// line 81 "Expression.jay"
  { liste.add("IF_A=B "); }
  break;
case 28:
					// line 85 "Expression.jay"
  { liste.add("POPI "+((String)yyVals[-2+yyTop]));}
  break;
case 33:
					// line 94 "Expression.jay"
  { liste.add("DIM "+((String)yyVals[-3+yyTop])+","+((String)yyVals[-2+yyTop]));}
  break;
					// line 605 "-"
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

					// line 104 "Expression.jay"
  /** lexical analyzer for arithmetic expressions.
    */
  public static class Scanner extends StreamTokenizer implements yyInput
  {
    public Scanner (Reader r)
    {
      super(r);
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
           
           try
           {
             int c=Integer.valueOf(sval);

             if (c>=0 && c<=255) return BYTE; else
             if (c>255 && c<=65535) return WORD; else
              System.out.println("Error: Byte or Word excepted ");

             //return sval.indexOf(".") < 0 ? Int : Real;
           } catch(Exception ex)
           {
           }
           
           if (sval.equalsIgnoreCase("LCDPRINTNUM")) return LCD_PRINT_NUM;
           
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
           if (sval.equalsIgnoreCase("ATAN")) return ATAN;
           
           
           return Variable;

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
}
					// line 727 "-"
