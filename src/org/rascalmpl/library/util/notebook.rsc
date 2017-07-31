module util::notebook

import IO;
import String;
import Message;
import util::REPL;
import util::ShellExec;

data Notebook
	= notebook(REPL repl, loc kernelPath)
	;
	
data KernelInfo
	= kernelInfo(str languageName, str projectPath, str moduleName, str variableName)
	;

//@javaClass{org.rascalmpl.library.util.Notebook}
//@reflect
//java str startNotebook(REPL repl);

str JUPYTER_PATH = "/Library/Frameworks/Python.framework/Versions/3.6/bin/jupyter";

loc startNotebook(KernelInfo kernelInfo){
	str kernelContentFile = createKernelFile(kernelInfo.languageName, kernelInfo.projectPath, kernelInfo.moduleName, kernelInfo.variableName);
	writeFile(|tmp:///<kernelInfo.languageName>|+"kernel.json", kernelContentFile);
	PID kernelInstallation = createProcess(JUPYTER_PATH, args=["kernelspec", "install", resolveLocation(|tmp:///<kernelInfo.languageName>|).path]);
	// TODO: generate the codemirror
	PID jupyterExecution = createProcess(JUPYTER_PATH, args =["notebook", "--no-browser"]);
	
	bool guard = false;
	for (_ <- [1..15], line := readLineFromErr(jupyterExecution), line != "") {
		if(contains(line,"http://localhost:"))
		{
			return |http://localhost:<split("localhost:", line)[1]>|;
		}
    }
}

str createKernelFile(str languageName, str projectPath, str moduleName, str variableName)
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
