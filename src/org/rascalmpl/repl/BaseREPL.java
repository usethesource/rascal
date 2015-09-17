package org.rascalmpl.repl;

import java.io.File;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import jline.Terminal;
import jline.console.ConsoleReader;
import jline.console.completer.Completer;
import jline.console.history.FileHistory;
import jline.internal.ShutdownHooks;
import jline.internal.ShutdownHooks.Task;

import org.fusesource.jansi.Ansi;

public abstract class BaseREPL {
  protected final ConsoleReader reader;
  private final OutputStream originalStdOut;
  protected final boolean prettyPrompt;
  protected final boolean allowColors;
  protected final Writer stdErr;
  protected volatile boolean keepRunning = true;
  private volatile Task historyFlusher = null;
  private volatile FileHistory history = null;
  private final Queue<String> commandQueue = new ConcurrentLinkedQueue<String>();

  public BaseREPL(InputStream stdin, OutputStream stdout, boolean prettyPrompt, boolean allowColors, File persistentHistory, Terminal terminal) throws IOException {
    this.originalStdOut = stdout;
    reader = new ConsoleReader(stdin, stdout, terminal);
    if (persistentHistory != null) {
      history = new FileHistory(persistentHistory);
      reader.setHistory(history);
      historyFlusher = new Task() {
        @Override
        public void run() throws Exception {
          history.flush();
        }
      };
      ShutdownHooks.add(historyFlusher);
    }
    reader.setExpandEvents(false);

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


  /**
   * During the constructor call initialize is called after the REPL is setup enough to have a stdout and std err to write to.
   * @param stdout the output stream to write normal output to.
   * @param stderr the error stream to write error messages on, depending on the environment and options passed, will print in red.
   */
  protected abstract void initialize(Writer stdout, Writer stderr);

  /**
   * Will be called everytime a new prompt is printed.
   * @return The string representing the prompt.
   */
  protected abstract String getPrompt();

  /**
   * After a newline is pressed, the current line is handed to this method.
   * @param line the current line entered.
   * @throws InterruptedException throw this exception to stop the REPL (instead of calling .stop())
   */
  protected abstract void handleInput(String line) throws InterruptedException;
  
  /**
   * Test if completion of statement in the current line is supported
   * @return true if the completeFragment method can provide completions
   */
  protected abstract boolean supportsCompletion();
  
  /**
   * If a user hits the TAB key, the current line and the offset is provided to try and complete a fragment of the current line.
   * @param line The current line.
   * @param cursor The cursor offset in the line.
   * @return suggestions for the line.
   */
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
   * Queue a command (separated by newlines) to be "entered"
   */
  public void queueCommand(String command) {
    commandQueue.addAll(Arrays.asList(command.split("[\\n\\r]")));
  }
  
  /**
   * This will run the console in the current thread, and will block until it is either:
   * <ul>
   *  <li> handleInput throws an InteruptedException.
   *  <li> input reaches the end of the stream
   *  <li> either the input or output stream throws an IOException 
   * </ul>
   */
  public void run() throws IOException {
    try {
      updatePrompt();
      while(keepRunning) {
        boolean handledQueue = false;
        String queuedCommand;
        while ((queuedCommand = commandQueue.poll()) != null) {
          handledQueue = true;
          reader.resetPromptLine(reader.getPrompt(), queuedCommand, 0);
          reader.println();
          reader.getHistory().add(queuedCommand);
          handleInput(queuedCommand);
        }
        if (handledQueue) {
          String oldPrompt = reader.getPrompt();
          reader.resetPromptLine("", "", 0);
          reader.setPrompt(oldPrompt);
        }

        updatePrompt();
        String line = reader.readLine(reader.getPrompt(), null, null);
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
        err.flush();
        stdErr.flush();
      }
      throw e;
    }
    catch (InterruptedException e) {
      // we are closing down, so do nothing, the finally clause will take care of it
    }
    finally {
      reader.getOutput().flush();
      originalStdOut.flush();
      if (historyFlusher != null) {
        ShutdownHooks.remove(historyFlusher);
        history.flush();
      }
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
