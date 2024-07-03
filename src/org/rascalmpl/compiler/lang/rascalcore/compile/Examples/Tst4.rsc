module lang::rascalcore::compile::Examples::Tst4

import IO;

data Foo = foo(int x);
list[Foo] foos = [foo(1), foo(2), foo(3)];

void f() {
    bool foo() = true;
    println(foo(x) in foos);
    //      ^^^ Error: "Expected 0 argument(s), found 1"
    }
    
    
//value main() = _f(3);

//data Tree;
//anno set[int] Tree@messages;
//
//data TModel(list[int] messages = []);
//
//list[int] f(TModel tm) = tm.messages;