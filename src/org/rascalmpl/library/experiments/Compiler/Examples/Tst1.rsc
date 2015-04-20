
@bootstrapParser
module experiments::Compiler::Examples::Tst1
import lang::rascal::\syntax::Rascal;

// Sanity check on the testing utilities themselves

Expression exp(str  s) = [Expression] s;

value main(list[value] args) = exp("1 + 2");