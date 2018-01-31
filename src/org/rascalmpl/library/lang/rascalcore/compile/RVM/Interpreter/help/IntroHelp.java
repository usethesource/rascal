package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.help;

import java.io.PrintWriter;

public class IntroHelp {

	static String[] helpText = {
				"Help for the compiler-based RascalShell.",
				"",
				"RascalShell commands:",
				"    quit or EOF            Quit this RascalShell",
				"    help <topic>           Open a browser with help about topic",
				"    apropos <topic>        Textual summary of help about topic",
				"    declarations           List all declarations and imports",
				"    undeclare <name>       Remove declaration of <name>",
				"    unimport <name>        Remove import of module <name>",
				"    set <option> <bool>    Set RascalShell <option> to true or false",
				"                           Option: profile, trace, coverage",
				"    set                    List all current option values",
				"    test                   Run all tests in currently imported modules",
				"    test <modules>         Run all test in given <modules>",
				"    edit <module>          Edit <module>",
				"    clean                  Clean bin directory",
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
				"    <CTRL>l                Clear screen"
				//":history                   Print the command history",
		};
	
	public static void print(PrintWriter stdout){
		for(String line : helpText){
			stdout.println(line);
		}
	}
}
