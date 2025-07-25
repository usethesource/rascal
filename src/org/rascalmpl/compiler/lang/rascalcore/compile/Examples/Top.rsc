module lang::rascalcore::compile::Examples::Top
import lang::rascalcore::compile::Examples::Left;
import lang::rascalcore::compile::Examples::Right;

Exp main(){
	x = Exp::and(\true(), \true());
    //x = Exp::or(maybe(), maybe());
	Exp y = x.lhs;
	return y;
}