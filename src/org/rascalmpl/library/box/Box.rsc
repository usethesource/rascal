module box::Box


data Box
    =  H (list[Box] h)
    |  V(list[Box] v)
    |  HOV (list[Box] hov)
    |  HV (list[Box] hv)
    |  I(list[Box] i)
    |  WD(list[Box] wd)
    | R(list[Box] r)
    |  A(list[Box] a)
    | SPACE(int space)
    |  L(str l)
    |  KW(Box kw)
    |  VAR(Box  var)
    | NM(Box  nm)
    | REF(int ref)
    | NULL()
    ;
    
alias text = list[str];