module box::rascal::Rascal
import box::Box;
import box::Concrete;
import rascal::\old-syntax::Rascal;
import rascal::\old-syntax::Declarations;

public Box getRascal(Tree q) {
if (Body a:=q) 
switch(a) {
	case `<Toplevel* toplevels> `:  
             return V(getArgs(toplevels, #Toplevel));
}
return NULL();
}
