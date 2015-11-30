package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.help;

import java.io.PrintWriter;

public class IntroHelp {

	static String[] helpText = {
				"Help for the compiler-based RascalShell.",
				"",
				"RascalShell commands:",
				"    quit or EOF            Quit this RascalShell",
				"    help keywords          List all Rascal keywords",
				"    help operators         List all Rascal operators and special symbols",
				"    help <text>            Help about a specific keyword, operator, function or topic",
				"    declarations           List all declarations",
				"    modules                List all imported modules",
				"    undeclare <name>       Remove declaration of <name>",
				"    unimport <name>        Remove import of module <name>",
				"    set <option> <value>   Set RascalShell <option> to <value>",
				"    e.g. set profile true",
				"         set trace false",
				"         set coverage true",
				"",
				"Debugging commands:",
				"    break                  List current break points",
				"    break <name>           Breakpoint at start of function <name>",
				"    break <name> <lino>    Breakpoint in function <name> at line <lino>",
				"    cl(ear) <bpno>         Clear breakpoint with index <bpno>",
				"",
				"Keyboard essentials:",
				"    <UP>                   Previous command in history",
				"    <DOWN>                 Next command in history",
				"    <CTRL>r                Backward search in history",
				"    <CTRL>s                Forward search in history",
				"    <TAB>                  Complete previous word",
				"    <CTRL>a                Move cursor to begin of line",
				"    <CTRL>e                Move cursor to end of line",
				"    <CTRL>k                Kill remainder of line after cursor",
				"    <CTRL>l                Clear screen",
				"",
				"Further help: XXX"
				//":edit <modulename>         Opens an editor for that module",
				//":test                      Runs all unit tests currently loaded",
				//":history                   Print the command history",
		};
	
	static void print(PrintWriter stdout){
		for(String line : helpText){
			stdout.println(line);
		}
	}
}
