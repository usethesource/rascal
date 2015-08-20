package org.rascalmpl.repl;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

import jline.Terminal;
import jline.console.ConsoleReader;
import jline.console.completer.Completer;

import org.apache.commons.compress.utils.Charsets;
import org.fusesource.jansi.Ansi;

public abstract class BaseREPL {
  protected final ConsoleReader reader;
  private final OutputStream originalStdOut;
  protected final boolean prettyPrompt;
  protected final boolean allowColors;
  protected final Writer stdErr;
  protected volatile boolean keepRunning = true;

  public BaseREPL(InputStream stdin, OutputStream stdout, boolean prettyPrompt, boolean allowColors, Terminal terminal) throws IOException {
    this.originalStdOut = stdout;
    this.reader = new ConsoleReader(stdin, stdout);//, terminal);
    prettyPrompt = prettyPrompt && terminal.isAnsiSupported();
    this.prettyPrompt = prettyPrompt;
    this.allowColors = allowColors;
    if (prettyPrompt && allowColors) {
      this.stdErr = new RedErrorWriter(reader.getOutput());
    }
    else if (prettyPrompt) {
      this.stdErr = new ItalicErrorWriter(reader.getOutput());
    }
    else {
      this.stdErr = new FilterWriter(reader.getOutput()) { }; // create a basic wrapper to avoid locking on stdout and stderr
    }
    initialize(reader.getOutput(), stdErr);
    if (supportsCompletion()) {
      reader.addCompleter(new Completer(){
        @Override
        public int complete(String buffer, int cursor, List<CharSequence> candidates) {
          CompletionResult res = completeFragment(buffer, cursor);
          candidates.clear();
          if (res != null && res.getOffset() > -1 && !res.getSuggestions().isEmpty()) {
            candidates.addAll(res.getSuggestions());
            return res.getOffset();
          }
          return -1;
        }
      });
      
    }
  }


  protected abstract void initialize(Writer stdout, Writer stderr);
  protected abstract String getPrompt();
  protected abstract void handleInput(String line) throws InterruptedException;
  protected abstract boolean supportsCompletion();
  protected abstract CompletionResult completeFragment(String line, int cursor);
  
  private String previousPrompt = "";
  private static final String PRETTY_PROMPT_PREFIX = Ansi.ansi().reset().bold().toString();
  private static final String PRETTY_PROMPT_POSTFIX = Ansi.ansi().boldOff().reset().toString();

  protected void updatePrompt() {
    String newPrompt = getPrompt();
    if (!newPrompt.equals(previousPrompt)) {
      previousPrompt = newPrompt;
      if (prettyPrompt) {
        reader.setPrompt(PRETTY_PROMPT_PREFIX + newPrompt + PRETTY_PROMPT_POSTFIX);
      }
      else {
        reader.setPrompt(newPrompt);
      }
    }
  }
  
  /**
   * This will run the console in the current thread, and will block until it is either:
   * <ul>
   *  <li> stopped using .stop() (from another thread!)
   *  <li> handleInput throws an exception.
   *  <li> either the input or output stream throws an IOException 
   * </ul>
   */
  public void run() throws IOException {
    try {
      while(keepRunning) {
        updatePrompt();
        String line = reader.readLine();
        if (line == null) { // EOF
          break;
        }
        handleInput(line);
      }
    }
    catch (IOException e) {
      try (PrintWriter err = new PrintWriter(stdErr, true)) {
        err.println("REPL Failed: ");
        if (!err.checkError()) {
          e.printStackTrace(err);
        }
        else {
          e.printStackTrace();
        }
      }
      throw e;
    }
    catch (InterruptedException e) {
      // we are closing down, so do nothing, the finally clause will take care of it
    }
    finally {
      reader.getOutput().flush();
      originalStdOut.flush();
      reader.shutdown();
    }
  }
  
  /**
   * stop the REPL without waiting for it to stop
   */
  public void stop() {
    keepRunning = false;
    reader.shutdown();
  }
}
