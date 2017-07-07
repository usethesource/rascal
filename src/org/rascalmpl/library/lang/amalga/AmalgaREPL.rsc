module lang::amalga::AmalgaREPL

import IO;
import List;
import String;
import ParseTree;
import util::REPL;
import util::notebook;
import util::Proposer;
import util::ShellExec;
import lang::amalga::Plugin;
import lang::amalga::Syntax;
import lang::amalga::Parser;
import lang::amalga::executionParser;
     
start syntax REPLCommand
	= def: Definition de
	| exx: Execution exe
	; 

list[Message] errors=[];

public REPL amalgaRepl 
       = repl("amalga", "hello", "\>", |home:///.amalgaHistory|, 
	         CommandResult (str line) { 
	         	return handler(line); 
	         },
	         Completion(str line, int cursor) {
	         	return completor(line, cursor); 
	         }
         );

/*
* This method handles every input from the user.
*/
public CommandResult handler(str name){
	command = parse(#start[REPLCommand], name);
	
	switch(command.top){
		case (REPLCommand)`<Execution execution>`: {
				return <eval(execution), errors, "">;
		}
		case (REPLCommand)`<Definition definition>`: {
			createFunctionHeaderFile("<definition.functionName>", getFunctionParametersAsString(definition.params));
			createFunctionImplementationFile(definition);
			return <"", errors, "">;
		} 
	}
}

/*
* This method translates an amalga function into a Halide function and saves the output as a .cpp file 
*/
void createFunctionImplementationFile(Definition definition){
	str func = "#include \"<definition.functionName>.h\"
				'<eval(definition)>";
	writeFile(|cwd:///generated_code/|+"<definition.functionName>.cpp", func);
}

/*
* This method creates a header file for each amalga function.
*/
void createFunctionHeaderFile(str name, str params){
	str head = functionHeader(name, params);
	writeFile(|cwd:///generated_code/|+"<name>.h", head);
}

str functionHeader(str name, str params) =
	"
	'#ifndef <name>_h
	'#define <name>_h
	'
	'#include \"Halide.h\"
	'#include \"halide_image_io.h\"
	'
	'// Include some support code for loading pngs.
	'using namespace Halide;
	'using namespace Halide::Tools;
	'Func <name>(<params>);
	'
	'
	'#endif /* <name>_h */
	'
	";
	
str getFunctionParametersAsString({Parameter ","}* params) 
	= "<for(/Parameter param := params){>
	  '	<eval(param)>
	  '<}>
	  ";

void createHeaderFile(str name, str tipo){
	str head = imageHeader(name, tipo);
	 writeFile(|cwd:///generated_code/|+"<name>.h", head);
}

str imageHeader(str name, str tipo)
	= 	"#ifndef <name>_h
		'#define <name>_h
		'#include \"Halide.h\"
		'#include \"halide_image_io.h\"
		'// Include some support code for loading pngs.
		'using namespace Halide;
		'using namespace Halide::Tools;
		'
		'<tipo> <name>();
		'
		'#endif /* <name>_h */
		'
		";

str eval((Execution)`<ExecutionCommand command>;`){
	switch(command){
		case(ExecutionCommand)`run( <Id function>( <{Id ","}* fParams>), <{Exp ","}* params>)`:{
			createMainFile("<function>", "<fParams>", "<executionParams(params)>", "<function>_output");
			return "The output has been saved in file <function>_output.png";
		}
		case(ExecutionCommand)`renderImage(<Id id>)`:{
			return "\<img src = ./generated_code/images/<id>.png\>";
		}
	}
}

str executionParams({Exp ","}* params){
	//tmp=[(split(".", "<e>")[0])+"()."+(split(".", "<e>")[1])| e <- l];
	str ans = "";
	for(Exp e <- params){
		list[str] tmp = split(".", "<e>");
		ans += tmp[0]+"()."+tmp[1]+", ";
	}
	ans = substring(ans, 0, size(ans)-2);
	return ans;
}

str eval(Type typ, Id id,(ExecutionCommand)`run( <Id function>( <{Id ","}* fParams>), <{Exp ","}* params>)`, str outputFileName){
	createMainFile("<function>", "<fParams>", "<executionParams(params)>", outputFileName, typ, id);
	executeAlgorithm("<function>", "<fParams>");
	return "";
}

void executeAlgorithm(str functionName, str functionParams){
	basePath = resolveLocation(|cwd:///generated_code/|).path;
	list[str] files =  ["<basePath>main_<functionName>.cpp", "<basePath><functionName>.cpp", "<basePath><functionParams>.cpp"]; 
	outputObject = resolveLocation(|cwd:///generated_code/<functionName>|).path;
	arguments = files + ["-g", "-I", "/Users/Mauricio/Downloads/halide/include", "-I", "/Users/Mauricio/Downloads/halide/tools", "-L", "/Users/Mauricio/Downloads/halide/bin", "-lHalide","-I","/usr/local/Cellar/libpng/1.6.25/include/libpng16","-L","/usr/local/Cellar/libpng/1.6.25/lib", "-lpng16","-o",outputObject, "-std=c++11"];
	pid = createProcess("g++", args=arguments);
	if(readEntireErrStream(pid)!="")error+= error(readEntireErrStream(pid), |cwd:///|);
	writeFile(|cwd:///generated_code/| + "<functionName>.sh", "DYLD_LIBRARY_PATH=/Users/Mauricio/Downloads/halide/bin <outputObject>");
	pid2 = createProcess("sh", args=[resolveLocation(|cwd:///generated_code/| + "<functionName>.sh").path]);
	if(readEntireErrStream(pid2)!="")error+= error(readEntireErrStream(pid2), |cwd:///|);	
}

str eval((Execution)`<Type typ> <Id id> = <ExecutionCommand exp>;`){
	switch(exp){
		case (ExecutionCommand)`loadImage(<String url>)`:{
			str tmp = "<url>";
			str path = substring(tmp,1,size(tmp)-1);
			list[int] tmm= readFileBytes(|file:///|+ "<path>");
			writeFileBytes(|cwd:///generated_code/images/|+"<id>.png", tmm);
			// creates .h file
			createHeaderFile(eval(id), eval(typ));
			// implementation .cpp file
			createImplementationFile(eval(id), eval(typ), eval(exp));
			return evalAnswer(exp, "<id>"); 
		}
		case(ExecutionCommand)`run( <Id function>( <{Id ","}* fParams>), <{Exp ","}* params>)`:{
			return eval(typ, id, exp, "<id>");
		}
	}
}

void createMainFile(str function, str fparams, str params, str outputFileName, Type typ, Id id){
	str implementation = mainImplementationFile(function, fparams, params, outputFileName, typ, id);
	writeFile(|cwd:///generated_code/|+"main_<function>.cpp", implementation);
}

str mainImplementationFile(str function, str fparams, str exeParams, str outputFileName, Type typ, Id id)
	= "
	'#include \"<function>.h\"
	'#include \"<fparams>.h\"
	'
	'
	'int main(int argc, char **argv){
	'	Func tmp = <function>(<fparams>());
	'	<eval(typ)> <eval(id)> = tmp.realize(<exeParams>);
	'	save_image(<eval(id)>, \"<resolveLocation(|cwd:///generated_code/images/|).path><outputFileName>.png\");
	'}
	";

void createImplementationFile(str name, str tipo, str command){
	str implementation = createImageImplementationFile(name, tipo, command);
	writeFile(|cwd:///generated_code/|+"<name>.cpp", implementation);
}

str createImageImplementationFile(str name, str tipo, str command)
	="
	' #include \"<name>.h\"
	'
	'<tipo> <name>(){
	'	return <command>
	'}
	";

str eval((Execution)`<ExecutionCommand exp>;`){
	return eval(exp);
}

str evalAnswer((ExecutionCommand)`loadImage(<String url>)`,str name){
	str tmp = "<url>";
	str path = substring(tmp,1,size(tmp)-1);
	str id = replaceAll(path,".","");
	id = replaceAll(id,"/","");
	if(endsWith(path,"tiff")){
		return "\<script\>loadTiffImage(\'<path>\',\'<id>\');\</script\>\<div id=\'<id>\'\>\</div\>";
	}
    else{
         return "\<img src = ./generated_code/images/<name>.png\>";
    }
}


str eval((ExecutionCommand)`renderImage(<Id id>)`){
	return "<id>";
}

Completion completor(str line, int cursor){
	proposerFunction = proposer(#Program);
	proposals = proposerFunction(line, cursor);
	return <0, ["<prop.newText>"|prop <- proposals]>;
}

//@javaClass{org.rascalmpl.library.lang.amalga.AmalgaREPL}
//@reflect
//public java list[str] completor(str line, int cursor);