module lang::rascal::check::C
import lang::rascal::check::B;
 
D X1 = d1(3);       D X2 = d1(3, b=false);
D Y1 = d2("z");     D Y2 = d2("z", m=1);
D Z1 = d3(true);    D Z2 = d3(true, t ="z");