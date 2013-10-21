module experiments::Compiler::muRascal2RVM::RascalReifiedTypes

import lang::rascal::\syntax::Rascal;
import ParseTree;

type[value] getExpressionType() = #Expression;
type[value] getStatementType() = #Statement;
type[value] getPatternType() = #Pattern;
type[value] getModuleType() = #Module; 