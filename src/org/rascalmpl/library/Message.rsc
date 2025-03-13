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
