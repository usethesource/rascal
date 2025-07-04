/*
 * Copyright (c) 2024-2025, NWO-I CWI and Swat.engineering
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.rascalmpl.repl.rascal;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jline.reader.EOFError;
import org.jline.reader.ParsedLine;
import org.jline.reader.Parser;
import org.jline.reader.SyntaxError;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.TreeAdapter;



/**
 * Implement a simple lexer for input that a user is typing. 
 * jline3 requires this for both auto completion and for multi-line editing.
 * Note: JLine only supports completion for the current word, so sometimes things are lexed differently than in the rascal grammar.
 */
public class RascalLineParser implements Parser {
    private final Function<String, ITree> commandParser;

    public RascalLineParser(Function<String, ITree> commandParser) {
        this.commandParser = commandParser;
    }

    @Override
    public ParsedLine parse(String line, int cursor, ParseContext context) throws SyntaxError {
        switch (context) {
            case UNSPECIFIED: // TODO: check if this is correct
            case ACCEPT_LINE:
                // we have to verify the input is correct rascal statement
                return parseFullRascalCommand(line, cursor, true);
            case COMPLETE:
                // for completion purposes, we want a specific kind of grouping
                // so we'll use a heuristic for this. 
                // in the future we might be able to use the parser with error recovery
                // but we would still have to think about grouping things together that aren't in the 
                // parse tree, such as `:` and the `set`
                // TODO: JV: we have syntax definitions for `:set` so that should not be an issue. See Rascal.rsc::Command
                // TODO: we could also write a simple grammar for this grouping and generate the parser for it.
                try {
                    // lets see, maybe it parses as a rascal expression
                    return parseFullRascalCommand(line, cursor, false);
                }
                catch (EOFError e) {
                    // otherwise fallback to regex party
                    return splitWordsOnly(line, cursor);
                }
            case SECONDARY_PROMPT:
                throw new SyntaxError(-1, -1, "Unsupported SECONDARY_PROMPT");
            case SPLIT_LINE:
                throw new SyntaxError(-1, -1, "Unsupported SPLIT_LINE");
            default:
                throw new UnsupportedOperationException("Unimplemented context: " + context);
        }
    }

    private ParsedLine splitWordsOnly(String line, int cursor) {
        // small line parser, in the future we might be able to use error recovery
        var words = new ArrayList<LexedWord>();
        parseWords(line, 0,  words);
        return new ParsedLineLexedWords(words, cursor, line);
    }

    /**
     * Recognize words in a way we get reasonable auto completion boundaries
     */
    private void parseWords(String buffer, int position, List<LexedWord> words) {
        /** are we interpolating inside of a string */
        boolean inString = false;
        boolean inLocation = false;
        while (position < buffer.length()) {
            position = eatWhiteSpace(buffer, position);
            if (position >= buffer.length()) {
                return;
            }
            char c = buffer.charAt(position);
            boolean isWord = true;
            int wordEnd = position;
            if (c == '"' || (c == '>' && inString)) {
                wordEnd = parseEndedAfter(buffer, position, RASCAL_STRING);
                inString = wordEnd != buffer.length() && buffer.charAt(wordEnd - 1) != '"';
            }
            else if (c == '|' || (c == '>' && inLocation)) {
                wordEnd = parseEndedAfter(buffer, position, RASCAL_LOCATION);
                inLocation = wordEnd != buffer.length() && buffer.charAt(wordEnd - 1) != '|';
            }
            else if (Character.isJavaIdentifierPart(c) || c == '\\') {
                wordEnd = parseEndedAfter(buffer, position, RASCAL_NAME);
            }
            else if (c == ':' && words.isEmpty()) {
                // can be a command start
                wordEnd++;
            }
            else {
                wordEnd++;
                isWord = false;
            }

            if (wordEnd == position) {
                wordEnd = buffer.length();
            }

            if (isWord) {
                words.add(new LexedWord(buffer, position, wordEnd));
            }
            position = wordEnd;
        }
    }

    private final class ParsedLineLexedWords implements ParsedLine {
        private final ArrayList<LexedWord> words;
        private final int cursor;
        private final String line;
        private final @Nullable LexedWord atCursor;

        private ParsedLineLexedWords(ArrayList<LexedWord> words, int cursor, String line) {
            this.words = words;
            this.cursor = cursor;
            this.line = line;
            if (cursor >= (line.length() - 1)) {
                if (words.isEmpty() || !words.get(words.size() - 1).cursorInside(cursor)) {
                    words.add(new LexedWord(line + " ", cursor, cursor));
                }
            }

            atCursor = words.stream()
                    .filter(l -> l.cursorInside(cursor))
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public String word() {
            return atCursor == null ? "" : atCursor.word();
        }

        @Override
        public int wordCursor() {
            return atCursor == null ? 0 : (cursor - atCursor.begin);
        }

        @Override
        public int wordIndex() {
            return atCursor == null ? -1 : words.indexOf(atCursor);
        }

        @Override
        public List<String> words() {
            return words.stream()
                .map(LexedWord::word)
                .collect(Collectors.toList());
        }

        @Override
        public String line() {
            return line;
        }

        @Override
        public int cursor() {
            return cursor;
        }
    }


    private static class LexedWord {

        private final String buffer;
        private final int begin;
        private final int end;

        public LexedWord(String buffer, int begin, int end) {
            this.buffer = buffer;
            this.begin = begin;
            this.end = end;
        }

        public boolean cursorInside(int cursor) {
            return begin <= cursor && cursor <= end;
        }

        String word() {
            return buffer.substring(begin, end);
        }
    }

    private static int parseEndedAfter(String buffer, int position, Pattern parser) {
        var matcher = parser.matcher(buffer);
        matcher.region(position, buffer.length());
        if (!matcher.find()) {
            return position;
        }
        return matcher.end();
    }

    // strings with rudementary interpolation support
    private static final Pattern RASCAL_STRING 
        = Pattern.compile("^[\">]([^\"<\\\\]|([\\\\].))*([\"<]|$)");
    // locations with rudementary interpolation support
    private static final Pattern RASCAL_LOCATION 
        = Pattern.compile("^[\\|\\>][^\\|\\<\\t-\\n\\r ]*[\\|\\<]?");

    private static final Pattern RASCAL_NAME
        = Pattern.compile("^((([A-Za-z_][A-Za-z0-9_]*)|([\\\\][A-Za-z_]([\\-A-Za-z0-9_])*))(::)?)+");

    // only unicode spaces & multi-line comments
    private static final Pattern RASCAL_WHITE_SPACE
        = Pattern.compile("^(\\p{Zs}|([/][*]([^*]|([*][^/]))*[*][/]))*");

    private int eatWhiteSpace(String buffer, int position) {
        return parseEndedAfter(buffer, position, RASCAL_WHITE_SPACE);
    }

    private ParsedLine parseFullRascalCommand(String line, int cursor, boolean completeStatementMode)  throws SyntaxError {
        // TODO: to support inline highlighting, we have to remove the ansi escapes before parsing
        // so for now we don't do any highlighting, but would be interesting after the error recovery is integrated.
        // JV: We can also add the ANSI escapes to the layout definitions in the grammar.

        try {
            return translateTree(commandParser.apply(line), line, cursor);
        } 
        catch (ParseError pe) {
            if (!completeStatementMode) {
                return splitWordsOnly(line, cursor);
            }

            if (pe.getOffset() == line.length()) {
                throw new EOFError(pe.getBeginLine(), pe.getBeginColumn(), "Incomplete command");
            }
            else {
                throw new SyntaxError(pe.getBeginLine(), cursor, "Command not recognized");
            }
        } 
        catch (Throwable e) {
            throw new EOFError(1, 0, "Unexpected failure during parsing of the command: " + e.getMessage());
        }
    }

    private ParsedLine translateTree(ITree command, String line, int cursor) {
        // todo: return CompletingParsedLine so that we can also help with quoting completion
       var result = new ArrayList<LexedWord>();

       collectWords(command, result, line, 0);
       return new ParsedLineLexedWords(result, cursor, line);
    }

    private int collectWords(ITree t, List<LexedWord> words, String line, int offset) {
        boolean isWord;
        if (TreeAdapter.isLayout(t)) {
            isWord = false;
        }
        else if (TreeAdapter.isLexical(t) || TreeAdapter.isLiteral(t) || TreeAdapter.isCILiteral(t)) {
            isWord = true;
        } 
        else if (TreeAdapter.isSort(t) && TreeAdapter.getSortName(t).equals("QualifiedName")) {
            isWord = true;
        }
        else if (TreeAdapter.isSort(t)) {
            var loc = TreeAdapter.getLocation(t);
            isWord = false;
            for (var c : t.getArgs()) {
                if (c instanceof ITree) {
                    offset = collectWords((ITree)c, words, line, offset);
                }
            }
            return loc == null ? offset : (loc.getOffset() + loc.getLength());
        }
        else if (TreeAdapter.isTop(t)) {
            isWord = false;
            var args = t.getArgs();
            var preLoc = TreeAdapter.getLocation((ITree)args.get(0));
            offset += preLoc == null ? 0 : preLoc.getLength();
            offset = collectWords((ITree)args.get(1), words, line, offset);
            var postLoc = TreeAdapter.getLocation((ITree)args.get(2));
            return offset + (postLoc == null ? 0 : postLoc.getLength());
        }
        else {
            isWord = false;
        }

        var loc = TreeAdapter.getLocation(t);
        var length = loc == null ? TreeAdapter.yield(t).length() : loc.getLength();
        if (isWord) {
            words.add(new LexedWord(line, offset, offset + length));
        }
        return offset + length;

    }
}
