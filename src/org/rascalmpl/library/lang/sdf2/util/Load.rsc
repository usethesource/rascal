@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module lang::sdf2::util::Load

import lang::sdf2::\syntax::Sdf2;
import IO;
import Exception;
import String;
import ParseTree;
import Set;

public SDF loadSDF2Module(str name, list[loc] path) {
  set[Module] modules = {};
  set[str] newnames = {name};
  set[str] done = {};
  
  while (newnames != {}) {
    <n,newnames> = takeOneFrom(newnames);
    
    if (n notin done) {
      file = find(n + ".sdf", path);
      \mod = parse(#start[Module], file).top;
      modules += \mod;
      newnames += getImports(\mod);
      done += {n};  
    }
  }

  def = "definition
         '
         '<for (Module m <- modules) {>
         '<m><}>";
  
  return parse(#SDF, def);
}

private set[str] getImports(Module \mod) {
  return { "<name.id>" | /Import i := \mod,  /ModuleName name := i};
}
