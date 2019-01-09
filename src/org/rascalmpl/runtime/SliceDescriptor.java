package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;

public final class SliceDescriptor{

	final int first;
	final int second;
	final int end;

	SliceDescriptor(int first, int second, int end){
		this.first = first;
		this.second = second;
		this.end = end;
	}
	
	
}
