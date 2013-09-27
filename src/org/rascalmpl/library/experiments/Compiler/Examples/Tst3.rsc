module experiments::Compiler::Examples::Tst3

int square(int n) = n * n when n < 10;
int square(int n) = 10 * n * n when n >= 10;


value main(list[value] args){
  return square(15);
}
  