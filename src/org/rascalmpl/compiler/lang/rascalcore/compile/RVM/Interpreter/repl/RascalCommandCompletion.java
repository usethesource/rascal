package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl;

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
import org.rascalmpl.repl.CompletionFunction;
import org.rascalmpl.repl.CompletionResult;

public class RascalCommandCompletion {

    private static final Pattern splitCommand = Pattern.compile("^[\\t ]*(?<command>[a-z]*)([\\t ]|$)");
    
    public static CompletionResult complete(String line, int cursor, SortedSet<String> commandOptions, CompletionFunction completeIdentifier, CompletionFunction completeModule, CommandExecutor executor) {
        Matcher m = splitCommand.matcher(line);
        if (m.find()) {
            String currentCommand = m.group("command");
            switch(currentCommand) {
            
                case "set": {
                    OffsetLengthTerm identifier = StringUtils.findRascalIdentifierAtOffset(line, cursor);
                    if (identifier != null && identifier.offset > m.end("command")) {
                        int atArg = line.split("\\s+").length - 1;
                        Collection<String> suggestions = new TreeSet<String>();
                        if(atArg == 1){
                              suggestions = commandOptions.stream()
                              .filter(s -> s.startsWith(identifier.term))
                              .sorted()
                              .collect(Collectors.toList());
                        } else {
                          if("true".startsWith(identifier.term)){
                            suggestions.add("true");
                          } else if("false".startsWith(identifier.term)){
                            suggestions.add("false");
                          }
                        }
                        if (suggestions != null && ! suggestions.isEmpty()) {
                            return new CompletionResult(identifier.offset, suggestions);
                        }
                    }
                    else if (line.trim().equals("set")) {
                        return new CompletionResult(line.length(), commandOptions);
                    }
                    return null;
                }
                
                case "undeclare": {
                	OffsetLengthTerm identifier = StringUtils.findRascalIdentifierAtOffset(line, cursor);
                    if (identifier != null && identifier.offset > m.end("command")) {
                    	Collection<String> suggestions = executor.completeDeclaredIdentifier(identifier.term);
                    	if (suggestions != null && ! suggestions.isEmpty()) {
                            return new CompletionResult(identifier.offset, suggestions);
                        }
                    }
                    return null;
                }
                	
                case "edit": {
                    CompletionResult suggestions = executor.completeModule(line, cursor);
                    return suggestions;
                }
                case "unimport": 
               {
                	OffsetLengthTerm identifier = StringUtils.findRascalIdentifierAtOffset(line, cursor);
                    if (identifier != null && identifier.offset > m.end("command")) {
                    	//Collection<String> suggestions = executor.completeImportedIdentifier(identifier.term);
                        Collection<String> suggestions = executor.completeImportedIdentifier(identifier.term);
                    	if (suggestions != null && ! suggestions.isEmpty()) {
                            return new CompletionResult(identifier.offset, suggestions);
                        }
                    }
                    return null;
                }
               
                case "test": 
                    return executor.completeModule(line, cursor); 
                
                case "break":	return completeIdentifier.complete(line, cursor);
                
                default: {
                    if (CompiledRascalREPL.SHELL_VERBS.contains(currentCommand)) {
                        return null; // nothing to complete after a full command
                    }
                    List<String> result = null;
                    if (currentCommand.isEmpty()) {
                        result = new ArrayList<>(CompiledRascalREPL.SHELL_VERBS);
                    }
                    else {
                        result = CompiledRascalREPL.SHELL_VERBS.stream()
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
