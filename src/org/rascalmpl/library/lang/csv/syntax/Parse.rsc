module lang::csv::\syntax::Parse

import lang::csv::\syntax::CSV;
import ParseTree;

public lang::csv::\syntax::CSV::Table parseCSV(loc l) = parse(#Table, l);
