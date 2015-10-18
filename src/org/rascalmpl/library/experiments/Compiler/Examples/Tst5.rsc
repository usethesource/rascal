module experiments::Compiler::Examples::Tst5
import experiments::Compiler::Execute;

value main() = compileAndLink(|std:///experiments/Compiler/Examples/Tst6.rsc|, true);