module experiments::Compiler::Tests::AllCompilerTests

/*
 * A suite of tests for the Rascal compiler:
 * - import this module in a RascalShell
 * - Type :test at the command line.
 * - Go and drink some latte ;-)
 */

// Note: Type commented out since it uses a definition of datatype D that is incompatible with TestUtils
// extend experiments::Compiler::Tests::Types;
extend experiments::Compiler::Tests::Booleans;
extend experiments::Compiler::Tests::Expressions;
extend experiments::Compiler::Tests::Statements;
extend experiments::Compiler::Tests::Patterns;
extend experiments::Compiler::Tests::StringTemplates;

extend experiments::Compiler::Examples::Run;
