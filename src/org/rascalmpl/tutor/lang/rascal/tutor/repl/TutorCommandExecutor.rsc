module lang::rascal::tutor::repl::TutorCommandExecutor

import util::Reflective;

@synopsis{A closure-based object wrapper for Rascal REPL}
@description{
Using an instance of CommandExecutor you can simulate the exact interactions
between a Rascal REPL user and the REPL. 

This was created to implement documentation pages with example REPL runs.
}
data CommandExecutor
  = executor(
        PathConfig pcfg,
        str () prompt,
        void () reset,
        map[str mimeType, str content] (str command) eval
  );

@synopsis{Instantiates a ((CommandExecutor)) to simulate a REPL}
@examples{
It's funny that the current example is also executed by a CommandExecutor of the tutor compiler.
Here we use to show how it works:

```rascal-shell
import lang::rascal::tutor::repl::TutorCommandExecutor;
import util::Reflective;
e = createExecutor(pathConfig());
// now we can find the current prompt:
e.prompt();
// and evaluate an assignment
e.eval("x = 1;");
// look what a continuation prompt looks like:
e.eval("println(\"abc\"")
e.prompt()
// finish the command we started
e.eval(")")
}
@javaClass{org.rascalmpl.tutor.lang.rascal.tutor.repl.TutorCommandExecutorCreator}
java CommandExecutor createExecutor(PathConfig pcfg);

test bool executorSmokeTest() {
  exec = createExecutor(pathConfig());

  output = exec.eval("import IO;");
  
  if (output["text/plain"] != "ok\n") {
    return false;
  }

  exec.eval("println(\"haai\")");

  if (output["application/rascal+stdout"] != "haai\n") {
    return false;
  }

  return true;
}
