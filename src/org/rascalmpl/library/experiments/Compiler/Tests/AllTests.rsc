module experiments::Compiler::Tests::AllTests

/*
 * A suite of tests for the Racsl compiler:
 * - import this module in a RascalShell
 * - Type :test at the command line.
 */

// Note: Type commented out since it uses a definition of datatype D that is incompatible with TestUtils
// extend experiments::Compiler::Tests::Types;
extend experiments::Compiler::Tests::Expressions;
extend experiments::Compiler::Tests::Statements;
extend experiments::Compiler::Tests::Patterns;
extend experiments::Compiler::Tests::StringTemplates;

extend experiments::Compiler::Examples::Run;


