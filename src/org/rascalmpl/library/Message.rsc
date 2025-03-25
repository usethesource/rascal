@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
@synopsis{Provides the `Message` datatype that represents error messages and warnings.}
@description{
Messages can be used to communicate information about source texts.
They can be interpreted by IDEs to display type errors and warnings, etc.

`Message`s are, for instance, used as additional keyword fields of
other data types (syntax trees), or collected in sets or lists of errors to 
be published in an IDE. See ((util::IDEServices)).
}
module Message

@synopsis{Symbolic representation of error messages with a source location of the cause.}
data Message 
    = error(str msg, loc at)
    | error(str msg)  
    | warning(str msg, loc at)
    | info(str msg, loc at)
    ;

@javaClass{org.rascalmpl.library.Messages}
@synopsis{Call the standard message pretty-printing and sorting code, writing the result to a string}
java str write(list[Message] messages);

@synopsis{Reusable message handler for commandline `main` functions}
@description{
This function takes care of some typical responsibilities of `main` functions:
* return an `int` to signal failure (!= 0) or success (0).
* print the collected and sorted error messages to stdout.
* implement the errorsAsWarnings feature.
* implement the warningsAsErrors feature.

With `errorsAsWarnings` we do not fail while we are still developing experimental code. The process
of testing and deployment may continue even if (fatal) errors were detected.

With `warningsAsErrors` we can signal a higher level of stability and compliance that the default.
Every new warnings will lead to a failing build, making sure that new issues can not creep in anymore.
}
@benefits{
* consistent error handling between different `main` functions
* consistent error printing
* consistent interpretation of errorsAsWarnings and warningsAsErrors
}
@pitfalls{
* stdout is used to print the messages; no further processing is possible.
* have to remember to return the result of this function as the return value of `main`
}
int mainMessageHandler(list[Message] messages, bool errorsAsWarnings = false, bool warningsAsErrors = false) {
  int FAILURE = 1;
  int SUCCESS = 0;

  if (errorsAsWarnings && warningsAsErrors) {
    println("[ERROR] the error handler is confused because both errorsAsWarnings and warningsAsErrors are set to true.")
    return FAILURE;
  }

  println(write(messages));

  hasErrors   = error  (_, _) <- messages;
  hasWarnings = warning(_, _) <- messages;

  switch (<hasErrors, hasWarnings, errorsAsWarnings, warningsAsErrors) {
    case <true, _    , false, _    > :
      return FAILURE;
    case <true, _    , true , _    > : 
      println("[INFO] errors have been de-escalated to warnings.");
      return SUCCESS;
    case <_   , true , _    , true > :
      println("[INFO] warnings have been escalated to errors");
      return SUCCESS;
    case <_   , false, _    , false> :
      return FAILURE;
    default:
      return hasErrors ? FAILURE : SUCCESS;
  }
}