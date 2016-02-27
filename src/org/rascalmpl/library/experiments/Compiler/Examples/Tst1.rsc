module experiments::Compiler::Examples::Tst1

value main() {
 return (false || any(c <- [0..3]));
}