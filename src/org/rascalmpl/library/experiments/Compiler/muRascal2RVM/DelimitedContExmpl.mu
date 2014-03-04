module DelimitedContExmpl

function F[0,] {
    return 55 + shift(cont(101)) + shift(cont(202));
}

function MAIN[2,args,kwargs] {
    return reset(F);
}