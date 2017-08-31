module util::notebook::Notebook

import IO;
import String;
import Message;
import ParseTree;
import util::REPL;
import util::ShellExec;
import util::notebook::Mode;
import util::notebook::CodeMirror;

	
data NotebookServer = 
	notebook(void () serve, void() stop);	
	
data KernelInfo
	= kernelInfo(str languageName, str projectPath, str moduleName, str variableName, loc logo = |tmp:///|);

str JUPYTER_PATH = "/Library/Frameworks/Python.framework/Versions/3.6/bin/jupyter";
loc JUPYTER_FRONTEND_PATH = |home:///Documents/Jupyter/forked-notebook/notebook/static/components/codemirror/mode/|;

/*
* This function starts a notebook WITHOUT a custom codemirror mode
*/
NotebookServer startNotebookServer(KernelInfo kernelInfo){
	generateKernel(kernelInfo);
	return notebook( void () { pid = startJupyterServer(); }, void () { killProcess(pid); });
}

/* 
* This function starts a notebook with a custom codemirror mode generated based on on the defined mode
*/
NotebookServer startNotebookServer(KernelInfo kernelInfo, Mode mode){
	generateKernel(kernelInfo);
	generateCodeMirror(mode);
	return notebook( void () { pid = startJupyterServer(); }, void () { killProcess(pid); });
}

/*
* This function starts a notebook with a custom codemirror mode generated based on the grammar
*/
NotebookServer startNotebookServer(KernelInfo kernelInfo, type[&T <: Tree] sym){
	generateKernel(kernelInfo);
	generateCodeMirror(grammar2mode(kernelInfo.languageName, sym));
	int pid = -1;
	return notebook( void () { pid = startJupyterServer(); }, void () { killProcess(pid); });
}

/*
* This function takes the url of a logo image (64x64) for the language to be displayed in the browser when the kernel is loaded
*/
void copyLogoToKernel(loc urlLogo, loc destPath){
	list[int] imgBytes= readFileBytes(urlLogo);
	writeFileBytes(destPath + "logo-64x64.png", imgBytes);
}

/*
* This function creates a code mirror mode using the mode received as parameter and re-builds the notebook front-end project.
*/
void generateCodeMirror(Mode mode){
	// Jupyter front-end path
	createCodeMirrorModeFile(mode, JUPYTER_FRONTEND_PATH + "<mode.name>/<mode.name>.js");
	// Re-build notebook front end
	createProcess("/usr/local/bin/node", args=["/usr/local/bin/npm", "run", "build"]);
}

void generateKernel(KernelInfo kernelInfo){
	writeFile(|tmp:///<kernelInfo.languageName>|+"kernel.json", createKernelFile(kernelInfo));
	if(kernelInfo.logo != |tmp:///|)
		copyLogoToKernel(kernelInfo.logo, |tmp:///<kernelInfo.languageName>|);
	PID kernelInstallation = createProcess(JUPYTER_PATH, args=["kernelspec", "install", resolveLocation(|tmp:///<kernelInfo.languageName>|).path]);
}

/*
* This function starts the jupyter server and returns the url in which the webserver is runing.
*/
PID startJupyterServer(){
	PID jupyterExecution = createProcess(JUPYTER_PATH, args =["notebook", "--no-browser"]);
	bool guard = false;
	for (_ <- [1..15], line := readLineFromErr(jupyterExecution), line != "") {
		if(contains(line,"http://localhost:"))
		{
			println("The notebook is running at: <|http://localhost:<split("localhost:", line)[1]>|>");
		}
    }
    return jupyterExecution;
}
/*
* This function produces the content of the kernel.json file using the kernel information received as parameter.
*/
str createKernelFile(KernelInfo kernelInfo) = 
	"{
  	'	\"argv\": [
    '		\"java\",
    '		\"-jar\",
    '		\"/Users/mveranom/Documents/Rascal/JupyterKernel/target/JupyterJavaKernel-1.0-SNAPSHOT-jar-with-dependencies.jar\",
    '		\"{connection_file}\",
    '		\"<kernelInfo.projectPath>\",
    '		\"<kernelInfo.moduleName>\",
    '		\"<kernelInfo.variableName>\",
    '		\"<kernelInfo.languageName>\"
  	'	],
  	'	\"display_name\": \"<firstUpperCase(kernelInfo.languageName)>\",
  	'	\"language\": \"<kernelInfo.languageName>\"
	'}";
	
/*
* This function replaces the first character of the string for the corresponding character in uppercase
*/
str firstUpperCase(str input){
	str first = stringChar(charAt(input, 0));
	return replaceFirst(input, first, toUpperCase(first)); 
}	

//@javaClass{org.rascalmpl.library.util.Notebook}
//@reflect
//java str startNotebook(REPL repl);