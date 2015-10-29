package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl;

import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.parseErrorMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.staticErrorMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.throwMessage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import org.rascalmpl.interpreter.control_exceptions.QuitException;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.result.IRascalResult;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.interpreter.utils.Timing;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.repl.BaseRascalREPL;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.Type;

import jline.Terminal;

public abstract class CompiledRascalREPL extends BaseRascalREPL {

  protected CommandExecutor executor;
  private boolean measureCommandTime;
  private boolean semiColonAdded = false;
  
  public CompiledRascalREPL(InputStream stdin, OutputStream stdout, boolean prettyPrompt, boolean allowColors, File persistentHistory, Terminal terminal)
      throws IOException {
    super(stdin, stdout, prettyPrompt, allowColors, persistentHistory, terminal);
  }

  public void setMeasureCommandTime(boolean measureCommandTime) {
    this.measureCommandTime = measureCommandTime;
  }
  
  public boolean getMeasureCommandTime() {
    return measureCommandTime;
  }
  
  @Override
  protected void cancelRunningCommandRequested() {
      // TODO: interrupt the RVM interpreter or the running compiler. After the intteruption be ready for new commands
      // reminder: this method is called from a different thread.
      // don't wait for the interruption to happen, that will cause deadlocks, just trigger a flag or something like it
  }
  
  @Override
  protected void terminateRequested() {
      // TODO: stop the RVM interpreted or the running compiler, don't worry about the state afterward.
      // reminder: this method is called from a different thread.
      // don't wait for the interruption to happen, that will cause deadlocks, just trigger a flag or something like it
  }
  
  @Override
  protected void stackTraceRequested() {
      // TODO: print current stack trace, without stopping  the running code.
      // reminder: this method is called from a different thread.
  }

  @Override
  protected void initialize(Writer stdout, Writer stderr) {
    executor = constructExecutor(stdout, stderr);
  }
  
  protected abstract CommandExecutor constructExecutor(Writer stdout, Writer stderr);
  
  @Override
  protected PrintWriter getErrorWriter() {
    return executor.getStdErr();
  }
  
  @Override
  protected PrintWriter getOutputWriter() {
    return executor.getStdOut();
  }

  @Override
  protected IRascalResult evalStatement(String statement, String lastLine) throws InterruptedException {
	  try {
		  if(semiColonAdded){
			  statement = statement + ";";
			  semiColonAdded = false;
		  }
		  if(statement.trim().equals(":quit")){
			  stop();
			  return null;
		  }
		  Timing tm = new Timing();
		  tm.start();
		  IValue value = executor.eval(null, statement, URIUtil.rootLocation("prompt"));
		  long duration = tm.duration();
		  if (measureCommandTime) {
			  executor.getStdErr().println("Time: " + (duration / 1000000) + "ms");
		  }
		  return new IRascalResult() {

			  @Override
			  public IValue getValue() {
				  return value;
			  }

			  @Override
			  public Type getType() {
				  return value.getType();	// TODO: change to static type?
			  }
		  };
	  }
	  catch (ParseError pe) {
		  executor.getStdErr().println(parseErrorMessage(lastLine, "prompt", pe));
		  return null;
	  }
	  catch (StaticError e) {
		  executor.getStdErr().println(staticErrorMessage(e));
		  return null;
	  }
	  catch (Throw e) {
		  executor.getStdErr().println(throwMessage(e));
		  return null;
	  }
	  catch (QuitException q) {
		  executor.getStdErr().println("Quiting REPL");
		  throw new InterruptedException();
	  }
	  //    catch (Throwable e) {
	  //      eval.getStdErr().println(throwableMessage(e, eval.getStackTrace()));
	  //      return null;
	  //    }
  }

  @Override
  protected boolean isStatementComplete(String command) {
	  try {
		  executor.parseCommand(null, command, URIUtil.rootLocation("prompt"));
	  }
	  catch (ParseError pe) {
		  String[] commandLines = command.split("\n");
		  int lastLine = commandLines.length;
		  int lastColumn = commandLines[lastLine - 1].length();

		  if (pe.getEndLine() + 1 == lastLine && lastColumn < pe.getEndColumn()) { 
			  semiColonAdded = false;
			  return false;
		  }
		  if (pe.getEndLine() + 1 == lastLine && lastColumn == pe.getEndColumn()) { 
			  semiColonAdded = true;
			  return isStatementComplete(command + ";");
		  }
	  }
	  return true;
  }

  @Override
  protected Collection<String> completePartialIdentifier(String qualifier, String term) {
      return executor.completePartialIdentifier(qualifier, term);
  }
  
  private static final SortedSet<String> commandLineOptions = new TreeSet<String>();
  static {
     commandLineOptions.add("profiling"); 
     commandLineOptions.add("tracing"); 
     commandLineOptions.add("coverage"); 
     commandLineOptions.add("debug"); 
     commandLineOptions.add("testsuite"); 
  }
  @Override
  protected SortedSet<String> getCommandLineOptions() {
      return commandLineOptions;
  }
  
  @Override
    protected Collection<String> completeModule(String qualifier, String partialModuleName) {
        return null;
    }
}
