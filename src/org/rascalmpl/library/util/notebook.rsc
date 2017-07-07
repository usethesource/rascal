module util::notebook

import IO;
import String;
import Message;
import util::REPL;
import util::ShellExec;

data REPL
  = repl(str title, str welcome, str prompt, loc history, 
         CommandResult (str line) handler,
         Completion (str line, int cursor) completor);


//@javaClass{org.rascalmpl.library.util.Notebook}
//@reflect
//java str startNotebook(REPL repl);

	str jupyter = "/Library/Frameworks/Python.framework/Versions/3.5/bin/jupyter";

str startNotebook(str moduleName, str variableName, str languageName){
	
	str kernelContentFile = createKernelFile(moduleName, variableName, languageName);
	loc kernelPath = |tmp:///<languageName>|+"kernel.json";
	writeFile(kernelPath, kernelContentFile);
	PID x = createProcess("/Library/Frameworks/Python.framework/Versions/3.5/bin/jupyter", args=["kernelspec", "install", resolveLocation(|tmp:///<languageName>|).path]);
	return resolveLocation(kernelPath).path +"//\\" +readEntireErrStream(x);

}

str createKernelFile(str moduleName, str variableName, str languageName)
	= "{
  	'\"argv\": [
    '	\"java\",
    '	\"-jar\",
    '	\"/Users/Mauricio/IdeaProjects/jupyterJavaKernel/target/JupyterJavaKernel-1.0-SNAPSHOT-jar-with-dependencies.jar\",
    '	\"{connection_file}\",
    '	\"<moduleName>\",
    '	\"<variableName>\"
  	'],
  	'\"display_name\": \"<languageName> Kernel\",
  	'\"language\": \"<languageName>\"
	'}
	"
	;

data Notebook
	= notebook(REPL repl, loc kernelPath)
	;