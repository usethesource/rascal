module experiments::Compiler::Examples::NestedOr

value main(list[value] args) {
    res = for( ( ( ([*int x1,*int y1] := [1,2] || [*int x1,*int y1] := [3,4]) && ([*int x2,*int y2] := [5,6] || [*int x2,*int y2] := [7,8]) ) 
                 || ( ([*int x1,*int y1] := [9,10] || [*int x1,*int y1] := [11,12]) && ([*int x2,*int y2] := [13,14] || [*int x2,*int y2] := [15,16]) ) )
               &&  ( ( ([*int x3,*int y3] := [17,18] || [*int x3,*int y3] := [19,20]) && ([*int x4,*int y4] := [21,22] || [*int x4,*int y4] := [23,24]) ) 
                     || ( ([*int x3,*int y3] := [25,26] || [*int x3,*int y3] := [27,28]) && ([*int x4,*int y4] := [29,30] || [*int x4,*int y4] := [31,32]) ) ) ) {
              append <x1,x2,x3,x4,y1,y2,y3,y4>;
          }
    return res;
}
