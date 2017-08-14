module util::notebook::Notebook

import IO;
import String;
import Message;
import ParseTree;
import util::REPL;
import util::ShellExec;
import util::notebook::Mode;
import util::notebook::CodeMirror;

data Notebook
	= notebook(REPL repl, loc kernelPath)
	;
	
data KernelInfo
	= kernelInfo(str languageName, str projectPath, str moduleName, str variableName)
	;

str JUPYTER_PATH = "/Library/Frameworks/Python.framework/Versions/3.6/bin/jupyter";
loc JUPYTER_FRONTEND_PATH = |home:///Documents/Jupyter/forked-notebook/notebook/static/components/codemirror/mode/|;

loc startNotebook(KernelInfo kernelInfo ){
	generateKernel(kernelInfo);
	return startJupyterServer();
}

loc startNotebook(KernelInfo kernelInfo, Mode mode){
	generateKernel(kernelInfo);
	generateCodeMirror(mode);
	return startJupyterServer();
}

loc startNotebook(KernelInfo kernelInfo, type[&T <: Tree] sym){
	generateKernel(kernelInfo);
	generateCodeMirror(grammar2mode(kernelInfo.languageName, sym));
	return startJupyterServer();
}


void generateCodeMirror(Mode mode){
	// Jupyter front end path
	createCodeMirrorModeFile(mode, JUPYTER_FRONTEND_PATH + "<mode.name>/<mode.name>.js");
	// Build notebook front end
	createProcess("/usr/local/bin/node", args=["/usr/local/bin/npm", "run", "build"]);
}

void generateKernel(KernelInfo kernelInfo){
	str kernelContentFile = createKernelFile(kernelInfo.languageName, kernelInfo.projectPath, kernelInfo.moduleName, kernelInfo.variableName);
	writeFile(|tmp:///<kernelInfo.languageName>|+"kernel.json", kernelContentFile);
	PID kernelInstallation = createProcess(JUPYTER_PATH, args=["kernelspec", "install", resolveLocation(|tmp:///<kernelInfo.languageName>|).path]);
}

loc startJupyterServer(){
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

//@javaClass{org.rascalmpl.library.util.Notebook}
//@reflect
//java str startNotebook(REPL repl);