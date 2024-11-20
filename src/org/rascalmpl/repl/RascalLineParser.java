package org.rascalmpl.repl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jline.reader.EOFError;
import org.jline.reader.ParsedLine;
import org.jline.reader.Parser;
import org.jline.reader.SyntaxError;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.NullRascalMonitor;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.TreeAdapter;

import io.usethesource.vallang.ISourceLocation;

public class RascalLineParser implements Parser {

    private static final ISourceLocation PROMPT_LOCATION = URIUtil.rootLocation("prompt");
    private final Supplier<IEvaluator<?>> eval;

    public RascalLineParser(Supplier<IEvaluator<?>> eval) {
        this.eval = eval;
    }

    @Override
    public ParsedLine parse(String line, int cursor, ParseContext context) throws SyntaxError {
        switch (context) {
            case UNSPECIFIED: // TODO: check if this is correct
            case ACCEPT_LINE:
                // we have to verify the input is correct rascal statement
                return parseFullRascalCommand(line, cursor);
            case COMPLETE:
                try {
                    // lets see, maybe it parses as a rascal expression
                    return parseFullRascalCommand(line, cursor);
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
        return new ParsedLine() {
            private final @MonotonicNonNull LexedWord atCursor = words.stream()
                    .filter(l -> l.cursorInside(cursor))
                    .findFirst()
                    .orElse(null);
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
            
        };
    }

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
                inString = wordEnd != position && buffer.charAt(wordEnd - 1) != '"';
                isWord = false;
            }
            else if (c == '|' || (c == '>' && inLocation)) {
                wordEnd = parseEndedAfter(buffer, position, RASCAL_LOCATION);
                inLocation = wordEnd != position && buffer.charAt(position - 1) == '<';
            }
            else if (Character.isJavaIdentifierPart(c) || c == ':' || c == '\\') {
                wordEnd = parseEndedAfter(buffer, position, RASCAL_NAME_OR_COMMAND);
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
            // TODO: review around edges
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
        = Pattern.compile("^[\">]([^\"<\\\\]|([\\\\].))*[\"<]");
    // locations with rudementary interpolation support
    private static final Pattern RASCAL_LOCATION 
        = Pattern.compile("^[\\|\\>][^\\|\\<\\t-\\n\\r ]*[\\|\\<]?");

    private static final Pattern RASCAL_NAME_OR_COMMAND
        = Pattern.compile("^(([:]?[A-Za-z_][A-Za-z0-9_]*)|([\\\\][A-Za-z_][\\-A-Za-z0-9_]*))");

    // only unicode spaces & multi-line comments
    private static final Pattern RASCAL_WHITE_SPACE
        = Pattern.compile("^(\\p{Zs}|([/][*]([^*]|([*][^/]))*[*][/]))*");

    private int eatWhiteSpace(String buffer, int position) {
        return parseEndedAfter(buffer, position, RASCAL_WHITE_SPACE);
    }

    private ParsedLine parseFullRascalCommand(String line, int cursor)  throws SyntaxError {
        // TODO: to support inline highlighting, we have to remove the ansi escapes before parsing
        try {
            return translateTree(eval.get().parseCommand(new NullRascalMonitor(), line, PROMPT_LOCATION), line, cursor);
        } 
        catch (ParseError pe) {
            throw new EOFError(pe.getBeginLine(), pe.getBeginColumn(), "Parse error");
        } 
        catch (Throwable e) {
            throw new EOFError(-1, -1, "Unexpected failure during pasing of the command: " + e.getMessage());
        }
    }

    private ParsedLine translateTree(ITree command, String line, int cursor) {
        // todo: return CompletingParsedLine so that we can also help with quoting completion
        return new ParsedLine() {
            List<ITree> wordList = null;
            ITree wordTree = null;
            
            private ITree cursorTree() {
                if (wordTree == null) {
                    wordTree = (ITree)TreeAdapter.locateLexical(command, cursor);
                    if (wordTree == null) {
                        wordTree = command;
                    }
                }
                return wordTree;
            }

            private List<ITree> wordList() {
                if (wordList == null) {
                    wordList = new ArrayList<>();
                    collectWords(command, wordList);
                }
                return wordList;
            }

            private void collectWords(ITree t, List<ITree> words) {
                if (TreeAdapter.isLexical(t)) {
                    words.add(t);
                }
                else if (TreeAdapter.isSort(t)) {
                    for (var c : t.getArgs()) {
                        if (c instanceof ITree) {
                            collectWords((ITree)c, words);
                        }
                    }
                }
            }

            @Override
            public String word() {
                return TreeAdapter.yield(cursorTree());
            }

            @Override
            public int wordCursor() {
                return cursor - TreeAdapter.getLocation(cursorTree()).getOffset();
            }

            @Override
            public int wordIndex() {
                return wordList().indexOf(cursorTree());
            }

            @Override
            public List<String> words() {
                return wordList()
                    .stream()
                    .map(TreeAdapter::yield)
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
        };
    }
}
