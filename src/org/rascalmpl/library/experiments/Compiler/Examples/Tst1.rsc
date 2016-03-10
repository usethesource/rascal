module experiments::Compiler::Examples::Tst1

import IO;
extend lang::java::m3::AST;
import lang::java::m3::TypeSymbol;

data Expression(
    loc src = |unknown:///|, 
    loc decl = |unresolved:///|,
    TypeSymbol typ = unresolved() 
);

int main() {
    return 0;
}
 
