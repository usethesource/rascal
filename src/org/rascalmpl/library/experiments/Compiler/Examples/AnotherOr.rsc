module experiments::Compiler::Examples::AnotherOr

data AnotherOrData = a();

anno list[int] AnotherOrData@l;

value main(list[value] args) {
    v = a()[@l = [1,2,3]];
    list[list[int]] res = [];
    if(v@l? && [*int x,*int y] := v@l) {
       res = res + [ x, y ];
       fail;
    }
    return res;
}