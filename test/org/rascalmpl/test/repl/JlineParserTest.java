package org.rascalmpl.test.repl;

import static org.junit.Assert.assertEquals;

import org.jline.reader.ParsedLine;
import org.jline.reader.Parser.ParseContext;
import org.junit.Test;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.repl.jline3.RascalLineParser;
import org.rascalmpl.uri.URIUtil;

public class JlineParserTest {

    private ParsedLine completeParser(String input) {
        return completeParser(input, input.length() - 1);
    }

    private ParsedLine completeParser(String input, int cursor) {
        var parser = new RascalLineParser(l -> { throw new ParseError("rascal parser not supported in the test yet", URIUtil.invalidURI(), 0, 0, 0, 0, 0, 0); });

        return parser.parse(input, cursor, ParseContext.COMPLETE);
    }

    @Test
    public void commandsParsedCorrectly() {
        assertEquals("se", completeParser(":se").word());
        assertEquals(":", completeParser(":set prof", 2).words().get(0));
        assertEquals("set", completeParser(":set prof", 2).word());
        assertEquals("prof", completeParser(":set prof", 6).word());
        assertEquals("", completeParser(":set ", 5).word());
    }

    @Test
    public void stringsAreNotParsedAsWords() {
        assertEquals(1, completeParser("\"long string with multiple spaces\"").words().size()); // word
        assertEquals("\"long string with multiple spaces\"", completeParser("\"long string with multiple spaces\"",11).word());
        assertEquals("\"long string with multiple spaces\"", completeParser("\"long string with multiple spaces\"").word());
        assertEquals(2, completeParser("x = \"long string with multiple spaces\";").words().size());
    }

    @Test
    public void stringInterpolation() {
        assertEquals("exp", completeParser("\"string <exp",11).word());
        assertEquals("exp", completeParser("\"string <exp more words",11).word());
        assertEquals("exp", completeParser("\"string <exp string ending\"",11).word());
        assertEquals(3, completeParser("\"string <exp> string ending\"",11).words().size());
        assertEquals(3, completeParser("\"string <exp> string ending\"").words().size()); // at the end always an extra one
    }

    @Test
    public void qualifiedNames() {
        assertEquals("IO::print", completeParser("IO::print").word());
        assertEquals("lang::rascal", completeParser("import lang::rascal").word());
    }

    @Test
    public void locations() {
        assertEquals("|", completeParser("|").word());
        assertEquals("|file", completeParser("|file").word());
        assertEquals("|file://", completeParser("|file://").word());
        assertEquals("|file:///home/dir", completeParser("|file:///home/dir").word());
        assertEquals("|file:///home/dir|", completeParser("|file:///home/dir|").word());
    }

    
}
