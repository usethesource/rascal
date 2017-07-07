module lang::amalga::Parser

import lang::amalga::Syntax;
import lang::amalga::executionParser;
import String;
import IO;
import List;
import Set;

public str evalProgram(Program program) = {
	"#include \"Halide.h\"
	'#include \"halide_image_io.h\"
	'#include \"clock.h\"
	'// Include some support code for loading pngs.
	'#include \"halide_image_io.h\"
	'using namespace Halide;
	'using namespace Halide::Tools;
	'<eval(program.definitions)>
	'<eval(program.executionScenarios)>";
};

public str evalDefinition(Definition d) = {
	"#include \"Halide.h\"
	'#include \"halide_image_io.h\"
	'#include \"clock.h\"
	'// Include some support code for loading pngs.
	'#include \"halide_image_io.h\"
	'using namespace Halide;
	'using namespace Halide::Tools;
	'<eval(d)>
	";
};

rel[str,str] callFunctionsInsideMain(Program program)={
 	<"<functionName>","<getParametersName(params)>"> | /(Definition) `function <Id functionName>(<{Parameter ","}* params>): <Expression+ body> end` := program
};

str getParametersName({Parameter ","}* params)={
	for(/Parameter parameter := params){
		return "<parameter.name.id>";
	}
};

str writeImagesDeclaration(Program program)={
	set[str] images = getImages(program);
	"<for( image <- images){><image><}>";
};

set[str] getImages(Program program) = { 
	"Image\<<c>\> <i> = load_image(<e>);" | /(Definition) `img  \<<Type c>\> <Id i> = loadImage(<String e>)` := program
};

str functionsSignatureDeclaration (Expression+ body){
	set[str] functionNames = funcNameBlockDeclaration(body) + funcNameDeclaration(body);
	str result = "Func <for( n <- functionNames){><n>,<}>;";
	return replaceLast(result, ",","");
}

set[str] funcNameBlockDeclaration(Expression+ body) = {
	 "<id>"|/(Exp)`<Id id>[<{Exp ","}* args>]-\> do: <Exp body> od` := body
};

set[str] funcNameDeclaration(Expression+ body) = {
	 "<id>"|/(Exp)`<Id id>[<{Exp ","}* args>]-\><Exp body>` := body
};

str variablesDeclaration(Expression+ body) = { 
	set[str] vars = getImageIndices1D(body) + getImageIndices2D(body) + getImageIndices3D(body);
	if(!isEmpty(vars)){
		replaceLast("Var <for( n <- vars){><n>, <}>\n", ",", ";");
	}
};

set[str] getImageIndices1D(Expression+ body) 
 = {"<x>" | /(Exp) `<Id name>[<Id x>]` := body};

set[str] getImageIndices2D(Expression+ body) 
  = { "<x>", "<y>" | /(Exp) `<Id name>[<Id x>,<Id y>]` := body };

set[str] getImageIndices3D(Expression+ body) 
  = { "<x>", "<y>", "<c>" | /(Exp) `<Id i>[<Id x>,<Id y>,<Id c>]` := body };

str eval((Definition) `<Definition* definitions>`) =
	"<for(/Definition def := definitions){>
	'<eval(def)>
	'<}>
	";

/*
*	This function returns the Halide version of image declarations
*	'clamped = BoundaryConditions::repeat_edge(<e>);
*/
 str eval((Definition) `<Type s>\<<Type c>\> <Id i> = loadImage(<String e>)`) = "";

/*
* This function takes an amalga function and transforms it into a C++ method
*/
str eval((Definition)`function <Id functionName>(<{Parameter ","}* params>): <Expression+ body> end`)
  = "
	'Func <functionName>(<for(/Parameter param := params){><eval(param)><}>){
	'	<functionsSignatureDeclaration(body)>
	'	<variablesDeclaration(body)>
	'	<for(/Expression statement := body){><eval(statement)><}>
	'}
	";


/*
* This function takes an amalga parameter in the form  img<uint32_t> param[x,y,c] and produces Image<uint32_t> input
*/
//str eval((Parameter) `<Type typ> <Exp name>`)  {
//	switch(typ){
//		case (Type)`img \<<Type t>\>`: return "Image\< <t> \> <name.id>";
//		default: return "";
//	}
//}

// TODO: watch out Jurgen removed a / here (see above commented out code)
str eval((Parameter) `img \<<Type t>\> <Exp name>`) = "Image\< <t> \> <name.id>";
default str eval(Parameter _) = "";

/*
* This function evalutes an expression.
*/
str eval((Expression)`<Exp e>;`) = "<eval(e)>";

/*
* Evaluates the implicit loops that uses the '-> do … od' operator
*/
str eval((Exp)`<Id id>[<{Exp ","}* params>]-\> do:<Exp body>od`) 
  = "<eval(id)>(<params>) = <eval(body)>;\n";
  
/*
* Evaluates a loop statement using '->' operator 
*/
str eval((Exp)`<Id id>[<{Exp ","}* params>]-\><Exp body>`) = {
	"<eval(id)>(<params>) = <eval(body)>;\n";
};

/*
* Translates the min command in Amalga to Halide
*/
str eval((Exp) `min(<Exp val>,<Exp val2>)`) ={
	"min(<eval(val)>, <eval(val2)>)";
};

/*
* Translates the cast command in Amalga to Halide
*/
str eval((Exp)`cast\<<Type t>\> (<Exp body>)`) = {
	"cast\<<eval(t)>\>(<eval(body)>)";
};

/*
* Evaluates the short if-else statement 
*/
str eval((Exp) `<Exp cond> ? <Exp then> : <Exp els>`) = "select(<eval(cond)>, <eval(then)>, <eval(els)>)";

str eval((Exp)`(<Exp e>)`) = {"(<eval(e)>)";};

str eval((Exp) `<Exp left> && <Exp right>`) = "<eval(left)> && <eval(right)>";

str eval((Exp) `<Exp left> == <Exp right>`) = "<eval(left)> == <eval(right)>";

str eval((Exp) `<Exp left> || <Exp right>`) = "<eval(left)> || <eval(right)>";

str eval((Exp) `<Exp left> \< <Exp right>`) = "<eval(left)> \< <eval(right)>";

str eval((Exp) `<Exp left> / <Exp right>`) = "<eval(left)>/<eval(right)>";

str eval((Exp) `<Exp left> * <Exp right>`) = "<eval(left)> * <eval(right)>";

str eval((Exp) `!<Exp exp>`)="!<eval(exp)>";

str eval((Exp) `<Id i>`)="<i>";

int eval((Exp)`<IntegerLiteral i>`)=toInt("<i>");

str eval((Exp)`<Id receiver>[<Exp x>]`)={
	"<eval(receiver)>(<eval(x)>)";
};

/*
* This function evaluates and replaces the 2D-matrix square brackets with parenthesis 
*/
str eval((Exp)`<Id receiver>[<Exp x>,<Exp y>]`)={
	"<eval(receiver)>(<eval(x)>,<eval(y)>)";
};

/*
* Transforms a 3D-matrix with square brackets into a 3D-matrix with parenthesis 
*/
str eval((Exp)`<Id receiver> [<Exp x> , <Exp y>, <Exp c>]`) = "<eval(receiver)>(<eval(x)>,<eval(y)>,<eval(c)>)";

str eval(Id id)="<id>";

/*
* This function evaluates the amalga built-in random int command 
*/
str eval((Exp)`randomInt(<{Exp ","}* params>)`)="Halide::random_int(<params>)";

/*
* This function evaluates the amalga built-in random uint command 
*/
str eval((Exp)`randomUint(<{Exp ","}* params>)`)="Halide::random_uint(<params>)";

/*
* This function evaluates the amalga built-in random float command 
*/
str eval((Exp)`randomFloat(<{Exp ","}* params>)`)="Halide::random_float(<params>)";

/*
* This function evaluates the amalga built-in size command with two parameters
*/
str eval((Exp)`size(<Exp e1>, <Exp e2>)`)={
	switch(e2){
			case /(Exp)`1`: return "<eval(e1)>.width()";
			case /(Exp)`2`: return "<eval(e1)>.height()";
			case /(Exp)`3`: return "<eval(e1)>.channels()";
	}
};

/*
* This function evaluates the amalga built-in size command with two parameters
*/
str eval((Exp)`size(<Exp e1>)`)={
	"size(<eval(e1)>)";
};

/*
* This function evaluates the amalga built-in return command
*/
str eval((Exp)`return <Exp exp>`)="	return <exp>;";

/*
* Evaluates the if-else statement 
*/
str eval((Exp)`if<Exp cond><Exp then>else<Exp elsePart>end`){
	return "select(<eval(cond)>, <eval(then)>, <eval(elsePart)>)";
}

str eval((Exp) `<Id id>(<{Exp ","}* parameters>)`)={
	"<id>(<parameters>)";
};

str eval((Exp) `<Exp left> . <Exp right>`)={
	"<eval(left)>.<right>f";
};