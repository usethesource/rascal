module lang::rascalcore::compile::muRascal::Load

import ParseTree;
import lang::rascalcore::compile::muRascal::Parse;
import lang::rascalcore::compile::muRascal::AST;
import lang::rascalcore::compile::muRascal::Implode;
import lang::rascalcore::compile::muRascal::Syntax;

MuModule load(loc l) = implodeMuRascal(parseMuRascal(l));
MuModule load(str src) = implodeMuRascal(parseMuRascal(src));

MuModule implodeMuRascal(Tree t) = preprocess(implode(#lang::rascalcore::compile::muRascal::AST::MuPreModule, t));