module experiments::Compiler::Examples::Or

value main(list[value] args) {
    n = 0;
    l = if( [*int x,*int y] := [1,2,3] || ([*int x,*int y] := [4,5,6] && [*int w,*int z] := [7,8,9] ) ) {
        n = n + 1;
        fail;
    }
    return n;
}