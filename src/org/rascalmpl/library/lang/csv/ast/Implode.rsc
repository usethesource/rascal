module lang::csv::ast::Implode

import lang::csv::syntax::CSV;
import lang::csv::ast::CSV;
import ParseTree;

public lang::csv::ast::CSV::Table implodeCSV(lang::csv::syntax::CSV::Table tbl)
  = implode(#lang::csv::ast::CSV::Table, tbl);
