module experiments::Compiler::Examples::Tst5

int twice (int n) = 2 * n;
int triple (int n) = 3 * n;

int dup (int n) = n + n;
str dup (str s) = s + s;

int trip(int n) = n + n + n;
str trip(str s) = s + s + s;

test bool twiceTriple1(){
    return (twice o triple)(5) == twice(triple(5));
}
