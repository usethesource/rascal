package org.rascalmpl.repl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import jline.Terminal;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.io.StandardTextWriter;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.result.IRascalResult;
import org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages;
import org.rascalmpl.interpreter.utils.StringUtils;
import org.rascalmpl.interpreter.utils.StringUtils.OffsetLengthTerm;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.RascalValueFactory;
import org.rascalmpl.values.uptr.TreeAdapter;

public abstract class BaseRascalREPL extends BaseREPL {
    protected enum State {
        FRESH,
        CONTINUATION,
        DEBUG,
        DEBUG_CONTINUATION
    }

    private State currentState = State.FRESH;
    
    protected State getState() {
        return currentState;
    }

  private final static int LINE_LIMIT = 200;
  private final static int CHAR_LIMIT = LINE_LIMIT * 20;
  protected String currentPrompt = ReadEvalPrintDialogMessages.PROMPT;
  private StringBuffer currentCommand;
  private final StandardTextWriter indentedPrettyPrinter;
  private final StandardTextWriter singleLinePrettyPrinter;
  
  public BaseRascalREPL(InputStream stdin, OutputStream stdout, boolean prettyPrompt, boolean allowColors, File persistentHistory,Terminal terminal)
      throws IOException {
    super(stdin, stdout, prettyPrompt, allowColors, persistentHistory, terminal);
    if (terminal.isAnsiSupported() && allowColors) {
      indentedPrettyPrinter = new ReplTextWriter();
      singleLinePrettyPrinter = new ReplTextWriter(false);
    }
    else {
      indentedPrettyPrinter = new StandardTextWriter();
      singleLinePrettyPrinter = new StandardTextWriter(false);
    }
  }

  @Override
  protected String getPrompt() {
    return currentPrompt;
  }

  @Override
  protected void handleInput(String line) throws InterruptedException {
    assert line != null;

    try {
      if (line.trim().length() == 0) {
        // cancel command
        getErrorWriter().println(ReadEvalPrintDialogMessages.CANCELLED);
        currentPrompt = ReadEvalPrintDialogMessages.PROMPT;
        currentCommand = null;
        currentState = State.FRESH;
        return;
      }
      if (currentCommand == null) {
        // we are still at a new command so let's see if the line is a full command
        if (isStatementComplete(line)) {
          printResult(evalStatement(line, line));
        }
        else {
          currentCommand = new StringBuffer(line);
          currentPrompt = ReadEvalPrintDialogMessages.CONTINUE_PROMPT;
          currentState = State.CONTINUATION;
          return;
        }
      }
      else {
        currentCommand.append('\n');
        currentCommand.append(line);
        if (isStatementComplete(currentCommand.toString())) {
          printResult(evalStatement(currentCommand.toString(), line));
          currentPrompt = ReadEvalPrintDialogMessages.PROMPT;
          currentCommand = null;
          currentState = State.FRESH;
          return;
        }
      }
    } catch (IOException ie) {
      throw new RuntimeException(ie);
    }
  }
  
  private void printResult(IRascalResult result) throws IOException {
    if (result == null) {
      return;
    }
    PrintWriter out = getOutputWriter();
    IValue value = result.getValue();
    if (value == null) {
      out.println("ok");
      out.flush();
      return;
    }
    Type type = result.getType();

    if (type.isAbstractData() && type.isSubtypeOf(RascalValueFactory.Tree)) {
    	out.print(type.toString());
        out.print(": ");
      // we first unparse the tree
      out.print("`");
      TreeAdapter.yield((IConstructor)result.getValue(), true, out);
      out.println("`");
      // write parse tree out one a single line for reference
      out.print("Tree: ");
      try (Writer wrt = new LimitedWriter(out, CHAR_LIMIT)) {
    	  singleLinePrettyPrinter.write(value, wrt);
      }
    }
    else {
    	out.print(type.toString());
    	out.print(": ");
    	// limit both the lines and the characters
    	try (Writer wrt = new LimitedWriter(new LimitedLineWriter(out, LINE_LIMIT), CHAR_LIMIT)) {
    		indentedPrettyPrinter.write(value, wrt);
    	}
    }
    out.println();
    out.flush();
  }

  protected abstract PrintWriter getErrorWriter();
  protected abstract PrintWriter getOutputWriter();

  protected abstract boolean isStatementComplete(String command);
  protected abstract IRascalResult evalStatement(String statement, String lastLine) throws InterruptedException;
  
  protected abstract Collection<String> completePartialIdentifier(String qualifier, String identifier);
  protected abstract Collection<String> completeModule(String qualifier, String partialModuleName);
  
  @Override
  protected CompletionResult completeFragment(String line, int cursor) {
      if (currentState == State.FRESH) {
          String trimmedLine = line.trim();
          if (trimmedLine.startsWith(":")) {
              return completeREPLCommand(line, cursor);
          }
          if (trimmedLine.startsWith("import ") || trimmedLine.startsWith("extend ")) {
              return completeModule(line, cursor);
          }
      }
      int locationStart = StringUtils.findRascalLocationStart(line, cursor);
      if (locationStart != -1) {
          return completeLocation(line, locationStart);
      }
      return completeIdentifier(line, cursor);
  }

  private CompletionResult completeIdentifier(String line, int cursor) {
      OffsetLengthTerm identifier = StringUtils.findRascalIdentifierAtOffset(line, cursor);
      if (identifier != null) {
          String[] qualified = StringUtils.splitQualifiedName(identifier.term);
          String qualifier = qualified.length == 2 ? qualified[0] : "";
          String qualifee = qualified.length == 2 ? qualified[1] : qualified[0];
          Collection<String> suggestions = completePartialIdentifier(qualifier, qualifee);
          if (suggestions != null && ! suggestions.isEmpty()) {
              return new CompletionResult(identifier.offset, suggestions);
          }
      }
      return null;
  }
  
  @Override
  protected boolean supportsCompletion() {
      return true;
  }
  
  @Override
  protected boolean printSpaceAfterFullCompletion() {
      return false;
  }
  
  private CompletionResult completeLocation(String line, int locationStart) {
      int locationEnd = StringUtils.findRascalLocationEnd(line, locationStart);
      try {
          String locCandidate = line.substring(locationStart + 1, locationEnd + 1);
          if (!locCandidate.contains("://")) {
              return null;
          }
          ISourceLocation directory = ValueFactoryFactory.getValueFactory().sourceLocation(new URI(locCandidate));
          String fileName = "";
          URIResolverRegistry reg = URIResolverRegistry.getInstance();
          if (!reg.isDirectory(directory)) {
              // split filename and directory
              String fullPath = directory.getPath();
              int lastSeparator = fullPath.lastIndexOf('/');
              fileName = fullPath.substring(lastSeparator +  1);
              fullPath = fullPath.substring(0, lastSeparator);
              directory = ValueFactoryFactory.getValueFactory().sourceLocation(directory.getScheme(), directory.getAuthority(), fullPath);
              if (!reg.isDirectory(directory)) {
                  return null;
              }
          }
          // prefix is the directory location minus the |'s
          String prefix = directory.toString().substring(1);
          prefix = prefix.substring(0, prefix.length() - 1);
          if (!prefix.endsWith("/")) {
              prefix += "/";
          }
          String[] filesInPath = reg.listEntries(directory);
          Set<String> result = new TreeSet<>(); // sort it up
          for (String currentFile : filesInPath) {
              if (currentFile.startsWith(fileName)) {
                  result.add(prefix + currentFile);
              }
          }
          return new CompletionResult(locationStart + 1, result);
      }
      catch (URISyntaxException|IOException e) {
          return null;
      }
  }

  private CompletionResult completeModule(String line, int cursor) {
      OffsetLengthTerm identifier = StringUtils.findRascalIdentifierAtOffset(line, line.length());
      if (identifier != null) {
          String[] qualified = StringUtils.splitQualifiedName(identifier.term);
          String qualifier = qualified.length == 2 ? qualified[0] : "";
          String qualifee = qualified.length == 2 ? qualified[1] : qualified[0];
          Collection<String> suggestions = completeModule(qualifier, qualifee);
          if (suggestions != null && ! suggestions.isEmpty()) {
              return new CompletionResult(identifier.offset, suggestions);
          }
      }
      return null;
  }
  

  private CompletionResult completeREPLCommand(String line, int cursor) {
      return RascalCommandCompletion.complete(line, cursor, (l,i) -> completeIdentifier(l,i), (l,i) -> completeModule(l,i));
  }
}
