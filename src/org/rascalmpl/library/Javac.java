package org.rascalmpl.library;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.utils.JavaCompiler;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;

public class Javac{
	private final IValueFactory values;
	
	public Javac(IValueFactory values){
		super();
		
		this.values = values;
	}
	
	public IList compile(IString className, IString sourceFile, IList opts) throws Exception{
		if(!opts.getType().getElementType().isStringType()){
			throw RuntimeExceptionFactory.illegalArgument(opts, null, null);
		}
		
		IList list = opts;
		ArrayList<java.lang.String> jargs = new ArrayList<java.lang.String>(list.length());
		for(IValue arg: list){
			jargs.add(((IString)arg).getValue());
		}
		
		JavaCompiler<?> javaCompiler = new JavaCompiler(this.getClass().getClassLoader(), null, jargs);
		Class<?> result = javaCompiler.compile(className.getValue(), sourceFile.getValue(), null, Object.class);
		
		InputStream classStream = result.getResourceAsStream(result.getName().replace('.', '/') + ".class");
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[8192];
		
		int bytesRead;
		while((bytesRead = classStream.read(buffer)) != -1){
			baos.write(buffer, 0, bytesRead);
		}
		
        IListWriter writer = values.listWriter();	
        for(java.lang.String line: baos.toString().split("\n")){
        	writer.append(values.string(line));
        }
        return writer.done();
    }
}
