package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

public class NameMangler {
	static public String mangleV0(String n) {
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

	static public String demangleV0(String n) {
		n = n.replace("$c", ":");
		n = n.replace("$s", "/");
		n = n.replace("$l", "(");
		n = n.replace("$r", ")");
		n = n.replace("$e", ";");
		return n.replace("$h", "#");
	}

	/*
	 * Java has very nice string manipulation methods, but alas the
	 * following C style method outperforms all of them.
	 */
	static public String mangle(String s) {
		char[] b = s.toCharArray();
		int toAdd = 0;
		for (int i = 0; i < b.length; i++) {
			switch (b[i]) {
			case '$': case ':':	case '/': case '(':
			case ')': case '[':	case ']': case ',':
			case '{': case '}': case '"': case '\\':
			case ' ': case ';': case '<': case '>':
			case '|': case '-': case '.': case '_':
				toAdd++;
			}
		}
		if (toAdd == 0) return s;
		char[] resarr = new char[toAdd + b.length];
		int o = 0;
		for (int i = 0; i < b.length; i++) {
			switch (b[i]) {
			case '$':
				resarr[o++] = '$'; resarr[o++] = '$'; break;
			case ':':
				resarr[o++] = '$'; resarr[o++] = '1'; break;
			case '/':
				resarr[o++] = '$'; resarr[o++] = '2'; break;
			case '(':
				resarr[o++] = '$'; resarr[o++] = '3'; break;
			case ')':
				resarr[o++] = '$'; resarr[o++] = '4'; break;
			case '[':
				resarr[o++] = '$'; resarr[o++] = '5'; break;
			case ']':
				resarr[o++] = '$'; resarr[o++] = '6'; break;
			case ',':
				resarr[o++] = '$'; resarr[o++] = '7'; break;
			case '{':
				resarr[o++] = '$'; resarr[o++] = '8'; break;
			case '}':
				resarr[o++] = '$'; resarr[o++] = '9'; break;
			case '"':
				resarr[o++] = '$'; resarr[o++] = '0'; break;
			case '\\':
				resarr[o++] = '$'; resarr[o++] = 'A'; break;
			case ' ':
				resarr[o++] = '$'; resarr[o++] = 'B'; break;
			case ';':
				resarr[o++] = '$'; resarr[o++] = 'C'; break;
			case '<':
				resarr[o++] = '$'; resarr[o++] = 'L'; break;
			case '>':
				resarr[o++] = '$'; resarr[o++] = 'G'; break;
			case '|':
				resarr[o++] = '$'; resarr[o++] = 'P'; break;
			case '-':
				resarr[o++] = '$'; resarr[o++] = 'M'; break;
			case '.':
				resarr[o++] = '$'; resarr[o++] = 'D'; break;
			case '_':
				resarr[o++] = '$'; resarr[o++] = 'U'; break;
			default:
				resarr[o++] = b[i];
			}
		}
		return new String(resarr);
	}
	public static void main(String[] argv) {
		System.out.println(NameMangler.mangle("Hello"));
		System.out.println(NameMangler.mangle("H:ello"));
		System.out.println(NameMangler.mangle("(Hello)"));
		System.out.println(NameMangler.mangle("(Help{} [0]{{ds}} :: llo)"));	
	}
}
