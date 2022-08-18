module lang::rascal::tutor::repl::TutorCommandExecutor

import util::Reflective;

data CommandExecutor
  = executor(
        PathConfig pcfg,
        str () prompt,
        void () reset,
        map[str mimeType, str content] (str command) eval
  );

@javaClass{org.rascalmpl.library.lang.rascal.tutor.repl.TutorCommandExecutorCreator}
java CommandExecutor createExecutor(PathConfig pcfg);

test bool executorSmokeTest() {
  exec = createExecutor(pathConfig());

  if (exec.prompt() != "rascal\>") {
    return false;
  }

  output = exec.eval("import IO;");
  
  if (output["text/plain"] != "ok\n") {
    return false;
  }

  exec.eval("println(\"haai\"");

  if (exec.prompt() != "\>\>\>\>\>\>\>") {
    return false;
  }

  output = exec.eval(")");

  if (output["application/rascal+stdout"] != "haai\n") {
    return false;
  }

  return true;
}