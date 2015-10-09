package org.rascalmpl.repl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.rascalmpl.interpreter.utils.StringUtils;
import org.rascalmpl.interpreter.utils.StringUtils.OffsetLengthTerm;

public class RascalCommandCompletion {
    private static final TreeSet<String> COMMAND_KEYWORDS;
    static {
        COMMAND_KEYWORDS = new TreeSet<>();
        COMMAND_KEYWORDS.add("set");
        COMMAND_KEYWORDS.add("undeclare");
        COMMAND_KEYWORDS.add("help");
        COMMAND_KEYWORDS.add("edit");
        COMMAND_KEYWORDS.add("unimport");
        COMMAND_KEYWORDS.add("declarations");
        COMMAND_KEYWORDS.add("quit");
        COMMAND_KEYWORDS.add("history");
        COMMAND_KEYWORDS.add("test");
        COMMAND_KEYWORDS.add("modules");
        COMMAND_KEYWORDS.add("clear");
    }


    private static final Pattern splitCommand = Pattern.compile("^[\\t ]*:(?<command>[a-z]*)([\\t ]|$)");
    public static CompletionResult complete(String line, int cursor, SortedSet<String> commandOptions, CompletionFunction completeIdentifier, CompletionFunction completeModule) {
        assert line.trim().startsWith(":");
        Matcher m = splitCommand.matcher(line);
        if (m.find()) {
            String currentCommand = m.group("command");
            switch(currentCommand) {
                case "set": {
                    OffsetLengthTerm identifier = StringUtils.findRascalIdentifierAtOffset(line, cursor);
                    if (identifier != null && identifier.offset > m.end("command")) {
                        Collection<String> suggestions = commandOptions.stream()
                                        .filter(s -> s.startsWith(identifier.term))
                                        .sorted()
                                        .collect(Collectors.toList());
                        if (suggestions != null && ! suggestions.isEmpty()) {
                            return new CompletionResult(identifier.offset, suggestions);
                        }
                    }
                    else if (line.trim().equals(":set")) {
                        return new CompletionResult(line.length(), commandOptions);
                    }
                    return null;
                }
                case "undeclare": return completeIdentifier.complete(line, cursor);
                case "edit":
                case "unimport": return completeModule.complete(line, line.length());
                default: {
                    if (COMMAND_KEYWORDS.contains(currentCommand)) {
                        return null; // nothing to complete after a full command
                    }
                    List<String> result = null;
                    if (currentCommand.isEmpty()) {
                        result = new ArrayList<>(COMMAND_KEYWORDS);
                    }
                    else {
                        result = COMMAND_KEYWORDS.stream()
                                        .filter(s -> s.startsWith(currentCommand))
                                        .collect(Collectors.toList());
                    }
                    if (!result.isEmpty()) {
                        return new CompletionResult(m.start("command"), result);
                    }
                }
            }
        }
        return null;
    }

}
