module lang::rascalcore::check::Test5

int x = 1;

int f(){
    int g() { x = 1; return x + 2;}

    return g();
}