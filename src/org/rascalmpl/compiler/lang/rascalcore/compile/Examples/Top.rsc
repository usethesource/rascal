module lang::rascalcore::compile::Examples::Top
import lang::rascalcore::compile::Examples::Left;
import lang::rascalcore::compile::Examples::Right;

Exp main(){
	x = or(maybe(), maybe());
	y = x.lhs;	// ok: lhs is overloaded
	return y;
}