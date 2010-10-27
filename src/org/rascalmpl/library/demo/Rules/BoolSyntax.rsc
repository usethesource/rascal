module demo::Rules::BoolSyntax

layout Whitespace = [\ \t\n]*;

start syntax Bool = "btrue"
					| "bfalse"
					| left Bool "&" Bool
					| right Bool "|" Bool
					"(" Bool ")"
					;
