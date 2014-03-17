package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

public class NameMangler {
	static public String mangle(String n) {
		n = n.replace(":", "$c");
		n = n.replace("/", "$s");
		n = n.replace("(", "$l");
		n = n.replace(")", "$r");
		n = n.replace(";", "$e");
		n = n.replace(" ", "$p");
		n = n.replace("[", "$L");
		n = n.replace("]", "$R");
		n = n.replace(",", "$C");
		n = n.replace("\\", "$b");
		n = n.replace("\"", "$q");
		
		return n.replace("#", "$h");
	}

	static public String demangle(String n) {
		n = n.replace("$c", ":");
		n = n.replace("$s", "/");
		n = n.replace("$l", "(");
		n = n.replace("$r", ")");
		n = n.replace("$e", ";");
		return n.replace("$h", "#");
	}
}
