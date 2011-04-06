@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module demo::AbstractPico::Analysis

import demo::AbstractPico::AbstractSyntax;

/*
 * Common abstractions for source code analysis
 */

alias ProgramPoint = int;

alias CFG = rel[ProgramPoint, ProgramPoint];

alias Entry = set[ProgramPoint];

alias Exit = set[ProgramPoint];

public data BLOCK = block(Entry entry, CFG graph, Exit exit);
