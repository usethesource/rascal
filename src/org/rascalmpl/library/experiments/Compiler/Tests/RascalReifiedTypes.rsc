module experiments::Compiler::Tests::RascalReifiedTypes

import lang::rascal::\syntax::Rascal;
import ParseTree;

type[value] getExpressionType() = #Expression;
type[value] getStatementType() = #Statement;
type[value] getPatternType() = #Pattern;
type[value] getModuleType() = #Module; 