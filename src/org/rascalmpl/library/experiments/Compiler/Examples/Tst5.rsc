
module experiments::Compiler::Examples::Tst5

import ParseTree;
import IO;

layout Whitespace = [\ ]*;
syntax V = "A" | "B";
syntax DATA = V;

syntax BODY = {DATA ";"}* datas;

value main(list[value] args) {
	b = [BODY] "A; B";
	for(DATA d <- b.datas){
		println("switch on <d>");
		switch(d){
		
		case (DATA) `<V _>`: println("<d> matches");
		
		}
	}
	return true;
}

//import lang::rascal::\syntax::Rascal;
//import IO;
//import ParseTree;
//
//
//value main(list[value] args) {
//	if((Body)`<Toplevel* tls>` := [Body] "data Bool = and(Bool, Bool); data Prop = or(Prop, Prop);") {
//    	list[Declaration] typesAndTags = [ ];
//		bool first = true;
//		
//		for ((Toplevel)`<Declaration decl>` <- tls) {
//			println("checkModule: switch on <decl>");
//			if(first){
//				first = false;
//				switch(decl) {
//					case (Declaration)`data <UserType _> = <Variant _> ;` : {
//						typesAndTags = typesAndTags + decl;
//						println("checkModule, added to typesAndTags: <decl>");
//						}
//					default: println("checkModule: no case for <decl>");
//				}
//			} else {
//				switch(decl) {
//					case (Declaration)`data <UserType _> = <Variant _> ;` : {
//						typesAndTags = typesAndTags + decl;
//						println("checkModule, added to typesAndTags: <decl>");
//						}
//					default: println("checkModule: no case for <decl>");
//				}
//			}
//			
//		}
//		println("checkModule, typesAndTags after for:"); for(t <- typesAndTags) println("    <t>");
//		return true;
//	}
//	return false;
//}	