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
package codeeditor;


import java.awt.*;
import java.util.*;
import javax.swing.text.*;

class JavaSyntaxDocument extends DefaultStyledDocument{
    private DefaultStyledDocument doc;
    private Element rootElement;
    private boolean multiLineComment;
    private MutableAttributeSet normal;
    private MutableAttributeSet keyword;
    private MutableAttributeSet comment;
    private MutableAttributeSet quote;
    private Hashtable keywords;
    private PanelEditor panel;
    
    public JavaSyntaxDocument(PanelEditor panel)
    {
        this.panel=panel;
        doc = this;
        rootElement = doc.getDefaultRootElement();
        putProperty( DefaultEditorKit.EndOfLineStringProperty, "\n" );
        normal = new SimpleAttributeSet();
        StyleConstants.setForeground(normal, Color.black);
        comment = new SimpleAttributeSet();
        Color green = new Color(0, 120, 0);
        StyleConstants.setForeground(comment, green);
        StyleConstants.setItalic(comment, true);
        keyword = new SimpleAttributeSet();
        Color blue = new Color(0, 0, 140);
        StyleConstants.setForeground(keyword, blue);
        StyleConstants.setBold(keyword, true);
        quote = new SimpleAttributeSet();
        Color red = new Color(140,0,0);
        StyleConstants.setForeground(quote, red);
        Object dummyObject = new Object();
        
        keywords = new Hashtable();
        keywords.put( "abstract", dummyObject );
        keywords.put( "boolean", dummyObject );
        keywords.put( "break", dummyObject );
        keywords.put( "byte", dummyObject );
        keywords.put( "byvalue", dummyObject );
        keywords.put( "case", dummyObject );
        keywords.put( "cast", dummyObject );
        keywords.put( "catch", dummyObject );
        keywords.put( "char", dummyObject );
        keywords.put( "class", dummyObject );
        keywords.put( "continue", dummyObject );
        keywords.put( "default", dummyObject );
        keywords.put( "do", dummyObject );
        keywords.put( "double", dummyObject );
        keywords.put( "else", dummyObject );
        keywords.put( "extends", dummyObject );
        keywords.put( "false", dummyObject );
        keywords.put( "final", dummyObject );
        keywords.put( "finally", dummyObject );
        keywords.put( "float", dummyObject );
        keywords.put( "for", dummyObject );
        keywords.put( "if", dummyObject );
        keywords.put( "implements", dummyObject );
        keywords.put( "import", dummyObject );
        keywords.put( "instanceof", dummyObject );
        keywords.put( "int", dummyObject );
        keywords.put( "interface", dummyObject );
        keywords.put( "long", dummyObject );
        keywords.put( "new", dummyObject );
        keywords.put( "null", dummyObject );
        keywords.put( "package", dummyObject );
        keywords.put( "private", dummyObject );
        keywords.put( "protected", dummyObject );
        keywords.put( "public", dummyObject );
        keywords.put( "return", dummyObject );
        keywords.put( "short", dummyObject );
        keywords.put( "static", dummyObject );
        keywords.put( "super", dummyObject );
        keywords.put( "switch", dummyObject );
        keywords.put( "synchronized", dummyObject );
        keywords.put( "this", dummyObject );
        keywords.put( "throw", dummyObject );
        keywords.put( "throws", dummyObject );
        keywords.put( "transient", dummyObject );
        keywords.put( "true", dummyObject );
        keywords.put( "try", dummyObject );
        keywords.put( "void", dummyObject );
        keywords.put( "volatile", dummyObject );
        keywords.put( "while", dummyObject );
    }
    
/*
 * Override to apply syntax highlighting after the document has been updated
 */
    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException{
        if (str.equals("{"))
            str = addMatchingBrace(offset);
        super.insertString(offset, str, a);
        processChangedLines(offset, str.length());
    }
    
/*
 * Override to apply syntax highlighting after the document has been updated
 */
    public void remove(int offset, int length) throws BadLocationException{
        super.remove(offset, length);
        processChangedLines(offset, 0);
    }
/*
 * Determine how many lines have been changed,
 * then apply highlighting to each line
 */
    private void processChangedLines(int offset, int length) throws BadLocationException {
        String content = doc.getText(0, doc.getLength());
        // The lines affected by the latest document update
        int startLine = rootElement.getElementIndex( offset );
        int endLine = rootElement.getElementIndex( offset + length );
        // Make sure all comment lines prior to the start line are commented
        // and determine if the start line is still in a multi line comment
        setMultiLineComment( commentLinesBefore( content, startLine ) );
        // Do the actual highlighting
        for (int i = startLine; i <= endLine; i++) applyHighlighting(content, i);
        // Resolve highlighting to the next end multi line delimiter
        if (isMultiLineComment())commentLinesAfter(content, endLine);
        else highlightLinesAfter(content, endLine);
    }
/*
 * Highlight lines when a multi line comment is still 'open'
 * (ie. matching end delimiter has not yet been encountered)
 */
    private boolean commentLinesBefore(String content, int line){
        int offset = rootElement.getElement( line ).getStartOffset();
        // Start of comment not found, nothing to do
        int startDelimiter = lastIndexOf( content, getStartDelimiter(), offset-2);
        if (startDelimiter < 0)return false;
        // Matching start/end of comment found, nothing to do
        int endDelimiter = indexOf( content, getEndDelimiter(), startDelimiter );
        if (endDelimiter < offset & endDelimiter != -1)return false;
        // End of comment not found, highlight the lines
        doc.setCharacterAttributes(startDelimiter, offset - startDelimiter + 1, comment, false);
        return true;
    }
/*
 * Highlight comment lines to matching end delimiter
 */
    private void commentLinesAfter(String content, int line){
        int offset = rootElement.getElement( line ).getEndOffset();
        // End of comment not found, nothing to do
        int endDelimiter = indexOf( content, getEndDelimiter(), offset );
        if (endDelimiter < 0) return;
        // Matching start/end of comment found, comment the lines
        int startDelimiter = lastIndexOf( content, getStartDelimiter(), endDelimiter );
        if (startDelimiter < 0 || startDelimiter <= offset){
            doc.setCharacterAttributes(offset, endDelimiter - offset + 1, comment, false);
        }
    }
    
/*
 * Highlight lines to start or end delimiter
 */
    private void highlightLinesAfter(String content, int line) throws BadLocationException{
        int offset = rootElement.getElement( line ).getEndOffset();
        // Start/End delimiter not found, nothing to do
        int startDelimiter = indexOf( content, getStartDelimiter(), offset );
        int endDelimiter = indexOf( content, getEndDelimiter(), offset );
        if (startDelimiter < 0)	startDelimiter = content.length();
        if (endDelimiter < 0)endDelimiter = content.length();
        int delimiter = Math.min(startDelimiter, endDelimiter);
        if (delimiter < offset)return;
        // Start/End delimiter found, reapply highlighting
        int endLine = rootElement.getElementIndex( delimiter );
        for (int i = line + 1; i < endLine; i++){
            Element branch = rootElement.getElement( i );
            Element leaf = doc.getCharacterElement( branch.getStartOffset() );
            AttributeSet as = leaf.getAttributes();
            if ( as.isEqual(comment) )applyHighlighting(content, i);
        }
    }
    
/*
 * Parse the line to determine the appropriate highlighting
 */
    private void applyHighlighting(String content, int line) throws BadLocationException{
        int startOffset = rootElement.getElement( line ).getStartOffset();
        int endOffset = rootElement.getElement( line ).getEndOffset() - 1;
        int lineLength = endOffset - startOffset;
        int contentLength = content.length();
        if (endOffset >= contentLength)endOffset = contentLength - 1;
        // check for multi line comments
        // (always set the comment attribute for the entire line)
        if (endingMultiLineComment(content, startOffset, endOffset)
        ||isMultiLineComment()||startingMultiLineComment(content, startOffset, endOffset)){
            doc.setCharacterAttributes(startOffset, endOffset - startOffset + 1, comment, false);
            return;
        }
        // set normal attributes for the line
        doc.setCharacterAttributes(startOffset, lineLength, normal, true);
        // check for single line comment
        int index = content.indexOf(getSingleLineDelimiter(), startOffset);
        if ( (index > -1) && (index < endOffset) ){
            doc.setCharacterAttributes(index, endOffset - index + 1, comment, false);
            endOffset = index - 1;
        }
        // check for tokens
        checkForTokens(content, startOffset, endOffset);
    }
/*
 * Does this line contain the start delimiter
 */
    private boolean startingMultiLineComment(String content, int startOffset, int endOffset) throws BadLocationException{
        int index = indexOf( content, getStartDelimiter(), startOffset );
        if ( (index < 0) || (index > endOffset) )return false;
        else{
            setMultiLineComment( true );
            return true;
        }
    }
/*
 * Does this line contain the end delimiter
 */
    private boolean endingMultiLineComment(String content, int startOffset, int endOffset) throws BadLocationException{
        int index = indexOf( content, getEndDelimiter(), startOffset );
        if ( (index < 0) || (index > endOffset) )return false;
        else{
            setMultiLineComment( false );
            return true;
        }
    }
/*
 * We have found a start delimiter
 * and are still searching for the end delimiter
 */
    private boolean isMultiLineComment(){
        return multiLineComment;
    }
    private void setMultiLineComment(boolean value){
        multiLineComment = value;
    }
/*
 * Parse the line for tokens to highlight
 */
    private void checkForTokens(String content, int startOffset, int endOffset){
        while (startOffset <= endOffset){
            // skip the delimiters to find the start of a new token
            while (isDelimiter(content.substring(startOffset, startOffset+1))){
                if (startOffset < endOffset)startOffset++;
                else return;
            }
            // Extract and process the entire token
            if (isQuoteDelimiter( content.substring(startOffset, startOffset + 1)))
                startOffset = getQuoteToken(content, startOffset, endOffset);
            else startOffset = getOtherToken(content, startOffset, endOffset);
        }
    }
/*
 *
 */
    private int getQuoteToken(String content, int startOffset, int endOffset){
        String quoteDelimiter = content.substring(startOffset, startOffset + 1);
        String escapeString = getEscapeString(quoteDelimiter);
        int index;
        int endOfQuote = startOffset;
        // skip over the escape quotes in this quote
        index = content.indexOf(escapeString, endOfQuote + 1);
        while ( (index > -1) && (index < endOffset) ){
            endOfQuote = index + 1;
            index = content.indexOf(escapeString, endOfQuote);
        }
        // now find the matching delimiter
        index = content.indexOf(quoteDelimiter, endOfQuote + 1);
        if ( (index < 0) || (index > endOffset) )endOfQuote = endOffset;
        else endOfQuote = index;
        doc.setCharacterAttributes(startOffset, endOfQuote-startOffset+1, quote, false);
        return endOfQuote + 1;
    }
    
    private int getOtherToken(String content, int startOffset, int endOffset){
        panel.undoable=false;
        int endOfToken = startOffset + 1;
        while (endOfToken <= endOffset ){
            if (isDelimiter(content.substring(endOfToken, endOfToken+1)))break;
            endOfToken++;
        }
        
        String token = content.substring(startOffset, endOfToken);
        if ( isKeyword( token ) ) doc.setCharacterAttributes(startOffset, endOfToken-startOffset, keyword, false);
        
        panel.undoable=true;
        return endOfToken + 1;
    }
/*
 * Assume the needle will the found at the start/end of the line
 */
    private int indexOf(String content, String needle, int offset){
        int index;
        while ( (index = content.indexOf(needle, offset)) != -1 ){
            String text = getLine( content, index ).trim();
            if (text.startsWith(needle) || text.endsWith(needle))break;
            else offset = index + 1;
        }
        return index;
    }
/*
 * Assume the needle will the found at the start/end of the line
 */
    private int lastIndexOf(String content, String needle, int offset){
        int index;
        while ( (index = content.lastIndexOf(needle, offset)) != -1 ){
            String text = getLine( content, index ).trim();
            if (text.startsWith(needle) || text.endsWith(needle))break;
            else offset = index - 1;
        }
        return index;
    }
    
    private String getLine(String content, int offset){
        int line = rootElement.getElementIndex( offset );
        Element lineElement = rootElement.getElement( line );
        int start = lineElement.getStartOffset();
        int end = lineElement.getEndOffset();
        return content.substring(start, end - 1);
    }
/*
 * Override for other languages
 */
    protected boolean isDelimiter(String character){
        String operands = ";:{}()[]+-/%<=>!&|^~*";
        if (Character.isWhitespace( character.charAt(0) ) || operands.indexOf(character)!= -1 )
            return true;
        else return false;
    }
/*
 * Override for other languages
 */
    protected boolean isQuoteDelimiter(String character){
        String quoteDelimiters = "\"'";
        if (quoteDelimiters.indexOf(character) < 0) return false;
        else return true;
    }
/*
 * Override for other languages
 */
    protected boolean isKeyword(String token){
        Object o = keywords.get( token );
        return o == null ? false : true;
    }
/*
 * Override for other languages
 */
    protected String getStartDelimiter(){
        return "/*";
    }
/*
 * Override for other languages
 */
    protected String getEndDelimiter(){
        return "*/";
    }
/*
 * Override for other languages
 */
    protected String getSingleLineDelimiter(){
        return "//";
    }
/*
 * Override for other languages
 */
    protected String getEscapeString(String quoteDelimiter){
        return "\\" + quoteDelimiter;
    }
/*
 *
 */
    protected String addMatchingBrace(int offset) throws BadLocationException{
        StringBuffer whiteSpace = new StringBuffer();
        int line = rootElement.getElementIndex( offset );
        int i = rootElement.getElement(line).getStartOffset();
        while (true){
            String temp = doc.getText(i, 1);
            if (temp.equals(" ") || temp.equals("\t")){
                whiteSpace.append(temp);
                i++;
            } else break;
        }
        return "{\n" + whiteSpace.toString() + whiteSpace.toString()+"\n" + whiteSpace.toString() + "}";
    }
}

