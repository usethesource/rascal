module experiments::Compiler::Examples::Tst2

import experiments::Compiler::Examples::Tst1;

data Prop = size(int n) | c (Color cc);

alias Props = list[Prop];

data Box = box(Prop prop);

Box box(Prop props...) = _box(props);