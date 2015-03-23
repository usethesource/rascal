
module experiments::Compiler::Examples::Tst2

import Type;
import ParseTree;

value main(list[value] args) = {prod(Symbol::empty(),[],{}), prod(layouts("$default$"),[],{})};