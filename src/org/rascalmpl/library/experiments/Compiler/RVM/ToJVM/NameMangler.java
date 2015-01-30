package org.rascalmpl.library.experiments.Compiler.RVM.ToJVM;

public class NameMangler {
	/*
	 * Java has very nice string manipulation methods, but alas the
	 * following C style method outperforms all of them.
	 *
	 * Needed to map RVM function names to valid JVM method names. 
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
}
