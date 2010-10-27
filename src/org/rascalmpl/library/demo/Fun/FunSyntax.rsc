module demo::Fun::Fun-syntax;

// TODO
import Patterns;
import Modules;

// TODO
//imports 
//	EmbedSortAsPatternAndVar[Exp] 
//	EmbedSortAsPatternAndVar[Var]

start syntax Module; // TODO

syntax Var = "prime" "(" Var ")"
			| [a-z]+
			# [a-z]
			;

syntax Exp = Var
			| "fn" Var "=>" Exp
			| Exp Exp
			;
