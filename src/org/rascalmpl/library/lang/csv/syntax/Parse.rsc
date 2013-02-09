module lang::csv::\syntax::Parse

import lang::csv::\syntax::CSV;
import ParseTree;

public Table parseCSV(loc l) = parse(#Table, l);