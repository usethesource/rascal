module lang::rascal::tutor::TutorCommandExecutor

import util::Reflective;

data CommandExecutor
  = executor(
        PathConfig pcfg,
        str () prompt,
        void () reset,
        map[str mimeType, str content] (str command) eval
  );

@javaClass{org.rascalmpl.library.lang.rascal.tutor.TutorCommandExecutorCreator}
java CommandExecutor createExecutor(PathConfig pcfg);