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

import org.fusesource.jansi.Ansi;

public abstract class BaseREPL {
  protected final ConsoleReader reader;
  protected final boolean prettyPrompt;
  protected final boolean allowColors;
  protected final Writer stdErr;
  protected volatile boolean keepRunning = true;
  protected volatile boolean stopped = false;

  public BaseREPL(InputStream stdin, OutputStream stdout, boolean prettyPrompt, boolean allowColors, Terminal terminal) throws IOException {
    this.reader = new ConsoleReader(stdin, stdout, terminal);
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
            String fragment = buffer.substring(res.getOffset(), res.getOffset() + res.getLength());
            boolean removePrefix = res.getSuggestions().stream().findAny().orElse("").startsWith(fragment);
            if (removePrefix) {
              res.getSuggestions().stream()
                .map(s -> s.substring(res.getLength()))
                .forEachOrdered(s -> candidates.add(s));
            }
            else {
              candidates.addAll(res.getSuggestions());
            }
            return res.getOffset() + res.getLength();
          }
          return -1;
        }
      });
      
    }
  }


  protected abstract void initialize(Writer stdout, Writer stderr);
  protected abstract String getPrompt();
  protected abstract void handleInput(String line);
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
  
  private Thread runningThread = null;
  /**
   * This will run the console in the current thread, and will block until it is either:
   * <ul>
   *  <li> stopped using .stop() (from another thread!)
   *  <li> handleInput throws an exception.
   *  <li> either the input or output stream throws an IOException 
   * </ul>
   */
  public void run() {
    try {
      runningThread = Thread.currentThread();
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
      try (PrintWriter err = new PrintWriter(stdErr)) {
        err.println("REPL Failed: ");
        e.printStackTrace(err);
      }
    }
    finally {
      stopped = true;
      reader.shutdown();
    }
  }
  
  /**
   * Stop the REPL, normally it will block until the REPL is stopped, except:
   * <ul>
   *  <li> stop is called from the same thread as run is on (e.g. run is higher up the stack somewhere)
   *  <li> run was never called
   * </ul>
   */
  public void stop() {
    keepRunning = false;
    try {
      reader.getInput().close();
      if (runningThread != null && runningThread != Thread.currentThread()) {
        while (!stopped) Thread.yield();
      }
    }
    catch (IOException e) {
    }
  }
  
  /**
   * stop the REPL without waiting for it to stop
   */
  public void signalStop() {
    keepRunning = false;
    try {
      reader.getInput().close();
    }
    catch (IOException e) {
    }
  }
  
  public boolean isStopped() {
    return stopped;
  }

}
