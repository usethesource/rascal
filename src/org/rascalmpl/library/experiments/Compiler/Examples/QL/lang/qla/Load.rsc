module experiments::Compiler::Examples::QL::lang::qla::Load

import experiments::Compiler::Examples::QL::lang::qla::Parse;
import experiments::Compiler::Examples::QL::lang::qla::AST;
import ParseTree;

Form load(loc l) = implodeQL(parseQL(l));
Form load(str src) = implodeQL(parseQL(src));

Form implodeQL(Tree f) = implode(#Form, f);