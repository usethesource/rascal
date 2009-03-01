module demo::PicoAbstract::PicoAnalysis

import demo::PicoAbstract::PicoAbstractSyntax;

//public data ProgramPoint = pp(EXP exp) | pp(STATEMENT stat);

alias ProgramPoint = int;

alias CFG = rel[int, int];

alias Entry = set[int];

alias Exit = set[int];

public data BLOCK = block(Entry entry, CFG graph, Exit exit);