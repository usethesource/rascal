module experiments::Compiler::Examples::Tst2

import Grammar;

Production Keywords =  
	choice(keywords("Keyword"),{
	     //prod(keywords("Keyword"),[lit("extern")],{}),prod(keywords("Keyword"),[lit("int")],{}),prod(keywords("Keyword"),[lit("signed")],{}),
	     //prod(keywords("Keyword"),[lit("char")],{}),prod(keywords("Keyword"),[lit("const")],{}),prod(keywords("Keyword"),[lit("typedef")],{}),
	     //prod(keywords("Keyword"),[lit("register")],{}),prod(keywords("Keyword"),[lit("union")],{}),prod(keywords("Keyword"),[lit("unsigned")],{}),
	     //prod(keywords("Keyword"),[lit("auto")],{}),prod(keywords("Keyword"),[lit("goto")],{}),prod(keywords("Keyword"),[lit("do")],{}),
	     //prod(keywords("Keyword"),[lit("continue")],{}),prod(keywords("Keyword"),[lit("for")],{}),
	     
	     prod(keywords("Keyword"),[lit("break")],{}),
	     prod(keywords("Keyword"),[lit("short")],{}),prod(keywords("Keyword"),[lit("double")],{}),prod(keywords("Keyword"),[lit("struct")],{}),
	     prod(keywords("Keyword"),[lit("case")],{}),prod(keywords("Keyword"),[lit("while")],{}),prod(keywords("Keyword"),[lit("switch")],{}),
	     prod(keywords("Keyword"),[lit("default")],{}),prod(keywords("Keyword"),[lit("float")],{}),prod(keywords("Keyword"),[lit("long")],{}),
	     prod(keywords("Keyword"),[lit("static")],{}),prod(keywords("Keyword"),[lit("sizeof")],{}),prod(keywords("Keyword"),[lit("volatile")],{}),
	     prod(keywords("Keyword"),[lit("void")],{}),prod(keywords("Keyword"),[lit("enum")],{}),prod(keywords("Keyword"),[lit("if")],{}),
	     prod(keywords("Keyword"),[lit("return")],{}),prod(keywords("Keyword"),[lit("else")],{})}
	 );

value main(list[value] args) = c:choice(_, {p, Production x:priority(_,/p), *r}) := Keywords;