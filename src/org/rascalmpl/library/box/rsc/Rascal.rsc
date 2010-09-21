module box::rsc::Rascal
import box::Box;
import box::Concrete;
import rascal::\old-syntax::Rascal;
import rascal::\old-syntax::Declarations;

public Box getRascal(Tree q) {
if (Body a:=q) 
switch(a) {
	case `<Toplevel* toplevels> `:  
             return V(1, getArgs(toplevels));
}
return NULL();
}
