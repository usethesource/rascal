module experiments::Compiler::muRascal::Load

import experiments::Compiler::muRascal::Parse;
import experiments::Compiler::muRascal::AST;
import experiments::Compiler::muRascal::Implode;
import ParseTree;

MuModule load(loc l) = implodeMuRascal(parseMuRascal(l));
MuModule load(str src) = implodeMuRascal(parseMuRascal(src));

MuModule implodeMuRascal(Tree t) = preprocess(implode(#Module, t));