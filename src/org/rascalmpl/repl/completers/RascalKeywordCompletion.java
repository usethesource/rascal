package org.rascalmpl.repl.completers;

import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

public class RascalKeywordCompletion implements Completer {
    
    private static final NavigableMap<String,String> RASCAL_TYPE_KEYWORDS;
    static {
        RASCAL_TYPE_KEYWORDS = new TreeMap<>();
        RASCAL_TYPE_KEYWORDS.put("void", "a type without any values");
        RASCAL_TYPE_KEYWORDS.put("int", "sequence of digits of arbitrary length");
        RASCAL_TYPE_KEYWORDS.put("real", "real numbers with arbitrary size and precision");
        RASCAL_TYPE_KEYWORDS.put("num", "int/real/rat type");
        RASCAL_TYPE_KEYWORDS.put("bool", "boolean type");
        RASCAL_TYPE_KEYWORDS.put("data", "user-defined type (Algebraic Data Type).");
        RASCAL_TYPE_KEYWORDS.put("datetime", "date/time/datetime values");
        RASCAL_TYPE_KEYWORDS.put("list", "ordered sequence of values");
        RASCAL_TYPE_KEYWORDS.put("lrel", "lists of tuples with relational calculus");
        RASCAL_TYPE_KEYWORDS.put("loc", "source locations");
        RASCAL_TYPE_KEYWORDS.put("map", "a set of key/value pairs");
        RASCAL_TYPE_KEYWORDS.put("node", "untyped trees");
        RASCAL_TYPE_KEYWORDS.put("set", "unordered sequence of values");
        RASCAL_TYPE_KEYWORDS.put("rel", "sets of tuples with relational calculus");
        RASCAL_TYPE_KEYWORDS.put("str", "a sequence of unicode codepoints");
        RASCAL_TYPE_KEYWORDS.put("tuple", "a sequence of elements");
        RASCAL_TYPE_KEYWORDS.put("value", "all possible values");
    }

    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        var words = line.words();
        if (words.size() == 1) {
            if ("import".startsWith(words.get(0))) {
                add(candidates, "import", "statement", "import a module into the repl");
            }
            if ("extend".startsWith(words.get(0))) {
                add(candidates, "extend", "statement", "extend a module into the repl");
            }
        }
        var firstWord = words.get(0);
        if (!firstWord.equals("import") && !firstWord.equals("extend") && !firstWord.equals(":")) {
            for (var can: RASCAL_TYPE_KEYWORDS.subMap(line.word(), true, line.word() + Character.MAX_VALUE, false).entrySet()) {
                add(candidates, can.getKey(), "type", can.getValue());
            }
        }
    }

    private static void add(List<Candidate> candidates, String value, String group, String description) {
        candidates.add(new Candidate(value, value, group, description, null, null, true));
    }
    
}
