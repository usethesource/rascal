module experiments::Compiler::Examples::Tst4

alias Person = tuple[str name, int age];
Person merge() = ( <"X", 3> | <it.name, it.age + i> | i <- [0..10]); 