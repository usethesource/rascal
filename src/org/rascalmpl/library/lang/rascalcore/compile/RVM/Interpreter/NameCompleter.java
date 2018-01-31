package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class NameCompleter {

	SortedSet<String> result;
	
	public NameCompleter(){
		result = new TreeSet<>(new Comparator<String>() {
			@Override
			public int compare(String a, String b) {
				if (a.charAt(0) == '\\') {
					a = a.substring(1);
				}
				if (b.charAt(0) == '\\') {
					b = b.substring(1);
				}
				return a.compareTo(b);
			}
		});
	}
	
	public SortedSet<String> getResult(){
		return result;
	}
	
    public void add(String completeName, String partialName) {
		if (completeName.startsWith(partialName) && !completeName.equals(partialName)) {
			if (completeName.contains("-")) {
			  completeName = "\\" + completeName;
			}
			result.add(completeName);
		}
	}
}
