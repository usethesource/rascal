//@bootstrapParser
module lang::rascalcore::compile::Examples::Tst2

extend ParseTree;
value main() = choice(sort("A"), {choice(sort("A"),{prod(sort("A"),[lit("a")],{})})});