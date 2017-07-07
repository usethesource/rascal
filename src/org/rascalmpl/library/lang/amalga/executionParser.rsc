module lang::amalga::executionParser

import lang::amalga::Syntax;

str eval((ExecutionScenario) `<ExecutionScenario+ scenarios>`) = {
	str result = "";
	scenrs = [];
	for(/ExecutionScenario scenario := scenarios){
		result += eval(scenario);
		scenrs += "<scenario.id>";
	}
	result += createMain(scenrs);
};

str createMain(list[str] scenarios)={
	"int main(int argc, char **argv) {
	'	<for(scenario <- scenarios){>
	'	if( strcmp(argv[1], \"<scenario>\") == 0){
	'		<scenario>();
	'	}<}>
	'
	'	return 0;
	'}";
};

/*
* This function takes an ExecutionScenario and produces a C++ method
* "exec" Id id "{" Execution+ commands "}"
*/
str eval((ExecutionScenario)`exec<Id execName>{<Execution+ statements>}`)={
	"void <execName>(){
	'	<for(/Execution execution := statements){><eval(execution)><}>
	'}
	'
	";
};

str eval((Execution) `<Type typ> <Id id> = <ExecutionCommand command>;`) = {
	"<eval(typ)> <id> = <eval(command)>
	";
};

str eval((Execution) `<ExecutionCommand command>;`) = {	
	"<eval(command)>
	";
};

str eval((Execution) `assert <Exp expression>;`) = {	
	"<eval(expression)>
	"; //TODO: how to use this un Halide 
};

str eval(Type amalgaType)={
	switch(amalgaType){
		case /(Type)`img \<<Type t>\>`: return "Image\<<t>\>";
		default: return "<amalgaType>";
	}
};

str eval(ExecutionCommand command)={
	switch(command){
		case /(ExecutionCommand)`loadImage(<String url>)`: return "load_image(<url>);";
		case /(ExecutionCommand) `run( <Id function>( <{Id ","}* fParams>), <{Exp ","}* params>)`:return "<function>(<fParams>).realize(<params>);" ;
		case /(ExecutionCommand)`loadImage(<String url>)`: return "load_image(\"<url>\");";
		case /(ExecutionCommand)`save( <Id output>, <String fullPath>)`: return "save_image(<output>,<fullPath>);";
		case /(ExecutionCommand)`renderImage( <Id image>)`: return "renderImage(<image>);";
	}
};