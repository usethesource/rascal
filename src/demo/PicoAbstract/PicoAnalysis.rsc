module demo::PicoAbstract::PicoAnalysis

import demo::PicoAbstract::PicoAbstractSyntax;

//public data ProgramPoint = pp(EXP exp) | pp(STATEMENT stat);

alias ProgramPoint = int;

alias CFG = rel[ProgramPoint, ProgramPoint];

alias Entry = set[ProgramPoint];

alias Exit = set[ProgramPoint];

public data BLOCK = block(Entry entry, CFG graph, Exit exit);