module PartialApplication

function F[3,p1,p2,p3] {
    p1 = p1 - p2 - p3;
    return p1;
}

function MAIN[2,args,kwargs,f1,f2,f3] {
    f1 = F(100,50);
    f2 = F(100,60);
    f3 = F(100);
    return f1(1) + f1(11) + f2(2) + f2(22) + f3(70,3) + f3(80,4) // 187
           + bind(f1,1)() + bind(f1,11)() + bind(f2,2)() + bind(f2,22)() + bind(f3,70,3)() + bind(f3(80),4)() 
           + bind(F(100)(90),5)() + F(100)(90)(5) + bind(F(100,90),5)() + bind(F,100,90,5)() + bind(F,100,90)(5); // + 212 = 399
}