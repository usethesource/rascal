module lang::rascal::tutor::TutorCommandExecutor

import util::Reflective;

data CommandExecutor
  = executor(
        PathConfig pcfg,
        str () prompt,
        void () reset,
        str (loc cwd, str command) eval,
        str () stdout,
        str () stderr,
        str () html
  );

@javaClass{org.rascalmpl.library.lang.rascal.tutor.TutorCommandExecutorCreator}
java CommandExecutor createExecutor(PathConfig pcfg);