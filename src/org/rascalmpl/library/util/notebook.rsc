module util::notebook

import IO;
import String;
import Message;
import util::REPL;
import util::ShellExec;

data Notebook
	= notebook(REPL repl, loc kernelPath)
	;


//@javaClass{org.rascalmpl.library.util.Notebook}
//@reflect
//java str startNotebook(REPL repl);

str JUPYTER_PATH = "/Library/Frameworks/Python.framework/Versions/3.6/bin/jupyter";

loc startNotebook(str projectPath, str moduleName, str variableName, str languageName){
	str kernelContentFile = createKernelFile(projectPath, moduleName, variableName, languageName);
	writeFile(|tmp:///<languageName>|+"kernel.json", kernelContentFile);
	PID kernelInstallation = createProcess(JUPYTER_PATH, args=["kernelspec", "install", resolveLocation(|tmp:///<languageName>|).path]);
	PID jupyterExecution = createBackgroundProcess(JUPYTER_PATH, args =["notebook", "--no-browser"]);
	return |http://localhost:8888/|;
}

str createKernelFile(str projectPath, str moduleName, str variableName, str languageName)
	= "{
  	'	\"argv\": [
    '		\"java\",
    '		\"-jar\",
    '		\"/Users/mveranom/Documents/Rascal/JupyterKernel/target/JupyterJavaKernel-1.0-SNAPSHOT-jar-with-dependencies.jar\",
    '		\"{connection_file}\",
    '		\"<projectPath>\",
    '		\"<moduleName>\",
    '		\"<variableName>\"
  	'	],
  	'	\"display_name\": \"<languageName> Kernel\",
  	'	\"language\": \"<languageName>\"
	'}
	"
	;
