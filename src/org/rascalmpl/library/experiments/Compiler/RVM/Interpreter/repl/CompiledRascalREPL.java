package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl;

import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.parseErrorMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.staticErrorMessage;
import static org.rascalmpl.interpreter.utils.ReadEvalPrintDialogMessages.thrownMessage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.rascalmpl.interpreter.control_exceptions.QuitException;
import org.rascalmpl.interpreter.result.IRascalResult;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.interpreter.utils.Timing;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Thrown;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.IDEServices;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.repl.BaseRascalREPL;
import org.rascalmpl.repl.CompletionResult;
import org.rascalmpl.uri.URIUtil;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;
import jline.Terminal;

public abstract class CompiledRascalREPL extends BaseRascalREPL {

  protected CommandExecutor executor;
  private boolean measureCommandTime;
//  private boolean semiColonAdded = false;
  
  public static final TreeSet<String> SHELL_VERBS;
  
  static {
	  	String[] shellVerbValues = {
		  // General commands
		  "apropos", "clean", "declarations", "edit", "help",  /*"modules",*/ "quit", "set", "test", "undeclare", "unimport",    
		  // Debugging commands
		  "break", "clear", "disable", "enable", "ignore"
	  	};
	  	SHELL_VERBS = new TreeSet<String>(Arrays.asList(shellVerbValues));
  }
  
  private PathConfig pcfg;
  protected final IDEServices ideServices;
  
  public CompiledRascalREPL(PathConfig pcfg, boolean prettyPrompt, boolean allowColors, boolean htmlOutput, File persistentHistory, IDEServices ideServices)
      throws IOException, URISyntaxException {
    super(prettyPrompt, allowColors, htmlOutput);
    this.pcfg = pcfg;
    this.ideServices = ideServices;
  }
  
  @Override
  protected boolean isREPLCommand(String line){
      if(line.length() > 0){
          int idx = line.indexOf(" ");
          if(idx > 0){
              return SHELL_VERBS.contains(line.substring(0,  idx));
          }
          return SHELL_VERBS.contains(line);
      }
	 return false;
  }
  
  @Override
  protected PrintWriter getErrorWriter() {
    return executor.getStdErr();
  }
  
  @Override
  protected PrintWriter getOutputWriter() {
    return executor.getStdOut();
  }

  public void setMeasureCommandTime(boolean measureCommandTime) {
    this.measureCommandTime = measureCommandTime;
  }
  
  public boolean getMeasureCommandTime() {
    return measureCommandTime;
  }
  
  @Override
  public void cancelRunningCommandRequested() {
 	  stop();
  }
  
  @Override
  public void terminateRequested() {
      stop();
  }
  
  @Override
  public void stackTraceRequested() {
      // TODO: print current stack trace, without stopping  the running code.
      // reminder: this method is called from a different thread.
  }

  @Override
  public void initialize(Writer stdout, Writer stderr) {
    try {
        executor = constructCommandExecutor(pcfg, new PrintWriter(stdout), new PrintWriter(stderr), ideServices);
    } catch (NoSuchRascalFunction | IOException | URISyntaxException e) {
        throw new RuntimeException(e);
    }
  }
  
  protected abstract CommandExecutor constructCommandExecutor(PathConfig pcfg, PrintWriter stdout, PrintWriter stderr, IDEServices ideServices) throws IOException, NoSuchRascalFunction, URISyntaxException;
  
  
  @Override
  public boolean isStatementComplete(String command) {
	  return executor.isStatementComplete(command);
  }

  @Override
  public IRascalResult evalStatement(String statement, String lastLine) throws InterruptedException {
	  try {
		  if(executor.semiColonAdded){
			  statement = statement + ";";
			  executor.semiColonAdded = false;
		  }
		  String[] words = statement.split(" ");
		  if(words.length > 0 && SHELL_VERBS.contains(words[0])){
			  if(words[0].equals("quit")){
			      executor.shutdown();
				  stop();
				  return null;
			  }
			  executor.evalShellCommand(words);
			  return null;
		  } else {

			  Timing tm = new Timing();
			  tm.start();
			  IValue value = executor.eval(statement, URIUtil.rootLocation("prompt"));
			  if(value != null){
			    Type tp = value.getType();
			    if(tp.isAbstractData() && tp.getName().equals("RuntimeException")){
			      throw new RascalShellExecutionException("Error: " + value.toString());
			    }
			  }
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
	  }
	  catch (ParseError pe) {
	      parseErrorMessage(executor.getStdErr(), lastLine, "prompt", pe, indentedPrettyPrinter);
		  return null;
	  }
	  catch (StaticError e) {
	      staticErrorMessage(executor.getStdErr(), e, indentedPrettyPrinter);
		  return null;
	  }
	  catch (Thrown e) {
	      thrownMessage(executor.getStdErr(), e, indentedPrettyPrinter);
		  return null;
	  }
	  catch (QuitException q) {
		  executor.getStdErr().println("Quiting REPL");
		  throw new InterruptedException();
	  }
	  catch (RascalShellExecutionException e) {
	    executor.getStdErr().println(e.getMessage());
	    return null;
	  }
    }

  @Override
  public boolean printSpaceAfterFullCompletion() {
      return false;
  }
  
  @Override
  protected Collection<String> completeModule(String qualifier, String partialModuleName) {
      List<String> entries = pcfg.listModuleEntries(qualifier);
      if (entries != null && entries.size() > 0) {
          if (entries.contains(partialModuleName)) {
              // we have a full directory name (at least the option)
              List<String> subEntries = pcfg.listModuleEntries(qualifier + "::" + partialModuleName);
              if (subEntries != null) {
                  entries.remove(partialModuleName);
                  subEntries.forEach(e -> entries.add(partialModuleName + "::" + e));
              }
          }
          return entries.stream()
                          .filter(m -> m.startsWith(partialModuleName))
                          .map(s -> qualifier.isEmpty() ? s : qualifier + "::" + s)
                          .sorted()
                          .collect(Collectors.toList());
      }
      return null;
  }
  
  @Override
  protected Collection<String> completePartialIdentifier(String line, int cursor, String qualifier, String term) {
     Collection<String> result = executor.completePartialIdentifier(qualifier, term);
     if(result == null){
    	 result = new TreeSet<String>();
     }
     if(cursor == term.length()){
    	 for(String verb : SHELL_VERBS){
    		 if(verb.startsWith(term)){
    			 result.add(verb);
    		 }
    	 }
     }
     return result;
  }
  
  private static final SortedSet<String> commandLineOptions = new TreeSet<String>();
  static {
     commandLineOptions.add("profile"); 
     commandLineOptions.add("trace"); 
     commandLineOptions.add("coverage");  
  }
  @Override
  protected SortedSet<String> getCommandLineOptions() {
      return commandLineOptions;
  }
  
  @Override
  protected CompletionResult completeREPLCommand(String line, int cursor) {
      return RascalCommandCompletion.complete(line, cursor, commandLineOptions, (l,i) -> completeIdentifier(l,i).joinWith(completeModule(l, i)), (l,i) -> completeModule(l,i), executor);
  }
  
}
