module demo::AbstractPico::Analysis

import demo::AbstractPico::AbstractSyntax;

/*
 * Comman abstractions for source code analysis
 */

alias ProgramPoint = int;

alias CFG = rel[ProgramPoint, ProgramPoint];

alias Entry = set[ProgramPoint];

alias Exit = set[ProgramPoint];

public data BLOCK = block(Entry entry, CFG graph, Exit exit);
