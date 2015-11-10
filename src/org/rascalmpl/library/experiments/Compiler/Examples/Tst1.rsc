module experiments::Compiler::Examples::Tst1
  
int inc(int n, int delta = 1) = n + delta;

list[int] rev(list[int] lst) {
    if(lst == [])
       return lst;
    hd = lst[0];
    return rev(lst[1..]) + hd;
}