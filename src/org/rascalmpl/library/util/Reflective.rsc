@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@contributor{Paul Klint - Paul.Klint@cwi.nl (CWI)}

module util::Reflective

import Exception;
import Message;
import ParseTree;
import Grammar;
import IO;

public Tree getModuleParseTree(str modulePath) {
    mloc = getModuleLocation(modulePath);
    return parseModule(mloc);
}

@javaClass{org.rascalmpl.library.util.Reflective}
@reflect{Uses Evaluator to get back the grammars imported by \mod}
public java Grammar getModuleGrammar(loc \mod);

@javaClass{org.rascalmpl.library.util.Reflective}
@reflect{Uses Evaluator to get back the parse tree for the given command}
public java Tree parseCommand(str command, loc location);

@javaClass{org.rascalmpl.library.util.Reflective}
@reflect{Uses Evaluator to get back the parse tree for the given commands}
public java Tree parseCommands(str commands, loc location);

@javaClass{org.rascalmpl.library.util.Reflective}
@reflect{Uses Evaluator to access the Rascal module parser}
@doc{This parses a module from a string, in its own evaluator context}
public java Tree parseModule(str moduleContent, loc location);

@javaClass{org.rascalmpl.library.util.Reflective}
@reflect{Uses Evaluator to access the Rascal module parser}
@doc{This parses a module on the search path, and loads it into the current evaluator including all of its imported modules}
public java Tree parseModule(loc location);

@javaClass{org.rascalmpl.library.util.Reflective}
@reflect{Uses Evaluator to access the Rascal module parser}
public java Tree parseModule(loc location, list[loc] searchPath);

@javaClass{org.rascalmpl.library.util.Reflective}
@reflect{Uses Evaluator to resolve a module name in the Rascal search path}
public java loc getModuleLocation(str modulePath);

@javaClass{org.rascalmpl.library.util.Reflective}
@reflect{Uses Evaluator to resolve a path name in the Rascal search path}
public java loc getSearchPathLocation(str filePath);