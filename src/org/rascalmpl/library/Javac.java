package org.rascalmpl.library;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;

public class Javac {
	private final IValueFactory values;
	
	public Javac(IValueFactory values){
		super();
		this.values = values;
	}
	
	public IList compile(IList args) throws Exception{
		if (!args.getType().getElementType().isStringType()) {
			throw RuntimeExceptionFactory.illegalArgument(args, null, null);
		}
		IList list = (IList)args;
		java.lang.String jargs[] = new java.lang.String[list.length()];
		int i = 0;
		for (IValue arg: list) {
			jargs[i++] = ((IString)arg).getValue();
		}
		StringWriter str = new StringWriter();
		PrintWriter out = new PrintWriter(str);
        com.sun.tools.javac.Main compiler = new com.sun.tools.javac.Main();
        compiler.compile(jargs, out);
        IListWriter writer = values.listWriter();	
        for (java.lang.String line: str.toString().split("\n")) {
        	writer.append(values.string(line));
        }
        return writer.done();
    }

}
